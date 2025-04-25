/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

/**
 * Send key presses to the specified client element.
 *
 * @author simon
 */
public class ZestClientElementSendKeys extends ZestClientElement {

    private String value = null;

    public ZestClientElementSendKeys(
            String sessionIdName, String type, String element, String value) {
        super(sessionIdName, type, element);
        this.value = value;
    }

    public ZestClientElementSendKeys() {
        super();
    }

    @Override
    public String invoke(ZestRuntime runtime) throws ZestClientFailException {
        String str = runtime.replaceVariablesInString(value, false);
        this.getWebElement(runtime).sendKeys(str);
        return str;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public ZestClientElementSendKeys deepCopy() {
        ZestClientElementSendKeys copy = this.deepCopy(ZestClientElementSendKeys::new);
        copy.setValue(this.getValue());
        return copy;
    }

    @Override
    public boolean isPassive() {
        return false;
    }
}
