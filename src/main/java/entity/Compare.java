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

import images.ComparableImage;
import images.DifferenceImage;
import images.PrimordialImage;
import utilities.ComparableGroup;
import utilities.ComparisonOptions;
import utilities.ComparisonResult;
import utilities.ImageGroup;
import utilities.PixelCheckResults;

public class Compare {
	final static Logger logger = Logger.getLogger(Compare.class);

	private List<ComparableGroup> listToCompare;

	public Compare() {
		listToCompare = new ArrayList<ComparableGroup>();
	}

	/**
	 * Puts image into the list to be compared against the primordial image.
	 * 
	 * By default the images will be compared absolutely. One different pixel
	 * will return false
	 * 
	 * @param comparableImage
	 * @param primordialImage
	 */
	public void putComparableImage(ComparableImage comparableImage, PrimordialImage primordialImage) {
		ImageGroup imageGroup = new ImageGroup(comparableImage, primordialImage);
		ComparableGroup comparableGroup = new ComparableGroup(imageGroup, new ComparisonOptions());

		listToCompare.add(comparableGroup);

	}

	/**
	 * 
	 * Puts image into the list to be compared against the primordial image.
	 * 
	 * @param comparableImage
	 * @param primordialImage
	 * @param options
	 *            The options that will specifically be used to compare the two
	 *            images provided
	 */
	public void putComparableImage(ComparableImage comparableImage, PrimordialImage primordialImage,
			ComparisonOptions options) {
		ImageGroup imageGroup = new ImageGroup(comparableImage, primordialImage);
		ComparableGroup comparableGroup = new ComparableGroup(imageGroup, options);

		listToCompare.add(comparableGroup);
	}

	/**
	 * Put all the comparable images into the list to be compared against the
	 * primordial image
	 * 
	 * @param comparableImageList
	 * @param primordialImage
	 */
	public void putAllComparableImages(List<ComparableImage> comparableImageList, PrimordialImage primordialImage) {
		ImageGroup imageGroup = new ImageGroup(comparableImageList, primordialImage);
		ComparableGroup comparableGroup = new ComparableGroup(imageGroup, new ComparisonOptions());

		listToCompare.add(comparableGroup);
	}

	/**
	 * Put all the comparable images into the list to be compared against the
	 * primordial image
	 * 
	 * @param comparableImageList
	 * @param primordialImage
	 * @param options
	 */
	public void putAllComparableImages(List<ComparableImage> comparableImageList, PrimordialImage primordialImage,
			ComparisonOptions options) {
		ImageGroup imageGroup = new ImageGroup(comparableImageList, primordialImage);
		ComparableGroup comparableGroup = new ComparableGroup(imageGroup, options);

		listToCompare.add(comparableGroup);
	}

