/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;


public class ZestConditionStatusCode extends ZestConditional {
	
	private int code;
	
	public ZestConditionStatusCode() {
		super();
	}
	
	public ZestConditionStatusCode(int index) {
		super(index);
	}
	
	@Override
	public boolean isTrue (ZestResponse response) {
		return code == response.getStatusCode();
	}


	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	@Override
	public ZestConditionStatusCode deepCopy() {
		ZestConditionStatusCode copy = new ZestConditionStatusCode(this.getIndex());
		copy.code = code;
		for (ZestStatement stmt : this.getIfStatements()) {
			copy.addIf(stmt.deepCopy());
		}
		for (ZestStatement stmt : this.getElseStatements()) {
			copy.addElse(stmt.deepCopy());
		}
		return copy;
	}
	
}
