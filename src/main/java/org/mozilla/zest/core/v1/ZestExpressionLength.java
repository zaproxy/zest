/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

// TODO: Auto-generated Javadoc
/** The Class ZestExpressionLength. */
public class ZestExpressionLength extends ZestExpression {

    /** The length. */
    private int length;

    /** The approx. */
    private int approx;

    /** The variable name. */
    private String variableName;

    /** Instantiates a new zest expression length. */
    public ZestExpressionLength() {
        this(null, 0, 0);
    }

    /**
     * Instantiates a new zest expression length.
     *
     * @param variableName the variable name
     * @param length the length
     * @param approx the approx
     */
    public ZestExpressionLength(String variableName, int length, int approx) {
        this(variableName, length, approx, false);
    }

    /**
     * Instantiates a new zest expression length.
     *
     * @param variableName the variable name
     * @param length the length
     * @param approx the approximation
     * @param inverse is inverse?
     */
    public ZestExpressionLength(String variableName, int length, int approx, boolean inverse) {
        super(inverse);
        this.variableName = variableName;
        this.length = length;
        this.approx = approx;
    }

    @Override
    public ZestExpressionLength deepCopy() {
        return new ZestExpressionLength(this.variableName, this.length, this.approx, isInverse());
    }

    /**
     * Gets the variable name.
     *
     * @return the variable name
     */
    public String getVariableName() {
        return variableName;
    }

    /**
     * Sets the variable name.
     *
     * @param variableName the new variable name
     */
    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    /**
     * Gets the length.
     *
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * Sets the length.
     *
     * @param length the new length
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Gets the approx.
     *
     * @return the approx
     */
    public int getApprox() {
        return approx;
    }

    /**
     * Sets the approx.
     *
     * @param approx the new approx
     */
    public void setApprox(int approx) {
        this.approx = approx;
    }

    @Override
    public boolean isTrue(ZestRuntime runtime) {
        if (this.variableName == null) {
            return false;
        }
        String value = runtime.getVariable(variableName);
        if (value == null) {
            return false;
        }
        boolean toReturn = Math.abs(length - value.length()) <= length * approx / 100;
        return toReturn;
    }

    @Override
    public String toString() {
        String expression =
                (isInverse() ? "NOT " : "")
                        + "Length: "
                        + length
                        + " +/- "
                        + (((double) (length * approx)) / 100);
        return expression;
    }
}
