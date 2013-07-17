/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;


public class ZestLoopBreak extends ZestElement {
	private ZestLoop loop=null;
	public ZestLoopBreak(ZestLoop loop){
		this.loop=loop;
	}
	private ZestLoopBreak(){
	}
	protected ZestLoop getZestLoop(){
		return this.loop;
	}
	protected ZestLoop setZestLoop(ZestLoop newLoop){
		ZestLoop oldLoop=this.loop;
		this.loop=newLoop;
		return oldLoop;
	}

	@Override
	public ZestLoopBreak deepCopy() {
		ZestLoopBreak copy=new ZestLoopBreak();
		copy.loop=this.loop;
		return copy;
	}
}
