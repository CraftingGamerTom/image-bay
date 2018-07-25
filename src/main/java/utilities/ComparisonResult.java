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
	final static Logger logger = Logger.getLogger(ComparisonResult.class);

	List<String> comparableImageNames;
	String primordialImageName;
	boolean isSame;
	List<DifferenceImage> allDifferenceImages;

	/**
	 * Create the ComparisonResult
	 * 
	 * @param comparableImageNames
	 *            The comparable image names
	 * @param primordialImageName
	 *            the primordial image name
	 * @param isSame
	 *            Were all the comparable images the same as the primordial
	 * @param differenceImage
	 *            An image that represents the differences in the images
	 */
	public ComparisonResult(List<String> comparableImageNames, String primordialImageName, boolean isSame,
			List<DifferenceImage> allDifferenceImages) {
		this.comparableImageNames = comparableImageNames;
		this.primordialImageName = primordialImageName;
		this.isSame = isSame;
		this.allDifferenceImages = allDifferenceImages;
	}

	/**
	 * Prints the result of the comparison in a user friendly way.
	 */
	public void printResults() {
		String imageNames = comparableImageNames.toString();

		if (isSame) {
			logger.info(imageNames + " were all equal to the PrimordialImage.");
		} else {
			logger.info(imageNames + " were not ALL equal to PrimordialImage.");

			List<String> differenceImageNames = new ArrayList<String>();
			for (DifferenceImage diff : allDifferenceImages) {
				differenceImageNames.add(diff.getName());
			}

			logger.info("Diff image name is: " + differenceImageNames.toString());
		}
	}

	/**
	 * Get the DifferenceImage containing the differences between the comparable
	 * image and the primordial image
	 * 
	 * @return DifferenceImage
	 */
	public DifferenceImage getDifferenceImage(int index) {
		return allDifferenceImages.get(index);
	}

	/**
	 * Get all the DifferenceImage objects containing the differences between
	 * the comparable
	 * image and the primordial image
	 * 
	 * @return DifferenceImage
	 */
	public List<DifferenceImage> getAllDifferenceImages() {
		return allDifferenceImages;
	}

	/**
	 * A boolean representation of the comparison result
	 * 
	 * @return true if all comparable images match the primordial image, false if
	 *         any
	 *         are not the same
	 */
	public boolean isMatching() {
		return isSame;
	}

}
