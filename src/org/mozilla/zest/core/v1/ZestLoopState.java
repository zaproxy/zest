/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;

/**
 * This class represents a state of the loop.
 *
 * @param <T> the generic type
 */
public abstract class ZestLoopState<T> extends ZestElement{
	
	/** the current token considered inside the loop. */
	private T currentToken;
	
	/** The current index. */
	private int currentIndex=0;
	
	/**
	 * Instantiates a new zest loop state.
	 */
	public ZestLoopState(){
		super();
	}
	
	/**
	 * main construptor.
	 *
	 * @param initializationTokenSet the set of token and the fisrt value to consider inside the loop
	 */
	public ZestLoopState(ZestLoopTokenSet<T> initializationTokenSet){
		if(initializationTokenSet==null){
			throw new IllegalArgumentException("a null token set is not allowed");
		}
		this.currentToken=initializationTokenSet.getToken(0);
	}
	public abstract ZestLoopTokenSet<T> getSet();
	
	/**
	 * returns the current token considered inside the loop.
	 *
	 * @return the current token considered inside the loop
	 */
	public T getCurrentToken(){
		return this.currentToken;
	}
	
	/**
	 * Sets the current token.
	 *
	 * @param newToken the new token
	 * @return the t
	 */
	protected T setCurrentToken(T newToken){
		T oldToken=this.getCurrentToken();
		this.currentToken=newToken;
		return oldToken;
	}
	
	/**
	 * returns the current index of the current token considered inside the loop.
	 *
	 * @return the index of the current token considered in the loop
	 */
	public int getCurrentIndex(){
		return this.currentIndex;
	}
	
	/**
	 * Increase index.
	 */
	protected void increaseIndex(){
		++this.currentIndex;
	}
	
	/**
	 * Sets the index.
	 *
	 * @param newIndex the new index
	 */
	protected void setIndex(int newIndex){
		this.currentIndex=newIndex;
	}
	
	/**
	 * this increase the state and goes to the next state.
	 *
	 * @return the new state
	 */
	public abstract boolean increase();
	/**
	 * this sets the state to the last state: i.e. the loop has finished
	 */
	public abstract void toLastState();
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestElement#deepCopy()
	 */
	@Override
	public abstract ZestLoopState<T> deepCopy();
	
	/**
	 * Checks if is last state.
	 *
	 * @return true, if is last state
	 */
	public abstract boolean isLastState();
	/**
	 * true if the two states are equals
	 * @param otherState the other state
	 * @return true if the two states are equals
	 */
	public boolean equals(ZestLoopState<T> otherState){
		return this.currentIndex==otherState.currentIndex && this.currentToken==otherState.currentToken;
	}
}
