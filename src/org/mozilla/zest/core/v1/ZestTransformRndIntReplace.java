/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.security.SecureRandom;


public class ZestTransformRndIntReplace extends ZestTransformation {

	private String requestString;
	private int minInt = 0;
	private int maxInt = Integer.MAX_VALUE;
	
	private transient SecureRandom rnd = new SecureRandom(); 
	
	public ZestTransformRndIntReplace() {
	}

	public ZestTransformRndIntReplace(String requestString) {
		super();
		this.requestString = requestString;
	}

	public ZestTransformRndIntReplace(String requestString, int minInt, int maxInt) {
		super();
		this.requestString = requestString;
		this.minInt = minInt;
		this.maxInt = maxInt;
	}

	@Override
	public void transform (ZestRunner runner, ZestRequest request) throws ZestTransformFailException {
		int val = minInt + rnd.nextInt(maxInt - minInt);
		request.setData(request.getData().replace(this.requestString, Integer.toString(val)));
	}

	@Override
	public ZestTransformRndIntReplace deepCopy() {
		return new ZestTransformRndIntReplace(this.requestString, this.minInt, this.maxInt);
	}

	public String getRequestString() {
		return requestString;
	}

	public void setRequestString(String requestString) {
		this.requestString = requestString;
	}

	public int getMinInt() {
		return minInt;
	}

	public void setMinInt(int minInt) {
		this.minInt = minInt;
	}

	public int getMaxInt() {
		return maxInt;
	}

	public void setMaxInt(int maxInt) {
		this.maxInt = maxInt;
	}

}