	public ComparisonResult compareImage(ComparableGroup comparableGroup) {

		ComparisonResult results = null;
		try {
			results = compare(comparableGroup);
		} catch (Exception e) {
			// TODO Handle Exceptions
			logger.error("An error occured when comparing an image.", e);
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
				logger.error("An error occured when comparing an image.", e);
			}
		}
		return comparisonResultList;
	}

	/**
	 * Compare all the comparable images to the primordial image
	 * 
	 * If the comparable image is the same as the primordial image then a difference
	 * image is not created
	 * 
	 * @param comparableGroup
	 * @return
	 */
	private ComparisonResult compare(ComparableGroup comparableGroup) {
		logger.debug("Begin comparing images.");
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
	private int getPixelGroupHeight(CompareVariables variables) {
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
	private int getPixelGroupWidth(CompareVariables variables) {
		int width = variables.getComparisonOptions().getPixelGroupSize().getWidth();
		if (width < 0) {
			width = variables.getPrimordialImage().getImage().getWidth();
		}
		return width;
	}

	private void compareEachComparableImage(CompareVariables variables, List<ComparableImage> comparableImages) {
		for (ComparableImage imageToCompare : comparableImages) {
			logger.debug("-- Comparing --");

			variables.setComparableImage(imageToCompare);
			addComparableImageNameToList(variables); // Add the image name to the list

			int primoridialWidth = variables.getPrimordialImage().getImage().getWidth();
			int primoridialHeight = variables.getPrimordialImage().getImage().getHeight();

			// Compare the image sizes
			if ((variables.getComparableImage().getImage().getWidth() == primoridialWidth)
					&& (variables.getComparableImage().getImage().getHeight() == primoridialHeight)) {

				if (logger.isDebugEnabled()) {
					// Determine Block ratio of area being compared for logging
					int widthBlocksCount = (variables.getComparisonOptions().getEndX()
							- variables.getComparisonOptions().getStartX()) / getPixelGroupWidth(variables);
					int heightBlocksCount = (variables.getComparisonOptions().getEndY()
							- variables.getComparisonOptions().getStartY()) / getPixelGroupHeight(variables);
					logger.debug("Width block count: " + widthBlocksCount);
					logger.debug("Height block count: " + heightBlocksCount);

					logger.debug("Start X: " + variables.getComparisonOptions().getStartX());
					logger.debug("Start Y: " + variables.getComparisonOptions().getStartY());
					logger.debug("End X: " + variables.getComparisonOptions().getEndX());
					logger.debug("End Y: " + variables.getComparisonOptions().getEndY());
				}
				// Define Counters
				variables.setPageX(variables.getComparisonOptions().getStartX());
				variables.setPageY(variables.getComparisonOptions().getStartY());

				crawlImage(variables);

				// Only save diff if there is something different
				if (!variables.isSame()) {
					createAndSaveDifferenceImage(variables);
				}

			} else { // Comparable image is not the same size as the primordial
				logger.info("Image " + variables.getPrimordialImage().getName()
						+ " is not the same size as the Primordial Image.");
				// Set Flag
				variables.setSame(false);
			}
		}
	}

	private void addComparableImageNameToList(CompareVariables variables) {
		// Put the Image name in the list for the result
		List<String> names = variables.getComparableImageNames();
		names.add(variables.getComparableImage().getName());
		variables.setComparableImageNames(names);
	}

	private void createDifferenceImage(CompareVariables variables) {
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

	private void crawlImage(CompareVariables variables) {
		// Crawl the complete blocks X
		while (variables.getPageX() < variables.getComparisonOptions().getEndX()) {
			logger.debug("PageX: " + variables.getPageX());
			variables.setPageY(variables.getComparisonOptions().getStartY()); // reset counter

			// Crawl the complete blocks Y
			while (variables.getPageY() < variables.getComparisonOptions().getEndY()) {

				logger.debug("PageY: " + variables.getPageY());

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
	 * Crawl a block (section of an image)
	 * 
	 * This method will traverse X & Y and check for any pixels that do not match.
	 * If a pixel fails to match, any further pixels in the block will not be
	 * compared, and the entire block will be marked as failed.
	 * 
	 * @param variables
	 */
	private void crawlBlock(CompareVariables variables) {
		// Crawl inner block X
		variables.setBlockX(variables.getPageX());
		while (variables.getBlockX() < (variables.getPageX() + getPixelGroupWidth(variables))
				&& variables.getBlockX() < variables.getComparisonOptions().getEndX()) {
			logger.debug("BlockX: " + variables.getBlockX());
			// Crawl inner block Y
			variables.setBlockY(variables.getPageY());
			while (variables.getBlockY() < (variables.getPageY() + getPixelGroupHeight(variables))
					&& variables.getBlockY() < variables.getComparisonOptions().getEndY()) {
				logger.debug("Checking pixel | " + variables.getBlockX() + ", " + variables.getBlockY());

				variables.getComparisonOptions().getImageMask();
				variables.getComparisonOptions().getImageMask().getImage();
				logger.debug(variables.getComparisonOptions().getImageMask().getName());

				// Ensure the area is not masked
				if (variables.getComparisonOptions().getImageMask().getImage() == null
						|| variables.getComparisonOptions().getImageMask().getImage().getRGB(variables.getBlockX(),
								variables.getBlockY()) == Color.TRANSLUCENT) {
					PixelCheckResults pixelCheckResults = checkthePixel(variables);

					variables.setInnerErrorFound(pixelCheckResults.isErrorFound());
					if (variables.isInnerErrorFound()) {
						// Set Overall Flag
						logger.debug("Failed Comparison | " + variables.getBlockX() + ", " + variables.getBlockY());
						variables.setSame(false);
						return;
					}
				}
				variables.setBlockY(variables.getBlockY() + 1);
			}
			variables.setBlockX(variables.getBlockX() + 1);
		}
	}

	public void createAndSaveDifferenceImage(CompareVariables variables) {

		// Put the difference image into result list
		List<DifferenceImage> allDifferenceImages = variables.getAllDifferenceImages();
		allDifferenceImages.add(variables.getDifferenceImage());
		variables.setAllDifferenceImages(allDifferenceImages);

		// Save the Image
		variables.getComparisonOptions().getResultsDestination().writeImage(variables.getDifferenceImage(),
				variables.getComparisonOptions().getImageType());
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
	private PixelCheckResults checkthePixel(CompareVariables variables) {

		// Handle different pixel
		if (variables.getComparableImage().getImage().getRGB(variables.getBlockX(), variables.getBlockY()) != variables
				.getPrimordialImage().getImage().getRGB(variables.getBlockX(), variables.getBlockY())) {
			BufferedImage diffImage = variables.getDifferenceImage().getImage();
			logger.debug("Pixel does not match.");
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
