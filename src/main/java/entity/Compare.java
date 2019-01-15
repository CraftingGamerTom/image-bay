/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import exceptions.InvalidImageSizeException;
import images.ComparableImage;
import images.DifferenceImage;
import utilities.ComparableGroup;
import utilities.ComparisonOptions;
import utilities.ComparisonResult;
import utilities.Destination;
import utilities.ImageGroup;
import utilities.PixelCheckResults;

public class Compare {
	final static Logger logger = Logger.getLogger(Compare.class);

	/**
	 * Compare a single comparable group.
	 * 
	 * @param comparableGroup
	 * @return ComparisonResult containing the results of the run
	 * @throws InvalidImageSizeException
	 */
	public static ComparisonResult compareImage(ComparableGroup comparableGroup) throws InvalidImageSizeException {

		try {
			return compare(comparableGroup);
		} catch (InvalidImageSizeException e) {
			throw e;
		} catch (Exception e) {
			// TODO Handle Exceptions
			logger.error("An error occured when comparing an image.", e);
			throw e;
		}
	}

	/**
	 * Compare all the images that were put into the list of images to compare
	 * 
	 * @return
	 * @throws InvalidImageSizeException
	 */
	public static List<ComparisonResult> compareImages(List<ComparableGroup> comparableGroups)
			throws InvalidImageSizeException {
		List<ComparisonResult> comparisonResultList = new ArrayList<ComparisonResult>();

		try {
			for (ComparableGroup comparableGroup : comparableGroups) {
				comparisonResultList.add(compare(comparableGroup));
			}
			return comparisonResultList;
		} catch (InvalidImageSizeException e) {
			throw e;
		} catch (Exception e) {
			// TODO Handle Exceptions
			logger.error("An error occured when comparing an image.", e);
			throw e;
		}
	}

	/**
	 * Compare all the comparable images to the primordial image
	 * 
	 * If the comparable image is the same as the primordial image then a difference
	 * image is not created
	 * 
	 * @param comparableGroup
	 * @return ComparisonResults containing the difference image and a boolean value
	 *         of whether the images matched or not
	 * @throws InvalidImageSizeException
	 */
	private static ComparisonResult compare(ComparableGroup comparableGroup) throws InvalidImageSizeException {
		// logger.trace("Begin comparing images.");
		CompareVariables variables = new CompareVariables();

		// Get Images
		ImageGroup imageGroup = comparableGroup.getImageGroup();

		// Setup
		variables.setPrimordialImage(imageGroup.getPrimordialImage());
		variables.setComparisonOptions(ComparisonOptions.validateDefaults(comparableGroup.getComparisonOptions(),
				variables.getPrimordialImage()));

		// Iterate through each Comparable Image to compare to the Primordial
		compareEachComparableImage(variables, imageGroup.getAllComparableImages());

		// Get Results
		ComparisonResult result = new ComparisonResult(variables.getComparableImageNames(),
				variables.getPrimordialImage().getName(), variables.isSame(), variables.getAllDifferenceImages());
		return result;
	}

	/**
	 * Get the pixel height and ensure it is valid
	 * 
	 * @param comparisonOptions
	 * @param imageHeight
	 * @return
	 */
	private static int getPixelGroupHeight(CompareVariables variables) {
		int height = variables.getComparisonOptions().getPixelGroupSize().getHeight();
		if (height < 0) {
			height = variables.getPrimordialImage().getImage().getHeight();
		}
		return height;
	}

	/**
	 * Get the pixel width and ensure it is valid
	 * 
	 * @param comparisonOptions
	 * @param imageWidth
	 * @return
	 */
	private static int getPixelGroupWidth(CompareVariables variables) {
		int width = variables.getComparisonOptions().getPixelGroupSize().getWidth();
		if (width < 0) {
			width = variables.getPrimordialImage().getImage().getWidth();
		}
		return width;
	}

