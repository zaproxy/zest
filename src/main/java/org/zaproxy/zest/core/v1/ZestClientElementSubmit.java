/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Submit the specified client element.
 *
 * @author simon
 */
public class ZestClientElementSubmit extends ZestClientElement {

    public ZestClientElementSubmit(String sessionIdName, String type, String element) {
        super(sessionIdName, type, element);
    }

    public ZestClientElementSubmit() {
        super();
    }

    @Override
    public String invoke(ZestRuntime runtime) throws ZestClientFailException {
        WebElement el = this.getWebElement(runtime);
        try {
            el.submit();
        } catch (UnsupportedOperationException e) {
            // This can fail in the input element is not in a form, as per the OWASP Juice Shop
            // login!
            el.sendKeys(Keys.RETURN);
        }
        return null;
    }

    @Override
    public ZestClientElementSubmit deepCopy() {
        return this.deepCopy(ZestClientElementSubmit::new);
    }

    @Override
    protected ExpectedCondition<WebElement> getExpectedCondition(By by) {
        return ExpectedConditions.elementToBeClickable(by);
    }

    @Override
    public boolean isPassive() {
        return false;
    }
}
