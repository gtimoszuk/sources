package pl.edu.mimuw.sources.json;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.mimuw.sources.GitAnalyzeHelper;
import pl.edu.mimuw.sources.model.AuthorAndFileInfo;
import pl.edu.mimuw.sources.model.AuthorPair;
import pl.edu.mimuw.sources.model.CommitInfo;
import pl.edu.mimuw.sources.model.PathModel.PathChangeModel;

public class AuthorsJsonCreator extends GitAnalyzeHelper {

	static final Logger LOGGER = LoggerFactory.getLogger(AuthorsJsonCreator.class);

	private final Set<AuthorAndFileInfo> authorAndFileInfos = new HashSet<AuthorAndFileInfo>();

	private final Map<String, List<AuthorAndFileInfo>> byFileMap = new HashMap<String, List<AuthorAndFileInfo>>();

	private final Map<AuthorPair, Integer> authorsMap = new HashMap<AuthorPair, Integer>();

	{
		timeString = "noData";
	}

	public AuthorsJsonCreator(String projectName, String repoUrl, String downloadPrefix) {
		super(projectName, repoUrl, downloadPrefix);
		fileName = downloadPrefix + projectName + "-" + "authors" + "-" + timeString + ".json";
	}

	public AuthorsJsonCreator(String projectName, String repoUrl, String downloadPrefix, String repoDir) {
		super(projectName, repoUrl, downloadPrefix, repoDir);
		fileName = downloadPrefix + projectName + "-" + "authors" + "-" + timeString + ".json";
	}

	private void buildAuthorsStructure() {
		while (!commitInfoStack.isEmpty()) {
			CommitInfo commitInfo = commitInfoStack.pop();
			if (commitInfo.getFiles() != null) {
				for (PathChangeModel pcm : commitInfo.getFiles()) {
					AuthorAndFileInfo info = new AuthorAndFileInfo(pcm.name, commitInfo.getAuthor());
					if (authorAndFileInfos.contains(info)) {
						findAndIncrease(info);
					} else {
						authorAndFileInfos.add(info);
					}
				}
			}

		}
	}

	private void buildByFileMap() {
		for (AuthorAndFileInfo andFileInfo : authorAndFileInfos) {
			if (byFileMap.containsKey(andFileInfo.getName())) {
				byFileMap.get(andFileInfo.getName()).add(andFileInfo);
			} else {
				List<AuthorAndFileInfo> list = new LinkedList<AuthorAndFileInfo>();
				list.add(andFileInfo);
				byFileMap.put(andFileInfo.getName(), list);
			}
		}
	}

	private void findAndIncrease(AuthorAndFileInfo info) {
		for (AuthorAndFileInfo authorAndFileInfo : authorAndFileInfos) {
			if (info.equals(authorAndFileInfo)) {
				authorAndFileInfo.increaseCount();
			}
		}

	}

	private void buildAuthorsMap() {
		for (String fileName : byFileMap.keySet()) {
			List<AuthorAndFileInfo> authorAndFileInfoList = byFileMap.get(fileName);
			for (AuthorAndFileInfo authorAndFileInfo : authorAndFileInfoList) {
				boolean ifToContinue = false;
				for (AuthorAndFileInfo secondAuthorAndFileInfo : authorAndFileInfoList) {
					if (ifToContinue) {
						AuthorPair authorPair = new AuthorPair(authorAndFileInfo.getAuthor(),
								secondAuthorAndFileInfo.getAuthor());
						if (authorsMap.containsKey(authorPair)) {
							authorsMap.put(authorPair, authorsMap.get(authorPair) + authorAndFileInfo.getCount());
						} else {
							authorsMap.put(authorPair, authorAndFileInfo.getCount());

						}

					}
					if (secondAuthorAndFileInfo.equals(authorAndFileInfo)) {
						ifToContinue = true;
					}

				}
			}
		}
	}

	private void printResult() {
		for (AuthorPair authorPair : authorsMap.keySet()) {
			LOGGER.info(authorPair.getFirstAuthor() + " - " + authorPair.getSecondAuthor() + ": "
					+ authorsMap.get(authorPair));
		}
		LOGGER.info("SIZE of authors map: " + authorsMap.size());

		Set<String> authors = new HashSet<String>();
		for (AuthorPair authorPair : authorsMap.keySet()) {
			authors.add(authorPair.getFirstAuthor());
			authors.add(authorPair.getSecondAuthor());
		}
		LOGGER.info("SIZE of authors : " + authors.size());

	}

	private void generateJSON() throws IOException {
		LOGGER.info("About to begin JSON creation, filename {}", fileName);
		/*
		 * File file = new File(fileName); if (!file.exists()) { file.mkdirs();
		 * file.createNewFile(); }
		 */
		Writer outputStream = null;
		try {
			outputStream = new BufferedWriter(new FileWriter(fileName));
			outputStream.append("[\n");
			boolean first = true;
			for (AuthorPair authorPair : authorsMap.keySet()) {
				if (first) {
					first = false;
				} else {
					outputStream.append(",\n");
				}
				outputStream.append("{\"author1\":\"" + authorPair.getFirstAuthor() + "\",");
				outputStream.append("\"author2\":\"" + authorPair.getSecondAuthor() + "\",");
				outputStream.append("\"value\":" + authorsMap.get(authorPair));
				outputStream.append("}");
			}

			outputStream.append("]\n");
		} catch (IOException e) {
			LOGGER.error("Exception during file writing {}", e);
		} finally {
			outputStream.close();
		}
		LOGGER.info("JSON file successfully generated");

	}

	public String generateAuthorsJSONFromExistingRepo() throws GitAPIException, IOException {
		LOGGER.info("begin project repo dir {} out dir", repoDir, fileName);
		buildRepository();
		analyzeHistory();
		buildAuthorsStructure();
		buildByFileMap();
		buildAuthorsMap();
		printResult();
		generateJSON();
		LOGGER.info("end");
		return fileName;
	}

}
