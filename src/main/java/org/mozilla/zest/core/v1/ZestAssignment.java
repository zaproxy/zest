/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.net.MalformedURLException;

// TODO: Auto-generated Javadoc
/** The Class ZestAction. */
public abstract class ZestAssignment extends ZestStatement {

    /** The variable name. */
    private String variableName;

    /** Instantiates a new zest action. */
    public ZestAssignment() {
        super();
    }

    /**
     * Instantiates a new zest assignment.
     *
     * @param variableName the variable name
     */
    public ZestAssignment(String variableName) {
        super();
        this.variableName = variableName;
    }

    /**
     * Instantiates a new zest action.
     *
     * @param index the index
     */
    public ZestAssignment(int index) {
        super(index);
    }

    @Override
    public boolean isSameSubclass(ZestElement ze) {
        return ze instanceof ZestAssignment;
    }

    @Override
    void setPrefix(String oldPrefix, String newPrefix) throws MalformedURLException {
        // Ignore
    }

    /**
     * Returns the variable name.
     *
     * @return the variable name
     */
    public String getVariableName() {
        return variableName;
    }

    /**
     * Sets the variable name.
     *
     * @param name the new variable name
     */
    public void setVariableName(String name) {
        this.variableName = name;
    }

    @Override
    public boolean isPassive() {
        return true;
    }

    /**
     * Invoke.
     *
     * @param response the response
     * @return the string
     * @throws ZestAssignFailException the zest assign fail exception
     */
    public abstract String assign(ZestResponse response, ZestRuntime runtime)
            throws ZestAssignFailException;
}
