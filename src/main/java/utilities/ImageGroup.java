/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package utilities;

import java.util.ArrayList;
import java.util.List;

import images.AlphaImage;
import images.ComparableImage;

/**
 * Class to store images that are meant to be compared against each other
 * 
 * @author Thomas Rokicki
 *
 */
public class ImageGroup {

	private List<ComparableImage> comparableImages;
	private AlphaImage alphaImage;

	/**
	 * Image group
	 * 
	 * @param comparableImageList
	 *            a List of ComparableImage(s)
	 * @param alphaImage
	 *            The image the comparableImage(s) will be compared to.
	 */
	public ImageGroup(List<ComparableImage> comparableImageList, AlphaImage alphaImage) {
		this.comparableImages = comparableImageList;
		this.alphaImage = alphaImage;
	}

	/**
	 * Image group
	 * 
	 * @param comparableImageList
	 *            a ComparableImage
	 * @param alphaImage
	 *            The image the comparableImage will be compared to.
	 */
	public ImageGroup(ComparableImage comparableImage, AlphaImage alphaImage) {
		comparableImages = new ArrayList<ComparableImage>();
		comparableImages.add(comparableImage);
		this.alphaImage = alphaImage;
	}

	/**
	 * Gets the alpha image for the group.
	 * 
	 * @return AlphaImage
	 */
	public AlphaImage getAlphaImage() {
		return alphaImage;
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
