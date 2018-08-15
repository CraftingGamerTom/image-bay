/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package entity;

import java.util.ArrayList;
import java.util.List;

import images.ComparableImage;
import images.DifferenceImage;
import images.PrimordialImage;
import utilities.ComparisonOptions;

public class CompareVariables {

	private PrimordialImage primordialImage;
	private ComparableImage comparableImage;
	private DifferenceImage differenceImage;
	private ComparisonOptions comparisonOptions;
	private List<String> comparableImageNames;
	private List<DifferenceImage> allDifferenceImages;
	private boolean isSame = true;
	private boolean innerErrorFound = false;
	private int pageX;
	private int pageY;
	private int blockX;
	private int blockY;

	CompareVariables() {
		comparableImageNames = new ArrayList<String>();
		allDifferenceImages = new ArrayList<DifferenceImage>();
	}

	public ComparisonOptions getComparisonOptions() {
		return comparisonOptions;
	}

	public void setComparisonOptions(ComparisonOptions comparisonOptions) {
		this.comparisonOptions = comparisonOptions;
	}

	public int getPageX() {
		return pageX;
	}

	public void setPageX(int pageX) {
		this.pageX = pageX;
	}

	public int getPageY() {
		return pageY;
	}

	public void setPageY(int pageY) {
		this.pageY = pageY;
	}

	public int getBlockX() {
		return blockX;
	}

	public void setBlockX(int blockX) {
		this.blockX = blockX;
	}

	public int getBlockY() {
		return blockY;
	}

	public void setBlockY(int blockY) {
		this.blockY = blockY;
	}

	public boolean isSame() {
		return isSame;
	}

	public void setSame(boolean isSame) {
		this.isSame = isSame;
	}

	public boolean isInnerErrorFound() {
		return innerErrorFound;
	}

	public void setInnerErrorFound(boolean innerErrorFound) {
		this.innerErrorFound = innerErrorFound;
	}

	public List<String> getComparableImageNames() {
		return comparableImageNames;
	}

	public void setComparableImageNames(List<String> comparableImageNames) {
		this.comparableImageNames = comparableImageNames;
	}

	public PrimordialImage getPrimordialImage() {
		return primordialImage;
	}

	public void setPrimordialImage(PrimordialImage primordialImage) {
		this.primordialImage = primordialImage;
	}

	public ComparableImage getComparableImage() {
		return comparableImage;
	}

	public void setComparableImage(ComparableImage comparableImage) {
		this.comparableImage = comparableImage;
	}

	public DifferenceImage getDifferenceImage() {
		return differenceImage;
	}

	public void setDifferenceImage(DifferenceImage differenceImage) {
		this.differenceImage = differenceImage;
	}

	public List<DifferenceImage> getAllDifferenceImages() {
		return allDifferenceImages;
	}

	public void setAllDifferenceImages(List<DifferenceImage> allDifferenceImages) {
		this.allDifferenceImages = allDifferenceImages;
	}

}
