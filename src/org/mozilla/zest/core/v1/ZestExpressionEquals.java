/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;


// TODO: Auto-generated Javadoc
/**
 * The Class ZestExpressionRegex.
 */
public class ZestExpressionEquals extends ZestExpression{
	
	/** The value to compare with. */
	private String value;
	
	/** The variableName which will be assigned to. */
	private String variableName;
	
	private boolean caseExact = false;
	
	/** The inverse. */
	private boolean inverse = false;
	
	/**
	 * Instantiates a new zest expression regex.
	 */
	public ZestExpressionEquals(){
		this("", null, false, false);
	}
	
	/**
	 * Instantiates a new zest expression regex.
	 *
	 * @param variableName the variableName
	 * @param regex the regex
	 */
	public ZestExpressionEquals(String variableName, String regex) {
		this(variableName, regex, false, false);
	}
	
	/**
	 * Instantiates a new zest expression regex.
	 *
	 * @param variableName the variableName
	 * @param regex the regex
	 * @param inverse the inverse
	 */
	public ZestExpressionEquals(String variableName, String value, boolean caseExact, boolean inverse) {
		super ();
		this.inverse=inverse;
		this.variableName = variableName;
		this.value = value;
		this.caseExact = caseExact;
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
		
		if (this.caseExact) {
			return str.equals(value);
		} else {
			return str.equalsIgnoreCase(value);
		}
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isCaseExact() {
		return caseExact;
	}

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
	 * @see org.mozilla.zest.core.v1.ZestExpression#isInverse()
	 */
	@Override
	public boolean isInverse() {
		return inverse;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestExpression#setInverse(boolean)
	 */
	@Override
	public void setInverse(boolean not) {
		inverse=not;
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestExpression#deepCopy()
	 */
	@Override
	public ZestExpressionEquals deepCopy() {
		return new ZestExpressionEquals(this.getVariableName(), this.getValue(), this.isCaseExact(), this.isInverse());
	}
	
}
