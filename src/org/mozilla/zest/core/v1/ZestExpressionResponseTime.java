/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;


public class ZestExpressionResponseTime extends ZestExpression {
	
	private boolean greaterThan = true;
	private long timeInMs;
	
	public ZestExpressionResponseTime() {
		super();
	}
	
	public ZestExpressionResponseTime(long time) {
		super();
		this.timeInMs=time;
	}
	
	public boolean isTrue (ZestResponse response) {
		if (greaterThan) {
			return response.getResponseTimeInMs() > this.timeInMs;
		} else {
			return response.getResponseTimeInMs() < this.timeInMs;
		}
	}

	public boolean isGreaterThan() {
		return greaterThan;
	}

	public void setGreaterThan(boolean greaterThan) {
		this.greaterThan = greaterThan;
	}

	public long getTimeInMs() {
		return timeInMs;
	}

	public void setTimeInMs(long timeInMs) {
		this.timeInMs = timeInMs;
	}

	@Override
	public ZestExpressionResponseTime deepCopy() {
		ZestExpressionResponseTime copy = new ZestExpressionResponseTime();
		copy.greaterThan = this.greaterThan;
		copy.timeInMs = this.timeInMs;
		return copy;
	}

	@Override
	public boolean evaluate(ZestResponse response) {
		return isTrue(response);
	}
	
}
