/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package com.craftinggamertom.image_bay.abettor;

import org.apache.log4j.Logger;

/**
 * Enum class to hold the values of different possible file types for the
 * images.
 * 
 * @author Thomas Rokicki
 *
 */
public enum ImageType {
	PNG("png", ".png"), BMP("bmp", ".bmp");

	final static Logger logger = Logger.getLogger(ImageType.class);
	private String type;
	private String extension;

	ImageType(String type, String extension) {
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
	public static ImageType evaluate(String description) throws IllegalArgumentException {
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
