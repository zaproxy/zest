/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

// TODO: Auto-generated Javadoc
/** The Class ZestInvalidCommonTestException. */
public class ZestInvalidCommonTestException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The statement. */
    private ZestStatement statement = null;

    /**
     * Instantiates a new zest invalid common test exception.
     *
     * @param statement the statement
     * @param message the message
     */
    public ZestInvalidCommonTestException(ZestStatement statement, String message) {
        super(message);
        this.statement = statement;
    }

    /**
     * Instantiates a new zest invalid common test exception.
     *
     * @param statement the statement
     */
    public ZestInvalidCommonTestException(ZestStatement statement) {
        super();
        this.statement = statement;
    }

    /**
     * Gets the statement.
     *
     * @return the statement
     */
    public ZestStatement getStatement() {
        return statement;
    }
}
