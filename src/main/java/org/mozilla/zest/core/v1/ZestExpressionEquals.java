/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

/** The Class ZestExpressionRegex. */
public class ZestExpressionEquals extends ZestExpression {

    /** The value to compare with. */
    private String value;

    /** The variableName which will be assigned to. */
    private String variableName;

    /** The case exact. */
    private boolean caseExact = false;

    /** Instantiates a new zest expression regex. */
    public ZestExpressionEquals() {
        this("", null, false, false);
    }

    /**
     * Instantiates a new zest expression regex.
     *
     * @param variableName the variableName
     * @param regex the regex
     */
    public ZestExpressionEquals(String variableName, String regex) {
        this(variableName, regex, false, false);
    }

    /**
     * Instantiates a new zest expression regex.
     *
     * @param variableName the variableName
     * @param value the value
     * @param caseExact the case exact
     * @param inverse the inverse
     */
    public ZestExpressionEquals(
            String variableName, String value, boolean caseExact, boolean inverse) {
        super(inverse);
        this.variableName = variableName;
        this.value = value;
        this.caseExact = caseExact;
    }

    @Override
    public boolean isTrue(ZestRuntime runtime) {
        String str = runtime.getVariable(variableName);
        if (str == null) {
            return false;
        }

        String val = runtime.replaceVariablesInString(value, false);

        if (this.caseExact) {
            return str.equals(val);
        } else {
            return str.equalsIgnoreCase(val);
        }
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
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value the new value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Checks if is case exact.
     *
     * @return true, if is case exact
     */
    public boolean isCaseExact() {
        return caseExact;
    }

    /**
     * Sets the case exact.
     *
     * @param caseExact the new case exact
     */
    public void setCaseExact(boolean caseExact) {
        this.caseExact = caseExact;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public ZestExpressionEquals deepCopy() {
        return new ZestExpressionEquals(
                this.getVariableName(), this.getValue(), this.isCaseExact(), this.isInverse());
    }
}
