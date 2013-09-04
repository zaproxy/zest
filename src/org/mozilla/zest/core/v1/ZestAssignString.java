/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

/**
 * The Class ZestAssignString assigns a string (which can include other variables) to the specified variable.
 */
public class ZestAssignString extends ZestAssignment {

	private String string = null;
	
	/**
	 * Instantiates a new zest assign random integer.
	 */
	public ZestAssignString() {
	}

	/**
	 * Instantiates a new zest assign random integer.
	 *
	 * @param variableName the variable name
	 */
	public ZestAssignString(String variableName) {
		super(variableName);
	}

	/**
	 * Instantiates a new zest assign random integer.
	 *
	 * @param variableName the variable name
	 * @param minInt the min int
	 * @param maxInt the max int
	 */
	public ZestAssignString(String variableName, String string) {
		super(variableName);
		this.string = string;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestTransformation#transform(org.mozilla.zest.core.v1.ZestRunner, org.mozilla.zest.core.v1.ZestRequest)
	 */
	@Override
	public String assign (ZestResponse response, ZestRuntime runtime) {
		return runtime.replaceVariablesInString(this.string, false);
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestElement#deepCopy()
	 */
	@Override
	public ZestAssignString deepCopy() {
		return new ZestAssignString(this.getVariableName(), this.string);
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}
}
