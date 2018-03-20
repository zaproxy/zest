/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

// TODO: Auto-generated Javadoc
/**
 * This class represents a state of the loop.
 *
 * @param <T> the generic type
 */
public abstract class ZestLoopState<T> extends ZestElement {

    /** the current token considered inside the loop. */
    private T currentToken;

    /** The current index. */
    private int currentIndex = 0;

    /** Instantiates a new zest loop state. */
    protected ZestLoopState() {
        super();
    }

    /**
     * main construptor.
     *
     * @param initializationTokenSet the set of token and the fisrt value to consider inside the
     *     loop
     */
    protected ZestLoopState(ZestLoopTokenSet<T> initializationTokenSet) {
        if (initializationTokenSet == null) {
            throw new IllegalArgumentException("a null token set is not allowed");
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
     * @param newToken the new token
     * @return the previous token
     */
    protected T setCurrentToken(T newToken) {
        T oldToken = this.getCurrentToken();
        this.currentToken = newToken;
        return oldToken;
    }

    /**
     * returns the current index of the current token considered inside the loop.
     *
     * @return the index of the current token considered in the loop
     */
    public int getCurrentIndex() {
        return this.currentIndex;
    }

    /** Increase loops indexes. */
    protected void increaseIndex() {
        this.currentIndex += 1;
    }

    /**
     * Sets the index.
     *
     * @param newIndex the new index
     */
    protected void setIndex(int newIndex) {
        this.currentIndex = newIndex;
    }

    /**
     * this increase the state and goes to the next state.
     *
     * @param set the set
     */
    protected abstract boolean increase(ZestLoopTokenSet<T> set);

    /**
     * this sets the state to the last state: i.e. the loop has finished
     *
     * @param set the set
     */
    public void toLastState(ZestLoopTokenSet<T> set) {
        if (set == null) {
            return;
        } else {
            this.setIndex(set.size());
            this.setCurrentToken(null);
        }
    }

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + currentIndex;
        result = prime * result + ((currentToken == null) ? 0 : currentToken.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ZestLoopState<?> other = (ZestLoopState<?>) obj;
        if (currentIndex != other.currentIndex) {
            return false;
        }
        if (currentToken == null) {
            if (other.currentToken != null) {
                return false;
            }
        } else if (!currentToken.equals(other.currentToken)) {
            return false;
        }
        return true;
    }
}
