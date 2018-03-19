/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

// TODO: Auto-generated Javadoc
/** The Class ZestExpressionResponseTime. */
public class ZestExpressionResponseTime extends ZestExpression {

    /** The greater than. */
    private boolean greaterThan;

    /** The time in ms. */
    private long timeInMs;

    /** Instantiates a new zest expression response time. */
    public ZestExpressionResponseTime() {
        this(0);
    }

    /**
     * Instantiates a new zest expression response time.
     *
     * @param time the time
     */
    public ZestExpressionResponseTime(long time) {
        this(time, true);
    }

    /**
     * Constructs a {@code ZestExpressionResponseTime} with the given time and whether the response
     * should be greater than the given time.
     *
     * @param time the time in milliseconds.
     * @param greaterThan if the response time should be greater than the given time.
     * @since 0.14
     */
    public ZestExpressionResponseTime(long time, boolean greaterThan) {
        this(time, greaterThan, false);
    }

    /**
     * Constructs a {@code ZestExpressionResponseTime} with the given time, whether the response
     * should be greater than the given time, and with the given inverse state.
     *
     * @param time the time in milliseconds.
     * @param greaterThan if the response time should be greater than the given time.
     * @param inverse if the expression should be the inverse.
     * @since 0.14
     */
    public ZestExpressionResponseTime(long time, boolean greaterThan, boolean inverse) {
        super(inverse);
        this.timeInMs = time;
        this.greaterThan = greaterThan;
    }

    @Override
    public boolean isTrue(ZestRuntime runtime) {
        ZestResponse response = runtime.getLastResponse();
        if (response == null) {
            return false;
        }
        if (greaterThan) {
            return response.getResponseTimeInMs() > this.timeInMs;
        } else {
            return response.getResponseTimeInMs() < this.timeInMs;
        }
    }

    /**
     * Checks if is greater than.
     *
     * @return true, if is greater than
     */
    public boolean isGreaterThan() {
        return greaterThan;
    }

    /**
     * Sets the greater than.
     *
     * @param greaterThan the new greater than
     */
    public void setGreaterThan(boolean greaterThan) {
        this.greaterThan = greaterThan;
    }

    /**
     * Gets the time in ms.
     *
     * @return the time in ms
     */
    public long getTimeInMs() {
        return timeInMs;
    }

    /**
     * Sets the time in ms.
     *
     * @param timeInMs the new time in ms
     */
    public void setTimeInMs(long timeInMs) {
        this.timeInMs = timeInMs;
    }

    @Override
    public ZestExpressionResponseTime deepCopy() {
        return new ZestExpressionResponseTime(timeInMs, greaterThan, isInverse());
    }

    @Override
    public String toString() {
        String expression =
                (isInverse() ? "NOT " : "")
                        + "Response Time "
                        + (isGreaterThan() ? "> " : "< ")
                        + timeInMs;
        return expression;
    }
}
