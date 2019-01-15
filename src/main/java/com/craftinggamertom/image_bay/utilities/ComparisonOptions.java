/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package com.craftinggamertom.image_bay.utilities;

import java.awt.Color;

import com.craftinggamertom.image_bay.abettor.ImageType;
import com.craftinggamertom.image_bay.abettor.PixelGroupSize;
import com.craftinggamertom.image_bay.images.ImageMask;
import com.craftinggamertom.image_bay.images.PrimordialImage;

/**
 * Class to maintain all the set options for the comparison
 * 
 * @author Thomas Rokicki
 *
 */
public class ComparisonOptions {

	private int startX;
	private int startY;
	private int endX;
	private int endY;
	private ImageMask imageMask;
	private PixelGroupSize pixelGroupSize;
	private Color errorColor;
	private Destination resultsDestination;
	private String diffImageName;
	private ImageType imageType;
	private boolean createMask;

	/**
	 * Create the ComparisonOptions object with default values
	 * 
	 * @category
	 * 			startX = -1;
	 *           startY = -1;
	 *           endX = -1;
	 *           endY = -1;
	 *           imageMask = null;
	 *           pixelGroupSize = PixelGroupSize.ABSOLUTE;
	 *           errorColor = Color.RED.getRGB();
	 *           destination = null;
	 *           diffImageName = ">-diff";
	 *           imageType = ImageType.PNG;
	 *           createMask = false;
	 * 
	 * @category
	 * 			THE FOLLOWING VARIABLES MUST BE SET BY THE USER:
	 *           Destination
	 * 
	 */
	public ComparisonOptions() {
		startX = 0;
		startY = 0;
		endX = -1;
		endY = -1;
		imageMask = null;
		pixelGroupSize = PixelGroupSize.ABSOLUTE;
		errorColor = Color.RED;
		resultsDestination = null;
		diffImageName = ">-diff";
		imageType = ImageType.PNG;
		createMask = false;
	}

	public int getStartX() {
		return startX;
	}

	/**
	 * Set the X value to start comparing
	 * 
	 * @param startX
	 */
	public void setStartX(int startX) {
		this.startX = startX;
	}

	public int getStartY() {
		return startY;
	}

	/**
	 * Set the Y value to start comparing
	 * 
	 * @param startY
	 */
	public void setStartY(int startY) {
		this.startY = startY;
	}

	public int getEndX() {
		return endX;
	}

	/**
	 * Set the X value to stop comparing at
	 * 
	 * @param endX
	 */
	public void setEndX(int endX) {
		this.endX = endX;
	}

	public int getEndY() {
		return endY;
	}

	/**
	 * Set the Y value to stop comparing at
	 * 
	 * @param endY
	 */
	public void setEndY(int endY) {
		this.endY = endY;
	}

	public ImageMask getImageMask() {
		return imageMask;
	}

	/**
	 * Set the image that will be used at the mask when comparing
	 * 
	 * @param imageMask
	 */
	public void setImageMask(ImageMask imageMask) {
		this.imageMask = imageMask;
	}

	public PixelGroupSize getPixelGroupSize() {
		return pixelGroupSize;
	}

	/**
	 * Set the block size to check
	 * 
	 * @param pixelGroupSize
	 */
	public void setPixelGroupSize(PixelGroupSize pixelGroupSize) {
		this.pixelGroupSize = pixelGroupSize;
	}

	public Color getErrorColor() {
		return errorColor;
	}

	/**
	 * Set the color that will be used to mark differences in images.
	 * 
	 * @param errorColor
	 */
	public void setErrorColor(Color errorColor) {
		this.errorColor = errorColor;
	}

	public Destination getResultsDestination() {
		return resultsDestination;
	}

	/**
	 * Set the destination where the difference images should be saved.
	 * 
	 * @param destination
	 */
	public void setResultsDestination(Destination destination) {
		this.resultsDestination = destination;
	}

	public String getDiffImageName() {
		return diffImageName;
	}

	/**
	 * Get the difference image naming convention. We have designated the symbol
	 * '>' to represent the primordial image's name. It will be inserted at run
	 * time.
	 * 
	 * Ex: ">-diff" will result in "ImageName-diff"
	 * 
	 * @param diffImageNaming
	 */
	public void setDiffImageName(String diffImageNaming) {
		this.diffImageName = diffImageNaming;
	}

	public ImageType getImageType() {
		return imageType;
	}

	/**
	 * Set the extension of the image.
	 * 
	 * @param imageType
	 */
	public void setImageType(ImageType imageType) {
		this.imageType = imageType;
	}

	public boolean isCreateMask() {
		return createMask;
	}

	/**
	 * Set whether a mask should be created or not.
	 * Default is false
	 * 
	 * If this is set to true, the difference image will not have the original
	 * image behind it. The background will be transparent.
	 * 
	 * @param createMask
	 */
	public void setCreateMask(boolean createMask) {
		this.createMask = createMask;
	}

	/**
	 * When the ComparisonOptions are about to be used, this method should be
	 * called to ensure the required values are valid and are no longer default.
	 * 
	 * @param options
	 * @param primordialImage
	 * @return
	 */
	public static ComparisonOptions validateDefaults(ComparisonOptions options, PrimordialImage primordialImage) {
		// Primordial Image Size for size comparing
		int primordialWidth = primordialImage.getImage().getWidth();
		int primordialHeight = primordialImage.getImage().getHeight();

		// Update the naming convention
		options.setDiffImageName(options.getDiffImageName().replaceAll(">",
				primordialImage.getName().substring(0, primordialImage.getName().indexOf('.'))));
		// Update the EndX
		options.setEndX(primordialWidth);
		// Update the EndY
		options.setEndY(primordialHeight);
		// Update the ImageMask
		if (options.getImageMask() == null) {
			options.setImageMask(new ImageMask(null, "NullMask"));
		}

		return options;
	}

}
