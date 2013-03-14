/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

public class ZestTestFailException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private final ZestTest test;

	public ZestTestFailException (ZestTest test) {
		this.test = test;
	}

	public ZestTest getTest() {
		return test;
	}
	
	
}
