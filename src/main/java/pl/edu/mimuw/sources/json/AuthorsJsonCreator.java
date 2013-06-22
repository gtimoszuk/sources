package pl.edu.mimuw.sources.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.mimuw.sources.GitAnalyzeHelper;

public class AuthorsJsonCreator extends GitAnalyzeHelper {

	static final Logger LOGGER = LoggerFactory.getLogger(AuthorsJsonCreator.class);

	private final Map<String, Integer> authorCount = new HashMap<String, Integer>();

	public AuthorsJsonCreator(String projectName, String repoUrl, String downloadPrefix) {
		super(projectName, repoUrl, downloadPrefix);
		fileName = downloadPrefix + projectName + "-" + "authors" + "-" + timeString + ".json";
	}

	public AuthorsJsonCreator(String projectName, String repoUrl, String downloadPrefix, String repoDir) {
		super(projectName, repoUrl, downloadPrefix, repoDir);
		fileName = downloadPrefix + projectName + "-" + "authors" + "-" + timeString + ".json";
	}

	private void buildAuthorsStructure() {

	}

	public String generateAuthorsJSONFromExistingRepo() throws GitAPIException, IOException {
		LOGGER.info("begin project repo dir {} out dir", repoDir, fileName);
		buildRepository();
		analyzeHistory();
		LOGGER.info("end");
		return fileName;
	}

}
