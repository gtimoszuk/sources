package pl.edu.mimuw.sources;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Stack;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.mimuw.sources.PathModel.PathChangeModel;
import au.com.bytecode.opencsv.CSVWriter;

public class GitHistoryAnalyzer {

	static final Logger LOGGER = LoggerFactory.getLogger(GitHistoryAnalyzer.class);
	private final String repoUrl;
	private final String repoDir;
	private Repository repository;
	private final Stack<CommitInfo> commitInfoStack = new Stack<CommitInfo>();
	private final String csvFileName;
	private final Utils utils = new Utils();
	private final CostCalculator costCalculator = new CostCalculator();

	public GitHistoryAnalyzer(String projectName, String repoUrl, String downloadPrefix) {
		this.repoUrl = repoUrl;
		String timeString = Calendar.getInstance().getTime().toString();
		repoDir = downloadPrefix + projectName + "-" + timeString;
		csvFileName = downloadPrefix + projectName + "-" + timeString + ".csv";
	}

	public GitHistoryAnalyzer(String projectName, String repoUrl, String downloadPrefix, String repoDir) {
		this.repoUrl = repoUrl;
		String timeString = Calendar.getInstance().getTime().toString();
		this.repoDir = repoDir;
		csvFileName = downloadPrefix + projectName + "-" + timeString + ".csv";
	}

	private void cloneMethod() throws GitAPIException {
		CloneCommand clone = new CloneCommand();
		clone.setURI(repoUrl);
		clone.setDirectory(new File(repoDir));
		clone.call();
		LOGGER.info("Cloning finished");
	}

	private void buildRepository() throws IOException {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		repository = builder.setGitDir(new File(repoDir + "/.git")).readEnvironment().findGitDir().build();
		LOGGER.info("Building finished");
	}

	private void analyzeHistory() throws GitAPIException {
		Git git = new Git(repository);
		LogCommand log = git.log();
		Iterable<RevCommit> logMsgs = log.call();

		for (RevCommit commit : logMsgs) {
			LOGGER.trace("autor: {}", commit.getAuthorIdent());
			List<PathChangeModel> modifiedFiles = getFilesInCommit(repository, commit);
			commitInfoStack.push(new CommitInfo(modifiedFiles, commit.getAuthorIdent().getName(), commit
					.getFullMessage(), commit.getCommitTime()));
		}
		LOGGER.info("Analysis finished");
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
				resultArray[7] = String.valueOf(commitInfo.getTime());
				resultArray[8] = String.valueOf(costCalculator.calculateCost(commitInfo));
				result.add(resultArray);
			}

		}
		LOGGER.trace("Result array: {}", result);
		return result;
	}

	private void generateCSV() throws IOException {
		LOGGER.info("About to begin CSV creation, filename {}", csvFileName);
		CSVWriter csvWriter = null;
		try {
			csvWriter = new CSVWriter(new FileWriter(csvFileName), ',');
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
		return csvFileName;
	}

	public String generateCVSWithHistoryFromExistingRepo() throws GitAPIException, IOException {
		LOGGER.info("begin project repo dir {} out dir", repoDir, csvFileName);
		buildRepository();
		analyzeHistory();
		generateCSV();
		LOGGER.info("end");
		return csvFileName;
	}

	private List<PathChangeModel> getFilesInCommit(Repository repository, RevCommit commit) {
		List<PathChangeModel> list = new ArrayList<PathChangeModel>();

		RevWalk rw = new RevWalk(repository);
		try {

			if (commit.getParentCount() == 0) {
				TreeWalk tw = new TreeWalk(repository);
				tw.reset();
				tw.setRecursive(true);
				tw.addTree(commit.getTree());
				while (tw.next()) {
					list.add(new PathChangeModel(tw.getPathString(), tw.getPathString(), 0, tw.getRawMode(0), tw
							.getObjectId(0).getName(), commit.getId().getName(), DiffEntry.ChangeType.ADD));
				}
				tw.release();
			} else {
				RevCommit parent = rw.parseCommit(commit.getParent(0).getId());
				DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
				df.setRepository(repository);
				df.setDiffComparator(RawTextComparator.DEFAULT);
				df.setDetectRenames(true);
				List<DiffEntry> diffs = df.scan(parent.getTree(), commit.getTree());
				for (DiffEntry diff : diffs) {
					String objectId = diff.getNewId().name();
					if (diff.getChangeType().equals(DiffEntry.ChangeType.DELETE)) {
						list.add(new PathChangeModel(diff.getOldPath(), diff.getOldPath(), 0, diff.getNewMode()
								.getBits(), objectId, commit.getId().getName(), diff.getChangeType()));
					} else if (diff.getChangeType().equals(DiffEntry.ChangeType.RENAME)) {
						list.add(new PathChangeModel(diff.getOldPath(), diff.getNewPath(), 0, diff.getNewMode()
								.getBits(), objectId, commit.getId().getName(), diff.getChangeType()));
					} else {
						list.add(new PathChangeModel(diff.getNewPath(), diff.getNewPath(), 0, diff.getNewMode()
								.getBits(), objectId, commit.getId().getName(), diff.getChangeType()));
					}
				}
			}
		} catch (Throwable t) {
			LOGGER.error("error: {}", t);
		} finally {
			rw.dispose();
		}
		return list;
	}

}
