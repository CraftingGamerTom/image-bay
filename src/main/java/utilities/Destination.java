/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import abettor.ImageType;
import images.CommonImage;

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
	 * Create the Destination Object placeholder with no destination defined
	 * 
	 * @see Destination(String path) to create a functioning Destination
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
	 */
	public void writeImage(CommonImage image, ImageType imageType) {
		if (path != null) {
			try {
				ImageIO.write(image.getImage(), imageType.getType(), new File(path + "\\" + image.getName()));
			} catch (IOException ioe) {
				logger.error(
						"Could not write the image to the desired destination. Enable debug mode for more information.");
				if (logger.isDebugEnabled()) {
					logger.debug("Debug is Enabled.", ioe);
				}
			}
		} else {
			logger.info("Path for Image to be saved was not defined.");
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
