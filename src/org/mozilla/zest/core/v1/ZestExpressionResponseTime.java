/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.util.regex.Pattern;

import org.mozilla.zest.impl.ZestUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class ZestExpressionResponseTime.
 */
public class ZestExpressionResponseTime extends ZestExpression {

	private final static Pattern pattern = Pattern
			.compile("(NOT\\s)?Response\\sTime\\s[<>]\\s"
					+ ZestUtils.START_VARIABLE_REGEX + "\\d+"
					+ ZestUtils.END_VARIABLE_REGEX);

	/** The greater than. */
	private boolean greaterThan = true;

	/** The time in ms. */
	private long timeInMs;

	/**
	 * Instantiates a new zest expression response time.
	 */
	public ZestExpressionResponseTime() {
		super();
	}

	/**
	 * Instantiates a new zest expression response time.
	 * 
	 * @param time
	 *            the time
	 */
	public ZestExpressionResponseTime(long time) {
		super();
		this.timeInMs = time;
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
		if (greaterThan) {
			return response.getResponseTimeInMs() > this.timeInMs;
		} else {
			return response.getResponseTimeInMs() < this.timeInMs;
		}
	}

	/**
	 * Checks if is greater than.
	 * 
	 * @return true, if is greater than
	 */
	public boolean isGreaterThan() {
		return greaterThan;
	}

	/**
	 * Sets the greater than.
	 * 
	 * @param greaterThan
	 *            the new greater than
	 */
	public void setGreaterThan(boolean greaterThan) {
		this.greaterThan = greaterThan;
	}

	/**
	 * Gets the time in ms.
	 * 
	 * @return the time in ms
	 */
	public long getTimeInMs() {
		return timeInMs;
	}

	/**
	 * Sets the time in ms.
	 * 
	 * @param timeInMs
	 *            the new time in ms
	 */
	public void setTimeInMs(long timeInMs) {
		this.timeInMs = timeInMs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.zest.core.v1.ZestExpression#deepCopy()
	 */
	@Override
	public ZestExpressionResponseTime deepCopy() {
		ZestExpressionResponseTime copy = new ZestExpressionResponseTime();
		copy.greaterThan = this.greaterThan;
		copy.timeInMs = this.timeInMs;
		return copy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String expression = (isInverse() ? "NOT " : "") + "Response Time "
				+ (isGreaterThan() ? "> " : "< ") + ZestUtils.START_VARIABLE
				+ timeInMs + ZestUtils.END_VARIABLE;
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
