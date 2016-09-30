/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;


/**
 * The Class ZestActionPrint.
 */
public class ZestActionPrint extends ZestAction {
	
	/** The message. */
	private String message;
	
	/**
	 * Instantiates a new zest action print.
	 */
	public ZestActionPrint() {
		super();
	}

	/**
	 * Instantiates a new zest action fail.
	 *
	 * @param index the index
	 */
	public ZestActionPrint(int index) {
		super(index);
	}

	/**
	 * Instantiates a new zest action print.
	 *
	 * @param message the message
	 */
	public ZestActionPrint(String message) {
		super();
		this.message = message;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestAction#isSameSubclass(org.mozilla.zest.core.v1.ZestElement)
	 */
	@Override
	public boolean isSameSubclass(ZestElement ze) {
		return ze instanceof ZestActionPrint;
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestAction#invoke(org.mozilla.zest.core.v1.ZestResponse)
	 */
	public String invoke(ZestResponse response, ZestRuntime runtime) throws ZestActionFailException {
		String str = runtime.replaceVariablesInString(this.message, false);
		runtime.output(str);
		return str;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#deepCopy()
	 */
	@Override
	public ZestActionPrint deepCopy() {
		ZestActionPrint copy = new ZestActionPrint(this.getIndex());
		copy.message = message;
		copy.setEnabled(this.isEnabled());
		return copy;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#isPassive()
	 */
	@Override
	public boolean isPassive() {
		return true;
	}
}
