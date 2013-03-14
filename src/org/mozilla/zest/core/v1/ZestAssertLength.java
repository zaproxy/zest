/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;


public class ZestAssertLength extends ZestAssertion {

	private int length;
	private int approx;
	
	public ZestAssertLength() {
	}
	
	public ZestAssertLength(int length, int approx) {
		super ();
		this.length = length;
		this.approx = approx;
	}
	
	public ZestAssertLength deepCopy() {
		return new ZestAssertLength(this.length, this.approx);
	}
	
	@Override
	public boolean isValid (ZestResponse response) {
		if (response.getBody() == null) {
			return false;
		}
		return Math.abs(length - response.getBody().length()) <= length * approx / 100;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getApprox() {
		return approx;
	}

	public void setApprox(int approx) {
		this.approx = approx;
	}
	
}
