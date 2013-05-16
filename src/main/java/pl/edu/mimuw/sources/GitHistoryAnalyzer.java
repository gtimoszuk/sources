package pl.edu.mimuw.sources;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.mimuw.sources.PathModel.PathChangeModel;
import au.com.bytecode.opencsv.CSVWriter;

public class GitHistoryAnalyzer extends GitAnalyzeHelper {

	static final Logger LOGGER = LoggerFactory.getLogger(GitHistoryAnalyzer.class);

	private final Utils utils = new Utils();
	private final CostCalculator costCalculator = new CostCalculator();

	public GitHistoryAnalyzer(String projectName, String repoUrl, String downloadPrefix) {
		super(projectName, repoUrl, downloadPrefix);
		fileName = downloadPrefix + projectName + "-" + timeString + ".csv";
	}

	public GitHistoryAnalyzer(String projectName, String repoUrl, String downloadPrefix, String repoDir) {
		super(projectName, repoUrl, downloadPrefix, repoDir);
		fileName = downloadPrefix + projectName + "-" + timeString + ".csv";
	}

	private List<String[]> adaptCommitInfoToCSV(CommitInfo commitInfo, int number) {
		List<String[]> result = new ArrayList<String[]>();
		for (PathChangeModel pathChangeModel : commitInfo.getFiles()) {
			if (pathChangeModel.name.endsWith(".java")) {
				String[] resultArray = new String[10];
				resultArray[0] = String.valueOf(number);
				resultArray[1] = commitInfo.getAuthor();
				resultArray[2] = pathChangeModel.changeType.toString();
				resultArray[3] = pathChangeModel.commitId;
				ClassInfo classInfo = utils.getClassAndPackage(pathChangeModel);
				resultArray[4] = classInfo.getModuleName();
				resultArray[5] = classInfo.getPackageName();
				resultArray[6] = classInfo.getClassName();
				resultArray[7] = (new Date(commitInfo.getTime())).toString();
				resultArray[8] = String.valueOf(costCalculator.calculateCost(commitInfo));
				result.add(resultArray);
			}

		}
		LOGGER.trace("Result array: {}", result);
		return result;
	}

	private void generateCSV() throws IOException {
		LOGGER.info("About to begin CSV creation, filename {}", fileName);
		CSVWriter csvWriter = null;
		try {
			csvWriter = new CSVWriter(new FileWriter(fileName), ',');
			String[] header = new String[] { "fake id", "author", "change type", "commit id", "module", "package",
					"class", "time", "commit cost", "", "" };
			csvWriter.writeNext(header);
			int i = 0;
			while (!commitInfoStack.isEmpty()) {
				CommitInfo commitInfo = commitInfoStack.pop();
				List<String[]> lines = adaptCommitInfoToCSV(commitInfo, i);
				csvWriter.writeAll(lines);
				i++;
			}
		} catch (IOException e) {
			LOGGER.error("Exception during file writing {}", e);

		} finally {
			csvWriter.close();
		}
		LOGGER.info("CSV file successfully generated");
	}

	public String generateCVSWithHistory() throws GitAPIException, IOException {
		LOGGER.info("begin");
		cloneMethod();
		buildRepository();
		analyzeHistory();
		generateCSV();
		LOGGER.info("end");
		return fileName;
	}

	public String generateCVSWithHistoryFromExistingRepo() throws GitAPIException, IOException {
		LOGGER.info("begin project repo dir {} out dir", repoDir, fileName);
		buildRepository();
		analyzeHistory();
		generateCSV();
		LOGGER.info("end");
		return fileName;
	}

}
