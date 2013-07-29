/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

// TODO: Auto-generated Javadoc
/**
 * The Class ZestExpressionLength.
 */
public class ZestExpressionLength extends ZestExpression {

	/** The length. */
	private int length;
	
	/** The approx. */
	private int approx;

	/**
	 * Instantiates a new zest expression length.
	 */
	public ZestExpressionLength() {
		this(0, 0);
	}

	/**
	 * Instantiates a new zest expression length.
	 *
	 * @param length the length
	 * @param approx the approx
	 */
	public ZestExpressionLength(int length, int approx) {
		super();
		this.length = length;
		this.approx = approx;
	}

	/**
	 * Instantiates a new zest expression length.
	 *
	 * @param length the length
	 * @param j the j
	 * @param b the b
	 */
	public ZestExpressionLength(int length, int j, boolean b) {
		this(length,j);
		this.setInverse(b);
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestExpression#deepCopy()
	 */
	public ZestExpressionLength deepCopy() {
		return new ZestExpressionLength(this.length, this.approx);
	}
	
	/**
	 * Gets the length.
	 *
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Sets the length.
	 *
	 * @param length the new length
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * Gets the approx.
	 *
	 * @return the approx
	 */
	public int getApprox() {
		return approx;
	}

	/**
	 * Sets the approx.
	 *
	 * @param approx the new approx
	 */
	public void setApprox(int approx) {
		this.approx = approx;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestExpressionElement#isTrue(org.mozilla.zest.core.v1.ZestResponse)
	 */
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
