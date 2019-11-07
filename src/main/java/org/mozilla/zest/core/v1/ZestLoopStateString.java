/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

/** The Class ZestLoopStateString. */
public class ZestLoopStateString extends ZestLoopState<String> {

    /** Instantiates a new zest loop state string. */
    public ZestLoopStateString() {
        super();
    }

    /**
     * Instantiates a new zest loop state string.
     *
     * @param initializationTokenSet the initialization token set
     */
    public ZestLoopStateString(ZestLoopTokenStringSet initializationTokenSet) {
        super(initializationTokenSet);
    }

    /**
     * Instantiates a new zest loop state string.
     *
     * @param values the values
     */
    public ZestLoopStateString(String[] values) {
        this(new ZestLoopTokenStringSet(values));
    }

    @Override
    public boolean increase(ZestLoopTokenSet<String> set) {
        this.increaseIndex();
        if (this.getCurrentIndex() >= set.size()) {
            this.setCurrentToken(null);
        } else {
            this.setCurrentToken(set.getToken(getCurrentIndex()));
        }
        return true;
    }

    @Override
    public ZestLoopState<String> deepCopy() {
        ZestLoopStateString copy = new ZestLoopStateString();
        copy.setCurrentToken(this.getCurrentToken());
        copy.setIndex(this.getCurrentIndex());
        return copy;
    }
}
