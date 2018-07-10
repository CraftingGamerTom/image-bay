/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package entity;

import java.util.ArrayList;
import java.util.List;

import images.AlphaImage;
import images.ComparableImage;
import utilities.ComparableGroup;
import utilities.ComparisonOptions;
import utilities.ImageGroup;

public class Compare {

	private List<ComparableGroup> listToCompare;

	public Compare() {
		listToCompare = new ArrayList<ComparableGroup>();
	}

	/**
	 * Puts two images into the list to be compared against each other.
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
	 * Puts two images into the list to be compared against each other.
	 * 
	 * @param image1
	 * @param image2
	 * @param options
	 *            The options that will specifically be used to compare the two
	 *            images provided
	 */
	public void putComparableImages(ComparableImage image1, AlphaImage image2, ComparisonOptions options) {

	}

	public void putComparableImages() {

	}

	public void putComparableImages() {

	}
}
