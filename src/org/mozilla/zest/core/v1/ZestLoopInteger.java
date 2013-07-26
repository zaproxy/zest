/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;

import java.util.LinkedList;
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
	public ZestLoopInteger(int[] values, List<ZestStatement> statements){
		this(values, 0, statements);
	}
	public ZestLoopInteger(int[] values, int startIndex, List<ZestStatement> statements){
		super();
		ZestLoopState<Integer> state=new ZestLoopState<>(new ZestLoopTokenSet<>(toArrayObject(values), startIndex));
		this.setState(state);
	}
	public ZestLoopInteger(int[] values){
		this(values, 0, new LinkedList<ZestStatement>());
	}
	private static Integer[] toArrayObject(int[] values){
		Integer[] boxedValues=new Integer[values.length];
		for(int i=0; i<values.length; i++){
			boxedValues[i]=values[i];
		}
		return boxedValues;
	}
}