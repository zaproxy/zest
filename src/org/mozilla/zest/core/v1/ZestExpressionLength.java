/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.util.regex.Pattern;

import org.mozilla.zest.impl.ZestUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class ZestExpressionLength.
 */
public class ZestExpressionLength extends ZestExpression {

	private final static Pattern pattern = Pattern
			.compile("(NOT\\s)?Length:\\s" + ZestUtils.START_VARIABLE_REGEX + "\\d+"
					+ ZestUtils.END_VARIABLE_REGEX + "\\s\\+/\\-\\s"
					+ ZestUtils.START_VARIABLE_REGEX + "\\d+"
					+ ZestUtils.END_VARIABLE_REGEX + "\\sin\\s"
					+ ZestUtils.START_VARIABLE_REGEX + "\\S+"
					+ ZestUtils.END_VARIABLE_REGEX);
	private String variableName;
	/** The length. */
	private int length;

	/** The approx. */
	private int approx;

	// /** The variable name. */
	// private String variableName;

	/**
	 * Instantiates a new zest expression length.
	 */
	public ZestExpressionLength() {
		this(null, 0, 0);
	}

	/**
	 * Instantiates a new zest expression length.
	 * 
	 * @param variableName
	 *            the variable name
	 * @param length
	 *            the length
	 * @param approx
	 *            the approx
	 */
	public ZestExpressionLength(String variableName, int length, int approx) {
		super();
		// super.setVariableName(variableName);
		this.variableName = variableName;
		this.length = length;
		this.approx = approx;
	}

	/**
	 * Instantiates a new zest expression length.
	 * 
	 * @param variableName
	 *            the variable name
	 * @param length
	 *            the length
	 * @param j
	 *            the j
	 * @param b
	 *            the b
	 */
	public ZestExpressionLength(String variableName, int length, int j,
			boolean b) {
		this(variableName, length, j);
		this.setInverse(b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.zest.core.v1.ZestExpression#deepCopy()
	 */
	public ZestExpressionLength deepCopy() {
		return new ZestExpressionLength(getVariableName(), this.length,
				this.approx);
		// return new ZestExpressionLength(this.variableName, this.length,
		// this.approx);
	}

	// /**
	// * Gets the variable name.
	// *
	// * @return the variable name
	// */
	// public String getVariableName() {
	// return variableName;
	// }

	// /**
	// * Sets the variable name.
	// *
	// * @param variableName the new variable name
	// */
	// public void setVariableName(String variableName) {
	// this.variableName = variableName;
	// }

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
	 * @param length
	 *            the new length
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
	 * @param approx
	 *            the new approx
	 */
	public void setApprox(int approx) {
		this.approx = approx;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mozilla.zest.core.v1.ZestExpressionElement#isTrue(org.mozilla.zest
	 * .core.v1.ZestResponse)
	 */
	@Override
	public boolean isTrue(ZestRuntime runtime) {
		if (getVariableName() == null) {
			return false;
		}
		String value = runtime.getVariable(getVariableName());
		if (value == null) {
			return false;
		}
		boolean toReturn = Math.abs(length - value.length()) <= length * approx
				/ 100;
		return toReturn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String expression = (isInverse() ? "NOT " : "") + "Length: "
				+ ZestUtils.START_VARIABLE + length + ZestUtils.END_VARIABLE
				+ " +/- " + ZestUtils.START_VARIABLE
				+ (((length * approx)) / 100) + ZestUtils.END_VARIABLE + " in "
				+ ZestUtils.START_VARIABLE + variableName
				+ ZestUtils.END_VARIABLE;
		return expression;
	}

	public static boolean isLiteralInstance(String s) {
		if (s == null || s.isEmpty()) {
			return false;
		}
		return pattern.matcher(s).matches();
	}

	public static Pattern getPattern() {
		return pattern;
	}

	public String getVariableName() {
		return this.variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
}
