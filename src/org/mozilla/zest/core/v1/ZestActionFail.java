/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

public class ZestActionFail extends ZestAction {
	
	private String message;
	
	public ZestActionFail() {
		super();
	}

	public ZestActionFail(int index) {
		super(index);
	}

	public ZestActionFail(String message) {
		super();
		this.message = message;
	}

	@Override
	public boolean isSameSubclass(ZestElement ze) {
		return ze instanceof ZestActionFail;
	}
	
	public String invoke(ZestResponse response) throws ZestActionFailException {
		throw new ZestActionFailException(this, this.message);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public ZestActionFail deepCopy() {
		ZestActionFail copy = new ZestActionFail(this.getIndex());
		copy.message = message;
		return copy;
	}
}
