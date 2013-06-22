package pl.edu.mimuw.sources.algo;

import java.util.Stack;

import pl.edu.mimuw.sources.Utils;
import pl.edu.mimuw.sources.model.ClassInfo;
import pl.edu.mimuw.sources.model.CommitInfo;
import pl.edu.mimuw.sources.model.PathModel.PathChangeModel;

import com.google.common.base.Strings;

public class CostCalculator {

	public final static double ROOT_COST = 1;
	public final static double COST_MULTIPLIER = 2;
	private final Utils utils = new Utils();

	public double calculateCost(CommitInfo commitInfo) {
		int maxPackageDepth = 0;
		Stack<String> packages = new Stack<String>();
		for (PathChangeModel pathChangeModel : commitInfo.getFiles()) {
			if (pathChangeModel.name.endsWith(".java")) {

				ClassInfo classInfo = utils.getClassAndPackage(pathChangeModel);
				if (classInfo.getPackageName().split("\\.").length > maxPackageDepth) {
					maxPackageDepth = classInfo.getPackageName().split("\\.").length;
				}
				packages.push(classInfo.getPackageName());
			}
		}
		if (packages.size() == 0) {
			return 0.0;
		} else {
			String commonPrefix = packages.pop();
			while (!packages.isEmpty()) {
				commonPrefix = Strings.commonPrefix(commonPrefix, packages.pop());
			}
			int numberOfCommonPackages = commonPrefix.split("\\.").length;
			int commitTreeHeight = maxPackageDepth - numberOfCommonPackages;
			double commitCostMultiplier = Math.pow(COST_MULTIPLIER, commitTreeHeight);
			double commitCost = ROOT_COST * commitCostMultiplier;
			return commitCost;
		}
	}
}
