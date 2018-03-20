/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.util.LinkedList;

/** this class represents a Loop through String values. */
public class ZestLoopString extends ZestLoop<String> {
    private ZestLoopTokenStringSet set;

    /** Instantiates a new zest loop string. */
    public ZestLoopString() {
        super();
        this.set = new ZestLoopTokenStringSet();
        super.init(set, new LinkedList<ZestStatement>());
    }

    /**
     * Instantiates a new zest loop string.
     *
     * @param name the name
     * @param values the values
     */
    public ZestLoopString(String name, String[] values) {
        super();
        this.set = new ZestLoopTokenStringSet(values);
        super.setVariableName(name);
        super.init(set, new LinkedList<ZestStatement>());
    }

    /**
     * Instantiates a new zest loop string.
     *
     * @param values the values
     */
    public ZestLoopString(String[] values) {
        super();
        this.set = new ZestLoopTokenStringSet(values);
        super.init(set, new LinkedList<ZestStatement>());
    }

    /**
     * Instantiates a new zest loop string.
     *
     * @param index the index of the statement loop.
     */
    public ZestLoopString(int index) {
        super(index);
        this.set = new ZestLoopTokenStringSet();
        super.init(set, new LinkedList<ZestStatement>());
    }

    /**
     * Instantiates a new zest loop string.
     *
     * @param index the index of the statement
     * @param values the values
     */
    public ZestLoopString(int index, String[] values) {
        super(index);
        this.set = new ZestLoopTokenStringSet(values);
        super.init(set, new LinkedList<ZestStatement>());
    }

    /**
     * Gets the values.
     *
     * @return the values
     */
    public String[] getValues() {
        ZestLoopTokenStringSet set = this.getSet();
        String[] array = new String[set.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = set.getToken(i);
        }
        return array;
    }

    @Override
    public ZestLoopString deepCopy() {
        ZestLoopString copy = new ZestLoopString(this.getIndex());
        copy.setVariableName(this.getVariableName());
        copy.set = this.set.deepCopy();
        copy.setCurrentState(this.getCurrentState().deepCopy());
        copy.setStatements(this.copyStatements());
        copy.setEnabled(this.isEnabled());
        return copy;
    }

    @Override
    public ZestLoopStateString getCurrentState() {
        return (ZestLoopStateString) super.getCurrentState();
    }

    @Override
    public ZestLoopTokenStringSet getSet() {
        return this.set;
    }

    @Override
    public void setSet(ZestLoopTokenSet<String> set) {
        if (set instanceof ZestLoopTokenStringSet) {
            this.set = (ZestLoopTokenStringSet) set;
            super.init(this.set, this.getStatements());
        } else if (set instanceof ZestLoopTokenFileSet) {
            ZestLoopTokenFileSet fileSet = (ZestLoopTokenFileSet) set;
            this.set = fileSet.getConvertedSet();
            super.init(fileSet.getConvertedSet(), this.getStatements());
        } else {
            throw new IllegalArgumentException(
                    "Invalid set. It must be instance of "
                            + ZestLoopTokenStringSet.class.getName());
        }
    }

    @Override
    public boolean isLastState() {
        return super.getCurrentState().isLastState(set);
    }

    @Override
    public void increase() {
        super.getCurrentState().increase(set);
    }

    @Override
    public void toLastState() {
        super.getCurrentState().toLastState(set);
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
}
