/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

public class ZestExpressionLength extends ZestExpression {

	private int length;
	private int approx;

	public ZestExpressionLength() {
		this(0, 0);
	}

	public ZestExpressionLength(int length, int approx) {
		super();
		this.length = length;
		this.approx = approx;
	}

	public ZestExpressionLength(int length, int j, boolean b) {
		this(length,j);
		this.setInverse(b);
	}

	public ZestExpressionLength deepCopy() {
		return new ZestExpressionLength(this.length, this.approx);
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

	@Override
	public boolean isTrue(ZestResponse response) {
		if (response.getBody() == null) {
			return false;
		}
		boolean toReturn = Math.abs(length - response.getBody().length()) <= length
				* approx / 100;
		return toReturn;
	}

}
