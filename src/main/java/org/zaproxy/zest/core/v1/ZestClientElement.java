/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

import java.time.Duration;
import java.util.function.Supplier;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** An abstract class representing an action on a client element. */
public abstract class ZestClientElement extends ZestClient {

    private String windowHandle = null;
    private String type = null;
    private String element = null;
    private int waitForMsec = 0;

    public ZestClientElement(String windowHandle, String type, String element, int waitForMsec) {
        super();
        this.windowHandle = windowHandle;
        this.type = type;
        this.element = element;
        this.waitForMsec = waitForMsec;
    }

    public ZestClientElement(String windowHandle, String type, String element) {
        super();
        this.windowHandle = windowHandle;
        this.type = type;
        this.element = element;
    }

    public ZestClientElement() {
        super();
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

    public int getWaitForMsec() {
        return waitForMsec;
    }

    public void setWaitForMsec(int waitForMsec) {
        this.waitForMsec = waitForMsec;
    }

    public WebElement getWebElement(ZestRuntime runtime) throws ZestClientFailException {

        WebDriver wd = runtime.getWebDriver(this.getWindowHandle());

        if (wd == null) {
            throw new ZestClientFailException(
                    this, "No client: " + runtime.getVariable(getWindowHandle()));
        }

        try {
            By by =
                    ZestScript.getBy(
                            runtime.replaceVariablesInString(this.getElement(), false), type);
            if (by == null) {
                throw new ZestClientFailException(this, "Unsupported type: " + type);
            }

            if (this.waitForMsec > 0) {
                WebDriverWait wait = new WebDriverWait(wd, Duration.ofMillis(waitForMsec));
                return wait.until(getExpectedCondition(by));
            }
            return wd.findElement(by);

        } catch (Exception e) {
            throw new ZestClientFailException(this, e);
        }
    }

    /**
     * Gets the excepted condition to wait for the element.
     *
     * <p>Implementations should override this method to provide a more appropriate condition, by
     * default it uses {@link ExpectedConditions#visibilityOfElementLocated(By)}.
     *
     * @param by the element locator, never {@code null}.
     * @return the expected condition, should not be {@code null}.
     * @since 0.27.0
     * @see #getWaitForMsec()
     */
    protected ExpectedCondition<WebElement> getExpectedCondition(By by) {
        return ExpectedConditions.visibilityOfElementLocated(by);
    }

    protected <T extends ZestClientElement> T deepCopy(Supplier<T> t) {
        T copy = t.get();
        copy.setType(this.getType());
        copy.setElement(this.getElement());
        copy.setEnabled(this.isEnabled());
        copy.setWaitForMsec(this.getWaitForMsec());
        copy.setWindowHandle(this.getWindowHandle());
        return copy;
    }
}
