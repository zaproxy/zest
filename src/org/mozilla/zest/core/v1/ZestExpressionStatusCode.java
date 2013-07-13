/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;


public class ZestExpressionStatusCode extends ZestExpression {
	
	private int code;
	
	public ZestExpressionStatusCode() {
		super();
	}
	
	public ZestExpressionStatusCode(int code) {
		super();
		this.code=code;
	}
	
	public boolean isTrue (ZestResponse response) {
		return code == response.getStatusCode();
	}
	
	@Override
	public boolean evaluate(ZestResponse response){
		boolean toReturn=isTrue(response);
		return isInverse()? (!toReturn) : toReturn;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	@Override
	public ZestExpressionStatusCode deepCopy() {
		ZestExpressionStatusCode copy = new ZestExpressionStatusCode();
		copy.code = code;
		return copy;
	}
	
}
