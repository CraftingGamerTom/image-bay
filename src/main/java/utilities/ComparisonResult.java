/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package utilities;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import images.DifferenceImage;

/**
 * ComparisonResult object maintains information from the image comparison that
 * occurred.
 * 
 * @author Thomas Rokicki
 *
 */
public class ComparisonResult {
	final static Logger logger = Logger.getLogger(DifferenceImage.class);

	List<String> comparableImageNames;
	String alphaImageName;
	boolean isSame;
	List<DifferenceImage> allDifferenceImages;

	/**
	 * Create the ComparisonResult
	 * 
	 * @param comparableImageNames
	 *            The comparable image names
	 * @param alphaImageName
	 *            the alpha image name
	 * @param isSame
	 *            Were all the comparable images the same as the alpha
	 * @param differenceImage
	 *            An image that represents the differences in the images
	 */
	public ComparisonResult(List<String> comparableImageNames, String alphaImageName, boolean isSame,
			List<DifferenceImage> allDifferenceImages) {
		this.comparableImageNames = comparableImageNames;
		this.alphaImageName = alphaImageName;
		this.isSame = isSame;
		this.allDifferenceImages = allDifferenceImages;
	}

	/**
	 * Prints the result of the comparison in a user friendly way.
	 */
	public void printResults() {
		String imageNames = comparableImageNames.toString();

		if (isSame) {
			logger.info(imageNames + "were all equal to the AlphaImage.");
		} else {
			logger.info(imageNames + "were not ALL equal to AlphaImage.");

			List<String> differenceImageNames = new ArrayList<String>();
			for (DifferenceImage diff : allDifferenceImages) {
				differenceImageNames.add(diff.getName());
			}

			logger.info("Diff image name is: " + differenceImageNames.toString());
		}
	}

	/**
	 * Get the DifferenceImage containing the differences between the comparable
	 * image and the alpha image
	 * 
	 * @return DifferenceImage
	 */
	public DifferenceImage getDifferenceImage(int index) {
		return allDifferenceImages.get(index);
	}

	/**
	 * Get all the DifferenceImage objects containing the differences between
	 * the comparable
	 * image and the alpha image
	 * 
	 * @return DifferenceImage
	 */
	public List<DifferenceImage> getAllDifferenceImages() {
		return allDifferenceImages;
	}

	/**
	 * A boolean representation of the comparison result
	 * 
	 * @return true if all comparable images match the alpha image, false if any
	 *         are not the same
	 */
	public boolean isMatching() {
		return isSame;
	}

}
