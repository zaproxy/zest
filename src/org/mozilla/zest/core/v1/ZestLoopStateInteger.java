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
	private ZestLoopTokenIntegerSet set;
	
	/** The current index. */
	private transient int currentIndex;
	
	/**
	 * Instantiates a new zest loop state integer.
	 *
	 * @param start the start
	 * @param end the end
	 */
	public ZestLoopStateInteger(int start, int end){
		super();
		this.set=new ZestLoopTokenIntegerSet(start, end);
		this.currentIndex=start;
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopState#increase()
	 */
	@Override
	public boolean increase() {
		++currentIndex;
		return currentIndex<set.getEnd();
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopState#toLastState()
	 */
	@Override
	public void toLastState() {
		currentIndex=set.getEnd();
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopState#deepCopy()
	 */
	@Override
	public ZestLoopStateInteger deepCopy() {
		ZestLoopStateInteger copy=new ZestLoopStateInteger(this.set.getStart(), this.set.getEnd());
		copy.currentIndex=this.currentIndex;
		copy.set=this.set.deepCopy();
		return copy;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopState#isLastState()
	 */
	@Override
	public boolean isLastState() {
		return currentIndex<set.getEnd();
	}

}
