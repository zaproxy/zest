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

// TODO: Auto-generated Javadoc
/**
 * The Class ZestLoopTokenStringSet.
 */
public class ZestLoopTokenStringSet extends ZestElement implements ZestLoopTokenSet<String> {
	
	/** The tokens. */
	private List<String> tokens;
	
	/**
	 * Instantiates a new zest loop token string set.
	 */
	public ZestLoopTokenStringSet(){
		super();
		this.tokens=new LinkedList<>();
	}
	
	/**
	 * Instantiates a new zest loop token string set.
	 *
	 * @param tokens the tokens
	 */
	public ZestLoopTokenStringSet(List<String> tokens){
		super();
		this.tokens=tokens;
	}
	
	/**
	 * Instantiates a new zest loop token string set.
	 *
	 * @param values the values
	 */
	public ZestLoopTokenStringSet(String[] values){
		super();
		tokens=new LinkedList<>();
		for(String value:values){
			tokens.add(value);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopTokenSet#addToken(java.lang.Object)
	 */
	@Override
	public void addToken(String token) {
		if(tokens==null){
			tokens=new LinkedList<>();
		}
		tokens.add(token);
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopTokenSet#getToken(int)
	 */
	@Override
	public String getToken(int index) {
		return tokens.get(index);
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopTokenSet#getTokens()
	 */
	@Override
	public List<String> getTokens() {
		return this.tokens;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopTokenSet#indexOf(java.lang.Object)
	 */
	@Override
	public int indexOf(String token) {
		return tokens.indexOf(token);
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopTokenSet#getLastConsideredToken()
	 */
	@Override
	public String getLastConsideredToken() {
		return this.tokens.get(tokens.size()-1);
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopTokenSet#size()
	 */
	@Override
	public int size() {
		return tokens.size();
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestElement#deepCopy()
	 */
	@Override
	public ZestLoopTokenStringSet deepCopy() {
		if(this.tokens==null){
			return new ZestLoopTokenStringSet();
		}
		ZestLoopTokenStringSet copy=new ZestLoopTokenStringSet(new LinkedList<String>());
		for(String token:this.tokens){
			copy.addToken(token);
		}
		return copy;
	}
	
	/**
	 * Removes the token.
	 *
	 * @param index the index
	 * @return the string
	 */
	public String removeToken(int index) {
		return this.tokens.remove(index);
	}
	
	/**
	 * Replace.
	 *
	 * @param indexOfReplace the index of replace
	 * @param newToken the new token
	 * @return the string
	 */
	public String replace(int indexOfReplace, String newToken) {
		String replaced=this.tokens.get(indexOfReplace);
		this.tokens.remove(indexOfReplace);
		this.tokens.add(indexOfReplace, newToken);
		return replaced;
	}

}
