/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;

// TODO: Auto-generated Javadoc
/**
 * This class represents a state of the loop.
 * 
 * @param <T>
 *            the generic type
 */
public abstract class ZestLoopState<T> extends ZestElement {

	/** the current token considered inside the loop. */
	private T currentToken;

	/** The current index. */
	private int currentIndex = 0;

	/**
	 * Instantiates a new zest loop state.
	 */
	public ZestLoopState() {
		super();
	}

	/**
	 * main construptor.
	 * 
	 * @param initializationTokenSet
	 *            the set of token and the fisrt value to consider inside the
	 *            loop
	 */
	public ZestLoopState(ZestLoopTokenSet<T> initializationTokenSet) {
		if (initializationTokenSet == null) {
			throw new IllegalArgumentException(
					"a null token set is not allowed");
		}
		if (initializationTokenSet.size() > 0) {
			this.currentToken = initializationTokenSet.getToken(0);
			this.currentIndex = 0;
		}
	}

	/**
	 * returns the current token considered inside the loop.
	 * 
	 * @return the current token considered inside the loop
	 */
	public T getCurrentToken() {
		return this.currentToken;
	}

	/**
	 * Sets the current token.
	 * 
	 * @param newToken
	 *            the new token
	 * @return the t
	 */
	protected T setCurrentToken(T newToken) {
		T oldToken = this.getCurrentToken();
		this.currentToken = newToken;
		return oldToken;
	}

	/**
	 * returns the current index of the current token considered inside the
	 * loop.
	 * 
	 * @return the index of the current token considered in the loop
	 */
	public int getCurrentIndex() {
		return this.currentIndex;
	}

	/**
	 * Increase index.
	 *
	 * @param step the step
	 */
	protected void increaseIndex(int step) {
		this.currentIndex += step;
	}

	/**
	 * Sets the index.
	 * 
	 * @param newIndex
	 *            the new index
	 */
	protected void setIndex(int newIndex) {
		this.currentIndex = newIndex;
	}

	/**
	 * this increase the state and goes to the next state.
	 *
	 * @param step The step of the counter for this loop
	 * @param set the set
	 * @return the new state
	 */
	public abstract boolean increase(int step, ZestLoopTokenSet<T> set);

	/**
	 * this sets the state to the last state: i.e. the loop has finished
	 *
	 * @param set the set
	 */
	public void toLastState(ZestLoopTokenSet<T> set) {
		this.setIndex(set.size());
		this.setCurrentToken(set.getLastToken());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.zest.core.v1.ZestElement#deepCopy()
	 */
	@Override
	public abstract ZestLoopState<T> deepCopy();

	/**
	 * Checks if is last state.
	 *
	 * @param set the set
	 * @return true, if is last state
	 */
	public boolean isLastState(ZestLoopTokenSet<T> set) {
		return (this.getCurrentIndex() >= set.size());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object otherObject) {
		if (otherObject instanceof ZestLoopState<?>) {
			ZestLoopState<?> otherState = (ZestLoopState<?>) otherObject;
			return this.currentIndex == otherState.currentIndex
					&& this.currentToken.equals(otherState.currentToken);
		}
		return false;
	}
}
