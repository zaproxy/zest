/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

/**
 * The Class ZestAssignString assigns a string (which can include other variables) to the specified variable.
 */
public class ZestAssignReplace extends ZestAssignment {

	private String replace = null;
	private String replacement = null;
	private boolean regex = false;
	private boolean caseExact = false;
	
	/**
	 * Instantiates a new zest assign random integer.
	 */
	public ZestAssignReplace() {
	}

	/**
	 * Instantiates a new zest assign random integer.
	 *
	 * @param variableName the variable name
	 */
	public ZestAssignReplace(String variableName) {
		super(variableName);
	}

	/**
	 * Instantiates a new zest assign random integer.
	 *
	 * @param variableName the variable name
	 * @param minInt the min int
	 * @param maxInt the max int
	 */
	public ZestAssignReplace(String variableName, String replace, String replacement, boolean regex, boolean caseExact) {
		super(variableName);
		this.replace = replace;
		this.replacement = replacement;
		this.regex = regex;
		this.caseExact = caseExact;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestTransformation#transform(org.mozilla.zest.core.v1.ZestRunner, org.mozilla.zest.core.v1.ZestRequest)
	 */
	@Override
	public String assign (ZestResponse response, ZestRuntime runtime) throws ZestAssignFailException {
		String var = runtime.getVariable(getVariableName());
		if (var == null) {
			return null;
		}
		String orig = runtime.replaceVariablesInString(var, false);
		// TODO handle caseExact/ignore
		if (regex) {
			try {
				return orig.replaceAll(replace, replacement);
			} catch (Exception e) {
				throw new ZestAssignFailException (this, e.getMessage());
			}
		} else {
			return orig.replace(this.replace, this.replacement);
		}
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestElement#deepCopy()
	 */
	@Override
	public ZestAssignReplace deepCopy() {
		return new ZestAssignReplace(this.getVariableName(), this.replace, this.replacement, this.regex, this.caseExact);
	}

	public String getReplace() {
		return replace;
	}

	public void setReplace(String replace) {
		this.replace = replace;
	}

	public String getReplacement() {
		return replacement;
	}

	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}

	public boolean isRegex() {
		return regex;
	}

	public void setRegex(boolean regex) {
		this.regex = regex;
	}

	public boolean isCaseExact() {
		return caseExact;
	}

	public void setCaseExact(boolean caseExact) {
		this.caseExact = caseExact;
	}

}
