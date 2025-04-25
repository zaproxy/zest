/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

import org.openqa.selenium.interactions.Actions;

/**
 * The element being hovered over must be in view already. It is recommended to use the "scroll to"
 * statement to ensure the element is visible before performing the mouse-over action.
 */
public class ZestClientElementMouseOver extends ZestClientElement {

    public ZestClientElementMouseOver(String sessionIdName, String type, String element) {
        super(sessionIdName, type, element);
    }

    public ZestClientElementMouseOver() {
        super();
    }

    @Override
    public String invoke(ZestRuntime runtime) throws ZestClientFailException {

        Actions actions = new Actions(runtime.getWebDriver(getWindowHandle()));
        actions.moveToElement(getWebElement(runtime)).perform();

        return null;
    }

    @Override
    public ZestClientElementMouseOver deepCopy() {
        return this.deepCopy(ZestClientElementMouseOver::new);
    }

    @Override
    public boolean isPassive() {
        return false;
    }
}
