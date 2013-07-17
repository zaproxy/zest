/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;

public class ZestLoopToken implements ZestToken {
	private String token=null;
	public ZestLoopToken(){
	}
	public ZestLoopToken(String token){
		this.token=token;
	}
	@Override
	public String getToken(){
		return this.token;
	}
	@Override
	public String setToken(String newToken){
		String oldToken=this.token;
		this.token=newToken;
		return oldToken;
	}
	@Override
	public int compareTo(ZestToken otherToken) {
		return this.token.compareTo(otherToken.getToken());
	}

	@Override
	public boolean equals(ZestToken token) {
		return this.token.equals(token.getToken());
	}
	@Override
	public ZestLoopToken deepCopy() {
		ZestLoopToken copy=new ZestLoopToken();
		copy.token=this.token;
		return copy;
	}
}
