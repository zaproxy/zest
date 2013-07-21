/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;

public class ZestLoopTokenInteger extends ZestLoopToken<Integer> {
	private static int counter=0;
	public ZestLoopTokenInteger(int index){
		super(index, ++counter);
	}

//	@Override
//	public int compareTo(ZestLoopToken<Integer> otherToken) {
//		return this.getValue()-otherToken.getValue();
//	}

	@Override
	public ZestLoopTokenInteger deepCopy() {
		ZestLoopTokenInteger copy=new ZestLoopTokenInteger(this.getValue());
		return copy;
	}
}
