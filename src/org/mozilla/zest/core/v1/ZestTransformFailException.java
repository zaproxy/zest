/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

public class ZestTransformFailException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private final ZestTransformation test;

	public ZestTransformFailException (ZestTransformation test) {
		this.test = test;
	}

	public ZestTransformation getTest() {
		return test;
	}
	
	
}
