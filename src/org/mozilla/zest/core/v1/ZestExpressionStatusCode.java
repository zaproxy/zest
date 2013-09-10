/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.util.regex.Pattern;

import org.mozilla.zest.impl.ZestUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class ZestExpressionStatusCode.
 */
public class ZestExpressionStatusCode extends ZestExpression {
	private final static Pattern pattern = Pattern
			.compile("(NOT\\s)?Status\\sCode:\\s" + ZestUtils.START_VARIABLE_REGEX
					+ "\\d+" + ZestUtils.END_VARIABLE_REGEX);

	/** The code. */
	private int code;

	/**
	 * Instantiates a new zest expression status code.
	 */
	public ZestExpressionStatusCode() {
		super();
	}

	/**
	 * Instantiates a new zest expression status code.
	 * 
	 * @param code
	 *            the code
	 */
	public ZestExpressionStatusCode(int code) {
		super();
		this.code = code;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mozilla.zest.core.v1.ZestExpressionElement#isTrue(org.mozilla.zest
	 * .core.v1.ZestResponse)
	 */
	public boolean isTrue(ZestRuntime runtime) {
		ZestResponse response = runtime.getLastResponse();
		if (response == null) {
			return false;
		}
		return code == response.getStatusCode();
	}

	/**
	 * Gets the code.
	 * 
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Sets the code.
	 * 
	 * @param code
	 *            the new code
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.zest.core.v1.ZestExpression#deepCopy()
	 */
	@Override
	public ZestExpressionStatusCode deepCopy() {
		ZestExpressionStatusCode copy = new ZestExpressionStatusCode();
		copy.code = code;
		return copy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String expression = (isInverse() ? "NOT " : "") + "Status Code: "
				+ ZestUtils.START_VARIABLE + code + ZestUtils.END_VARIABLE;
		return expression;
	}

	public static boolean isLiteralInstance(String literal) {
		if (literal == null || literal.isEmpty()) {
			return false;
		}
		return pattern.matcher(literal).matches();
	}

	public static Pattern getPattern() {
		return pattern;
	}
}
