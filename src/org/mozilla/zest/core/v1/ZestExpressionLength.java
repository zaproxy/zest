/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

public class ZestExpressionLength extends ZestExpression{

	private int length;
	private int approx;
	public ZestExpressionLength() {
		super(null);
	}	
	public ZestExpressionLength(int length, int approx) {
		this(null, length, approx);
	}
	public ZestExpressionLength(ZestExpressionElement parent){
		this(parent,0, 0);
	}
	public ZestExpressionLength(ZestExpressionElement parent, int length, int approx){
		super(parent);
		this.length=length;
		this.approx=approx;
	}
	
	public ZestExpressionLength deepCopy() {
		return new ZestExpressionLength(this.length, this.approx);
	}
	@Override
	public boolean evaluate (ZestResponse response) {
		if (response.getBody() == null) {
			return false;
		}
		return Math.abs(length - response.getBody().length()) <= length * approx / 100;
	}
	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getApprox() {
		return approx;
	}

	public void setApprox(int approx) {
		this.approx = approx;
	}
	
}
