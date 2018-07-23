/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package abettor;

import org.apache.log4j.Logger;

/**
 * Class to define the level of precision a screen can be compared to.
 * 
 * @author Thomas Rokicki
 *
 */
public enum Precision {
	ABSOLUTE("absolute", -1, -1), P_20x20("20x20", 20, 20), P_5x5("5x5", 5, 5), P_1x1("1x1", 1, 1);

	final static Logger logger = Logger.getLogger(ImageType.class);
	private String description;
	private int width;
	private int height;

	Precision(String description, int width, int height) {
		this.description = description;
		this.width = width;
		this.height = height;
	}

	/**
	 * Get the Precision based on the description
	 * 
	 * @param description
	 *            (Ex '1x1', '5x5')
	 * @return
	 */
	public static Precision evaluate(String description) throws IllegalArgumentException {
		if (description.equalsIgnoreCase(ABSOLUTE.getDescription())) {
			return ABSOLUTE;
		} else if (description.equalsIgnoreCase(P_20x20.getDescription())) {
			return P_20x20;
		} else if (description.equalsIgnoreCase(P_5x5.getDescription())) {
			return P_5x5;
		} else {
			throw new IllegalArgumentException();
		}
	}

	public String getDescription() {
		return description;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
