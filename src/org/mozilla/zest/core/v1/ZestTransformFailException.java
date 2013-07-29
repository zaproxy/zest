/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

// TODO: Auto-generated Javadoc
/**
 * The Class ZestTransformFailException.
 */
public class ZestTransformFailException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The test. */
	private final ZestTransformation test;

	/**
	 * Instantiates a new zest transform fail exception.
	 *
	 * @param test the test
	 * @param message the message
	 */
	public ZestTransformFailException (ZestTransformation test, String message) {
		super(message);
		this.test = test;
	}

	/**
	 * Instantiates a new zest transform fail exception.
	 *
	 * @param test the test
	 */
	public ZestTransformFailException (ZestTransformation test) {
		super();
		this.test = test;
	}

	/**
	 * Gets the test.
	 *
	 * @return the test
	 */
	public ZestTransformation getTest() {
		return test;
	}
	
	
}
