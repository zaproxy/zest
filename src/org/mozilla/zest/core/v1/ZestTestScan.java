/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;


public class ZestTestScan extends ZestTest {

	private String targetParameter;

	public ZestTestScan() {
	}
	
	public ZestTestScan(String targetParameter) {
		super ();
		this.targetParameter = targetParameter;
	}
	
	public String getTargetParameter() {
		return targetParameter;
	}

	public void setTargetParameter(String targetParameter) {
		this.targetParameter = targetParameter;
	}

	@Override
	public ZestTestScan deepCopy() {
		return new ZestTestScan(this.targetParameter);
	}

}
