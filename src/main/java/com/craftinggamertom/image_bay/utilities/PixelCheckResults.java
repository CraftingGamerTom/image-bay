/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package com.craftinggamertom.image_bay.utilities;

import java.awt.image.BufferedImage;

public class PixelCheckResults {

	private boolean errorFound;
	private BufferedImage differenceImage;

	public PixelCheckResults(BufferedImage differenceImage, boolean errorFound) {

		this.differenceImage = differenceImage;
		this.errorFound = errorFound;
	}

	public boolean isErrorFound() {
		return errorFound;
	}

	public BufferedImage getDifferenceImage() {
		return differenceImage;
	}

}
