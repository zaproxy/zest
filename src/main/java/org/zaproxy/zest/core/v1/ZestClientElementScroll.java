/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

import org.openqa.selenium.JavascriptExecutor;

/**
 * Scroll in the current frame by (x,y) co-ordinates
 *
 * @author aryan
 */
public class ZestClientElementScroll extends ZestClientElement {

    private int Xvalue = 0;
    private int Yvalue = 0;

    public ZestClientElementScroll(
            String sessionIdName, String type, String element, int Xvalue, int Yvalue) {
        super(sessionIdName, type, element);
        this.Xvalue = Xvalue;
        this.Yvalue = Yvalue;
    }

    public ZestClientElementScroll() {
        super();
    }

    public int getXValue() {
        return Xvalue;
    }

    public int getYValue() {
        return Yvalue;
    }

    public void setXValue(int value) {
        this.Xvalue = value;
    }

    public void setYValue(int value) {
        this.Yvalue = value;
    }

    @Override
    public String invoke(ZestRuntime runtime) throws ZestClientFailException {

        JavascriptExecutor js = (JavascriptExecutor) runtime.getWebDriver(this.getWindowHandle());
        String script = String.format("window.scrollBy(%d,%d);", Xvalue, Yvalue);
        js.executeScript(script, "");

        return null;
    }

    @Override
    public ZestStatement deepCopy() {
        ZestClientElementScroll copy =
                new ZestClientElementScroll(
                        this.getWindowHandle(),
                        this.getType(),
                        this.getElement(),
                        this.Xvalue,
                        this.Yvalue);
        copy.setEnabled(this.isEnabled());
        return copy;
    }

    @Override
    public boolean isPassive() {
        return false;
    }
}
