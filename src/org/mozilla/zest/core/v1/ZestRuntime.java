/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;


// TODO: Auto-generated Javadoc
/**
 * The Interface ZestRuntime.
 */
public interface ZestRuntime {
	
	/**
	 * Get the current value of the specified variable.
	 *
	 * @param name the name
	 * @return the current value of the specified variable
	 */
	String getVariable(String name);
	
	/**
	 * Set the value of the specified variable.
	 * @param name
	 * @param value
	 */
	void setVariable(String name, String value);

	/**
	 * Get the last response.
	 *
	 * @return the last response
	 */
	ZestResponse getLastResponse();
	
	/**
	 * Replace any variables in the supplied string
	 * @param str
	 * @param urlEncode
	 * @return the string with the variables replaces
	 */
	String replaceVariablesInString (String str, boolean urlEncode);

	/**
	 * Outputs the specified string
	 * @param str
	 */
	void output(String str);

}
