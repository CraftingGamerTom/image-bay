/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.List;

import org.junit.Test;

import abettor.PixelGroupSize;
import entity.Compare;
import images.ComparableImage;
import images.ImageMask;
import images.PrimordialImage;
import utilities.ComparisonOptions;
import utilities.ComparisonResult;
import utilities.Destination;

public class Comparison {

	// Access Destination
	private static Destination primordialImageDestination = new Destination(
			"/media/tcrokicki/MainDrive/workspaces/BLUSTREAM/image-bay/src/test/resources/primordial-images");
	private static Destination comparableImageDestination = new Destination(
			"/media/tcrokicki/MainDrive/workspaces/BLUSTREAM/image-bay/src/test/resources/comparable-images");
	private static Destination maskDestination = new Destination(
			"/media/tcrokicki/MainDrive/workspaces/BLUSTREAM/image-bay/src/test/resources/mask-images");
	private static Destination resultsDestination = new Destination(
			"/media/tcrokicki/MainDrive/workspaces/BLUSTREAM/image-bay/src/test/resources/results");

	/**
	 * Test that two identical images are found to be the same
	 */
	@Test
	public void testIdentical4x4ImageAbsolute() {
		System.out.println("testIdentical4x4ImageAbsolute");
		// Create Images
		String primordialImageName = "4x4-black-purple.png";
		String comparableImageName = "4x4-black-purple-same.png";
		PrimordialImage primordialImage = new PrimordialImage(primordialImageDestination.readImage(primordialImageName),
				primordialImageName);
		ComparableImage comparableImage = new ComparableImage(comparableImageDestination.readImage(comparableImageName),
				comparableImageName);

		// Get Options
		ComparisonOptions options = new ComparisonOptions();
		options.setResultsDestination(resultsDestination);
		options.setPixelGroupSize(PixelGroupSize.ABSOLUTE);

		// Compare
		Compare compare = new Compare();
		compare.putComparableImage(comparableImage, primordialImage, options);

		// Get Results
		List<ComparisonResult> allResults = compare.compareAllImages();
		ComparisonResult result = allResults.get(0);

		result.printResults();
		assertTrue(result.isMatching());
	}

	/**
	 * Test that two identical images are found to be the same
	 */
	@Test
	public void testIdentical50x50Image20x20() {
		System.out.println("testIdentical50x50Image20x20");

		// Create Images
		String primordialImageName = "50x50-black-purple.png";
		String comparableImageName = "50x50-black-purple-same.png";
		PrimordialImage primordialImage = new PrimordialImage(primordialImageDestination.readImage(primordialImageName),
				primordialImageName);
		ComparableImage comparableImage = new ComparableImage(comparableImageDestination.readImage(comparableImageName),
				comparableImageName);

		// Get Options
		ComparisonOptions options = new ComparisonOptions();
		options.setResultsDestination(resultsDestination);
		options.setPixelGroupSize(PixelGroupSize.P_20x20);

		// Compare
		Compare compare = new Compare();
		compare.putComparableImage(comparableImage, primordialImage, options);

		// Get Results
		List<ComparisonResult> allResults = compare.compareAllImages();
		ComparisonResult result = allResults.get(0);

		result.printResults();
		assertTrue(result.isMatching());
	}

	/**
	 * Test that partial area of two identical images are found to be the same
	 */
	@Test
	public void testPartial50x50Identical20x20() {
		System.out.println("testPartial50x50Identical20x20");

		// Create Images
		String primordialImageName = "50x50-black-purple.png";
		String comparableImageName = "50x50-black-purple-same.png";
		PrimordialImage primordialImage = new PrimordialImage(primordialImageDestination.readImage(primordialImageName),
				primordialImageName);
		ComparableImage comparableImage = new ComparableImage(comparableImageDestination.readImage(comparableImageName),
				comparableImageName);

		// Get Options
		ComparisonOptions options = new ComparisonOptions();
		options.setResultsDestination(resultsDestination);
		options.setStartX(1);
		options.setStartY(1);
		options.setEndX(2);
		options.setEndY(2);
		options.setPixelGroupSize(PixelGroupSize.P_20x20);

		// Compare
		Compare compare = new Compare();
		compare.putComparableImage(comparableImage, primordialImage, options);

		// Get Results
		List<ComparisonResult> allResults = compare.compareAllImages();
		ComparisonResult result = allResults.get(0);

		result.printResults();
		assertTrue(result.isMatching());
	}

