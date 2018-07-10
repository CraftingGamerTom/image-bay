/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package images;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

import org.apache.log4j.Logger;

public class ComparableImage {
	final static Logger logger = Logger.getLogger(ComparableImage.class);

	BufferedImage image;

	public ComparableImage(BufferedImage image) {
		this.image = image;
	}

	/**
	 * Return the AlphaImage image.
	 * 
	 * @return
	 */
	public RenderedImage getImage() {
		return image;
	}
}
