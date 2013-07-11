package pl.edu.mimuw.sources;

import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.mimuw.sources.json.AuthorsJsonCreator;

public class AuthorsJsonCreatorTest {

	static final Logger LOGGER = LoggerFactory.getLogger(AuthorsJsonCreatorTest.class);

	public final static String OUT_DIR = "/home/ballo0/GTI/projects/authorsResults/";

	@Test
	public void junitTestFromExistingRepo() throws GitAPIException, IOException {
		AuthorsJsonCreator authorsJsonCreator = new AuthorsJsonCreator("junit",
				"https://github.com/junit-team/junit.git", OUT_DIR, "/home/ballo0/GTI/projects/sourcesProjects/junit/");
		authorsJsonCreator.generateAuthorsJSONFromExistingRepo();
	}

	@Test
	public void zibernateTestFromExistingRepo() throws GitAPIException, IOException {
		AuthorsJsonCreator authorsJsonCreator = new AuthorsJsonCreator("zhibernate",
				"https://github.com/gtimoszuk/zhibernate.git", OUT_DIR,
				"/home/ballo0/GTI/projects/sourcesProjects/zhibernate/");
		authorsJsonCreator.generateAuthorsJSONFromExistingRepo();
	}

	@Test
	public void jLoximFromExistingRepo() throws GitAPIException, IOException {
		AuthorsJsonCreator authorsJsonCreator = new AuthorsJsonCreator("jLoxim", "empty", OUT_DIR,
				"/home/ballo0/GTI/projects/sourcesProjects/jloxim/");
		authorsJsonCreator.generateAuthorsJSONFromExistingRepo();
	}

	@Test
	public void byteManFromExistingRepo() throws GitAPIException, IOException {
		AuthorsJsonCreator authorsJsonCreator = new AuthorsJsonCreator("byteMan",
				"https://github.com/bytemanproject/byteman.git", OUT_DIR,
				"/home/ballo0/GTI/projects/sourcesProjects/byteMan/");
		authorsJsonCreator.generateAuthorsJSONFromExistingRepo();
	}

	@Test
	public void mavenFromExistingRepo() throws GitAPIException, IOException {
		AuthorsJsonCreator authorsJsonCreator = new AuthorsJsonCreator("maven3",
				"https://git-wip-us.apache.org/repos/asf/maven.git", OUT_DIR,
				"/home/ballo0/GTI/projects/sourcesProjects/maven3/");
		authorsJsonCreator.generateAuthorsJSONFromExistingRepo();
	}

	@Test
	public void springFrameworkFromExistingRepo() throws GitAPIException, IOException {
		AuthorsJsonCreator authorsJsonCreator = new AuthorsJsonCreator("spring-framework",
				"https://github.com/SpringSource/spring-framework.git", OUT_DIR,
				"/home/ballo0/GTI/projects/sourcesProjects/spring-framework/");
		authorsJsonCreator.generateAuthorsJSONFromExistingRepo();
	}

	@Test
	public void hibernateTestFromExistingRepo() throws GitAPIException, IOException {
		AuthorsJsonCreator authorsJsonCreator = new AuthorsJsonCreator("hibernate",
				"https://github.com/hibernate/hibernate-orm.git", OUT_DIR,
				"/home/ballo0/GTI/projects/sourcesProjects/hibernate/");
		authorsJsonCreator.generateAuthorsJSONFromExistingRepo();
	}

	@Test
	public void ActionBarSherlockFromExistingRepo() throws GitAPIException, IOException {
		AuthorsJsonCreator authorsJsonCreator = new AuthorsJsonCreator("actionBarSherlock",
				"https://github.com/JakeWharton/ActionBarSherlock.git", OUT_DIR,
				"/home/ballo0/GTI/projects/sourcesProjects/actionBarSherlock/");
		authorsJsonCreator.generateAuthorsJSONFromExistingRepo();
	}

}
