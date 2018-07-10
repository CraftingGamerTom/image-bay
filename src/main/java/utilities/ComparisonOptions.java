/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package utilities;

import abettor.Precision;
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
	private Destination destination;

	public ComparisonOptions() {}

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

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}
}
