/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

// TODO: Auto-generated Javadoc
/** The Class ZestExpressionStatusCode. */
public class ZestExpressionStatusCode extends ZestExpression {

    /** The code. */
    private int code;

    /** Instantiates a new zest expression status code. */
    public ZestExpressionStatusCode() {
        super();
    }

    /**
     * Instantiates a new zest expression status code.
     *
     * @param code the code
     */
    public ZestExpressionStatusCode(int code) {
        super();
        this.code = code;
    }

    @Override
    public boolean isTrue(ZestRuntime runtime) {
        ZestResponse response = runtime.getLastResponse();
        if (response == null) {
            return false;
        }
        return code == response.getStatusCode();
    }

    /**
     * Gets the code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets the code.
     *
     * @param code the new code
     */
    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public ZestExpressionStatusCode deepCopy() {
        ZestExpressionStatusCode copy = new ZestExpressionStatusCode();
        copy.code = code;
        copy.setInverse(isInverse());
        return copy;
    }

    @Override
    public String toString() {
        String expression = (isInverse() ? "NOT " : "") + "Status Code: " + code;
        return expression;
    }
}
