/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.security.SecureRandom;

// TODO: Auto-generated Javadoc
/** The Class ZestTransformRndIntReplace assigned a random integer to the specified variable. */
public class ZestAssignRandomInteger extends ZestAssignment {

    /** The min int. */
    private int minInt = 0;

    /** The max int. */
    private int maxInt = Integer.MAX_VALUE;

    /** The rnd. */
    private transient SecureRandom rnd = new SecureRandom();

    /** Instantiates a new zest assign random integer. */
    public ZestAssignRandomInteger() {}

    /**
     * Instantiates a new zest assign random integer.
     *
     * @param variableName the variable name
     */
    public ZestAssignRandomInteger(String variableName) {
        super(variableName);
    }

    /**
     * Instantiates a new zest assign random integer.
     *
     * @param variableName the variable name
     * @param minInt the min int
     * @param maxInt the max int
     */
    public ZestAssignRandomInteger(String variableName, int minInt, int maxInt) {
        super(variableName);
        this.minInt = minInt;
        this.maxInt = maxInt;
    }

    @Override
    public String assign(ZestResponse response, ZestRuntime runtime)
            throws ZestAssignFailException {
        int val = minInt + rnd.nextInt(maxInt - minInt);
        return Integer.toString(val);
    }

    @Override
    public ZestAssignRandomInteger deepCopy() {
        ZestAssignRandomInteger copy =
                new ZestAssignRandomInteger(this.getVariableName(), this.minInt, this.maxInt);
        copy.setEnabled(this.isEnabled());
        return copy;
    }

    /**
     * Gets the min int.
     *
     * @return the min int
     */
    public int getMinInt() {
        return minInt;
    }

    /**
     * Sets the min int.
     *
     * @param minInt the new min int
     */
    public void setMinInt(int minInt) {
        this.minInt = minInt;
    }

    /**
     * Gets the max int.
     *
     * @return the max int
     */
    public int getMaxInt() {
        return maxInt;
    }

    /**
     * Sets the max int.
     *
     * @param maxInt the new max int
     */
    public void setMaxInt(int maxInt) {
        this.maxInt = maxInt;
    }
}
