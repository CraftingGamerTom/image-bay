/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package utilities;

import java.awt.Color;

import abettor.ImageType;
import abettor.Precision;
import images.AlphaImage;
import images.ImageMask;

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
	private Precision precision;
	private int errorColor;
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
	 *           precision = Precision.ABSOLUTE;
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
		precision = Precision.ABSOLUTE;
		errorColor = Color.RED.getRGB();
		resultsDestination = null;
		diffImageName = ">-diff";
		imageType = ImageType.PNG;
		createMask = false;
	}

	public int getStartX() {
		return startX;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

	public int getStartY() {
		return startY;
	}

	public void setStartY(int startY) {
		this.startY = startY;
	}

	public int getEndX() {
		return endX;
	}

	public void setEndX(int endX) {
		this.endX = endX;
	}

	public int getEndY() {
		return endY;
	}

	public void setEndY(int endY) {
		this.endY = endY;
	}

	public ImageMask getImageMask() {
		return imageMask;
	}

	public void setImageMask(ImageMask imageMask) {
		this.imageMask = imageMask;
	}

	public Precision getPrecision() {
		return precision;
	}

	public void setPrecision(Precision precision) {
		this.precision = precision;
	}

	public int getErrorColor() {
		return errorColor;
	}

	public void setErrorColor(int errorColor) {
		this.errorColor = errorColor;
	}

	public Destination getResultsDestination() {
		return resultsDestination;
	}

	public void setResultsDestination(Destination destination) {
		this.resultsDestination = destination;
	}

	public String getDiffImageName() {
		return diffImageName;
	}

	/**
	 * Get the difference image naming convention. We have designated the symbol
	 * '>' to represent the alpha image's name. It will be inserted at run
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

	public void setImageType(ImageType imageType) {
		this.imageType = imageType;
	}

	public boolean isCreateMask() {
		return createMask;
	}

	public void setCreateMask(boolean createMask) {
		this.createMask = createMask;
	}

	/**
	 * When the ComparisonOptions are about to be used, this method should be
	 * called to ensure the required values are valid and are no longer default.
	 * 
	 * @param options
	 * @param alphaImage
	 * @return
	 */
	public static ComparisonOptions validateDefaults(ComparisonOptions options, AlphaImage alphaImage) {
		// Alpha Image Size for size comparing
		int alphaWidth = alphaImage.getImage().getWidth();
		int alphaHeight = alphaImage.getImage().getHeight();

		// Update the naming convention
		options.setDiffImageName(options.getDiffImageName().replaceAll(">",
				alphaImage.getName().substring(0, alphaImage.getName().indexOf('.'))));
		// Update the EndX
		options.setEndX(alphaWidth);
		// Update the EndY
		options.setEndY(alphaHeight);
		// Update the ImageMask
		if (options.getImageMask() == null) {
			options.setImageMask(new ImageMask(null, "NullMask"));
		}

		return options;
	}

}
