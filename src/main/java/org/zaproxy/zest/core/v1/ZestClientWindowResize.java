/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

public class ZestClientWindowResize extends ZestClient {

    private String windowHandle;
    private int x;
    private int y;

    public ZestClientWindowResize() {
        super();
    }

    public ZestClientWindowResize(String windowHandle, int x, int y) {
        super();
        this.windowHandle = windowHandle;
        this.x = x;
        this.y = y;
    }

    public String getWindowHandle() {
        return windowHandle;
    }

    public void setWindowHandle(String windowHandle) {
        this.windowHandle = windowHandle;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int value) {
        this.x = value;
    }

    public void setY(int value) {
        this.y = value;
    }

    @Override
    public ZestClientWindowResize deepCopy() {
        ZestClientWindowResize copy = new ZestClientWindowResize(getWindowHandle(), getX(), getY());
        copy.setEnabled(isEnabled());
        return copy;
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

        wd.manage().window().setSize(new Dimension(x, y));
        return null;
    }
}
