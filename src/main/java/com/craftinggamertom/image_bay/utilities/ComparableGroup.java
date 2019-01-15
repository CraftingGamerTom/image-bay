/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package com.craftinggamertom.image_bay.utilities;

/**
 * Object holding the images and the information needed to compare two images.
 * 
 * @author Thomas Rokicki
 *
 */
public class ComparableGroup {

	private ImageGroup imageGroup;
	private ComparisonOptions comparisonOptions;

	public ComparableGroup(ImageGroup imageGroup, ComparisonOptions comparisonOptions) {
		this.imageGroup = imageGroup;
		this.comparisonOptions = comparisonOptions;
	}

	/**
	 * Get the image group for the comparable group
	 * 
	 * @return ImageGroup
	 */
	public ImageGroup getImageGroup() {
		return imageGroup;
	}

	/**
	 * Get the comparison options for the comparable group
	 * 
	 * @return ComparisonOptions
	 */
	public ComparisonOptions getComparisonOptions() {
		return comparisonOptions;
	}

}
