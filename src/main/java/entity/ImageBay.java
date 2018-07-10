/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package entity;

import java.util.Map;

import utilities.ImageGroup;
import utilities.OptionGroup;

public class ImageBay {

	private Map<ImageGroup, OptionGroup> a;
	private Compare compare;

	/**
	 * ImageBay object.
	 * This object is responsible for easily using the ImageBay API to compare
	 * images.
	 * 
	 * If you so desire, use the Compare object instead of this Assessor class.
	 */
	public ImageBay() {
		compare = new Compare();
	}

}
