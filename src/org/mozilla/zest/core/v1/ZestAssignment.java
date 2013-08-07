/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;


// TODO: Auto-generated Javadoc
/**
 * The Class ZestAction.
 */
public abstract class ZestAssignment extends ZestStatement {

	/** The variable name. */
	private String variableName;

	/**
	 * Instantiates a new zest action.
	 */
	public ZestAssignment() {
		super();
	}

	public ZestAssignment(String variableName) {
		super();
		this.variableName = variableName;
	}

	/**
	 * Instantiates a new zest action.
	 *
	 * @param index the index
	 */
	public ZestAssignment(int index) {
		super(index);
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#isSameSubclass(org.mozilla.zest.core.v1.ZestElement)
	 */
	@Override
	public boolean isSameSubclass(ZestElement ze) {
		return ze instanceof ZestAssignment;
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#setPrefix(java.lang.String, java.lang.String)
	 */
	@Override
	void setPrefix(String oldPrefix, String newPrefix) throws MalformedURLException {
		// Ignore
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#getTokens(java.lang.String, java.lang.String)
	 */
	@Override
	Set<String> getTokens(String tokenStart, String tokenEnd) {
		return new HashSet<String>();
	}

	/**
	 * Returns the variable name
	 */
	public String getVariableName() {
		return variableName;
	}

	/**
	 * Sets the variable name
	 */
	public void setVariableName(String name) {
		this.variableName = name;
	}

	/**
	 * Invoke.
	 *
	 * @param response the response
	 * @return the string
	 * @throws ZestActionFailException the zest action fail exception
	 */
	public abstract String assign(ZestResponse response) throws ZestAssignFailException;
	
}
