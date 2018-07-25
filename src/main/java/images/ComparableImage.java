/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package images;

import java.awt.image.BufferedImage;

/**
 * Image that is to be compared to the Primordial image
 * 
 * @author Thomas Rokicki
 *
 */
public class ComparableImage extends CommonImage {
	public ComparableImage(BufferedImage image, String imageName) {
		super(image, imageName);
	}
}
