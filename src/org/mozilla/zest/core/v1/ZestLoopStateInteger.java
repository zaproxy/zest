/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;

public class ZestLoopStateInteger extends ZestLoopState<Integer> {
	private ZestLoopTokenIntegerSet set;
	private transient int currentIndex;
	public ZestLoopStateInteger(int start, int end){
		super();
		this.set=new ZestLoopTokenIntegerSet(start, end);
		this.currentIndex=start;
	}
	@Override
	public boolean increase() {
		++currentIndex;
		return currentIndex<set.getEnd();
	}

	@Override
	public void toLastState() {
		currentIndex=set.getEnd();
	}

	@Override
	public ZestLoopStateInteger deepCopy() {
		ZestLoopStateInteger copy=new ZestLoopStateInteger(this.set.getStart(), this.set.getEnd());
		copy.currentIndex=this.currentIndex;
		copy.set=this.set.deepCopy();
		return copy;
	}

	@Override
	public boolean isLastState() {
		return currentIndex<set.getEnd();
	}

}
