/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

public class ZestAssertFailException extends Exception {

	private static final long serialVersionUID = 1L;

	private ZestAssertion assertion;
	
	public ZestAssertFailException(ZestAssertion assertion) {
		this.assertion = assertion;
	}

	public ZestAssertion getAssertion() {
		return assertion;
	}
}
