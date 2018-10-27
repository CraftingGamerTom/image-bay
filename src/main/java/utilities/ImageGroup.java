/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package utilities;

import java.util.ArrayList;
import java.util.List;

import images.ComparableImage;
import images.PrimordialImage;

/**
 * Class to store images that are meant to be compared against each other
 * 
 * @author Thomas Rokicki
 *
 */
public class ImageGroup {

	private List<ComparableImage> comparableImages;
	private PrimordialImage primordialImage;

	/**
	 * Image group
	 * 
	 * @param comparableImageList
	 *            a List of ComparableImage(s)
	 * @param primordialImage
	 *            The image the comparableImage(s) will be compared to.
	 */
	public ImageGroup(List<ComparableImage> comparableImageList, PrimordialImage primordialImage) {
		this.comparableImages = comparableImageList;
		this.primordialImage = primordialImage;
	}

	/**
	 * Image group
	 * 
	 * @param comparableImageList
	 *            a ComparableImage
	 * @param primordialImage
	 *            The image the comparableImage will be compared to.
	 */
	public ImageGroup(ComparableImage comparableImage, PrimordialImage primordialImage) {
		comparableImages = new ArrayList<ComparableImage>();
		comparableImages.add(comparableImage);
		this.primordialImage = primordialImage;
	}

	/**
	 * Gets the primordial image for the group.
	 * 
	 * @return PrimordialImage
	 */
	public PrimordialImage getPrimordialImage() {
		return primordialImage;
	}

	/**
	 * Gets the comparable image for the group. If there is a list of comparable
	 * images, this method will only ever return the first image in the list.
	 * 
	 * @return ComparableImage
	 */
	public ComparableImage getComparableImage() {
		return comparableImages.get(0);
	}

	/**
	 * Gets the comparable image in the specified index.
	 * 
	 * @param index
	 * @return ComparableImage
	 */
	public ComparableImage getComparableImage(int index) {
		return comparableImages.get(index);
	}

	/**
	 * Gets the list of comparable images in the image group.
	 * 
	 * @return List<ComparableImage>
	 */
	public List<ComparableImage> getAllComparableImages() {
		return comparableImages;
	}

}
