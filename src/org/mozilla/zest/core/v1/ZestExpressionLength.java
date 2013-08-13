/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

/**
 * The Class ZestExpressionLength.
 */
public class ZestExpressionLength extends ZestExpression {

	/** The length. */
	private int length;
	
	/** The approx. */
	private int approx;
	
	private String variableName;

	/**
	 * Instantiates a new zest expression length.
	 */
	public ZestExpressionLength() {
		this(null, 0, 0);
	}

	/**
	 * Instantiates a new zest expression length.
	 *
	 * @param length the length
	 * @param approx the approx
	 */
	public ZestExpressionLength(String variableName, int length, int approx) {
		super();
		this.variableName = variableName;
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
	public ZestExpressionLength(String variableName, int length, int j, boolean b) {
		this(variableName, length,j);
		this.setInverse(b);
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestExpression#deepCopy()
	 */
	public ZestExpressionLength deepCopy() {
		return new ZestExpressionLength(this.variableName, this.length, this.approx);
	}
	
	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
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
	public boolean isTrue(ZestRuntime runtime) {
		if (this.variableName == null) {
			return false;
		}
		String value = runtime.getVariable(variableName);
		if (value == null) {
			return false;
		}
		boolean toReturn = Math.abs(length - value.length()) <= length
				* approx / 100;
		return toReturn;
	}
	
	@Override
	public String toString(){
		String expression=(isInverse()?"NOT ":"")+ "Length: "+length+" +/- "+(((double)(length*approx))/100);
		return expression;
	}

}
