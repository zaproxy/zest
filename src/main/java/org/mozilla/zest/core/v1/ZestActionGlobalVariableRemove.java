/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

/**
 * A {@link ZestAction} that removes a {@link ZestRuntime#setGlobalVariable(String, String) global
 * variable}.
 *
 * @since 0.14.0
 * @see ZestAssignGlobalVariable
 * @see ZestActionGlobalVariableSet
 */
public class ZestActionGlobalVariableRemove extends ZestAction {

    private String globalVariableName;

    public ZestActionGlobalVariableRemove() {
        super();
    }

    public ZestActionGlobalVariableRemove(String globalVariableName) {
        super();
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
    public String invoke(ZestResponse response, ZestRuntime runtime) {
        runtime.setGlobalVariable(globalVariableName, null);
        return null;
    }

    @Override
    public boolean isPassive() {
        return true;
    }

    @Override
    public ZestStatement deepCopy() {
        ZestActionGlobalVariableRemove copy =
                new ZestActionGlobalVariableRemove(globalVariableName);
        copy.setEnabled(isEnabled());
        return copy;
    }
}
