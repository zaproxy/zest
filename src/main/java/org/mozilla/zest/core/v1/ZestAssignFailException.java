/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

// TODO: Auto-generated Javadoc
/** The Class ZestActionFailException. */
public class ZestAssignFailException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The assignment that failed. */
    private ZestAssignment assign = null;

    /**
     * Instantiates a new zest action fail exception.
     *
     * @param assign the assign
     * @param message the message
     */
    public ZestAssignFailException(ZestAssignment assign, String message) {
        super(message);
        this.assign = assign;
    }

    /**
     * Instantiates a new zest action fail exception.
     *
     * @param assign the assign
     */
    public ZestAssignFailException(ZestAssignment assign) {
        super();
        this.assign = assign;
    }

    /**
     * Gets the action.
     *
     * @return the action
     */
    public ZestAssignment getAssignment() {
        return assign;
    }
}
