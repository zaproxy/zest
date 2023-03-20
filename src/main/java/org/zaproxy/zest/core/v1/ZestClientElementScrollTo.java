/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

import org.openqa.selenium.JavascriptExecutor;

public class ZestClientElementScrollTo extends ZestClientElement {

    public ZestClientElementScrollTo(String sessionIdName, String type, String element) {
        super(sessionIdName, type, element);
    }

    public ZestClientElementScrollTo() {
        super();
    }

    @Override
    public String invoke(ZestRuntime runtime) throws ZestClientFailException {

        JavascriptExecutor js = (JavascriptExecutor) runtime.getWebDriver(getWindowHandle());
        String script = "arguments[0].scrollIntoView();";
        js.executeScript(script, getWebElement(runtime));

        return null;
    }

    @Override
    public ZestClientElementScrollTo deepCopy() {
        ZestClientElementScrollTo copy =
                new ZestClientElementScrollTo(getWindowHandle(), getType(), getElement());
        copy.setEnabled(isEnabled());
        return copy;
    }

    @Override
    public boolean isPassive() {
        return false;
    }
}
