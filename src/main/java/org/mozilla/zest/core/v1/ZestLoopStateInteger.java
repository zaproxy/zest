/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

/** The Class ZestLoopStateInteger. */
public class ZestLoopStateInteger extends ZestLoopState<Integer> {

    /** The set. */
    private ZestLoopStateInteger() {
        super();
    }

    /**
     * Instantiates a new zest loop state integer.
     *
     * @param set the set
     */
    public ZestLoopStateInteger(ZestLoopTokenIntegerSet set) {
        super(set);
        this.setCurrentToken(set.getStart());
    }

    @Override
    public boolean increase(ZestLoopTokenSet<Integer> set) {
        ZestLoopTokenIntegerSet integerSet = (ZestLoopTokenIntegerSet) set;
        this.setIndex(getCurrentIndex() + integerSet.getStep());
        if (!isLastState(set)) {
            super.setCurrentToken(this.getCurrentToken() + integerSet.getStep());
            return getCurrentIndex() < set.size();
        } else {
            super.setCurrentToken(set.getLastToken());
            return false;
        }
    }

    @Override
    public ZestLoopStateInteger deepCopy() {
        ZestLoopStateInteger copy = new ZestLoopStateInteger();
        copy.setCurrentToken(this.getCurrentToken());
        copy.setIndex(this.getCurrentIndex());
        return copy;
    }
}
