/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.net.URL;

/**
 * A {@link ZestExpression} that checks if the {@link ZestRuntime#getLastRequest() last request} has
 * a given protocol (for example, HTTPS).
 *
 * <p>The check is done in a case-insensitive manner. The expression returns {@code false} if no
 * protocol was set (that is, {@code null}).
 *
 * @since 0.14
 */
public class ZestExpressionProtocol extends ZestExpression {

    private String protocol;

    /** Constructs a {@code ZestExpressionProtocol} with no protocol. */
    public ZestExpressionProtocol() {
        super();
    }

    /**
     * Constructs a {@code ZestExpressionProtocol} with the given protocol.
     *
     * @param protocol the protocol that the request must have for the expression to return {@code
     *     true}.
     */
    public ZestExpressionProtocol(String protocol) {
        this(protocol, false);
    }

    /**
     * Constructs a {@code ZestExpressionProtocol} with the given protocol and inverse state.
     *
     * @param protocol the protocol that the request must have for the expression to return {@code
     *     true}.
     * @param inverse if the expression should be the inverse.
     */
    public ZestExpressionProtocol(String protocol, boolean inverse) {
        super(inverse);
        this.protocol = protocol;
    }

    /**
     * Tells whether or not the {@link ZestRuntime#getLastRequest() last request} matches {@link
     * #getProtocol() the protocol} set.
     */
    @Override
    public boolean isTrue(ZestRuntime runtime) {
        if (protocol == null) {
            return false;
        }

        ZestRequest request = runtime.getLastRequest();
        if (request == null) {
            return false;
        }

        URL url = request.getUrl();
        if (url == null) {
            return false;
        }

        return protocol.equalsIgnoreCase(url.getProtocol());
    }

    /**
     * Gets the protocol that the request must have.
     *
     * @return the protocol, might be {@code null}.
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Sets the protocol that the request must have.
     *
     * @param protocol the new protocol
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public ZestExpressionProtocol deepCopy() {
        return new ZestExpressionProtocol(protocol, isInverse());
    }

    @Override
    public String toString() {
        return (isInverse() ? "NOT " : "") + "Protocol: " + protocol;
    }
}
