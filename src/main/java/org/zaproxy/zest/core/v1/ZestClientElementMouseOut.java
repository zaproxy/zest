/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class ZestClientElementMouseOut extends ZestClientElement {

    public ZestClientElementMouseOut(String sessionIdName, String type, String element) {
        super(sessionIdName, type, element);
    }

    public ZestClientElementMouseOut() {
        super();
    }

    @Override
    public String invoke(ZestRuntime runtime) throws ZestClientFailException {

        WebDriver wd = runtime.getWebDriver(getWindowHandle());
        Actions actions = new Actions(wd);
        WebElement body = wd.findElement(By.tagName("body"));
        actions.moveToElement(body).perform();

        return null;
    }

    @Override
    public ZestStatement deepCopy() {
        ZestClientElementMouseOut copy =
                new ZestClientElementMouseOut(getWindowHandle(), getType(), getElement());
        copy.setEnabled(isEnabled());
        return copy;
    }

    @Override
    public boolean isPassive() {
        return false;
    }
}
