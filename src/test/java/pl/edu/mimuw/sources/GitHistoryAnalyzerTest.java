package pl.edu.mimuw.sources;

import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GitHistoryAnalyzerTest {

	static final Logger LOGGER = LoggerFactory.getLogger(GitHistoryAnalyzerTest.class);

	public final static String OUT_DIR = "/home/ballo0/GTI/projects/sourcesResults/";

	@Ignore
	@Test
	public void junitTest() throws GitAPIException, IOException {
		GitHistoryAnalyzer gitHistoryAnalyzer = new GitHistoryAnalyzer("junit",
				"https://github.com/junit-team/junit.git", OUT_DIR);
		gitHistoryAnalyzer.generateCVSWithHistory();
	}

	@Test
	public void junitTestFromExistingRepo() throws GitAPIException, IOException {
		GitHistoryAnalyzer gitHistoryAnalyzer = new GitHistoryAnalyzer("junit",
				"https://github.com/junit-team/junit.git", OUT_DIR, "/home/ballo0/GTI/projects/sourcesProjects/junit/");
		gitHistoryAnalyzer.generateCVSWithHistoryFromExistingRepo();
	}

	@Ignore
	@Test
	public void zibernateTest() throws GitAPIException, IOException {
		GitHistoryAnalyzer gitHistoryAnalyzer = new GitHistoryAnalyzer("zhibernate",
				"https://github.com/gtimoszuk/zhibernate.git", OUT_DIR);
		gitHistoryAnalyzer.generateCVSWithHistory();
	}

	@Test
	public void zibernateTestFromExistingRepo() throws GitAPIException, IOException {
		GitHistoryAnalyzer gitHistoryAnalyzer = new GitHistoryAnalyzer("zhibernate",
				"https://github.com/gtimoszuk/zhibernate.git", OUT_DIR,
				"/home/ballo0/GTI/projects/sourcesProjects/zhibernate/");
		gitHistoryAnalyzer.generateCVSWithHistoryFromExistingRepo();
	}

	@Ignore
	@Test
	public void ActionBarSherlock() throws GitAPIException, IOException {
		GitHistoryAnalyzer gitHistoryAnalyzer = new GitHistoryAnalyzer("actionBarSherlock",
				"https://github.com/JakeWharton/ActionBarSherlock.git", OUT_DIR);
		gitHistoryAnalyzer.generateCVSWithHistory();
	}

	@Test
	public void ActionBarSherlockFromExistingRepo() throws GitAPIException, IOException {
		GitHistoryAnalyzer gitHistoryAnalyzer = new GitHistoryAnalyzer("actionBarSherlock",
				"https://github.com/JakeWharton/ActionBarSherlock.git", OUT_DIR,
				"/home/ballo0/GTI/projects/sourcesProjects/actionBarSherlock/");
		gitHistoryAnalyzer.generateCVSWithHistoryFromExistingRepo();
	}

	@Test
	public void hibernateTestFromExistingRepo() throws GitAPIException, IOException {
		GitHistoryAnalyzer gitHistoryAnalyzer = new GitHistoryAnalyzer("hibernate",
				"https://github.com/hibernate/hibernate-orm.git", OUT_DIR, "/tmp/hibernate/");
		gitHistoryAnalyzer.generateCVSWithHistoryFromExistingRepo();
	}

	@Ignore
	@Test
	public void hibernateTest() throws GitAPIException, IOException {
		GitHistoryAnalyzer gitHistoryAnalyzer = new GitHistoryAnalyzer("hibernate",
				"https://github.com/hibernate/hibernate-orm.git", OUT_DIR);
		gitHistoryAnalyzer.generateCVSWithHistory();
	}

	@Ignore
	@Test
	public void springFramework() throws GitAPIException, IOException {
		GitHistoryAnalyzer gitHistoryAnalyzer = new GitHistoryAnalyzer("spring-framework",
				"https://github.com/SpringSource/spring-framework.git", OUT_DIR);
		gitHistoryAnalyzer.generateCVSWithHistory();
	}

	@Test
	public void springFrameworkFromExistingRepo() throws GitAPIException, IOException {
		GitHistoryAnalyzer gitHistoryAnalyzer = new GitHistoryAnalyzer("spring-framework",
				"https://github.com/SpringSource/spring-framework.git", OUT_DIR,
				"/home/ballo0/GTI/projects/sourcesProjects/spring-framework/");
		gitHistoryAnalyzer.generateCVSWithHistoryFromExistingRepo();
	}

	@Ignore
	@Test
	public void maven() throws GitAPIException, IOException {
		GitHistoryAnalyzer gitHistoryAnalyzer = new GitHistoryAnalyzer("maven3",
				"https://git-wip-us.apache.org/repos/asf/maven.git", OUT_DIR);
		gitHistoryAnalyzer.generateCVSWithHistory();
	}

	@Test
	public void mavenFromExistingRepo() throws GitAPIException, IOException {
		GitHistoryAnalyzer gitHistoryAnalyzer = new GitHistoryAnalyzer("maven3",
				"https://git-wip-us.apache.org/repos/asf/maven.git", OUT_DIR,
				"/home/ballo0/GTI/projects/sourcesProjects/maven3/");
		gitHistoryAnalyzer.generateCVSWithHistoryFromExistingRepo();
	}

	@Ignore
	@Test
	public void byteMan() throws GitAPIException, IOException {
		GitHistoryAnalyzer gitHistoryAnalyzer = new GitHistoryAnalyzer("byteMan",
				"https://github.com/bytemanproject/byteman.git", OUT_DIR);
		gitHistoryAnalyzer.generateCVSWithHistory();
	}

	@Test
	public void byteManFromExistingRepo() throws GitAPIException, IOException {
		GitHistoryAnalyzer gitHistoryAnalyzer = new GitHistoryAnalyzer("byteMan",
				"https://github.com/bytemanproject/byteman.git", OUT_DIR,
				"/home/ballo0/GTI/projects/sourcesProjects/byteMan/");
		gitHistoryAnalyzer.generateCVSWithHistoryFromExistingRepo();
	}

}
