/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;

import java.util.Arrays;
import java.util.List;

import org.mockito.internal.util.ArrayUtils;

import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;
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
	private ZestLoopInteger(ZestLoopState<Integer> initializationState){
		super(initializationState);
	}
	/**
	 * construptor
	 * @param initializationState the initialization state with the set of value to loop through and the first value
	 * @param statements the list of the statements inside the loop
	 */
	private ZestLoopInteger(ZestLoopState<Integer> initializationState, List<ZestStatement> statements){
		super(initializationState, statements);
	}
	public ZestLoopInteger(int[] values, List<ZestStatement> statements){
		Integer[] boxedValues=new Integer[values.length];
		for(int i=0; i<values.length; i++){
			boxedValues[i]=values[i];
		}
		ZestLoopState<Integer> state=new ZestLoopState<>(new ZestLoopTokenSet<>(boxedValues));
		this.setState(state);
		this.setStatement(statements);
	}
}