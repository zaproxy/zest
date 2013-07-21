/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;

public class ZestLoopState<T> extends ZestElement{
	private ZestLoopTokenSet<T> tokens;
	private int currentIndex=ZestLoopTokenSet.DEFAULT_INDEX_START;
	private ZestLoopToken<T> currentToken;
	public ZestLoopState(ZestLoopTokenSet<T> initializationTokenSet){
		this.tokens=initializationTokenSet;
		this.currentIndex=tokens.getIndexStart();
		this.currentToken=tokens.getFirstConsideredToken();
	}
	public ZestLoopToken<T> getCurrentToken(){
		return this.currentToken;
	}
	public int getCurrentIndex(){
		return this.currentIndex;
	}
	public boolean increase(){
		++currentIndex;
		if(tokens==null || currentIndex>=tokens.size()){
			return false;
		}
		this.currentToken=tokens.getToken(currentIndex);
		return true;
	}
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
