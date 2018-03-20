/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** The Class ZestExpressionStatusCode. */
public class ZestExpressionClientElementExists extends ZestExpression {

    private String windowHandle = null;
    private String type = null;
    private String element = null;

    /** Instantiates a new {@code ZestExpressionClientElementExists}. */
    public ZestExpressionClientElementExists() {
        super();
    }

    /**
     * Instantiates a new {@code ZestExpressionClientElementExists}.
     *
     * @param windowHandle the window handle.
     * @param type the type of the expression.
     * @param element the element to check for existence.
     */
    public ZestExpressionClientElementExists(String windowHandle, String type, String element) {
        this(windowHandle, type, element, false);
    }

    /**
     * Constructs a {@code ZestExpressionClientElementExists} with the given data.
     *
     * @param windowHandle the window handle.
     * @param type the type of the expression.
     * @param element the element to check for existence.
     * @param inverse if the expression should be the inverse.
     * @since 0.14
     */
    public ZestExpressionClientElementExists(
            String windowHandle, String type, String element, boolean inverse) {
        super(inverse);
        this.windowHandle = windowHandle;
        this.type = type;
        this.element = element;
    }

    public String getWindowHandle() {
        return windowHandle;
    }

    public void setWindowHandle(String windowHandle) {
        this.windowHandle = windowHandle;
    }

    public String getElement() {
        return element;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setElement(String element) {
        this.element = element;
    }

    @Override
    public boolean isTrue(ZestRuntime runtime) {
        WebDriver wd = runtime.getWebDriver(this.getWindowHandle());

        if (wd == null) {
            return false;
        }
        String elem = runtime.replaceVariablesInString(this.getElement(), false);
        By by = null;

        if ("className".equalsIgnoreCase(type)) {
            by = By.className(elem);
        } else if ("cssSelector".equalsIgnoreCase(type)) {
            by = By.cssSelector(elem);
        } else if ("id".equalsIgnoreCase(type)) {
            by = By.id(elem);
        } else if ("linkText".equalsIgnoreCase(type)) {
            by = By.linkText(elem);
        } else if ("name".equalsIgnoreCase(type)) {
            by = By.name(elem);
        } else if ("partialLinkText".equalsIgnoreCase(type)) {
            by = By.partialLinkText(elem);
        } else if ("tagName".equalsIgnoreCase(type)) {
            by = By.tagName(elem);
        } else if ("xpath".equalsIgnoreCase(type)) {
            by = By.xpath(elem);
        } else {
            return false;
        }

        return wd.findElements(by).size() > 0;
    }

    @Override
    public ZestExpressionClientElementExists deepCopy() {
        return new ZestExpressionClientElementExists(
                this.windowHandle, this.getType(), this.getElement(), isInverse());
    }
}
