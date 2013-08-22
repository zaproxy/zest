/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class ZestExpressionRegex.
 */
public class ZestExpressionRegex extends ZestExpression{
	
	private final static String regexLiteral="(NOT\\s)?REGEX:\\s.*";

	/** The regex. */
	private String regex;
	
	/** The variableName. */
	private String variableName;
	
	/** The case exact. */
	private boolean caseExact = false;

	/** The pattern. */
	private transient Pattern pattern = null;

	/**
	 * Instantiates a new zest expression regex.
	 */
	public ZestExpressionRegex(){
		this("", null, false, false);
	}
	
	/**
	 * Instantiates a new zest expression regex.
	 *
	 * @param variableName the variableName
	 * @param regex the regex
	 */
	public ZestExpressionRegex(String variableName, String regex) {
		this(variableName, regex, false, false);
	}
	
	/**
	 * Instantiates a new zest expression regex.
	 *
	 * @param variableName the variableName
	 * @param regex the regex
	 * @param caseExact the case exact
	 * @param inverse the inverse
	 */
	public ZestExpressionRegex(String variableName, String regex, boolean caseExact, boolean inverse) {
		super ();
		this.variableName = variableName;
		this.caseExact = caseExact;
		this.setInverse(inverse);
		this.regex = regex;
		if (regex != null) {
			if (caseExact) {
				this.pattern = Pattern.compile(regex);
			} else {
				this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestExpressionElement#isTrue(org.mozilla.zest.core.v1.ZestResponse)
	 */
	public boolean isTrue (ZestRuntime runtime) {
		ZestResponse response = runtime.getLastResponse();
		if (response == null) {
			return false;
		}
		String str = runtime.getVariable(variableName);		
		if (str == null) {
			return false;
		}
		if (pattern == null && regex != null) {
			if (caseExact) {
				this.pattern = Pattern.compile(regex);
			} else {
				this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			}
		}
		
		return pattern.matcher(str).find();
	}

	/**
	 * Gets the variable name.
	 *
	 * @return the variable name
	 */
	public String getVariableName() {
		return variableName;
	}

	/**
	 * Sets the variable name.
	 *
	 * @param variableName the new variable name
	 */
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	/**
	 * Gets the regex.
	 *
	 * @return the regex
	 */
	public String getRegex() {
		return regex;
	}

	/**
	 * Sets the regex.
	 *
	 * @param regex the new regex
	 */
	public void setRegex(String regex) {
		this.regex = regex;
		this.pattern = Pattern.compile(regex);
	}


	/**
	 * Checks if is case exact.
	 *
	 * @return true, if is case exact
	 */
	public boolean isCaseExact() {
		return caseExact;
	}

	/**
	 * Sets the case exact.
	 *
	 * @param caseExact the new case exact
	 */
	public void setCaseExact(boolean caseExact) {
		this.caseExact = caseExact;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestExpression#isLeaf()
	 */
	@Override
	public boolean isLeaf() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestExpression#deepCopy()
	 */
	@Override
	public ZestExpressionRegex deepCopy() {
		return new ZestExpressionRegex(this.getVariableName(), this.getRegex(), this.isCaseExact(), this.isInverse());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		String expression=(isInverse()?"NOT ":"")+"REGEX: "+regex;
		return expression;
	}
	public static boolean isLiteralInstance(String s){
		return s.matches(regexLiteral);
	}
	
}
