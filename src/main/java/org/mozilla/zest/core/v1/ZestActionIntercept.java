/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

/**
 * Intercepts can be used by Man in the Middle proxies to select requests and responses to intercept
 * or break on. They are ignored by this reference implementation.
 */
public class ZestActionIntercept extends ZestAction {

    /** Instantiates a new zest action intercept. */
    public ZestActionIntercept() {
        super();
    }

    /**
     * Instantiates a new zest action intercept.
     *
     * @param index the index
     */
    public ZestActionIntercept(int index) {
        super(index);
    }

    @Override
    public ZestActionIntercept deepCopy() {
        ZestActionIntercept copy = new ZestActionIntercept(this.getIndex());
        copy.setEnabled(this.isEnabled());
        return copy;
    }

    @Override
    public String invoke(ZestResponse response, ZestRuntime runtime)
            throws ZestActionFailException {
        // Ignore
        return "";
    }

    @Override
    public boolean isPassive() {
        return false;
    }
}
