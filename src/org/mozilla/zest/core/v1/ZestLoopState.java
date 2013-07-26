/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;
/**
 * This class represent a state of the loop
 */
public abstract class ZestLoopState<T> extends ZestElement{
	/**
	 * the value of the current index which univocally defines a token
	 */
	private int currentIndex=ZestLoopTokenSet.DEFAULT_INDEX_START;
	/**
	 * the current token considered inside the loop
	 */
	private ZestLoopToken<T> currentToken;
	public ZestLoopState(){
	}
	/**
	 * main construptor
	 * @param initializationTokenSet the set of token and the fisrt value to consider inside the loop
	 */
	public ZestLoopState(ZestLoopTokenSet<T> initializationTokenSet){
		if(initializationTokenSet==null){
			throw new IllegalArgumentException("a null token set is not allowed");
		}
		this.currentToken=initializationTokenSet.getFirstConsideredToken();
		this.currentIndex=initializationTokenSet.getIndexStart();
	}
	/**
	 * returns the current token considered inside the loop
	 * @return the current token considered inside the loop
	 */
	public ZestLoopToken<T> getCurrentToken(){
		return this.currentToken;
	}
	protected ZestLoopToken<T> setCurrentToken(ZestLoopToken<T> newToken){
		ZestLoopToken<T> oldToken=this.getCurrentToken();
		this.currentToken=newToken;
		return oldToken;
	}
	/**
	 * returns the current index of the current token considered inside the loop
	 * @return the index of the current token considered in the loop
	 */
	public int getCurrentIndex(){
		return this.currentIndex;
	}
	protected void increaseIndex(){
		++this.currentIndex;
	}
	protected void setIndex(int newIndex){
		this.currentIndex=newIndex;
	}
	/**
	 * this increase the state and goes to the next state
	 * @return the new state
	 */
	public abstract boolean increase();
	/**
	 * this sets the state to the last state: i.e. the loop has finished
	 */
	public abstract void toLastState();
	@Override
	public abstract ZestLoopState<T> deepCopy();
	public abstract boolean isLastState();
}
