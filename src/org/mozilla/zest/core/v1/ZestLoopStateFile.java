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
 * The Class ZestLoopStateFile.
 */
public class ZestLoopStateFile extends ZestLoopState<String> {
	
	/** The converted state. */
	private transient ZestLoopStateString convertedState;


	/**
	 * Instantiates a new zest loop state file.
	 */
	public ZestLoopStateFile() {
		super();
	}

	/**
	 * Instantiates a new zest loop state file.
	 *
	 * @param set the set
	 */
	public ZestLoopStateFile(ZestLoopTokenStringSet set){
		super(set);
		this.convertedState=set.getFirstState();
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopState#deepCopy()
	 */
	@Override
	public ZestLoopState<String> deepCopy() {
		ZestLoopStateFile copy = new ZestLoopStateFile();
		if(this.convertedState==null){
		System.err.println("this converted state is null? ");
		}
		copy.convertedState = (ZestLoopStateString) this.convertedState
				.deepCopy();
		return copy;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopState#increase(int, org.mozilla.zest.core.v1.ZestLoopTokenSet)
	 */
	@Override
	public boolean increase(int step, ZestLoopTokenSet<String> set) {
		return this.convertedState.increase(step, set);
	}
}
