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
 * The Class ZestLoopTokenIntegerSet.
 */
public class ZestLoopTokenIntegerSet extends ZestElement implements
		ZestLoopTokenSet<Integer> {
	
	/** The start. */
	private int start=0;
	
	/** The end. */
	private int end=0;
	
	/**
	 * Instantiates a new zest loop token integer set.
	 */
	public ZestLoopTokenIntegerSet(){
		this(0,0);
	}
	/**
	 * Instantiates a new zest loop token integer set.
	 *
	 * @param indexStart the index start
	 * @param indexEnd the index end
	 */
	public ZestLoopTokenIntegerSet(int indexStart, int indexEnd){
		super();
		this.start=indexStart;
		this.end=indexEnd;
	}
	
	/**
	 * Gets the start.
	 *
	 * @return the start
	 */
	public int getStart(){
		return start;
	}
	
	/**
	 * Gets the end.
	 *
	 * @return the end
	 */
	public int getEnd(){
		return end;
	}
	
	/**
	 * Sets the start.
	 *
	 * @param newStart the new start
	 * @return the int
	 */
	public int setStart(int newStart){
		int oldStart=this.start;
		this.start=newStart;
		return oldStart;
	}
	
	/**
	 * Sets the end.
	 *
	 * @param newEnd the new end
	 * @return the int
	 */
	public int setEnd(int newEnd){
		int oldEnd=this.end;
		this.end=newEnd;
		return oldEnd;
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestElement#deepCopy()
	 */
	@Override
	public ZestLoopTokenIntegerSet deepCopy() {
		return new ZestLoopTokenIntegerSet(this.start, this.end);
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopTokenSet#addToken(java.lang.Object)
	 */
	@Override//TODO remove from interface??
	public void addToken(Integer token) {
		int newTokenValue=token;
		if(newTokenValue==end+1){
			this.end=newTokenValue;
		}
		else if(newTokenValue==start-1){
			this.start=newTokenValue;
		}
		else{
			throw new IllegalArgumentException("Operation not allowed: the set must be a continue set of integers " +
												"Start: "+getStart()+"; End: "+getEnd());
		}
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopTokenSet#getToken(int)
	 */
	@Override//TODO remove from interface?
	public Integer getToken(int index) {
		if(index<0){
			throw new IllegalArgumentException("the index must be non negative.");
		}
		if(start+index<end){//restrictive
			return (start+index);//???
		}
		throw new IllegalArgumentException("the index given is not inside this set.");
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopTokenSet#getTokens()
	 */
	@Override//TODO remove from interface?
	public List<Integer> getTokens() {
		LinkedList<Integer> toReturn=new LinkedList<>();
		for(int i=start; i<end; i++){
			toReturn.addLast(i);
		}
		return toReturn;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopTokenSet#indexOf(java.lang.Object)
	 */
	@Override
	public int indexOf(Integer token) {
		int tokenValue=token;
		int index= tokenValue-start;
		if(index<0 || index >=end-start){
			throw new IllegalArgumentException("token not contained in this set.");
		}
		return index;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopTokenSet#getLastConsideredToken()
	 */
	@Override
	public Integer getLastToken() {
		return end;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopTokenSet#size()
	 */
	@Override
	public int size() {
		return end-start;
	}
	

	@Override
	public boolean equals(Object otherObject){
		if(otherObject instanceof ZestLoopTokenIntegerSet){
			ZestLoopTokenIntegerSet otherSet=(ZestLoopTokenIntegerSet) otherObject;
			return this.start==otherSet.start && this.end==otherSet.end;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopTokenSet#getFirstState()
	 */
	@Override
	public ZestLoopStateInteger getFirstState() {
		ZestLoopStateInteger fisrtState=new ZestLoopStateInteger(this);
		return fisrtState;
	}

}
