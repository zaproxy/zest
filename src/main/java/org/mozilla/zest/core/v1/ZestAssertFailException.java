/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

// TODO: Auto-generated Javadoc
/** The Class ZestAssertFailException. */
public class ZestAssertFailException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The assertion. */
    private ZestAssertion assertion;

    /**
     * Instantiates a new zest assert fail exception.
     *
     * @param assertion the assertion
     */
    public ZestAssertFailException(ZestAssertion assertion) {
        this.assertion = assertion;
    }

    /**
     * Gets the assertion.
     *
     * @return the assertion
     */
    public ZestAssertion getAssertion() {
        return assertion;
    }
}
