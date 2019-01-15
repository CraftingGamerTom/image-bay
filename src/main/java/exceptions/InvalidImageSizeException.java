/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package exceptions;

public class InvalidImageSizeException extends Exception {
	/**
	 * Auto-Generated
	 */
	private static final long serialVersionUID = -494576213844055068L;

	/**
	 * An Exception with default error message
	 */
	public InvalidImageSizeException(Throwable e) {
		super("Image sizes did not match.", e);
	}

	/**
	 * An Exception with custom error message
	 */
	public InvalidImageSizeException(String errorMessage, Throwable e) {
		super(errorMessage, e);
	}

	/**
	 * An Not Found Exception with default error message
	 */
	public InvalidImageSizeException() {
		super("Image sizes did not match.");
	}

	/**
	 * An Not Found Exception with custom error message
	 */
	public InvalidImageSizeException(String errorMessage) {
		super(errorMessage);
	}
}
