/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;
/**
 * this class represents a loop through integers
 */
public class ZestLoopTokenInteger extends ZestLoopToken<Integer> {
	/**
	 * main construptor
	 * @param value the value of this token
	 */
	public ZestLoopTokenInteger(int value){
		super(value);
	}

	@Override
	public ZestLoopTokenInteger deepCopy() {
		ZestLoopTokenInteger copy=new ZestLoopTokenInteger(this.getValue());
		return copy;
	}
}
