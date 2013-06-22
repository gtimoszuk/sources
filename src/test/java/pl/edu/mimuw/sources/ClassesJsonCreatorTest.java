package pl.edu.mimuw.sources;

import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.mimuw.sources.json.ClassesJsonCreator;

public class ClassesJsonCreatorTest {

	static final Logger LOGGER = LoggerFactory.getLogger(ClassesJsonCreatorTest.class);

	public final static String OUT_DIR = "/home/ballo0/GTI/projects/classesResults/";

	@Test
	public void junitTestFromExistingRepo() throws GitAPIException, IOException {
		ClassesJsonCreator classesJsonCreator = new ClassesJsonCreator("junit",
				"https://github.com/junit-team/junit.git", OUT_DIR, "/home/ballo0/GTI/projects/sourcesProjects/junit/");
		classesJsonCreator.generateClassesJSONFromExistingRepo();
	}

	@Test
	public void jLoximFromExistingRepo() throws GitAPIException, IOException {
		ClassesJsonCreator classesJsonCreator = new ClassesJsonCreator("jLoxim", "empty", OUT_DIR,
				"/home/ballo0/GTI/projects/sourcesProjects/jloxim/");
		classesJsonCreator.generateClassesJSONFromExistingRepo();
	}

	@Test
	public void byteManFromExistingRepo() throws GitAPIException, IOException {
		ClassesJsonCreator classesJsonCreator = new ClassesJsonCreator("byteMan",
				"https://github.com/bytemanproject/byteman.git", OUT_DIR,
				"/home/ballo0/GTI/projects/sourcesProjects/byteMan/");
		classesJsonCreator.generateClassesJSONFromExistingRepo();
	}

	@Test
	public void mavenFromExistingRepo() throws GitAPIException, IOException {
		ClassesJsonCreator classesJsonCreator = new ClassesJsonCreator("maven3",
				"https://git-wip-us.apache.org/repos/asf/maven.git", OUT_DIR,
				"/home/ballo0/GTI/projects/sourcesProjects/maven3/");
		classesJsonCreator.generateClassesJSONFromExistingRepo();
	}

	@Test
	public void springFrameworkFromExistingRepo() throws GitAPIException, IOException {
		ClassesJsonCreator classesJsonCreator = new ClassesJsonCreator("spring-framework",
				"https://github.com/SpringSource/spring-framework.git", OUT_DIR,
				"/home/ballo0/GTI/projects/sourcesProjects/spring-framework/");
		classesJsonCreator.generateClassesJSONFromExistingRepo();
	}

	@Test
	public void hibernateTestFromExistingRepo() throws GitAPIException, IOException {
		ClassesJsonCreator classesJsonCreator = new ClassesJsonCreator("hibernate",
				"https://github.com/hibernate/hibernate-orm.git", OUT_DIR,
				"/home/ballo0/GTI/projects/sourcesProjects/hibernate/");
		classesJsonCreator.generateClassesJSONFromExistingRepo();
	}

	@Test
	public void ActionBarSherlockFromExistingRepo() throws GitAPIException, IOException {
		ClassesJsonCreator classesJsonCreator = new ClassesJsonCreator("actionBarSherlock",
				"https://github.com/JakeWharton/ActionBarSherlock.git", OUT_DIR,
				"/home/ballo0/GTI/projects/sourcesProjects/actionBarSherlock/");
		classesJsonCreator.generateClassesJSONFromExistingRepo();
	}

	@Test
	public void zibernateTestFromExistingRepo() throws GitAPIException, IOException {
		ClassesJsonCreator classesJsonCreator = new ClassesJsonCreator("zhibernate",
				"https://github.com/gtimoszuk/zhibernate.git", OUT_DIR,
				"/home/ballo0/GTI/projects/sourcesProjects/zhibernate/");
		classesJsonCreator.generateClassesJSONFromExistingRepo();
	}
}
