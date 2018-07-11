/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package entity;

import java.awt.Color;
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
	 * @param comparableImage
	 * @param alphaImage
	 */
	public void putComparableImage(ComparableImage comparableImage, AlphaImage alphaImage) {
		ImageGroup imageGroup = new ImageGroup(comparableImage, alphaImage);
		ComparableGroup comparableGroup = new ComparableGroup(imageGroup, new ComparisonOptions());

		listToCompare.add(comparableGroup);

	}

	/**
	 * 
	 * Puts image into the list to be compared against the alpha image.
	 * 
	 * @param comparableImage
	 * @param alphaImage
	 * @param options
	 *            The options that will specifically be used to compare the two
	 *            images provided
	 */
	public void putComparableImage(ComparableImage comparableImage, AlphaImage alphaImage, ComparisonOptions options) {
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
	 * Compare all the comparable images to the alpha image
	 * 
	 * If the comparable image is the same as the alpha image then a difference
	 * image is not created
	 * 
	 * @param comparableGroup
	 * @return
	 */
	private ComparisonResult compare(ComparableGroup comparableGroup) {
		logger.debug("Begin comparing images.");
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
			logger.debug("-- Comparing --");
			// Put the Image name in the list for the result
			comparableImageNames.add(imageToCompare.getName());

			// ComparableImage Size for size comparison
			int imageWidth = imageToCompare.getImage().getWidth();
			int imageHeight = imageToCompare.getImage().getHeight();

			// Compare the image sizes
			if (imageWidth == alphaWidth && imageHeight == alphaHeight) {

				// Get actual images
				BufferedImage bufferedImageToCompare = imageToCompare.getImage();
				BufferedImage bufferedAlphaImage = alphaImage.getImage();
				BufferedImage bufferedImageMask = comparisonOptions.getImageMask().getImage();

				// Determine whether to create a Diff or a Mask
				BufferedImage theDiffImage;
				if (comparisonOptions.isCreateMask()) {
					theDiffImage = new BufferedImage(alphaWidth, alphaHeight, alphaImage.getType());
				} else {
					theDiffImage = imageToCompare.getImage();
				}

				// Ensure the precision is valid
				int precisionWidth = comparisonOptions.getPrecision().getWidth();
				int precisionHeight = comparisonOptions.getPrecision().getHeight();

				if (precisionWidth < 0) {
					precisionWidth = imageWidth;
				}
				if (precisionHeight < 0) {
					precisionHeight = imageHeight;
				}

				// Determine Block ratio of area being compared
				int widthBlocksCount = (comparisonOptions.getEndX() - comparisonOptions.getStartX()) / precisionWidth;
				int heightBlocksCount = (comparisonOptions.getEndY() - comparisonOptions.getStartY()) / precisionHeight;

				// Determine what is left over on the edge of the image
				// int widthBlocksRemainder = (comparisonOptions.getStartY() -
				// comparisonOptions.getStartX())
				// % comparisonOptions.getPrecision().getWidth();
				// int heightBlocksRemainder = (comparisonOptions.getEndY() -
				// comparisonOptions.getEndY())
				// % comparisonOptions.getPrecision().getHeight();

				if (logger.isDebugEnabled()) {
					logger.debug("Width block count: " + widthBlocksCount);
					logger.debug("Height block count: " + heightBlocksCount);

					logger.debug("Start X: " + comparisonOptions.getStartX());
					logger.debug("Start Y: " + comparisonOptions.getStartY());
					logger.debug("End X: " + comparisonOptions.getEndX());
					logger.debug("End Y: " + comparisonOptions.getEndY());
				}

				// Define Counters
				int pageX = comparisonOptions.getStartX();
				int pageY = comparisonOptions.getStartY();

				// Crawl the complete blocks X
				while (pageX < comparisonOptions.getEndX()) {

					// logger.debug("PageX: " + pageX);

					pageY = comparisonOptions.getStartY(); // reset counter
					// Crawl the complete blocks Y
					while (pageY < comparisonOptions.getEndY()) {

						// logger.debug("PageY: " + pageY);

						/*
						 * flag to ensure the work is not repeated when error is
						 * found.
						 */
						boolean innerErrorFound = false;

						// Crawl inner block X
						for (int blockX = pageX; blockX < (pageX + precisionWidth)
								&& blockX < comparisonOptions.getEndX(); blockX++) {

							// logger.debug("BlockX: " + blockX);

							// Ensure an error was not found and already handled
							if (!innerErrorFound) {
								// Ensure an error was not found and already
								// handled
								if (!innerErrorFound) {
									// Crawl inner block Y
									for (int blockY = pageY; blockY < (pageY + precisionHeight)
											&& blockY < comparisonOptions.getEndY(); blockY++) {

										// logger.debug("BlockY: " + blockY);

										// logger.debug("Checking pixel: " +
										// blockX + ", " + blockY);

										// Ensure the area is not masked
										if (bufferedImageMask.getRGB(blockX, blockY) == Color.TRANSLUCENT) {

											// logger.debug("Not a masked
											// pixel.");

											// Handle different pixel
											if (bufferedImageToCompare.getRGB(blockX, blockY) != bufferedAlphaImage
													.getRGB(blockX, blockY)) {

												// logger.debug("Pixel does not
												// match.");

												// Set Overall Flag
												isSame = false;

												for (int modifyBlockX = pageX; modifyBlockX < (pageX + precisionWidth)
														&& blockX < comparisonOptions.getEndX(); modifyBlockX++) {
													for (int modifyBlockY = pageY; modifyBlockY < (pageY
															+ precisionHeight)
															&& blockY < comparisonOptions.getEndY(); modifyBlockY++) {

														// logger.debug("Marking
														// errored pixel: " +
														// modifyBlockX + ", "
														// + modifyBlockY);

														// Set the Error area
														theDiffImage.setRGB(modifyBlockX, modifyBlockY,
																comparisonOptions.getErrorColor());
													}
												}
												// Flag to stop crawling this
												// block
												innerErrorFound = true;
												break;
											}
										}
									}
								}
							} else {
								break; // Break loop to stop handling a mismatch
							}
						}

						// Increment block Y-Axis
						pageY += precisionHeight;
					}
					// Increment block X-Axis
					pageX += precisionWidth;
				}

				// Create the Difference Image to be used
				DifferenceImage differenceImage = new DifferenceImage(theDiffImage,
						comparisonOptions.getDiffImageName() + comparisonOptions.getImageType().getExtension());
				// Put the difference image into result list
				allDifferenceImages.add(differenceImage);

				// Only save diff if there is something different
				if (!isSame) {
					// Save the Image
					comparisonOptions.getResultsDestination().writeImage(differenceImage,
							comparisonOptions.getImageType());
				}

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
