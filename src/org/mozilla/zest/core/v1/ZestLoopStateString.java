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
 * The Class ZestLoopStateString.
 */
public class ZestLoopStateString extends ZestLoopState<String> {
	
//	/** The tokens. */
//	private ZestLoopTokenStringSet tokens;

	/**
	 * Instantiates a new zest loop state string.
	 */
	public ZestLoopStateString() {
		super();
	}

	/**
	 * Instantiates a new zest loop state string.
	 *
	 * @param initializationTokenSet the initialization token set
	 */
	public ZestLoopStateString(ZestLoopTokenStringSet initializationTokenSet) {
		super(initializationTokenSet);
	}

	/**
	 * Instantiates a new zest loop state string.
	 *
	 * @param values the values
	 */
	public ZestLoopStateString(String[] values) {
		this(new ZestLoopTokenStringSet(values));
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopState#increase()
	 */
	@Override
	public boolean increase(int step, ZestLoopTokenSet<String> set) {
		if (this.getCurrentIndex() + 1 >= set.size()) {
			return false;
		} else {
			this.increaseIndex(1);
			this.setCurrentToken(set.getToken(getCurrentIndex()));
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopState#toLastState()
	 */
	@Override
	public void toLastState(ZestLoopTokenSet<String> set) {
		if (this.isLastState(set)) {
			return;
		} else {
			this.setCurrentToken(set.getLastToken());
			this.setIndex(set.size()-1);
		}
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopState#deepCopy()
	 */
	@Override
	public ZestLoopState<String> deepCopy() {
		ZestLoopStateString copy=new ZestLoopStateString();
		copy.setCurrentToken(this.getCurrentToken());
		copy.setIndex(this.getCurrentIndex());
		return copy;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopState#isLastState()
	 */
	@Override
	public boolean isLastState(ZestLoopTokenSet<String> set) {
		return set==null || this.getCurrentIndex()+1>=set.size();
	}

//	/**
//	 * Gets the token set.
//	 *
//	 * @return the token set
//	 */
//	public ZestLoopTokenStringSet getTokenSet() {
//		return this.tokens;
//	}

//	@Override
//	public ZestLoopTokenStringSet getSet() {
//		return this.tokens;
//	}

}
