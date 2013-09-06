/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;


/**
 * The Class ZestActionPrint.
 */
public class ZestActionSleep extends ZestAction {
	
	/** The number of milliseconds to sleep. */
	private long milliseconds;
	
	/**
	 * Instantiates a new zest action print.
	 */
	public ZestActionSleep() {
		super();
	}

	/**
	 * Instantiates a new zest action fail.
	 *
	 * @param index the index
	 */
	public ZestActionSleep(int index) {
		super(index);
	}

	/**
	 * Instantiates a new zest action print.
	 *
	 * @param message the message
	 */
	public ZestActionSleep(long milliseconds) {
		super();
		this.milliseconds = milliseconds;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestAction#isSameSubclass(org.mozilla.zest.core.v1.ZestElement)
	 */
	@Override
	public boolean isSameSubclass(ZestElement ze) {
		return ze instanceof ZestActionSleep;
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestAction#invoke(org.mozilla.zest.core.v1.ZestResponse)
	 */
	public String invoke(ZestResponse response, ZestRuntime runtime) throws ZestActionFailException {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			throw new ZestActionFailException(this, e);
		}
		return Long.toString(milliseconds);
	}


	public long getMilliseconds() {
		return milliseconds;
	}

	public void setMilliseconds(long milliseconds) {
		this.milliseconds = milliseconds;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#deepCopy()
	 */
	@Override
	public ZestActionSleep deepCopy() {
		ZestActionSleep copy = new ZestActionSleep(this.getIndex());
		copy.milliseconds = milliseconds;
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
