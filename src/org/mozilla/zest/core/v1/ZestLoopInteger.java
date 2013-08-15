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
	 * Instantiates a new zest loop integer.
	 */
	public ZestLoopInteger(){
		super( new ZestLoopTokenIntegerSet(0, 0), new LinkedList<ZestStatement>());
	}
	
	/**
	 * Instantiates a new zest loop integer.
	 *
	 * @param name the name
	 */
	public ZestLoopInteger(String name){
		super(name, new ZestLoopTokenIntegerSet(0, 0), new LinkedList<ZestStatement>());
	}
	
	/**
	 * Instantiates a new zest loop integer.
	 *
	 * @param name the name
	 * @param start the start
	 * @param end the end
	 */
	public ZestLoopInteger(String name, int start, int end){
		super(name, new ZestLoopTokenIntegerSet(start, end), new LinkedList<ZestStatement>());
	}
	
	/**
	 * Instantiates a new zest loop integer.
	 *
	 * @param stmts the stmts
	 */
	public ZestLoopInteger(List<ZestStatement> stmts){
		super( new ZestLoopTokenIntegerSet(0, 0), stmts);
	}
	
	/**
	 * Instantiates a new zest loop integer.
	 *
	 * @param name the name
	 * @param stmts the stmts
	 */
	public ZestLoopInteger(String name,List<ZestStatement> stmts){
		super(name, new ZestLoopTokenIntegerSet(0, 0), stmts);
	}
	/**
	 * main construptor (empty).
	 *
	 * @param index the index
	 * @param start the start
	 * @param end the end
	 * @param statements the statements
	 */
	public ZestLoopInteger(int index, int start, int end, List<ZestStatement> statements){
		super(index,new ZestLoopTokenIntegerSet(start, end), statements);
	}
	
	/**
	 * Instantiates a new zest loop integer.
	 *
	 * @param index the index
	 * @param name the name
	 * @param start the start
	 * @param end the end
	 * @param statements the statements
	 */
	public ZestLoopInteger(int index,String name, int start, int end, List<ZestStatement> statements){
		super(index,name, new ZestLoopTokenIntegerSet(start, end), statements);
	}
	
	/**
	 * Instantiates a new zest loop integer.
	 *
	 * @param start the start
	 * @param end the end
	 * @param statements the statements
	 */
	public ZestLoopInteger(int start, int end, List<ZestStatement> statements){
		super(new ZestLoopTokenIntegerSet(start, end),statements);
	}
	
	/**
	 * Instantiates a new zest loop integer.
	 *
	 * @param name the name
	 * @param start the start
	 * @param end the end
	 * @param statements the statements
	 */
	public ZestLoopInteger(String name,int start, int end, List<ZestStatement> statements){
		super(name,new ZestLoopTokenIntegerSet(start, end),statements);
	}
	
	/**
	 * Instantiates a new zest loop integer.
	 *
	 * @param index the index
	 * @param start the start
	 * @param end the end
	 */
	public ZestLoopInteger(int index, int start, int end){
		super(index,new ZestLoopTokenIntegerSet(start, end), new LinkedList<ZestStatement>());
	}
	
	/**
	 * Gets the start.
	 *
	 * @return the start
	 */
	public int getStart(){
		return this.getSet().getStart();
	}
	
	/**
	 * Gets the end.
	 *
	 * @return the end
	 */
	public int getEnd(){
		return this.getSet().getEnd();
	}
	
	/**
	 * Instantiates a new zest loop integer.
	 *
	 * @param start the start
	 * @param end the end
	 */
	public ZestLoopInteger(int start, int end){
		this(start, end, new LinkedList<ZestStatement>());
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoop#getCurrentState()
	 */
	@Override
	public ZestLoopStateInteger getCurrentState(){
		return (ZestLoopStateInteger)super.getCurrentState();
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoop#setStep(int)
	 */
	public void setStep(int step){
		super.setStep(step);
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoop#deepCopy()
	 */
	@Override
	public ZestLoopInteger deepCopy(){
		ZestLoopStateInteger state=this.getCurrentState().deepCopy();
		ZestLoopTokenIntegerSet set=this.getSet();
		ZestLoopInteger copy=new ZestLoopInteger(set.getStart(), set.getEnd());
		for(ZestStatement stmt:this.getStatements()){
			copy.addStatement(stmt.deepCopy());
		}
		copy.setCurrentState(state);
		return copy;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoop#getSet()
	 */
	@Override
	public ZestLoopTokenIntegerSet getSet() {
		return (ZestLoopTokenIntegerSet) super.getSet();
	}
	@Override
	public void setSet(ZestLoopTokenSet<Integer> newSet){
		super.setSet(newSet);
	}
}