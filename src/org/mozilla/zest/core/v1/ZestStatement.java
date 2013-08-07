/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.net.MalformedURLException;
import java.util.Set;


// TODO: Auto-generated Javadoc
/**
 * The Class ZestStatement.
 */
public abstract class ZestStatement extends ZestElement {

	/** The index. */
	private int index;
	
	/** The previous. */
	private transient ZestStatement previous = null;
	
	/** The next. */
	private transient ZestStatement next = null;

	/**
	 * Instantiates a new zest statement.
	 *
	 * @param index the index
	 */
	public ZestStatement(int index) {
		this.index = index;
	}

	/**
	 * Instantiates a new zest statement.
	 */
	public ZestStatement() {
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestElement#isSameSubclass(org.mozilla.zest.core.v1.ZestElement)
	 */
	@Override
	public boolean isSameSubclass(ZestElement ze) {
		return ze instanceof ZestStatement;
	}

	/**
	 * Gets the previous.
	 *
	 * @return the previous
	 */
	public ZestStatement getPrevious() {
		return previous;
	}

	/**
	 * Insert before.
	 *
	 * @param stmt the stmt
	 */
	protected void insertBefore(ZestStatement stmt) {
		ZestStatement prv = stmt.getPrevious();
		this.previous = prv;
		if (prv != null) {
			prv.setNext(this);
		}
		this.setNext(stmt);
		stmt.previous = this;
		this.setIndex(stmt.getIndex(), true);
	}
	
	/**
	 * Insert after.
	 *
	 * @param stmt the stmt
	 */
	protected void insertAfter(ZestStatement stmt) {
		ZestStatement nxt = stmt.getNext();
		this.next = nxt;
		if (nxt != null) {
			nxt.previous = this;
		}
		this.previous = stmt;
		stmt.setNext(this);
		this.setIndex(stmt.getIndex()+1, true);
	}
	
	/**
	 * Removes the.
	 */
	protected void remove() {
		ZestStatement nxt = next;
		if (this instanceof ZestContainer) {
			nxt = ((ZestContainer)this).getLast().getNext();
		}
		if (previous != null) {
			previous.setNext(nxt);
		}
		if (nxt != null) {
			nxt.previous = previous;
			nxt.setIndex(previous.getIndex()+1, true);
		}
	}

	/**
	 * Gets the next.
	 *
	 * @return the next
	 */
	public ZestStatement getNext() {
		return next;
	}

	/**
	 * Sets the next.
	 *
	 * @param next the new next
	 */
	protected void setNext(ZestStatement next) {
		this.next = next;
	}
	
	/**
	 * Gets the index.
	 *
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Sets the index.
	 *
	 * @param index the index
	 * @param recurse the recurse
	 */
	private void setIndex(int index, boolean recurse) {
		this.index = index;
		if (recurse && this.next != null) {
			if (this.equals(this.next)) {
				// Sanity check
				throw new IllegalArgumentException();
			}
			this.next.setIndex(index+1, true);
		}
	}
	
	/**
	 * Sets the prefix.
	 *
	 * @param oldPrefix the old prefix
	 * @param newPrefix the new prefix
	 * @throws MalformedURLException the malformed url exception
	 */
	abstract void setPrefix(String oldPrefix, String newPrefix) throws MalformedURLException;

	/**
	 * Gets the tokens.
	 *
	 * @param tokenStart the token start
	 * @param tokenEnd the token end
	 * @return the tokens
	 */
	abstract Set<String> getTokens(String tokenStart, String tokenEnd);

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestElement#deepCopy()
	 */
	public abstract ZestStatement deepCopy();

	/* Useful when debuging ;)
	public String toString () {
		return this.index + ":" + this.getElementType() + "-" + this.hashCode();
	}
	*/
}
