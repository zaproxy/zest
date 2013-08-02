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
// TODO: Auto-generated Javadoc

/**
 * This class represent a loop through a set of integers.
 */
public class ZestLoopInteger extends ZestLoop<Integer> {
	
	/**
	 * main construptor (empty).
	 *
	 * @param index the index
	 * @param start the start
	 * @param end the end
	 * @param statements the statements
	 */
	public ZestLoopInteger(int index, int start, int end, List<ZestStatement> statements){
		super(index, new ZestLoopStateInteger(start, end), statements);
	}
	
	/**
	 * Instantiates a new zest loop integer.
	 *
	 * @param start the start
	 * @param end the end
	 * @param statements the statements
	 */
	public ZestLoopInteger(int start, int end, List<ZestStatement> statements){
		super(new ZestLoopStateInteger(start, end),statements);
	}
	
	/**
	 * Instantiates a new zest loop integer.
	 *
	 * @param index the index
	 * @param start the start
	 * @param end the end
	 */
	public ZestLoopInteger(int index, int start, int end){
		this(index, start, end, new LinkedList<ZestStatement>());
	}
	
	/**
	 * Instantiates a new zest loop integer.
	 *
	 * @param start the start
	 * @param end the end
	 */
	public ZestLoopInteger(int start, int end){
	}
	@Override
	public ZestLoopStateInteger getCurrentState(){
		return (ZestLoopStateInteger)super.getCurrentState();
	}
	@Override
	public ZestLoopInteger deepCopy(){
		ZestLoopStateInteger state=this.getCurrentState().deepCopy();
		ZestLoopTokenIntegerSet set=state.getSet();
		ZestLoopInteger copy=new ZestLoopInteger(set.getStart(), set.getEnd());
		for(ZestStatement stmt:this.getStatements()){
			copy.addStatement(stmt.deepCopy());
		}
		copy.setState(state);
		return copy;
	}
}