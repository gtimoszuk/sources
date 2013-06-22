package pl.edu.mimuw.sources.json;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.mimuw.sources.GitAnalyzeHelper;
import pl.edu.mimuw.sources.model.CommitInfo;
import pl.edu.mimuw.sources.model.PathModel;

public class ClassesJsonCreator extends GitAnalyzeHelper {

	static final Logger LOGGER = LoggerFactory.getLogger(ClassesJsonCreator.class);

	private final Map<String, Integer> classCount = new HashMap<String, Integer>();

	private final Map<String, Set<String>> classesCommitedTogether = new HashMap<String, Set<String>>();

	{
		timeString = "noData";
	}

	public ClassesJsonCreator(String projectName, String repoUrl, String downloadPrefix) {
		super(projectName, repoUrl, downloadPrefix);
		fileName = downloadPrefix + projectName + "-" + "classes" + "-" + timeString + ".json";
	}

	public ClassesJsonCreator(String projectName, String repoUrl, String downloadPrefix, String repoDir) {
		super(projectName, repoUrl, downloadPrefix, repoDir);
		fileName = downloadPrefix + projectName + "-" + "classes" + "-" + timeString + ".json";
	}

	private void buildStrucure() {
		while (!commitInfoStack.isEmpty()) {
			CommitInfo commitInfo = commitInfoStack.pop();
			Set<PathModel.PathChangeModel> toBeRemoved = new HashSet<PathModel.PathChangeModel>();
			for (PathModel.PathChangeModel pcm : commitInfo.getFiles()) {
				if (!pcm.name.endsWith("java")) {
					toBeRemoved.add(pcm);
				}
			}
			for (PathModel.PathChangeModel pcm : toBeRemoved) {
				commitInfo.getFiles().remove(pcm);
			}

			for (PathModel.PathChangeModel pcm : commitInfo.getFiles()) {
				if (classCount.containsKey(pcm.name)) {
					classCount.put(pcm.name, classCount.get(pcm.name) + 1);
				} else {
					classCount.put(pcm.name, 1);
				}
			}
			if (commitInfo.getFiles().size() > 1) {
				PathModel.PathChangeModel head = commitInfo.getFiles().get(0);
				String headName = head.name;
				commitInfo.getFiles().remove(0);
				for (PathModel.PathChangeModel pcm : commitInfo.getFiles()) {
					if (classesCommitedTogether.containsKey(headName)) {
						Set<String> classSet = classesCommitedTogether.get(headName);
						classSet.add(pcm.name);
					} else {
						Set<String> classSet = new HashSet<String>();
						classSet.add(pcm.name);
						classesCommitedTogether.put(headName, classSet);
					}
				}
			}
		}
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
			for (String s : classCount.keySet()) {
				if (first) {
					first = false;
				} else {
					outputStream.append(",\n");
				}
				outputStream.append("{");
				outputStream.append("\"name\":\"" + s + "\",");
				outputStream.append("\"size\":\"" + classCount.get(s) + "\",");
				outputStream.append("\"imports\":[");
				boolean sfirst = true;
				if (classesCommitedTogether.containsKey(s)) {
					for (String imported : classesCommitedTogether.get(s)) {
						if (sfirst) {
							sfirst = false;
						} else {
							outputStream.append(",");
						}
						outputStream.append("\"" + imported + "\"");

					}
				}
				outputStream.append("]");
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

	public String generateClassesJSONFromExistingRepo() throws GitAPIException, IOException {
		LOGGER.info("begin project repo dir {} out dir", repoDir, fileName);
		buildRepository();
		analyzeHistory();
		buildStrucure();
		generateJSON();
		LOGGER.info("end");
		return fileName;
	}

}
