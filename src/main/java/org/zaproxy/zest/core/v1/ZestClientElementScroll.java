/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

import org.openqa.selenium.JavascriptExecutor;

public class ZestClientElementScroll extends ZestClientElement {

    private int x;
    private int y;

    public ZestClientElementScroll(
            String sessionIdName, String type, String element, int x, int y) {
        super(sessionIdName, type, element);
        this.x = x;
        this.y = y;
    }

    public ZestClientElementScroll() {
        super();
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
    public String invoke(ZestRuntime runtime) throws ZestClientFailException {

        JavascriptExecutor js = (JavascriptExecutor) runtime.getWebDriver(getWindowHandle());
        String x1 = String.valueOf(x);
        String y1 = String.valueOf(y);
        String script = String.format("window.scrollBy(%s,%s);", x1, y1);
        js.executeScript(script, "");

        return null;
    }

    @Override
    public ZestClientElementScroll deepCopy() {
        ZestClientElementScroll copy =
                new ZestClientElementScroll(
                        getWindowHandle(),
                        getType(),
                        getElement(),
                        x,
                        y);
        copy.setEnabled(isEnabled());
        return copy;
    }

    @Override
    public boolean isPassive() {
        return false;
    }
}
