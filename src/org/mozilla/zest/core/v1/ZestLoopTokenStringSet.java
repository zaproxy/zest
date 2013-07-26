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

public class ZestLoopTokenStringSet extends ZestLoopTokenSet<String> {
	private List<ZestLoopToken<String>> tokens;
	public ZestLoopTokenStringSet(){
		super();
		this.tokens=new LinkedList<>();
	}
	public ZestLoopTokenStringSet(List<ZestLoopToken<String>> tokens){
		super();
		this.tokens=tokens;
	}
	public ZestLoopTokenStringSet(String[] values){
		super();
		tokens=new LinkedList<>();
		for(String value:values){
			tokens.add(new ZestLoopToken<String>(value));
		}
	}
	@Override
	public void addToken(ZestLoopToken<String> token) {
		if(tokens==null){
			tokens=new LinkedList<>();
		}
		tokens.add(token);
	}

	@Override
	public ZestLoopToken<String> getToken(int index) {
		return tokens.get(index);
	}

	@Override
	public List<ZestLoopToken<String>> getTokens() {
		return this.tokens;
	}

	@Override
	public int indexOf(ZestLoopToken<String> token) {
		return tokens.indexOf(token);
	}

	@Override
	public ZestLoopToken<String> getFirstConsideredToken() {
		return tokens.get(this.getIndexStart());
	}

	@Override
	public ZestLoopToken<String> getLastConsideredToken() {
		return this.tokens.get(tokens.size()-1);
	}

	@Override
	public int size() {
		return tokens.size();
	}

	@Override
	public ZestLoopTokenStringSet deepCopy() {
		if(this.tokens==null){
			return new ZestLoopTokenStringSet();
		}
		ZestLoopTokenStringSet copy=new ZestLoopTokenStringSet(new LinkedList<ZestLoopToken<String>>());
		for(ZestLoopToken<String> token:this.tokens){
			copy.addToken(token.deepCopy());
		}
		copy.setIndexStart(this.getIndexStart());
		return copy;
	}
	public ZestLoopToken<String> removeToken(int index) {
		return this.tokens.remove(index);
	}
	public ZestLoopToken<String> replace(int indexOfReplace, ZestLoopToken<String> newToken) {
		ZestLoopToken<String> replaced=this.tokens.get(indexOfReplace);
		this.tokens.remove(indexOfReplace);
		this.tokens.add(indexOfReplace, newToken);
		return replaced;
	}

}
