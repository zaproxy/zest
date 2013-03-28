/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Set;


public abstract class ZestStatement extends ZestElement {

	private int index;
	
	private transient ZestStatement previous = null;
	private transient ZestStatement next = null;

	public ZestStatement(int index) {
		this.index = index;
	}

	public ZestStatement() {
	}

	@Override
	public boolean isSameSubclass(ZestElement ze) {
		return ze instanceof ZestStatement;
	}

	public ZestStatement getPrevious() {
		return previous;
	}

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

	public ZestStatement getNext() {
		return next;
	}

	protected void setNext(ZestStatement next) {
		this.next = next;
	}
	
	public int getIndex() {
		return index;
	}
	
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
	
	abstract void setPrefix(String oldPrefix, String newPrefix) throws MalformedURLException;

	abstract Set<String> getTokens(String tokenStart, String tokenEnd);

	abstract void setUpRefs(ZestScript script);

	public abstract List<ZestTransformation> getTransformations();

	abstract ZestStatement deepCopy();

	/* Useful when debuging ;)
	public String toString () {
		return this.index + ":" + this.getElementType() + "-" + this.hashCode();
	}
	*/
}
