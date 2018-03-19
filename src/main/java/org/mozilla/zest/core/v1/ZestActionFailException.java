/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

// TODO: Auto-generated Javadoc
/** The Class ZestActionFailException. */
public class ZestActionFailException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The action. */
    private ZestAction action = null;

    /**
     * Instantiates a new zest action fail exception.
     *
     * @param action the action
     * @param message the message
     */
    public ZestActionFailException(ZestAction action, String message) {
        super(message);
        this.action = action;
    }

    /**
     * Instantiates a new zest action fail exception.
     *
     * @param action the action
     * @param cause the cause
     */
    public ZestActionFailException(ZestAction action, Throwable cause) {
        super(cause);
        this.action = action;
    }

    /**
     * Instantiates a new zest action fail exception.
     *
     * @param action the action
     */
    public ZestActionFailException(ZestAction action) {
        super();
        this.action = action;
    }

    /**
     * Gets the action.
     *
     * @return the action
     */
    public ZestAction getAction() {
        return action;
    }
}
