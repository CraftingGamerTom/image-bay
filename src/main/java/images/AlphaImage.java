/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package images;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

import org.apache.log4j.Logger;

/**
 * The image that is to be compared against.
 * 
 * @author Thomas Rokicki
 *
 */
public class AlphaImage {
	final static Logger logger = Logger.getLogger(AlphaImage.class);

	BufferedImage image;

	public AlphaImage(BufferedImage image) {
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
