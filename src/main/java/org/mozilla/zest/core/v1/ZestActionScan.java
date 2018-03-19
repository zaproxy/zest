/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

// TODO: Auto-generated Javadoc
/** The Class ZestActionScan. */
public class ZestActionScan extends ZestAction {

    /** The target parameter. */
    private String targetParameter;

    /** Instantiates a new zest action scan. */
    public ZestActionScan() {
        super();
    }

    /**
     * Instantiates a new zest action scan.
     *
     * @param targetParameter the target parameter
     */
    public ZestActionScan(String targetParameter) {
        super();
        this.targetParameter = targetParameter;
    }

    /**
     * Instantiates a new zest action scan.
     *
     * @param index the index
     */
    public ZestActionScan(int index) {
        super(index);
    }

    /**
     * Gets the target parameter.
     *
     * @return the target parameter
     */
    public String getTargetParameter() {
        return targetParameter;
    }

    /**
     * Sets the target parameter.
     *
     * @param targetParameter the new target parameter
     */
    public void setTargetParameter(String targetParameter) {
        this.targetParameter = targetParameter;
    }

    @Override
    public ZestActionScan deepCopy() {
        ZestActionScan copy = new ZestActionScan(this.getIndex());
        copy.targetParameter = this.targetParameter;
        copy.setEnabled(this.isEnabled());
        return copy;
    }

    @Override
    public String invoke(ZestResponse response, ZestRuntime runtime)
            throws ZestActionFailException {
        throw new ZestActionFailException(this);
    }

    @Override
    public boolean isPassive() {
        return false;
    }
}
