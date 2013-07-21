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


public class ZestLoopTokenSet<T> extends ZestElement{
	protected static final int DEFAULT_INDEX_START=0;
	private List<ZestLoopToken<T>> tokens;
	private int indexStart=DEFAULT_INDEX_START;
	public ZestLoopTokenSet(){
		this.tokens=new LinkedList<>();
	}
	public ZestLoopTokenSet(List<ZestLoopToken<T>> tokens, int startIndex){
		this.tokens=tokens;
		this.indexStart=startIndex;
	}
	public ZestLoopTokenSet(List<ZestLoopToken<T>> tokens){
		this.tokens=tokens;
	}
	public int getIndexStart(){
		return this.indexStart;
	}
	public void addToken(ZestLoopToken<T> token){
		if(tokens==null){
			this.tokens=new LinkedList<>();
		}
		if(token==null){
			throw new IllegalArgumentException("null ZestLoopToken is invalid");
		}
		this.tokens.add(token);
	}
	public ZestLoopToken<T> getToken(int index){
		return tokens.get(index);
	}
	public int indexOf(ZestLoopToken<T> token){
		if(tokens==null || tokens.isEmpty())
			return -1;
		boolean found=false;
		int i;
		for(i=0;!found && i<tokens.size(); i++){
			found=tokens.get(i).equals(token);
		}
		if(found){
			return i-1;
		}
		else{
			return -1;
		}
	}
	public ZestLoopToken<T> getFirstConsideredToken(){
		return tokens.get(indexStart);
	}
	public ZestLoopToken<T> removeToken(int index){
		return tokens.remove(index);
	}
	public int setIndexStart(int newStrart){
		int copyOfIndexStart=this.indexStart;
		this.indexStart=newStrart;
		return copyOfIndexStart;
	}
	public ZestLoopToken<T> replace(int index, ZestLoopToken<T> newToken){
		ZestLoopToken<T> replaced=tokens.remove(index);
		tokens.add(index, newToken);
		return replaced;
	}
	public int size(){
		if(tokens==null){
			return -1;
		}
		return tokens.size();
	}
	@Override
	public ZestLoopTokenSet<T> deepCopy() {
		if(tokens==null){
			return new ZestLoopTokenSet<T>(null, this.indexStart);
		}
		LinkedList<ZestLoopToken<T>> copyOfTokens=new LinkedList<>();
		for(int i=0; i<tokens.size(); i++){
			copyOfTokens.add(this.tokens.get(i).deepCopy());
		}
		return new ZestLoopTokenSet<T>(copyOfTokens, this.indexStart);
	}
}
