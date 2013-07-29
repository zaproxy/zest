/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.security.SecureRandom;


// TODO: Auto-generated Javadoc
/**
 * The Class ZestTransformRndIntReplace.
 */
public class ZestTransformRndIntReplace extends ZestTransformation {

	/** The request string. */
	private String requestString;
	
	/** The min int. */
	private int minInt = 0;
	
	/** The max int. */
	private int maxInt = Integer.MAX_VALUE;
	
	/** The rnd. */
	private transient SecureRandom rnd = new SecureRandom(); 
	
	/**
	 * Instantiates a new zest transform rnd int replace.
	 */
	public ZestTransformRndIntReplace() {
	}

	/**
	 * Instantiates a new zest transform rnd int replace.
	 *
	 * @param requestString the request string
	 */
	public ZestTransformRndIntReplace(String requestString) {
		super();
		this.requestString = requestString;
	}

	/**
	 * Instantiates a new zest transform rnd int replace.
	 *
	 * @param requestString the request string
	 * @param minInt the min int
	 * @param maxInt the max int
	 */
	public ZestTransformRndIntReplace(String requestString, int minInt, int maxInt) {
		super();
		this.requestString = requestString;
		this.minInt = minInt;
		this.maxInt = maxInt;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestTransformation#transform(org.mozilla.zest.core.v1.ZestRunner, org.mozilla.zest.core.v1.ZestRequest)
	 */
	@Override
	public void transform (ZestRunner runner, ZestRequest request) throws ZestTransformFailException {
		int val = minInt + rnd.nextInt(maxInt - minInt);
		request.setData(request.getData().replace(this.requestString, Integer.toString(val)));
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestElement#deepCopy()
	 */
	@Override
	public ZestTransformRndIntReplace deepCopy() {
		return new ZestTransformRndIntReplace(this.requestString, this.minInt, this.maxInt);
	}

	/**
	 * Gets the request string.
	 *
	 * @return the request string
	 */
	public String getRequestString() {
		return requestString;
	}

	/**
	 * Sets the request string.
	 *
	 * @param requestString the new request string
	 */
	public void setRequestString(String requestString) {
		this.requestString = requestString;
	}

	/**
	 * Gets the min int.
	 *
	 * @return the min int
	 */
	public int getMinInt() {
		return minInt;
	}

	/**
	 * Sets the min int.
	 *
	 * @param minInt the new min int
	 */
	public void setMinInt(int minInt) {
		this.minInt = minInt;
	}

	/**
	 * Gets the max int.
	 *
	 * @return the max int
	 */
	public int getMaxInt() {
		return maxInt;
	}

	/**
	 * Sets the max int.
	 *
	 * @param maxInt the new max int
	 */
	public void setMaxInt(int maxInt) {
		this.maxInt = maxInt;
	}

}
