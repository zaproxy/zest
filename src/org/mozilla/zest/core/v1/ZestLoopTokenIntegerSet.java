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

public class ZestLoopTokenIntegerSet extends ZestElement implements
		ZestLoopTokenSet<Integer> {
	private int start;
	private int end;
	public ZestLoopTokenIntegerSet(int indexStart, int indexEnd){
		super();
		this.start=indexStart;
		this.end=indexEnd;
	}
	public int getStart(){
		return start;
	}
	public int getEnd(){
		return end;
	}
	public int setStart(int newStart){
		int oldStart=this.start;
		this.start=newStart;
		return oldStart;
	}
	public int setEnd(int newEnd){
		int oldEnd=this.end;
		this.end=newEnd;
		return oldEnd;
	}
	@Override
	public ZestLoopTokenIntegerSet deepCopy() {
		return new ZestLoopTokenIntegerSet(this.start, this.end);
	}

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
			throw new IllegalArgumentException("Operation not allowed: the set must be a continue set of integers");
		}
	}

	@Override//TODO remove from interface?
	public Integer getToken(int index) {
		if(index<0){
			throw new IllegalArgumentException("the index must be non negative.");
		}
		if(start+index<end){//restrictive
			return (start+index);//???
		}
		throw new IllegalArgumentException("the index give is not inside this set.");
	}

	@Override//TODO remove from interface?
	public List<Integer> getTokens() {
		LinkedList<Integer> toReturn=new LinkedList<>();
		for(int i=start; i<end; i++){
			toReturn.addLast(i);
		}
		return toReturn;
	}

	@Override
	public int indexOf(Integer token) {
		int tokenValue=token;
		int index= tokenValue-start;
		if(index<0 || index >=end-start){
			throw new IllegalArgumentException("token not contained in this set.");
		}
		return index;
	}

	@Override
	public Integer getLastConsideredToken() {
		return end;
	}

	@Override
	public int size() {
		return end-start;
	}

}
