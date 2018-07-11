/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package images;

import java.awt.image.BufferedImage;

import org.apache.log4j.Logger;

/**
 * Parent class for all images in this library
 * 
 * @author Thomas Rokicki
 *
 */
public class CommonImage {
	final static Logger logger = Logger.getLogger(CommonImage.class);

	BufferedImage image;
	String name;

	public CommonImage(BufferedImage image, String name) {
		this.image = image;
		this.name = name;
	}

	/**
	 * Return the image.
	 * 
	 * @return
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * The name of the file
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the image type of the image.
	 * 
	 * @return int for the Image Type.
	 */
	public int getType() {
		return image.getType();
	}
}
