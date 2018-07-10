/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import images.AlphaImage;
import images.ComparableImage;
import images.ImageMask;

/**
 * Class that stores the location for where an image should be put on the file
 * system and has the logic to handle the I/O of the images
 * 
 * @author Thomas Rokicki
 *
 */
public class Destination {

	final static Logger logger = Logger.getLogger(Destination.class);

	private String path;

	/**
	 * Create the Destination Object with no destination defined
	 */
	public Destination() {}

	/**
	 * Create a destination object with a desired path
	 * 
	 * @param path
	 */
	public Destination(String path) {
		this.path = path;
	}

	/**
	 * Write to the destination with the specified image name
	 * 
	 * @param image
	 *            to be saved
	 * @param imageType
	 *            the type of the image (The file extension - Ex: .png,
	 *            .bmp, etc)
	 * @param imageName
	 *            the name of the image (must include the extension)
	 */
	public void writeImage(ComparableImage image, String imageType, String imageName) {
		try {
			ImageIO.write(image.getImage(), imageType, new File(path + "\\" + imageName));
		} catch (IOException ioe) {
			logger.error(
					"Could not write the image to the desired destination. Enable debug mode for more information.");
			if (logger.isDebugEnabled()) {
				logger.debug("Debug is Enabled.", ioe);
			}
		}
	}

	/**
	 * Write to the destination with the specified image name
	 * 
	 * @param image
	 *            to be saved
	 * @param imageType
	 *            the type of the image (The file extension - Ex: .png,
	 *            .bmp, etc)
	 * @param imageName
	 *            the name of the image (must include the extension)
	 */
	public void writeImage(AlphaImage image, String imageType, String imageName) {
		try {
			ImageIO.write(image.getImage(), imageType, new File(path + "\\" + imageName));
		} catch (IOException ioe) {
			logger.error(
					"Could not write the image to the desired destination. Enable debug mode for more information.");
			if (logger.isDebugEnabled()) {
				logger.debug("Debug is Enabled.", ioe);
			}
		}
	}

	/**
	 * Write to the destination with the specified image name
	 * 
	 * @param image
	 *            to be saved
	 * @param imageType
	 *            the type of the image (The file extension - Ex: .png,
	 *            .bmp, etc)
	 * @param imageName
	 *            the name of the image (must include the extension)
	 */
	public void writeImage(ImageMask image, String imageType, String imageName) {
		try {
			ImageIO.write(image.getImage(), imageType, new File(path + "\\" + imageName));
		} catch (IOException ioe) {
			logger.error(
					"Could not write the image to the desired destination. Enable debug mode for more information.");
			if (logger.isDebugEnabled()) {
				logger.debug("Debug is Enabled.", ioe);
			}
		}
	}

	/**
	 * Read from the destination with the specified image name
	 * 
	 * @param imageName
	 *            the name of the image (must include the extension - Ex: .png,
	 *            .bmp, etc)
	 */
	public BufferedImage readImage(String imageName) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(path + "\\" + imageName));
		} catch (IOException ioe) {
			logger.error("IOException thrown when reading image. Enable debug mode for more information.");
			if (logger.isDebugEnabled()) {
				logger.debug("Debug is Enabled.", ioe);
			}
		}
		return image;
	}

}
