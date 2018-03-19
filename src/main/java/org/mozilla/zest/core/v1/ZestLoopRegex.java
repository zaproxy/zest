/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

/**
 * This class represent a loop through a list of regex matches for the given variable and pattern a
 * file.
 */
public class ZestLoopRegex extends ZestLoop<String> {

    private ZestLoopTokenRegexSet set = null;

    public ZestLoopRegex(
            String variableName,
            String inputVariableName,
            String regex,
            int groupIndex,
            boolean caseExact) {
        super(variableName);
        this.set = new ZestLoopTokenRegexSet(this, inputVariableName, regex, groupIndex, caseExact);
    }

    /** Instantiates a new zest loop regex. */
    public ZestLoopRegex() {
        super();
        this.set = new ZestLoopTokenRegexSet(this, "", "", 0, false);
    }

    /**
     * Instantiates a new zest loop regex.
     *
     * @param index the index of the statement
     */
    private ZestLoopRegex(int index) {
        super(index);
    }

    @Override
    public ZestLoopRegex deepCopy() {
        ZestLoopRegex copy;
        copy = new ZestLoopRegex(this.getIndex());
        copy.setVariableName(getVariableName());
        copy.setCurrentState(this.getCurrentState().deepCopy());
        copy.setStatements(this.copyStatements());
        copy.setSet(this.getSet().deepCopy());
        copy.setEnabled(this.isEnabled());
        return copy;
    }

    @Override
    public ZestLoopState<String> getCurrentState() {
        return (ZestLoopStateRegex) super.getCurrentState();
    }

    @Override
    public ZestLoopTokenRegexSet getSet() {
        if (this.set != null) {
            this.set.setLoop(this);
        }
        return this.set;
    }

    @Override
    public boolean isLastState() {
        return super.getCurrentState().isLastState(getSet());
    }

    @Override
    public void increase() {
        super.getCurrentState().increase(getSet());
    }

    @Override
    public void toLastState() {
        getCurrentState().toLastState(getSet());
    }

    @Override
    public String getCurrentToken() {
        if (super.getCurrentToken() == null) {
            super.init(getSet(), getStatements());
        }
        return super.getCurrentToken();
    }

    public boolean loop() {
        return super.loop(getSet());
    }

    public void endLoop() {
        super.endLoop(getSet());
    }

    public String getInputVariableName() {
        return getSet().getInputVariableName();
    }

    public String getRegex() {
        return getSet().getRegex();
    }

    public boolean isCaseExact() {
        return getSet().isCaseExact();
    }

    public void setInputVariableName(String variableName) {
        getSet().setInputVariableName(variableName);
    }

    public void setRegex(String regex) {
        getSet().setRegex(regex);
    }

    public void setCaseExact(boolean caseExact) {
        getSet().setCaseExact(caseExact);
    }

    public int getGroupIndex() {
        return getSet().getGroupIndex();
    }

    public void setGroupIndex(int groupIndex) {
        getSet().setGroupIndex(groupIndex);
    }
}
