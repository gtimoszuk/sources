package pl.edu.mimuw.sources;

import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GitHistoryAnalyzerTest {

	static final Logger LOGGER = LoggerFactory.getLogger(GitHistoryAnalyzer.class);

	@Test
	public void junitTest() throws GitAPIException, IOException {
		GitHistoryAnalyzer gitHistoryAnalyzer = new GitHistoryAnalyzer("junit",
				"https://github.com/junit-team/junit.git", "/tmp/");
		gitHistoryAnalyzer.generateCVSWithHistory();
	}

	@Test
	public void zibernateTest() throws GitAPIException, IOException {
		GitHistoryAnalyzer gitHistoryAnalyzer = new GitHistoryAnalyzer("zhibernate",
				"https://github.com/gtimoszuk/zhibernate.git", "/tmp/");
		gitHistoryAnalyzer.generateCVSWithHistory();
	}

	@Test
	public void ActionBarSherlock() throws GitAPIException, IOException {
		GitHistoryAnalyzer gitHistoryAnalyzer = new GitHistoryAnalyzer("actionBarSherlock",
				"https://github.com/JakeWharton/ActionBarSherlock.git", "/tmp/");
		gitHistoryAnalyzer.generateCVSWithHistory();
	}

	@Test
	public void hibernateTest() throws GitAPIException, IOException {
		GitHistoryAnalyzer gitHistoryAnalyzer = new GitHistoryAnalyzer("hibernate",
				"https://github.com/hibernate/hibernate-orm.git", "/tmp/");
		gitHistoryAnalyzer.generateCVSWithHistory();
	}

	@Test
	public void springFramework() throws GitAPIException, IOException {
		GitHistoryAnalyzer gitHistoryAnalyzer = new GitHistoryAnalyzer("spring-framework",
				"https://github.com/SpringSource/spring-framework.git", "/tmp/");
		gitHistoryAnalyzer.generateCVSWithHistory();
	}

	@Test
	public void maven() throws GitAPIException, IOException {
		GitHistoryAnalyzer gitHistoryAnalyzer = new GitHistoryAnalyzer("maven3",
				"https://git-wip-us.apache.org/repos/asf/maven.git", "/tmp/");
		gitHistoryAnalyzer.generateCVSWithHistory();
	}

}
