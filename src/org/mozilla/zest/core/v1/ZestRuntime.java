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
	 * Get the last response.
	 *
	 * @return the last response
	 */
	ZestResponse getLastResponse();
	
}
