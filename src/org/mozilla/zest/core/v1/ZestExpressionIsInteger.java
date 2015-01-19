/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

/**
 * Represent an expression that tests to see if a variable value is an integer.
 */
public class ZestExpressionIsInteger extends ZestExpression{
	
	/** The variableName to test. */
	private String variableName;
	
	/**
	 * Instantiates a new zest expression regex.
	 */
	public ZestExpressionIsInteger(){
		this("");
	}
	
	/**
	 * Instantiates a new zest expression isInteger.
	 *
	 * @param variableName the variableName
	 */
	public ZestExpressionIsInteger(String variableName) {
		super ();
		this.variableName = variableName;
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestExpressionElement#isTrue(org.mozilla.zest.core.v1.ZestResponse)
	 */
	public boolean isTrue (ZestRuntime runtime) {
		String str = runtime.getVariable(variableName);		
		if (str == null) {
			return false;
		}
		
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
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
	public ZestExpressionIsInteger deepCopy() {
		return new ZestExpressionIsInteger(this.getVariableName());
	}
	
}
