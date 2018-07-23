/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package images;

import java.awt.image.BufferedImage;

/**
 * An image that represents the area that is not to be compared.
 * 
 * @author Thomas Rokicki
 *
 */
public class ImageMask extends CommonImage {
	public ImageMask(BufferedImage image, String imageName) {
		super(image, imageName);
	}
}
