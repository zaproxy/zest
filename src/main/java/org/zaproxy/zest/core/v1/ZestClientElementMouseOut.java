/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

import org.openqa.selenium.interactions.Actions;

/**
 * Hovers out from the element.
 *
 * @author aryan 
 */
public class ZestClientElementMouseOut extends ZestClientElement {

    public ZestClientElementMouseOut(String sessionIdName, String type, String element) {
        super(sessionIdName, type, element);
    }

    public ZestClientElementMouseOut() {
        super();
    }

    @Override
    public String invoke(ZestRuntime runtime) throws ZestClientFailException {

        Actions actions = new Actions(runtime.getWebDriver(this.getWindowHandle()));
        actions.moveToElement(this.getWebElement(runtime)).moveToElement(null).perform();

        return null;
    }

    @Override
    public ZestStatement deepCopy() {
        ZestClientElementMouseOut copy =
                new ZestClientElementMouseOut(
                        this.getWindowHandle(), this.getType(), this.getElement());
        copy.setEnabled(this.isEnabled());
        return copy;
    }

    @Override
    public boolean isPassive() {
        return false;
    }
}
