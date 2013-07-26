/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;

public class ZestLoopStateString extends ZestLoopState<String> {
	private ZestLoopTokenStringSet tokens;

	public ZestLoopStateString() {
		super();
	}

	public ZestLoopStateString(ZestLoopTokenStringSet initializationTokenSet) {
		super(initializationTokenSet);
		this.tokens = initializationTokenSet;
	}

	@Override
	public boolean increase() {
		if (this.tokens == null || this.getCurrentIndex() + 1 >= tokens.size()) {
			return false;
		} else {
			this.increaseIndex();
			this.setCurrentToken(tokens.getToken(getCurrentIndex()));
			return true;
		}
	}

	@Override
	public void toLastState() {
		if (this.tokens == null || this.tokens.size() == 0) {
			return;
		} else {
			this.setCurrentToken(tokens.getLastConsideredToken());
			this.setIndex(tokens.indexOf(getCurrentToken()));
		}
	}

	@Override
	public ZestLoopState<String> deepCopy() {
		if(this.tokens==null){
			return new ZestLoopStateString();
		}
		ZestLoopStateString copy=new ZestLoopStateString();
		copy.tokens=this.tokens.deepCopy();
		copy.setCurrentToken(this.getCurrentToken().deepCopy());
		copy.setCurrentToken(this.getCurrentToken());
		copy.setIndex(this.getCurrentIndex());
		return copy;
	}

	@Override
	public boolean isLastState() {
		return tokens==null || this.getCurrentIndex()+1>=tokens.size();
	}

	public ZestLoopTokenStringSet getTokenSet() {
		return this.tokens;
	}

}
