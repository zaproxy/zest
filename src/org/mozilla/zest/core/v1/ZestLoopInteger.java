/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;

import java.util.List;
/**
 * This class represent a loop through a set of integers
 */
public class ZestLoopInteger extends ZestLoop<Integer> {
	/**
	 * main construptor (empty)
	 */
	public ZestLoopInteger(){
		super();
	}
	/**
	 * construptor with initialization
	 * @param initializationState the initialization state with the set of value to loop through and the first value
	 */
	public ZestLoopInteger(ZestLoopState<Integer> initializationState){
		super(initializationState);
	}
	/**
	 * construptor
	 * @param initializationState the initialization state with the set of value to loop through and the first value
	 * @param statements the list of the statements inside the loop
	 */
	public ZestLoopInteger(ZestLoopState<Integer> initializationState, List<ZestStatement> statements){
		super(initializationState, statements);
	}
}