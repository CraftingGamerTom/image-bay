/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package utilities;

public class ComparisonOptionsModifier {

	/**
	 * Ingest a string and return an updated String
	 * 
	 * This method is meant to convert the '*' character to the Alpha Image
	 * name
	 * 
	 * @param diffImageNaming
	 */
	static String modifyDiffImageNaming(String diffImageNaming, String alphaImageName) {
		if (diffImageNaming.contains("*")) {
			String[] parts = diffImageNaming.split("*");

			diffImageNaming = "";
			for (int i = 0; i < parts.length - 2; i++) {
				diffImageNaming += (parts[i] + alphaImageName);
			}
			diffImageNaming += parts[parts.length - 1];
		}
		return diffImageNaming;
	}

}
