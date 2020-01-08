/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

// TODO: Auto-generated Javadoc
/**
 * this class represents a Set of loop tokens.
 *
 * @param <T> the generic type
 */
public interface ZestLoopTokenSet<T> {

    /**
     * returns the token at a given index.
     *
     * @param index the index of the token
     * @return the token at the given index
     */
    T getToken(int index);

    /**
     * returns the index of a given token.
     *
     * @param token the token whose index we are searching for
     * @return the index of the token
     */
    int indexOf(T token);

    /**
     * returns the last token the loop may consider.
     *
     * @return the last token the loop may consider
     */
    T getLastToken();

    /**
     * returns the size of this set.
     *
     * @return the size of this set
     */
    int size();

    /**
     * Deep copy.
     *
     * @return the zest loop token set
     */
    ZestLoopTokenSet<T> deepCopy();

    /**
     * Gets the first state.
     *
     * @return the first state
     */
    ZestLoopState<T> getFirstState();
}
