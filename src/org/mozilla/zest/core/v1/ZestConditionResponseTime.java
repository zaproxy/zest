/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;


public class ZestConditionResponseTime extends ZestConditional {
	
	private boolean greaterThan = true;
	private long timeInMs;
	
	public ZestConditionResponseTime() {
		super();
	}
	
	public ZestConditionResponseTime(int index) {
		super(index);
	}
	
	@Override
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
	public ZestConditionResponseTime deepCopy() {
		ZestConditionResponseTime copy = new ZestConditionResponseTime(this.getIndex());
		copy.greaterThan = this.greaterThan;
		copy.timeInMs = this.timeInMs;
		for (ZestStatement stmt : this.getIfStatements()) {
			copy.addIf(stmt.deepCopy());
		}
		for (ZestStatement stmt : this.getElseStatements()) {
			copy.addElse(stmt.deepCopy());
		}
		return copy;
	}
	
}
