/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package entity;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import images.AlphaImage;
import images.ComparableImage;
import images.DifferenceImage;
import utilities.ComparableGroup;
import utilities.ComparisonOptions;
import utilities.ComparisonResult;
import utilities.ImageGroup;

public class Compare {
	final static Logger logger = Logger.getLogger(Compare.class);

	private List<ComparableGroup> listToCompare;

	public Compare() {
		listToCompare = new ArrayList<ComparableGroup>();
	}

	/**
	 * Puts image into the list to be compared against the alpha image.
	 * 
	 * By default the images will be compared absolutely. One different pixel
	 * will return false
	 * 
	 * @param image1
	 * @param image2
	 */
	public void putComparableImages(ComparableImage comparableImage, AlphaImage alphaImage) {
		ImageGroup imageGroup = new ImageGroup(comparableImage, alphaImage);
		ComparableGroup comparableGroup = new ComparableGroup(imageGroup, new ComparisonOptions());

		listToCompare.add(comparableGroup);

	}

	/**
	 * 
	 * Puts image into the list to be compared against the alpha image.
	 * 
	 * @param image1
	 * @param image2
	 * @param options
	 *            The options that will specifically be used to compare the two
	 *            images provided
	 */
	public void putComparableImages(ComparableImage comparableImage, AlphaImage alphaImage, ComparisonOptions options) {
		ImageGroup imageGroup = new ImageGroup(comparableImage, alphaImage);
		ComparableGroup comparableGroup = new ComparableGroup(imageGroup, options);

		listToCompare.add(comparableGroup);
	}

	/**
	 * Put all the comparable images into the list to be compared against the
	 * alpha image
	 * 
	 * @param comparableImageList
	 * @param alphaImage
	 */
	public void putAllComparableImages(List<ComparableImage> comparableImageList, AlphaImage alphaImage) {
		ImageGroup imageGroup = new ImageGroup(comparableImageList, alphaImage);
		ComparableGroup comparableGroup = new ComparableGroup(imageGroup, new ComparisonOptions());

		listToCompare.add(comparableGroup);
	}

	/**
	 * Put all the comparable images into the list to be compared against the
	 * alpha image
	 * 
	 * @param comparableImageList
	 * @param alphaImage
	 * @param options
	 */
	public void putAllComparableImages(List<ComparableImage> comparableImageList, AlphaImage alphaImage,
			ComparisonOptions options) {
		ImageGroup imageGroup = new ImageGroup(comparableImageList, alphaImage);
		ComparableGroup comparableGroup = new ComparableGroup(imageGroup, options);

		listToCompare.add(comparableGroup);
	}

	public ComparisonResult compareImage(ComparableGroup comparableGroup) {

		ComparisonResult results = null;
		try {
			results = compare(comparableGroup);
		} catch (Exception e) {
			// TODO Handle Exceptions
		}
		return results;
	}

	public List<ComparisonResult> compareAllImages() {

		List<ComparisonResult> comparisonResultList = new ArrayList<ComparisonResult>();

		ComparisonResult result = null;
		for (ComparableGroup comparableGroup : listToCompare) {
			try {
				result = compare(comparableGroup);
				comparisonResultList.add(result);
			} catch (Exception e) {
				// TODO Handle Exceptions
			}
		}
		return comparisonResultList;
	}

	/**
	 * Compare all the comparable images to the alpha image
	 * 
	 * If the comparable image is the same as the alpha image then a difference
	 * image is not created
	 * 
	 * @param comparableGroup
	 * @return
	 */
	private ComparisonResult compare(ComparableGroup comparableGroup) {
		List<String> comparableImageNames = new ArrayList<String>();
		boolean isSame = true; // flag
		List<DifferenceImage> allDifferenceImages = new ArrayList<DifferenceImage>();

		// Get Images
		ImageGroup imageGroup = comparableGroup.getImageGroup();
		List<ComparableImage> comparableImages = imageGroup.getAllComparableImages();
		AlphaImage alphaImage = imageGroup.getAlphaImage();
		// Get Options
		ComparisonOptions comparisonOptions = ComparisonOptions.validateDefaults(comparableGroup.getComparisonOptions(),
				alphaImage);

		// Alpha Image Size for size comparing
		int alphaWidth = alphaImage.getImage().getWidth();
		int alphaHeight = alphaImage.getImage().getHeight();

		// Iterate through each Comparable Image to compare to the Alpha
		for (ComparableImage imageToCompare : comparableImages) {
			// Put the Image name in the list for the result
			comparableImageNames.add(imageToCompare.getName());

			// ComparableImage Size for size comparison
			int imageWidth = imageToCompare.getImage().getWidth();
			int imageHeight = imageToCompare.getImage().getHeight();

			// Compare the image sizes
			if (imageWidth == alphaWidth && imageHeight == alphaHeight) {

				// Get actual images
				BufferedImage renderedImageToCompare = imageToCompare.getImage();
				BufferedImage renderedAlphaImage = alphaImage.getImage();

				// TODO Add two more nested for loops to handle precision

				for (int x = comparisonOptions.getStartX(); x < comparisonOptions.getEndX(); x++) {
					for (int y = comparisonOptions.getStartY(); y < comparisonOptions.getEndY(); y++) {
						if (renderedImageToCompare.getRGB(x, y) != renderedAlphaImage.getRGB(x, y)) {
							// Set Flag
							isSame = false;
							// Create the Difference Image
							DifferenceImage differenceImage = new DifferenceImage(
									new BufferedImage(alphaWidth, alphaHeight, alphaImage.getType()),
									comparisonOptions.getDiffImageName());
							// Put the difference image into result list
							allDifferenceImages.add(differenceImage);
							// Save the Image
							comparisonOptions.getDestination().writeImage(differenceImage,
									comparisonOptions.getImageType());

							// TODO Mark everything inside inner for loop as not
							// matching
						}
					}
				}

				// TODO Handle Partial Y

				// TODO Handle Partial X

			} else { // Comparable image is not the same size as the alpha
				logger.info("Image " + alphaImage.getName() + " is not the same size as the Alpha Image.");
				// Set Flag
				isSame = false;
			}
		}

		ComparisonResult result = new ComparisonResult(comparableImageNames, alphaImage.getName(), isSame,
				allDifferenceImages);
		return result;
	}

}