	/**
	 * Iterate through each comparable group and compare
	 * 
	 * @param variables
	 * @param comparableImages
	 * @throws InvalidImageSizeException
	 */
	private static void compareEachComparableImage(CompareVariables variables, List<ComparableImage> comparableImages)
			throws InvalidImageSizeException {
		for (ComparableImage imageToCompare : comparableImages) {
			logger.debug("Comparing " + imageToCompare.getName() + " to " + variables.getPrimordialImage().getName());

			variables.setComparableImage(imageToCompare);
			addComparableImageNameToList(variables); // Add the image name to the list

			// Check for unmatching image size
			ensureImageSizesMatch(variables);

			if (logger.isTraceEnabled()) {
				// Determine Block ratio of area being compared for logging
				logger.trace("Comparison details: ");
				int widthBlocksCount = (variables.getComparisonOptions().getEndX()
						- variables.getComparisonOptions().getStartX()) / getPixelGroupWidth(variables);
				int heightBlocksCount = (variables.getComparisonOptions().getEndY()
						- variables.getComparisonOptions().getStartY()) / getPixelGroupHeight(variables);
				logger.trace("Width block count: " + widthBlocksCount);
				logger.trace("Height block count: " + heightBlocksCount);

				logger.trace("Start X: " + variables.getComparisonOptions().getStartX());
				logger.trace("Start Y: " + variables.getComparisonOptions().getStartY());
				logger.trace("End X: " + variables.getComparisonOptions().getEndX());
				logger.trace("End Y: " + variables.getComparisonOptions().getEndY());
				logger.trace("End comparison details.");
			}
			// Define Counters
			variables.setPageX(variables.getComparisonOptions().getStartX());
			variables.setPageY(variables.getComparisonOptions().getStartY());

			crawlImage(variables);

			// Only save diff if there is something different
			if (!variables.isSame()) {
				saveDifferenceImage(variables);
			}
		}
	}

	/**
	 * Check that the images are the same size.
	 * 
	 * isSame flag is set to false if the images are not the same size
	 * 
	 * @param variables
	 * @return true if the images are the same size, false if not the same size
	 * @throws InvalidImageSizeException
	 */
	private static void ensureImageSizesMatch(CompareVariables variables) throws InvalidImageSizeException {
		int primoridialWidth = variables.getPrimordialImage().getImage().getWidth();
		int primoridialHeight = variables.getPrimordialImage().getImage().getHeight();

		// Compare the image sizes
		if ((variables.getComparableImage().getImage().getWidth() == primoridialWidth)
				&& (variables.getComparableImage().getImage().getHeight() == primoridialHeight)) {
			// Compare mask image size to left
			BufferedImage maskImage = variables.getComparisonOptions().getImageMask().getImage();
			// If mask image is null then ignore
			if (maskImage != null) {
				// If mask image size matches continue
				if ((maskImage.getWidth() != primoridialWidth) || (maskImage.getHeight() != primoridialHeight)) {
					variables.setSame(false); // Set Flag
					throw new InvalidImageSizeException("Mask image size does not match");
				}
			}
		} else {
			variables.setSame(false); // Set Flag
			throw new InvalidImageSizeException("Image sizes do not match.");
		}
	}

	/**
	 * Add the comparable image's name to the list of names.
	 * 
	 * Gets the list, adds the name to it, then saves the list
	 * 
	 * @param variables
	 */
	private static void addComparableImageNameToList(CompareVariables variables) {
		// Put the Image name in the list for the result
		List<String> names = variables.getComparableImageNames();
		names.add(variables.getComparableImage().getName());
		variables.setComparableImageNames(names);
	}

	/**
	 * Save the difference image to the desired result destination
	 * 
	 * @param variables
	 */
	public static void saveDifferenceImage(CompareVariables variables) {

		// Put the difference image into result list
		List<DifferenceImage> allDifferenceImages = variables.getAllDifferenceImages();
		allDifferenceImages.add(variables.getDifferenceImage());
		variables.setAllDifferenceImages(allDifferenceImages);

		// Save the Image (if destination is not null)
		Destination differenceDestination = variables.getComparisonOptions().getResultsDestination();
		if (differenceDestination != null) {
			differenceDestination.writeImage(variables.getDifferenceImage(),
					variables.getComparisonOptions().getImageType());
		}
	}

	/**
	 * Method to increment pixel by pixel
	 * 
	 * @param variables
	 */
	private static void crawlImage(CompareVariables variables) {
		// Crawl the complete blocks X
		while (variables.getPageX() < variables.getComparisonOptions().getEndX()) {
			// logger.debug("PageX: " + variables.getPageX());
			variables.setPageY(variables.getComparisonOptions().getStartY()); // reset counter

			// Crawl the complete blocks Y
			while (variables.getPageY() < variables.getComparisonOptions().getEndY()) {

				// logger.debug("PageY: " + variables.getPageY());

				createDifferenceImage(variables);
				variables.setInnerErrorFound(false); // Set flag
				crawlBlock(variables); // Crawl the block

				// Increment block Y-Axis
				variables.setPageY(variables.getPageY() + getPixelGroupHeight(variables));
			}
			// Increment block X-Axis
			variables.setPageX(variables.getPageX() + getPixelGroupWidth(variables));
		}
	}

