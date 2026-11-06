/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.zaproxy.zest.impl.ZestUtils;

/** An abstract class representing an action on a client element. */
public abstract class ZestClientElement extends ZestClient {

    private String windowHandle = null;
    private String type = null;
    private String element = null;
    private int waitForMsec = 0;
    private List<String> shadowPath;

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

    /**
     * Gets the CSS selectors of the shadow hosts, outermost first, containing the element.
     *
     * <p>Each selector is relative to the root (document or shadow root) that contains the host,
     * and the {@link #getElement() element} is relative to the shadow root of the last host. The
     * {@link #getType() type} must be {@code cssSelector} when the path is not empty.
     *
     * @return the shadow path, might be {@code null} or empty if the element is not in a shadow
     *     root.
     */
    public List<String> getShadowPath() {
        return shadowPath;
    }

    public void setShadowPath(List<String> shadowPath) {
        this.shadowPath = shadowPath;
    }

    private By createBy(ZestRuntime runtime) {
        if (shadowPath == null || shadowPath.isEmpty()) {
            return ZestScript.getBy(
                    runtime.replaceVariablesInString(this.getElement(), false), type);
        }
        if (!"cssSelector".equalsIgnoreCase(type)) {
            return null;
        }
        List<String> path = new ArrayList<>(shadowPath.size() + 1);
        for (String host : shadowPath) {
            path.add(runtime.replaceVariablesInString(host, false));
        }
        path.add(runtime.replaceVariablesInString(element, false));
        return new ShadowBy(path);
    }

    public WebElement getWebElement(ZestRuntime runtime) throws ZestClientFailException {

        WebDriver wd = runtime.getWebDriver(this.getWindowHandle());

        if (wd == null) {
            throw new ZestClientFailException(
                    this, "No client: " + runtime.getVariable(getWindowHandle()));
        }

        try {
            By by = createBy(runtime);
            if (by == null) {
                throw new ZestClientFailException(this, "Unsupported type: " + type);
            }

            if (this.waitForMsec > 0) {
                WebDriverWait wait = new WebDriverWait(wd, Duration.ofMillis(waitForMsec));
                try {
                    return wait.until(getExpectedCondition(by));
                } catch (Exception e) {
                    // Ignore, as some frameworks keep elements hidden until you act on them
                    return ZestUtils.withoutImplicitWait(wd, () -> findElement(wd, by));
                }
            }
            return findElement(wd, by);

        } catch (Exception e) {
            throw new ZestClientFailException(this, e);
        }
    }

    private WebElement findElement(WebDriver wd, By by) {
        return wd.findElement(by);
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
        if (shadowPath != null) {
            copy.setShadowPath(new ArrayList<>(shadowPath));
        }
        return copy;
    }
}
