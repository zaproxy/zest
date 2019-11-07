/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

/**
 * A {@link ZestAction} that sets the value of a {@link ZestRuntime#setGlobalVariable(String,
 * String) global variable}.
 *
 * <p>The value might reference script variables which are replaced before setting it.
 *
 * @since 0.14.0
 * @see ZestAssignGlobalVariable
 * @see ZestActionGlobalVariableRemove
 */
public class ZestActionGlobalVariableSet extends ZestAction {

    private String globalVariableName;
    private String value;

    public ZestActionGlobalVariableSet() {
        super();
    }

    public ZestActionGlobalVariableSet(String globalVariableName, String value) {
        super();
        this.globalVariableName = globalVariableName;
        this.value = value;
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

    /**
     * Gets the value for the global variable.
     *
     * @return the value for the global variable, might be {@code null}.
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value for the global variable.
     *
     * @param value the value for the global variable.
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String invoke(ZestResponse response, ZestRuntime runtime) {
        String replacedValue = runtime.replaceVariablesInString(value, false);
        runtime.setGlobalVariable(globalVariableName, replacedValue);
        return replacedValue;
    }

    @Override
    public boolean isPassive() {
        return true;
    }

    @Override
    public ZestStatement deepCopy() {
        ZestActionGlobalVariableSet copy =
                new ZestActionGlobalVariableSet(globalVariableName, value);
        copy.setEnabled(isEnabled());
        return copy;
    }
}