	/**
	 * Initialize the difference image to be used if there is a failure during the
	 * comparison
	 * 
	 * @param variables
	 */
	private static void createDifferenceImage(CompareVariables variables) {
		// Determine whether to create a Diff or a Mask
		BufferedImage theDiffImage;
		if (variables.getComparisonOptions().isCreateMask()) {
			// Create a transparent image to be marked
			theDiffImage = new BufferedImage(variables.getPrimordialImage().getImage().getWidth(),
					variables.getPrimordialImage().getImage().getHeight(), variables.getPrimordialImage().getType());
		} else {
			// Use primordial image as background
			theDiffImage = variables.getPrimordialImage().getImage();
		}
		variables.setDifferenceImage(
				new DifferenceImage(theDiffImage, variables.getComparisonOptions().getDiffImageName()
						+ variables.getComparisonOptions().getImageType().getExtension()));
	}

	/**
	 * Crawl a block (section of an image)
	 * 
	 * This method will traverse X & Y and check for any pixels that do not match.
	 * If a pixel fails to match, any further pixels in the block will not be
	 * compared, and the entire block will be marked as failed.
	 * 
	 * @param variables
	 */
	private static void crawlBlock(CompareVariables variables) {
		// Crawl inner block X
		variables.setBlockX(variables.getPageX());
		while (variables.getBlockX() < (variables.getPageX() + getPixelGroupWidth(variables))
				&& variables.getBlockX() < variables.getComparisonOptions().getEndX()) {
			// logger.debug("BlockX: " + variables.getBlockX());
			// Crawl inner block Y
			variables.setBlockY(variables.getPageY());
			while (variables.getBlockY() < (variables.getPageY() + getPixelGroupHeight(variables))
					&& variables.getBlockY() < variables.getComparisonOptions().getEndY()) {
				// logger.debug("Checking pixel | " + variables.getBlockX() + ", " +
				// variables.getBlockY());

				variables.getComparisonOptions().getImageMask();
				variables.getComparisonOptions().getImageMask().getImage();
				// logger.debug(variables.getComparisonOptions().getImageMask().getName());

				// Ensure the area is not masked
				BufferedImage maskImage = variables.getComparisonOptions().getImageMask().getImage();
				if (maskImage == null
						|| maskImage.getRGB(variables.getBlockX(), variables.getBlockY()) == Color.TRANSLUCENT) {
					PixelCheckResults pixelCheckResults = checkthePixel(variables);

					variables.setInnerErrorFound(pixelCheckResults.isErrorFound());
					if (variables.isInnerErrorFound()) {
						// Set Overall Flag
						// logger.debug("Failed Comparison | " + variables.getBlockX() + ", " +
						// variables.getBlockY());
						variables.setSame(false);
						return;
					}
				}
				variables.setBlockY(variables.getBlockY() + 1);
			}
			variables.setBlockX(variables.getBlockX() + 1);
		}
	}

	/**
	 * Compare a pixel based on its x & y. If the pixel does not match; mark the
	 * corresponding pixel on the difference image.
	 * 
	 * The difference image is null if the pixels match
	 * 
	 * @param variables
	 * @return PixelCheckResults
	 */
	private static PixelCheckResults checkthePixel(CompareVariables variables) {

		// Handle different pixel
		if (variables.getComparableImage().getImage().getRGB(variables.getBlockX(), variables.getBlockY()) != variables
				.getPrimordialImage().getImage().getRGB(variables.getBlockX(), variables.getBlockY())) {
			BufferedImage diffImage = variables.getDifferenceImage().getImage();
			// logger.debug("Pixel does not match.");
			Graphics2D g2d = diffImage.createGraphics();
			g2d.setColor(variables.getComparisonOptions().getErrorColor());
			g2d.fillRect(variables.getPageX(), variables.getPageY(), getPixelGroupWidth(variables),
					getPixelGroupHeight(variables));
			g2d.dispose();

			// TODO find out which method is faster
			/*
			 * for (int modifyBlockX = pageX; modifyBlockX < (pageX + pixelGroupSizeWidth)
			 * && blockX < comparisonOptions.getEndX(); modifyBlockX++) {
			 * for (int modifyBlockY = pageY; modifyBlockY < (pageY + pixelGroupSizeHeight)
			 * && blockY < comparisonOptions.getEndY(); modifyBlockY++) {
			 * 
			 * errorFound = true;
			 * // logger.debug("Marking errored pixel: " + modifyBlockX +
			 * // ", " + modifyBlockY);
			 * 
			 * // Set the Error area
			 * diffImage.setRGB(modifyBlockX, modifyBlockY,
			 * comparisonOptions.getErrorColor().getRGB());
			 * }
			 * }
			 */

			return new PixelCheckResults(diffImage, true);
		}
		return new PixelCheckResults(null, false);
	}

}
