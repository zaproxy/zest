/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;

public abstract class ZestLoopToken<T> extends ZestElement{
	private T value;
	protected ZestLoopToken(T value, int index){
		if(value==null){
			throw new IllegalArgumentException("null param is invalid");
		}
		if(index<0){
			throw new IllegalArgumentException("index must be non negative");
		}
		this.value=value;
	}
//	public int compareTo(ZestLoopToken<T> otherToken){
//		return this.index-otherToken.index;
//	}
	public boolean equals(ZestLoopToken<T> token){
		return this.value.equals(token.getValue());
	}
	public abstract ZestLoopToken<T> deepCopy();
	public T getValue(){
		return this.value;
	}
}