	/**
	 * Test that different sized images are found to be different
	 */
	@Test
	public void testDifferentSizedImages() {
		System.out.println("testDifferentSizedImages");

		// Create Images
		String primordialImageName = "50x50-black-purple.png";
		String comparableImageName = "4x4-black-purple-same.png";
		PrimordialImage primordialImage = new PrimordialImage(primordialImageDestination.readImage(primordialImageName),
				primordialImageName);
		ComparableImage comparableImage = new ComparableImage(comparableImageDestination.readImage(comparableImageName),
				comparableImageName);

		// Get Options
		ComparisonOptions options = new ComparisonOptions();
		options.setDiffImageName("testDifferentSizedImages");
		options.setResultsDestination(resultsDestination);
		options.setPixelGroupSize(PixelGroupSize.ABSOLUTE);

		// Compare
		Compare compare = new Compare();
		compare.putComparableImage(comparableImage, primordialImage, options);

		// Get Results
		List<ComparisonResult> allResults = compare.compareAllImages();
		ComparisonResult result = allResults.get(0);

		result.printResults();
		assertFalse(result.isMatching());
	}

	/**
	 * Test that a mask is created properly based on images that do not match
	 */
	@Test
	public void test50x50CreateMask1x1() {
		System.out.println("test50x50CreateMask1x1");

		// Create Images
		String primordialImageName = "50x50-black-purple.png";
		String comparableImageName = "50x50-black-purple-errored.png";
		PrimordialImage primordialImage = new PrimordialImage(primordialImageDestination.readImage(primordialImageName),
				primordialImageName);
		ComparableImage comparableImage = new ComparableImage(comparableImageDestination.readImage(comparableImageName),
				comparableImageName);

		// Set Options
		ComparisonOptions options = new ComparisonOptions();
		options.setDiffImageName("test50x50CreateMask1x1-mask");
		options.setResultsDestination(maskDestination);
		options.setPixelGroupSize(PixelGroupSize.P_1x1);
		options.setCreateMask(true);

		// Compare
		Compare compare = new Compare();
		compare.putComparableImage(comparableImage, primordialImage, options);

		// Get Results
		List<ComparisonResult> allResults = compare.compareAllImages();
		ComparisonResult result = allResults.get(0);

		// ASSUMING COMPARISONS WORK
		// Compare the two images again, using the mask created above
		Compare resultComparer = new Compare();
		ComparisonOptions compareOptions = new ComparisonOptions();
		compareOptions.setErrorColor(Color.BLUE);
		compareOptions.setDiffImageName("test50x50CreateMask1x1-diff");
		compareOptions.setResultsDestination(resultsDestination);
		compareOptions.setImageMask(
				new ImageMask(result.getDifferenceImage(0).getImage(), result.getDifferenceImage(0).getName()));
		compareOptions.setPixelGroupSize(PixelGroupSize.P_1x1);

		resultComparer.putComparableImage(comparableImage, primordialImage, compareOptions);

		List<ComparisonResult> allCompareResultsResults = resultComparer.compareAllImages();
		ComparisonResult compareResultsResult = allCompareResultsResults.get(0);

		// Assert that the mask worked and the image 'matches'
		compareResultsResult.printResults();
		assertTrue(compareResultsResult.isMatching());
	}

	/**
	 * Test that a mask is created properly based on images that do not match
	 * 
	 * Mask is created with a 20x20 pixel group size then compared 1x1
	 * 
	 */
	@Test
	public void test50x50CreateMask20x20() {
		System.out.println("test50x50CreateMask20x20");

		// Create Images
		String primordialImageName = "50x50-black-purple.png";
		String comparableImageName = "50x50-black-purple-errored.png";
		PrimordialImage primordialImage = new PrimordialImage(primordialImageDestination.readImage(primordialImageName),
				primordialImageName);
		ComparableImage comparableImage = new ComparableImage(comparableImageDestination.readImage(comparableImageName),
				comparableImageName);

		// Set Options
		ComparisonOptions options = new ComparisonOptions();
		options.setDiffImageName("test50x50CreateMask20x20-mask");
		options.setResultsDestination(maskDestination);
		options.setCreateMask(true);
		options.setPixelGroupSize(PixelGroupSize.P_20x20);

		// Compare
		Compare compare = new Compare();
		compare.putComparableImage(comparableImage, primordialImage, options);

		// Get Results
		List<ComparisonResult> allResults = compare.compareAllImages();
		ComparisonResult result = allResults.get(0);
		result.printResults();

		// ASSUMING COMPARISONS WORK
		// Compare the two images again, using the mask created above
		Compare resultComparer = new Compare();
		ComparisonOptions compareOptions = new ComparisonOptions();
		compareOptions.setResultsDestination(resultsDestination);
		compareOptions.setErrorColor(Color.BLUE);
		compareOptions.setDiffImageName("test50x50CreateMask20x20-diff");
		compareOptions.setImageMask(new ImageMask(maskDestination.readImage("test50x50CreateMask20x20-mask.png"),
				"test50x50CreateMask20x20-mask.png"));
		compareOptions.setPixelGroupSize(PixelGroupSize.P_1x1);

		resultComparer.putComparableImage(comparableImage, primordialImage, compareOptions);

		List<ComparisonResult> allCompareResultsResults = resultComparer.compareAllImages();
		ComparisonResult compareResultsResult = allCompareResultsResults.get(0);

		// Assert that the mask worked and the image 'matches'
		compareResultsResult.printResults();
		assertTrue(compareResultsResult.isMatching());
	}

}
