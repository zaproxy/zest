/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

public class ZestActionScan extends ZestAction {

	private String targetParameter;

	public ZestActionScan() {
		super();
	}
	
	public ZestActionScan(String targetParameter) {
		super ();
		this.targetParameter = targetParameter;
	}
	
	public ZestActionScan(int index) {
		super(index);
	}

	public String getTargetParameter() {
		return targetParameter;
	}

	public void setTargetParameter(String targetParameter) {
		this.targetParameter = targetParameter;
	}

	@Override
	public ZestActionScan deepCopy() {
		ZestActionScan copy = new ZestActionScan(this.getIndex());
		copy.targetParameter = this.targetParameter;
		return copy;
	}

	@Override
	public String invoke(ZestResponse response) throws ZestActionFailException {
		throw new ZestActionFailException(this);
	}

}
