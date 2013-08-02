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
 * this class represents a Loop through String values
 */
public class ZestLoopValues extends ZestLoop<String> {
	/**
	 * main construptor
	 */
	public ZestLoopValues(){
		super();
	}
	/**
	 * construptor
	 * @param initializationState the initialization state with the set of tokens and the<br>
	 * first considered token in the loop
	 */
	public ZestLoopValues(ZestLoopState<String> initializationState){
		super(initializationState);
	}
	/**
	 * construptor
	 * @param initializationState the initialization state with the set of tokens and the <br>
	 * first considered token in the loop
	 * @param statements the statements inside the loop
	 */
	public ZestLoopValues(ZestLoopState<String> initializationState, List<ZestStatement> statements){
		super(initializationState, statements);
	}
}
