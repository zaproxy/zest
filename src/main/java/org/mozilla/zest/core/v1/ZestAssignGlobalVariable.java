/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

/**
 * A {@link ZestAssignment} from a {@link ZestRuntime#getGlobalVariable(String) global variable}.
 *
 * @since 0.14.0
 * @see ZestActionGlobalVariableRemove
 * @see ZestActionGlobalVariableSet
 */
public class ZestAssignGlobalVariable extends ZestAssignment {

    private String globalVariableName;

    /**
     * Constructs a {@code ZestAssignGlobalVariable} with no target variable nor global variable.
     */
    public ZestAssignGlobalVariable() {}

    /**
     * Constructs a {@code ZestAssignGlobalVariable} with the given target variable.
     *
     * @param variableName the name of the variable where to assign the global variable, might be
     *     {@code null}.
     */
    public ZestAssignGlobalVariable(String variableName) {
        super(variableName);
    }

    /**
     * Constructs a {@code ZestAssignGlobalVariable} with the given target variable and global
     * variable.
     *
     * @param variableName the name of the variable where to assign the global variable, might be
     *     {@code null}.
     * @param globalVariableName the name of the global variable, might be {@code null}.
     */
    public ZestAssignGlobalVariable(String variableName, String globalVariableName) {
        super(variableName);
        this.globalVariableName = globalVariableName;
    }

    /**
     * Gets the name of the global variable.
     *
     * @return the name of the global variable, might be {@code null}.
     */
    public String getGlobalVariableName() {
        return globalVariableName;
    }

    /**
     * Sets the name of the global variable.
     *
     * @param globalVariableName the name of the global variable.
     */
    public void setGlobalVariableName(String globalVariableName) {
        this.globalVariableName = globalVariableName;
    }

    @Override
    public String assign(ZestResponse response, ZestRuntime runtime) {
        return runtime.getGlobalVariable(globalVariableName);
    }

    @Override
    public ZestAssignGlobalVariable deepCopy() {
        ZestAssignGlobalVariable copy =
                new ZestAssignGlobalVariable(this.getVariableName(), this.globalVariableName);
        copy.setEnabled(this.isEnabled());
        return copy;
    }
}
