/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package images;

import java.awt.image.BufferedImage;

/**
 * The image that is created to display the differences between the comparable
 * images and the Primordial image
 * 
 * @author Thomas Rokicki
 *
 */
public class DifferenceImage extends CommonImage {
	public DifferenceImage(BufferedImage image, String imageName) {
		super(image, imageName);
	}
}
