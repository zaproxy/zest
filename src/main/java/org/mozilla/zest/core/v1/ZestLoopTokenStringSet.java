/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.util.LinkedList;
import java.util.List;

// TODO: Auto-generated Javadoc
/** The Class ZestLoopTokenStringSet. */
public class ZestLoopTokenStringSet extends ZestElement implements ZestLoopTokenSet<String> {

    /** The tokens. */
    private List<String> tokens = new LinkedList<>();

    /** Instantiates a new zest loop token string set. */
    public ZestLoopTokenStringSet() {
        super();
    }

    /**
     * Instantiates a new zest loop token string set.
     *
     * @param tokens the tokens
     */
    public ZestLoopTokenStringSet(List<String> tokens) {
        super();
        this.tokens = tokens;
    }

    /**
     * Instantiates a new zest loop token string set.
     *
     * @param values the values
     */
    public ZestLoopTokenStringSet(String[] values) {
        super();
        tokens = new LinkedList<>();
        for (String value : values) {
            tokens.add(value);
        }
    }

    public void addToken(String token) {
        if (tokens == null) {
            tokens = new LinkedList<>();
        }
        tokens.add(token);
    }

    @Override
    public String getToken(int index) {
        if (tokens == null || tokens.isEmpty()) {
            return null;
        } else {
            return tokens.get(index);
        }
    }
    /**
     * returns the tokens of this Set
     *
     * @return the tokens of this set
     */
    public List<String> getTokens() {
        return this.tokens;
    }

    @Override
    public int indexOf(String token) {
        return tokens.indexOf(token);
    }

    @Override
    public String getLastToken() {
        if (tokens == null || tokens.isEmpty()) {
            return null;
        } else {
            return this.tokens.get(tokens.size() - 1);
        }
    }

    @Override
    public int size() {
        return tokens.size();
    }

    @Override
    public ZestLoopTokenStringSet deepCopy() {
        if (this.tokens == null) {
            return new ZestLoopTokenStringSet();
        }
        ZestLoopTokenStringSet copy = new ZestLoopTokenStringSet();
        for (String token : this.tokens) {
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
        String replaced = this.tokens.get(indexOfReplace);
        this.tokens.remove(indexOfReplace);
        this.tokens.add(indexOfReplace, newToken);
        return replaced;
    }

    @Override
    public ZestLoopStateString getFirstState() {
        ZestLoopStateString firstState = new ZestLoopStateString(this);
        return firstState;
    }
}
