/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;


public class ZestAssertStatusCode extends ZestAssertion {

	private int code;
	
	public ZestAssertStatusCode() {
	}
	
	public ZestAssertStatusCode(int code) {
		super ();
		this.code = code;
	}
	
	@Override
	public boolean isValid (ZestResponse response) {
		return code == response.getStatusCode();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	@Override
	ZestAssertStatusCode deepCopy() {
		return new ZestAssertStatusCode(this.code);
	}

}
