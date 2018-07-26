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
		List<String> comparableImageNames = new ArrayList<String>();
		boolean isSame = true; // flag
		List<DifferenceImage> allDifferenceImages = new ArrayList<DifferenceImage>();

		// Get Images
		ImageGroup imageGroup = comparableGroup.getImageGroup();
		List<ComparableImage> comparableImages = imageGroup.getAllComparableImages();
		PrimordialImage primordialImage = imageGroup.getPrimordialImage();
		// Get Options
		ComparisonOptions comparisonOptions = ComparisonOptions.validateDefaults(comparableGroup.getComparisonOptions(),
				primordialImage);

		// Primordial Image Size for size comparing
		int primordialWidth = primordialImage.getImage().getWidth();
		int primordialHeight = primordialImage.getImage().getHeight();

		// Iterate through each Comparable Image to compare to the Primordial
		for (ComparableImage imageToCompare : comparableImages) {
			logger.debug("-- Comparing --");
			// Put the Image name in the list for the result
			comparableImageNames.add(imageToCompare.getName());

			// ComparableImage Size for size comparison
			int imageWidth = imageToCompare.getImage().getWidth();
			int imageHeight = imageToCompare.getImage().getHeight();

			// Compare the image sizes
			if (imageWidth == primordialWidth && imageHeight == primordialHeight) {

				// Get actual images
				BufferedImage bufferedImageToCompare = imageToCompare.getImage();
				BufferedImage bufferedPrimordialImage = primordialImage.getImage();
				BufferedImage bufferedImageMask = comparisonOptions.getImageMask().getImage();

				// Determine whether to create a Diff or a Mask
				BufferedImage theDiffImage;
				if (comparisonOptions.isCreateMask()) {
					// Create a transparent image to be marked
					theDiffImage = new BufferedImage(primordialWidth, primordialHeight, primordialImage.getType());
				} else {
					// Use primordial image as background
					theDiffImage = imageToCompare.getImage();
				}

				// Ensure the PixelGroupSize is valid
				int pixelGroupWidth = getPixelGroupWidth(comparisonOptions, imageWidth);
				int pixelGroupHeight = getPixelGroupHeight(comparisonOptions, imageHeight);

				// Determine Block ratio of area being compared
				int widthBlocksCount = (comparisonOptions.getEndX() - comparisonOptions.getStartX()) / pixelGroupWidth;
				int heightBlocksCount = (comparisonOptions.getEndY() - comparisonOptions.getStartY())
						/ pixelGroupHeight;

				// Determine what is left over on the edge of the image
				// int widthBlocksRemainder = (comparisonOptions.getStartY() -
				// comparisonOptions.getStartX())
				// % comparisonOptions.getPixelGroupSize().getWidth();
				// int heightBlocksRemainder = (comparisonOptions.getEndY() -
				// comparisonOptions.getEndY())
				// % comparisonOptions.getPixelGroupSize().getHeight();

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
						for (int blockX = pageX; blockX < (pageX + pixelGroupWidth)
								&& blockX < comparisonOptions.getEndX(); blockX++) {

							// logger.debug("BlockX: " + blockX);

							// Ensure an error was not found and already handled
							if (!innerErrorFound) {
								// Crawl inner block Y
								for (int blockY = pageY; blockY < (pageY + pixelGroupHeight)
										&& blockY < comparisonOptions.getEndY(); blockY++) {

									// logger.debug("BlockY: " + blockY);

									// logger.debug("Checking pixel: " +
									// blockX + ", " + blockY);

									PixelCheckResults pixelCheckResults = null;

									// Ensure the area is not masked
									if (bufferedImageMask == null) {
										// logger.debug("Not a masked
										// pixel.");

										pixelCheckResults = checkthePixel(bufferedImageToCompare,
												bufferedPrimordialImage, theDiffImage, comparisonOptions,
												pixelGroupWidth, pixelGroupHeight, pageX, pageY, blockX, blockY);
									} else if (bufferedImageMask.getRGB(blockX, blockY) == Color.TRANSLUCENT) {
										// logger.debug("Not a masked pixel.");

										pixelCheckResults = checkthePixel(bufferedImageToCompare,
												bufferedPrimordialImage, theDiffImage, comparisonOptions,
												pixelGroupWidth, pixelGroupHeight, pageX, pageY, blockX, blockY);

									}

									if (pixelCheckResults != null) {
										// Flag to stop crawling this block
										innerErrorFound = pixelCheckResults.isErrorFound();
										if (innerErrorFound) {
											// Set Overall Flag
											isSame = false;
											break;

										}
									}

								}
							} else {
								break; // Break loop to stop handling a mismatch
							}
						}

						// Increment block Y-Axis
						pageY += pixelGroupHeight;
					}
					// Increment block X-Axis
					pageX += pixelGroupWidth;
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

			} else { // Comparable image is not the same size as the primordial
				logger.info("Image " + primordialImage.getName() + " is not the same size as the Primordial Image.");
				// Set Flag
				isSame = false;
			}
		}

		ComparisonResult result = new ComparisonResult(comparableImageNames, primordialImage.getName(), isSame,
				allDifferenceImages);
		return result;
	}

	/**
	 * Get the pixel height and ensure it is valid
	 * 
	 * @param comparisonOptions
	 * @param imageHeight
	 * @return
	 */
	private int getPixelGroupHeight(ComparisonOptions comparisonOptions, int imageHeight) {
		int height = comparisonOptions.getPixelGroupSize().getHeight();
		if (height < 0) {
			height = imageHeight;
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
	private int getPixelGroupWidth(ComparisonOptions comparisonOptions, int imageWidth) {
		int width = comparisonOptions.getPixelGroupSize().getWidth();
		if (width < 0) {
			width = imageWidth;
		}
		return width;
	}

	/**
	 * Compare the pixels based on its x & y and update the difference image if
	 * there was an unmatching pixel.
	 * 
	 * @param bufferedImageToCompare
	 * @param bufferedPrimordialImage
	 * @param diffImage
	 * @param comparisonOptions
	 * @param pixelGroupSizeWidth
	 * @param pixelGroupSizeHeight
	 * @param pageX
	 * @param pageY
	 * @param blockX
	 * @param blockY
	 * @return
	 */
	private PixelCheckResults checkthePixel(BufferedImage bufferedImageToCompare, BufferedImage bufferedPrimordialImage,
			BufferedImage diffImage, ComparisonOptions comparisonOptions, int pixelGroupSizeWidth,
			int pixelGroupSizeHeight, int pageX, int pageY, int blockX, int blockY) {

		boolean errorFound = false;

		// Handle different pixel
		if (bufferedImageToCompare.getRGB(blockX, blockY) != bufferedPrimordialImage.getRGB(blockX, blockY)) {

			// logger.debug("Pixel does not match.");
			Graphics2D g2d = diffImage.createGraphics();
			g2d.setColor(comparisonOptions.getErrorColor());
			g2d.fillRect(pageX, pageY, pixelGroupSizeWidth, pixelGroupSizeHeight);
			g2d.dispose();
			errorFound = true;

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
		}
		return new PixelCheckResults(diffImage, errorFound);
	}

}
