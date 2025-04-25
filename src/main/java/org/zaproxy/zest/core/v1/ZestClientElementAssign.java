/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

import org.openqa.selenium.WebElement;

/**
 * Clear the specified client element.
 *
 * @author simon
 */
public class ZestClientElementAssign extends ZestClientElement {

    private String variableName = "";
    private String attribute = "";

    public ZestClientElementAssign(
            String variableName, String sessionIdName, String type, String element) {
        super(sessionIdName, type, element);
        this.variableName = variableName;
    }

    public ZestClientElementAssign(
            String variableName,
            String sessionIdName,
            String type,
            String element,
            String attribute) {
        this(variableName, sessionIdName, type, element);
        this.attribute = attribute;
    }

    public ZestClientElementAssign() {
        super();
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public String invoke(ZestRuntime runtime) throws ZestClientFailException {
        String val;
        if (this.attribute != null && this.attribute.length() > 0) {
            val = getAttribute(getWebElement(runtime), attribute);
        } else {
            val = this.getWebElement(runtime).getText();
        }
        runtime.setVariable(variableName, val);

        return val;
    }

    private static String getAttribute(WebElement element, String name) {
        String value = element.getDomAttribute(name);
        if (value != null) {
            return value;
        }
        return element.getDomProperty(name);
    }

    @Override
    public ZestClientElementAssign deepCopy() {
        ZestClientElementAssign copy = this.deepCopy(ZestClientElementAssign::new);
        copy.setVariableName(this.getVariableName());
        copy.setAttribute(this.getAttribute());
        return copy;
    }

    @Override
    public boolean isPassive() {
        return false;
    }
}
