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
	
	/** The tokens. */
	private ZestLoopTokenStringSet tokens;

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
		this.tokens = initializationTokenSet;
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
	public boolean increase() {
		if (this.tokens == null || this.getCurrentIndex() + 1 >= tokens.size()) {
			return false;
		} else {
			this.increaseIndex();
			this.setCurrentToken(tokens.getToken(getCurrentIndex()));
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopState#toLastState()
	 */
	@Override
	public void toLastState() {
		if (this.isLastState()) {
			return;
		} else {
			this.setCurrentToken(tokens.getLastToken());
			this.setIndex(tokens.size()-1);
		}
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopState#deepCopy()
	 */
	@Override
	public ZestLoopState<String> deepCopy() {
		if(this.tokens==null){
			return new ZestLoopStateString();
		}
		ZestLoopStateString copy=new ZestLoopStateString();
		copy.tokens=this.tokens.deepCopy();
		copy.setCurrentToken(this.getCurrentToken());
		copy.setCurrentToken(this.getCurrentToken());
		copy.setIndex(this.getCurrentIndex());
		return copy;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopState#isLastState()
	 */
	@Override
	public boolean isLastState() {
		return tokens==null || this.getCurrentIndex()+1>=tokens.size();
	}

	/**
	 * Gets the token set.
	 *
	 * @return the token set
	 */
	public ZestLoopTokenStringSet getTokenSet() {
		return this.tokens;
	}

	@Override
	public ZestLoopTokenStringSet getSet() {
		return this.tokens;
	}

}
