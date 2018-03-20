/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import org.openqa.selenium.WebDriver;

/**
 * A class which allows you to get a session associated with a popup window
 *
 * @author simon
 */
public class ZestClientSwitchToFrame extends ZestClient {

    private String windowHandle = null;
    private int frameIndex = -1;
    private String frameName = null;
    private boolean parent = false;

    public ZestClientSwitchToFrame() {
        super();
    }

    /**
     * Only one of index, name or parent=true should be used
     *
     * @param windowHandle
     * @param index
     * @param name
     * @param parent
     */
    public ZestClientSwitchToFrame(String windowHandle, int index, String name, boolean parent) {
        super();
        this.windowHandle = windowHandle;
        this.frameIndex = index;
        this.frameName = name;
        this.parent = parent;
    }

    public String getWindowHandle() {
        return windowHandle;
    }

    public void setWindowHandle(String windowHandle) {
        this.windowHandle = windowHandle;
    }

    @Override
    public ZestStatement deepCopy() {
        ZestClientSwitchToFrame copy =
                new ZestClientSwitchToFrame(windowHandle, frameIndex, frameName, parent);
        copy.setEnabled(this.isEnabled());
        return copy;
    }

    public int getFrameIndex() {
        return frameIndex;
    }

    public String getFrameName() {
        return frameName;
    }

    public boolean isParent() {
        return parent;
    }

    public void setFrameIndex(int index) {
        this.frameIndex = index;
    }

    public void setFrameName(String name) {
        this.frameName = name;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }

    @Override
    public boolean isPassive() {
        return false;
    }

    @Override
    public String invoke(ZestRuntime runtime) throws ZestClientFailException {

        WebDriver wd = runtime.getWebDriver(this.getWindowHandle());

        if (wd == null) {
            throw new ZestClientFailException(
                    this, "No client: " + runtime.getVariable(getWindowHandle()));
        }

        if (this.frameIndex >= 0) {
            wd.switchTo().frame(this.frameIndex);
        } else if (this.isParent()) {
            wd.switchTo().parentFrame();
        } else {
            wd.switchTo().frame(this.frameName);
        }

        return this.windowHandle;
    }
}
