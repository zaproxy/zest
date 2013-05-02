/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;


public class ZestActionFail extends ZestAction {
	
	public enum Priority {PRIORITY_INFO, PRIORITY_LOW, PRIORITY_MEDIUM, PRIORITY_HIGH};

	private String message;
	private String priority;
	
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

	public ZestActionFail(String message, String priority) {
		super();
		this.message = message;
		this.setPriority(priority);
	}

	public ZestActionFail(String message, Priority priority) {
		super();
		this.message = message;
		this.setPriority(priority);
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
	
	public String getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority.name();
	}

	public void setPriority(String priority) {
		try {
			Priority.valueOf(priority);
		} catch (Exception e) {
			throw new IllegalArgumentException("Unsupported priority: " + priority);
		}
		this.priority = priority;
	}

	@Override
	public ZestActionFail deepCopy() {
		ZestActionFail copy = new ZestActionFail(this.getIndex());
		copy.message = message;
		copy.priority = priority;
		return copy;
	}

}
