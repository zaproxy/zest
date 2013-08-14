/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;

// TODO: Auto-generated Javadoc
/**
 * The Class ZestLoopStateInteger.
 */
public class ZestLoopStateInteger extends ZestLoopState<Integer> {

	/** The set. */
	// private ZestLoopTokenIntegerSet set;
	private ZestLoopStateInteger() {
		super();
	}

	/**
	 * Instantiates a new zest loop state integer.
	 *
	 * @param set the set
	 */
	public ZestLoopStateInteger(ZestLoopTokenIntegerSet set) {
		super(set);
		this.setCurrentToken(set.getStart());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.zest.core.v1.ZestLoopState#increase()
	 */
	@Override
	public boolean increase(int step, ZestLoopTokenSet<Integer> set) {
		super.increaseIndex(step);
		super.setCurrentToken(super.getCurrentToken() + step);
		return super.getCurrentToken() < set.getToken(set.size() - 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.zest.core.v1.ZestLoopState#toLastState()
	 */
	@Override
	public void toLastState(ZestLoopTokenSet<Integer> set) {
		super.setCurrentToken(set.getToken(set.getToken(set.size() - 1)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.zest.core.v1.ZestLoopState#deepCopy()
	 */
	@Override
	public ZestLoopStateInteger deepCopy() {
		ZestLoopStateInteger copy = new ZestLoopStateInteger();
		copy.setCurrentToken(this.getCurrentToken());
		copy.setIndex(this.getCurrentIndex());
		return copy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.zest.core.v1.ZestLoopState#isLastState()
	 */
	@Override
	public boolean isLastState(ZestLoopTokenSet<Integer> set) {
		return !(super.getCurrentToken() < set.getToken(set.size() - 1));
	}

	// @Override
	// public ZestLoopTokenIntegerSet getSet() {
	// return this.set;
	// }
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopState#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object otherObject) {
		if (otherObject instanceof ZestLoopStateInteger) {
			ZestLoopStateInteger otherState = (ZestLoopStateInteger) otherObject;
			return super.equals(otherState);
		}
		return false;
	}

}
