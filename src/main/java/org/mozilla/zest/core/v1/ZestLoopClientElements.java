/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.io.FileNotFoundException;
import java.io.IOException;

/** This class represent a loop through a list of strings given in input through a file. */
public class ZestLoopClientElements extends ZestLoop<String> {

    private ZestLoopTokenClientElementsSet set = null;

    public ZestLoopClientElements(
            String variableName,
            String windowHandle,
            String type,
            String element,
            String attribute) {
        super(variableName);
        this.set = new ZestLoopTokenClientElementsSet(this, windowHandle, type, element, attribute);
    }

    /**
     * Instantiates a new zest loop file.
     *
     * @throws FileNotFoundException the file not found exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public ZestLoopClientElements() {
        this.set = new ZestLoopTokenClientElementsSet(this, "", "", "", "");
    }

    /**
     * Instantiates a new zest loop file.
     *
     * @param index the index of the statement
     * @throws IOException
     * @throws FileNotFoundException
     */
    private ZestLoopClientElements(int index) {
        super(index);
    }

    @Override
    public ZestLoopClientElements deepCopy() {
        ZestLoopClientElements copy;
        copy = new ZestLoopClientElements(this.getIndex());
        copy.setVariableName(getVariableName());
        copy.setCurrentState(this.getCurrentState().deepCopy());
        copy.setStatements(this.copyStatements());
        copy.setSet(this.getSet().deepCopy());
        copy.setEnabled(this.isEnabled());
        return copy;
    }

    @Override
    public ZestLoopStateClientElements getCurrentState() {
        return (ZestLoopStateClientElements) super.getCurrentState();
    }

    @Override
    public ZestLoopTokenClientElementsSet getSet() {
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

    public String getWindowHandle() {
        return getSet().getWindowHandle();
    }

    public String getElement() {
        return getSet().getElement();
    }

    public String getType() {
        return getSet().getType();
    }

    public String getAttribute() {
        return getSet().getAttribute();
    }

    public void setWindowHandle(String windowHandle) {
        getSet().setWindowHandle(windowHandle);
    }

    public void setElement(String element) {
        getSet().setElement(element);
    }

    public void setType(String type) {
        getSet().setType(type);
    }

    public void setAttribute(String attribute) {
        getSet().setAttribute(attribute);
    }
}
