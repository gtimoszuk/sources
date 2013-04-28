package pl.edu.mimuw.sources;

public class Utils {

	public ClassInfo getClassAndPackage(PathModel.PathChangeModel pathChangeModel) {

		String name = pathChangeModel.name;

		String[] nameSplit = name.split("/");
		int splitSize = nameSplit.length;
		if (splitSize < 2) {
			return new ClassInfo(removeTrailingJava(nameSplit[0]));
		} else {
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < splitSize - 2; i++) {
				stringBuilder.append(nameSplit[i]);
				stringBuilder.append(".");
			}
			stringBuilder.append(nameSplit[splitSize - 2]);
			String[] moduleAndPackage = fixName(stringBuilder.toString());
			// module name
			ClassInfo classInfo = new ClassInfo(moduleAndPackage[0], moduleAndPackage[1], nameSplit[splitSize - 1]);
			return classInfo;
		}
	}

	public static String[] fixName(String name) {
		String[] prefixesToBeRemovedFromTheBegining = new String[] { "src.main.java.", "src.test.java.", "src.main.",
				"src.test.", "src." };
		for (int i = 0; i < prefixesToBeRemovedFromTheBegining.length; i++) {
			if (name.startsWith(prefixesToBeRemovedFromTheBegining[i])) {
				return new String[] { "", name.substring(prefixesToBeRemovedFromTheBegining[i].length()) };
			}
		}

		String[] suffixesToBeRemoved = new String[] { ".src.main.java.", ".src.test.java." };
		for (int i = 0; i < suffixesToBeRemoved.length; i++) {
			if (name.indexOf(suffixesToBeRemoved[i]) > 0) {
				int moduleEndPosition = name.indexOf(suffixesToBeRemoved[i]);
				return new String[] { name.substring(0, moduleEndPosition),
						name.substring(moduleEndPosition + suffixesToBeRemoved[1].length()) };

			}
		}

		return new String[] { "", name };
	}

	public String removeTrailingJava(String s) {
		return s.split("\\.")[0];
	}
}
