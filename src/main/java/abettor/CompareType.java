/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package abettor;

import org.apache.log4j.Logger;

/**
 * Enum class to hold the values of different possible file types for the
 * images.
 * 
 * @author Thomas Rokicki
 *
 */
public enum CompareType {
	PNG("png", ".png"), BMP("bmp", ".bmp");

	final static Logger logger = Logger.getLogger(CompareType.class);
	private String type;
	private String extension;

	CompareType(String type, String extension) {
		this.type = type;
		this.extension = extension;
	}

	/**
	 * Get the CompareType based on the description
	 * 
	 * @param description
	 *            (Ex '1x1', '5x5')
	 * @return
	 */
	public static CompareType evaluate(String description) throws IllegalArgumentException {
		if (description.equalsIgnoreCase(PNG.getType())) {
			return PNG;
		} else if (description.equalsIgnoreCase(BMP.getType())) {
			return BMP;
		} else {
			throw new IllegalArgumentException();
		}
	}

	public String getType() {
		return type;
	}

	public String getExtension() {
		return extension;
	}
}
