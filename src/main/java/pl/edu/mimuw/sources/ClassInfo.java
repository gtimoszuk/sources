package pl.edu.mimuw.sources;

public class ClassInfo {

	private String moduleName = "";
	private String packageName = "";
	private String className = "";

	public ClassInfo(String moduleName, String packageName, String className) {
		this.moduleName = moduleName;
		this.packageName = packageName;
		this.className = className;
	}

	public ClassInfo(String packageName, String className) {
		this.packageName = packageName;
		this.className = className;
		moduleName = "";
	}

	public ClassInfo(String className) {
		this.className = className;
		moduleName = "";
		packageName = "";
	}

	public String getModuleName() {
		return moduleName;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getClassName() {
		return className;
	}
}
