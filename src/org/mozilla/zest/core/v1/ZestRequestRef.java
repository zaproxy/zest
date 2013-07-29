/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

// TODO: Auto-generated Javadoc
/**
 * The Interface ZestRequestRef.
 */
public interface ZestRequestRef {

	/**
	 * Gets the request.
	 *
	 * @return the request
	 */
	ZestRequest getRequest();
	
	/**
	 * Gets the request id.
	 *
	 * @return the request id
	 */
	int getRequestId();
	
	/**
	 * Sets the request.
	 *
	 * @param request the new request
	 */
	void setRequest(ZestRequest request);
}
