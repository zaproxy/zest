/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.util.LinkedList;

/** This class represent a loop through a set of integers. */
public class ZestLoopInteger extends ZestLoop<Integer> {

    private ZestLoopTokenIntegerSet set;
    /** Instantiates a new zest loop integer. */
    public ZestLoopInteger() {
        super();
        this.set = new ZestLoopTokenIntegerSet();
        super.init(getSet(), new LinkedList<ZestStatement>());
    }

    /**
     * Instantiates a new zest loop integer.
     *
     * @param name the name of the loop
     */
    public ZestLoopInteger(String name) {
        super();
        super.setVariableName(name);
        this.set = new ZestLoopTokenIntegerSet();
        super.init(set, new LinkedList<ZestStatement>());
    }

    /**
     * Instantiates a new zest loop integer.
     *
     * @param name the name of the loop
     * @param start the start index
     * @param end the end index
     */
    public ZestLoopInteger(String name, int start, int end) {
        super();
        super.setVariableName(name);
        this.set = new ZestLoopTokenIntegerSet(start, end);
        super.init(set, new LinkedList<ZestStatement>());
    }

    /**
     * Instantiates a new zest loop integer.
     *
     * @param index the index of the statement
     * @param start the start index
     * @param end the end index
     */
    public ZestLoopInteger(int index, int start, int end) {
        super(index);
        this.set = new ZestLoopTokenIntegerSet(start, end);
        super.init(set, new LinkedList<ZestStatement>());
    }

    /**
     * Gets the start index.
     *
     * @return the start index
     */
    public int getStart() {
        return this.getSet().getStart();
    }

    /**
     * Gets the end index.
     *
     * @return the end index
     */
    public int getEnd() {
        return this.getSet().getEnd();
    }

    /**
     * Instantiates a new zest loop integer.
     *
     * @param start the start index
     * @param end the end index
     */
    public ZestLoopInteger(int start, int end) {
        this("", start, end);
    }

    @Override
    public ZestLoopStateInteger getCurrentState() {
        return (ZestLoopStateInteger) super.getCurrentState();
    }
    /**
     * sets the step for the loop
     *
     * @param step the step for this loop
     */
    public void setStep(int step) {
        this.getSet().setStep(step);
    }
    /**
     * returns the step of this loop
     *
     * @return the step of this loop
     */
    public int getStep() {
        return this.getSet().getStep();
    }

    @Override
    public ZestLoopInteger deepCopy() {
        ZestLoopStateInteger state = this.getCurrentState().deepCopy();
        ZestLoopTokenIntegerSet set = this.getSet();
        ZestLoopInteger copy = new ZestLoopInteger(set.getStart(), set.getEnd());
        copy.setVariableName(this.getVariableName());
        for (ZestStatement stmt : this.getStatements()) {
            copy.addStatement(stmt.deepCopy());
        }
        copy.setCurrentState(state);
        copy.setEnabled(this.isEnabled());
        return copy;
    }

    @Override
    public ZestLoopTokenIntegerSet getSet() {
        return this.set;
    }

    @Override
    public void setSet(ZestLoopTokenSet<Integer> newSet) {
        super.setSet(newSet);
        this.set = (ZestLoopTokenIntegerSet) newSet;
    }

    @Override
    public boolean isLastState() {
        return getCurrentState().getCurrentIndex() >= this.getSet().size();
    }

    @Override
    public void increase() {
        this.getCurrentState().increase(getSet());
    }

    @Override
    public void toLastState() {
        this.getCurrentState().toLastState(getSet());
    }

    @Override
    public Integer getCurrentToken() {
        if (super.getCurrentToken() == null) {
            super.init(getSet(), getStatements());
        }
        return super.getCurrentToken();
    }

    public boolean loop() {
        return super.loop(getSet());
    }

    public void endLoop() {
        this.endLoop(getSet());
    }
    /**
     * sets a new start index
     *
     * @param newStart the new start index
     */
    public void setStart(int newStart) {
        this.setSet(new ZestLoopTokenIntegerSet(newStart, getEnd()));
    }
    /**
     * sets the new end index
     *
     * @param newEnd the new end index
     */
    public void setEnd(int newEnd) {
        this.setSet(new ZestLoopTokenIntegerSet(this.getStart(), newEnd));
    }
}
