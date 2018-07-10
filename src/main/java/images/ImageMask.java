/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package images;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

/**
 * An image that represents the area that is not to be compared.
 * 
 * @author Thomas Rokicki
 *
 */
public class ImageMask {

	private BufferedImage image;

	public ImageMask(BufferedImage image) {
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
