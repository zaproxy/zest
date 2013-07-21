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
public class ZestLoopState<T> extends ZestElement{
	/**
	 * the set of tokens the script has to loop through
	 */
	private ZestLoopTokenSet<T> tokens;
	/**
	 * the value of the current index which univocally defines a token
	 */
	private int currentIndex=ZestLoopTokenSet.DEFAULT_INDEX_START;
	/**
	 * the current token considered inside the loop
	 */
	private ZestLoopToken<T> currentToken;
	/**
	 * main construptor
	 * @param initializationTokenSet the set of token and the fisrt value to consider inside the loop
	 */
	public ZestLoopState(ZestLoopTokenSet<T> initializationTokenSet){
		this.tokens=initializationTokenSet;
		this.currentIndex=tokens.getIndexStart();
		this.currentToken=tokens.getFirstConsideredToken();
	}
	/**
	 * returns the current token considered inside the loop
	 * @return the current token considered inside the loop
	 */
	public ZestLoopToken<T> getCurrentToken(){
		return this.currentToken;
	}
	/**
	 * returns the current index of the current token considered inside the loop
	 * @return the index of the current token considered in the loop
	 */
	public int getCurrentIndex(){
		return this.currentIndex;
	}
	/**
	 * this increase the state and goes to the next state
	 * @return the new state
	 */
	public boolean increase(){
		++currentIndex;
		if(tokens==null || currentIndex>=tokens.size()){
			return false;
		}
		this.currentToken=tokens.getToken(currentIndex);
		return true;
	}
	/**
	 * this sets the state to the last state: i.e. the loop has finished
	 */
	public void endState(){
		this.currentIndex=tokens.size();
	}
	@Override
	public ZestLoopState<T> deepCopy() {
		if(this==null){
			return null;
		}
		ZestLoopTokenSet<T> copyOfTokenSet=this.tokens.deepCopy();
		ZestLoopState<T> copyOfState=new ZestLoopState<>(copyOfTokenSet);
		copyOfState.currentIndex=this.currentIndex;
		copyOfState.currentToken=this.currentToken.deepCopy();
		return copyOfState;
	}
}
