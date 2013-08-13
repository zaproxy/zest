/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;

import java.util.List;

/**
 * this class represents a Set of loop tokens.
 *
 * @param <T> the generic type
 */
public interface ZestLoopTokenSet<T>{
	
	/**
	 * adds a new token.
	 *
	 * @param token the new token
	 */
	public void addToken(T token);
	
	/**
	 * returns the token at a given index.
	 *
	 * @param index the index of the token
	 * @return the token at the given index
	 */
	public T getToken(int index);
	
	/**
	 * returns the list of tokens.
	 *
	 * @return the list of tokens
	 */
	public List<T> getTokens();//TODO really needed?
	
	/**
	 * returns the index of a given token.
	 *
	 * @param token the token whose index we are searching for
	 * @return the index of the token
	 */
	public int indexOf(T token);
	
	/**
	 * returns the last token the loop may consider.
	 *
	 * @return the last token the loop may  consider
	 */
	public T getLastToken();

	/**
	 * returns the size of this set.
	 *
	 * @return the size of this set
	 */
	public int size();
	
	/**
	 * Deep copy.
	 *
	 * @return the zest loop token set
	 */
	public ZestLoopTokenSet<T> deepCopy();
	
	public ZestLoopState<T> getFirstState();
}
