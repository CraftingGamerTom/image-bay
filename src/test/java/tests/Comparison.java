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
			Comparison.class.getClassLoader().getResource("primordial-images").getPath());
	private static Destination comparableImageDestination = new Destination(
			Comparison.class.getClassLoader().getResource("comparable-images").getPath());
	private static Destination maskDestination = new Destination(
			Comparison.class.getClassLoader().getResource("mask-images").getPath());
	private static Destination resultsDestination = new Destination(
			Comparison.class.getClassLoader().getResource("results").getPath());

	/**
	 * Test that two identical images are found to be the same
	 */
	@Test
	public void testIdentical4x4ImageAbsolute() {

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
		assertTrue(result.isMatching());
	}

	/**
	 * Test that two identical images are found to be the same
	 */
	@Test
	public void testIdentical50x50Image20x20() {

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
		assertTrue(result.isMatching());
	}

	/**
	 * Test that partial area of two identical images are found to be the same
	 */
	@Test
	public void testPartial50x50Identical20x20() {

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
		assertTrue(result.isMatching());
	}

	/**
	 * Test that different sized images are found to be different
	 */
	@Test
	public void testDifferentSizedImages() {

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
		assertFalse(result.isMatching());
	}

	/**
	 * Test that a mask is created properly based on images that do not match
	 */
	@Test
	public void test50x50CreateMask1x1() {

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
		options.setResultsDestination(resultsDestination);
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
		options.setResultsDestination(resultsDestination);
		options.setCreateMask(true);
		options.setPixelGroupSize(PixelGroupSize.P_20x20);

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
		compareOptions.setResultsDestination(resultsDestination);
		compareOptions.setErrorColor(Color.BLUE);
		compareOptions.setDiffImageName("test50x50CreateMask20x20-diff");
		compareOptions.setImageMask(
				new ImageMask(result.getDifferenceImage(0).getImage(), result.getDifferenceImage(0).getName()));
		compareOptions.setPixelGroupSize(PixelGroupSize.P_1x1);

		resultComparer.putComparableImage(comparableImage, primordialImage, compareOptions);

		List<ComparisonResult> allCompareResultsResults = resultComparer.compareAllImages();
		ComparisonResult compareResultsResult = allCompareResultsResults.get(0);

		// Assert that the mask worked and the image 'matches'
		assertTrue(compareResultsResult.isMatching());
	}

}
