/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package images;

import java.awt.image.BufferedImage;

/**
 * The image that is to be compared against.
 * 
 * @author Thomas Rokicki
 *
 */
public class AlphaImage extends CommonImage {
	public AlphaImage(BufferedImage image, String imageName) {
		super(image, imageName);
	}
}