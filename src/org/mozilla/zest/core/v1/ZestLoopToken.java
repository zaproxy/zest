/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;
/**
 * this class represent a token of a loop
 */
public class ZestLoopToken<T> extends ZestElement{
	/**
	 * the value of this token
	 */
	private T value=null;
	/**
	 * construptor
	 * @param value the value of the token
	 */
	public ZestLoopToken(T value){
		if(value==null){
			throw new IllegalArgumentException("null param is invalid");
		}
		this.value=value;
	}
	/**
	 * Returns true if the two tokens are equals (same value)
	 * @param token the token in param
	 * @return true if token is equal to this
	 */
	public boolean equals(ZestLoopToken<T> token){
		return this.getValue().equals(token.getValue());
	}
	@Override
	public ZestLoopToken<T> deepCopy(){
		return new ZestLoopToken<>(this.getValue());
	}
	/**
	 * returns the value of this token
	 * @return the value of this token
	 */
	public T getValue(){
		return this.value;
	}
}
