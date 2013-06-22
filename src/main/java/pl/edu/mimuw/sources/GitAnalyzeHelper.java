package pl.edu.mimuw.sources;

import java.io.File;
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

import pl.edu.mimuw.sources.model.CommitInfo;
import pl.edu.mimuw.sources.model.PathModel.PathChangeModel;

public abstract class GitAnalyzeHelper {

	static final Logger LOGGER = LoggerFactory.getLogger(GitAnalyzeHelper.class);

	protected final Stack<CommitInfo> commitInfoStack = new Stack<CommitInfo>();

	protected String repoUrl;
	protected String repoDir;
	protected Repository repository;
	protected String timeString = Calendar.getInstance().getTime().toString();
	protected String fileName;

	public GitAnalyzeHelper(String projectName, String repoUrl, String downloadPrefix) {
		this.repoUrl = repoUrl;
		repoDir = downloadPrefix + projectName + "-" + timeString;
	}

	public GitAnalyzeHelper(String projectName, String repoUrl, String downloadPrefix, String repoDir) {
		this.repoUrl = repoUrl;
		this.repoDir = repoDir;
	}

	protected void cloneMethod() throws GitAPIException {
		CloneCommand clone = new CloneCommand();
		clone.setURI(repoUrl);
		clone.setDirectory(new File(repoDir));
		clone.call();
		LOGGER.info("Cloning finished");
	}

	protected void buildRepository() throws IOException {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		repository = builder.setGitDir(new File(repoDir + "/.git")).readEnvironment().findGitDir().build();
		LOGGER.info("Building finished");
	}

	protected void analyzeHistory() throws GitAPIException {
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
