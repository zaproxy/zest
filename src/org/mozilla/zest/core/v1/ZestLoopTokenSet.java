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

/**
 * this class represents a Set of loop tokens
 */
public class ZestLoopTokenSet<T> extends ZestElement{
	/**
	 * the default value of start
	 */
	protected static final int DEFAULT_INDEX_START=0;
	/**
	 * a list of tokens
	 */
	private List<ZestLoopToken<T>> tokens;
	/**
	 * the index of the first token to consider in loop
	 */
	private int indexStart=DEFAULT_INDEX_START;
	/**
	 * main construptor
	 */
	public ZestLoopTokenSet(){
		this.tokens=new LinkedList<>();
	}
	/**
	 * construptor
	 * @param tokens the list of tokens to loop through
	 * @param startIndex the index of the first element to consider
	 */
	public ZestLoopTokenSet(List<ZestLoopToken<T>> tokens, int startIndex){
		this.tokens=tokens;
		this.indexStart=startIndex;
	}
	/**
	 * construptor
	 * @param tokens the tokens to loop through
	 */
	public ZestLoopTokenSet(List<ZestLoopToken<T>> tokens){
		this.tokens=tokens;
	}
	/**
	 * construptor
	 * @param values the values of the token to consider
	 */
	public ZestLoopTokenSet(T[] values){
		if(tokens==null){
			tokens=new LinkedList<>();
		}
		for(int i=0; i<values.length; i++){
			tokens.add(new ZestLoopToken<T>(values[i]));
		}
	}
	/**
	 * construptor
	 * @param values values of the token to consider
	 * @param startIndex the index of the first element to consider inside the loop
	 */
	public ZestLoopTokenSet(T[] values, int startIndex){
		this(values);
		this.indexStart=startIndex;
	}
	/**
	 * returns the index of the first token to consider
	 * @return the index of the first token to consider
	 */
	public int getIndexStart(){
		return this.indexStart;
	}
	/**
	 * adds a new token
	 * @param token the new token
	 */
	public void addToken(ZestLoopToken<T> token){
		if(tokens==null){
			this.tokens=new LinkedList<>();
		}
		if(token==null){
			throw new IllegalArgumentException("null ZestLoopToken is invalid");
		}
		this.tokens.add(token);
	}
	/**
	 * returns the token at a given index
	 * @param index the index of the token
	 * @return the token at the given index
	 */
	public ZestLoopToken<T> getToken(int index){
		return tokens.get(index);
	}
	
	public List<ZestLoopToken<T>> getTokens(){
		return this.tokens;
	}
	/**
	 * returns the index of a given token
	 * @param token the token whose index we are searching for
	 * @return the index of the token
	 */
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
	/**
	 * returns the first token the loop will consider
	 * @return the first token the loop will consider
	 */
	public ZestLoopToken<T> getFirstConsideredToken(){
		return tokens.get(indexStart);
	}
	/**
	 * removes a token
	 * @param index the index of the token to remove
	 * @return return the token removed
	 */
	public ZestLoopToken<T> removeToken(int index){
		return tokens.remove(index);
	}
	/**
	 * sets the first token to consider inside the loop
	 * @param newStrart the index of the first token to consider
	 * @return the index of the previous token considered
	 */
	public int setIndexStart(int newStrart){
		int copyOfIndexStart=this.indexStart;
		this.indexStart=newStrart;
		return copyOfIndexStart;
	}
	/**
	 * replaces a token  and returns the previous one
	 * @param index the position of the token we want to replace
	 * @param newToken the new token which will replace the old token
	 * @return the previous token stored at index position
	 */
	public ZestLoopToken<T> replace(int index, ZestLoopToken<T> newToken){
		ZestLoopToken<T> replaced=tokens.remove(index);
		tokens.add(index, newToken);
		return replaced;
	}
	/**
	 * returns the size of this set
	 * @return the size of this set
	 */
	public int size(){
		if(tokens==null){
			return -1;
		}
		return tokens.size();
	}
	@Override
	public ZestLoopTokenSet<T> deepCopy() {
		if(tokens==null){
			ZestLoopTokenSet<T> copy=new ZestLoopTokenSet<>();
			copy.tokens=null;
			copy.indexStart=this.indexStart;
			return copy;
		}
		LinkedList<ZestLoopToken<T>> copyOfTokens=new LinkedList<>();
		for(int i=0; i<tokens.size(); i++){
			copyOfTokens.add(this.tokens.get(i).deepCopy());
		}
		return new ZestLoopTokenSet<T>(copyOfTokens, this.indexStart);
	}
	
}
