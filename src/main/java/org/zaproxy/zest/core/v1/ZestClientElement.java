/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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
     * @return the shadow path, might be {@code null} or empty if the element is not in a shadow
     *     root.
     * @since 0.37.0
     * @see #setShadowPath(List)
     */
    public List<String> getShadowPath() {
        return shadowPath;
    }

    /**
     * Sets the CSS selectors of the shadow hosts, outermost first, containing the element.
     *
     * <p>Each selector should match a single element, otherwise the first match is used. The {@link
     * #getType() type} must be {@code cssSelector}, otherwise the element fails when invoked.
     *
     * <p>Each selector is relative to the root (document or shadow root) that contains the host,
     * and the {@link #getElement() element} is relative to the shadow root of the last host.
     *
     * @param shadowPath the shadow path, {@code null} or empty if the element is not in a shadow
     *     root.
     * @since 0.37.0
     * @see #getShadowPath()
     */
    public void setShadowPath(List<String> shadowPath) {
        this.shadowPath = shadowPath;
    }

    private By createBy(ZestRuntime runtime) throws ZestClientFailException {
        if (shadowPath == null || shadowPath.isEmpty()) {
            return ZestScript.getBy(
                    runtime.replaceVariablesInString(this.getElement(), false), type);
        }
        if (!"cssSelector".equalsIgnoreCase(type)) {
            throw new ZestClientFailException(
                    this, "A shadow path requires the type cssSelector, but was: " + type);
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

    /**
     * Sends the given keys to the element.
     *
     * <p>If the element is in a shadow root and it is not interactable (some drivers can't focus
     * elements in closed shadow roots) the element is focused through the page and the keys are
     * sent to the focused element.
     *
     * @param runtime the runtime.
     * @param element the element to send the keys to.
     * @param keys the keys.
     * @throws ZestClientFailException if the element is not interactable or could not be focused.
     */
    protected void sendKeys(ZestRuntime runtime, WebElement element, CharSequence... keys)
            throws ZestClientFailException {
        try {
            element.sendKeys(keys);
        } catch (ElementNotInteractableException e) {
            if (shadowPath == null || shadowPath.isEmpty()) {
                throw new ZestClientFailException(this, e);
            }
            WebDriver wd = runtime.getWebDriver(windowHandle);
            Object focused =
                    ((JavascriptExecutor) wd)
                            .executeScript(
                                    "arguments[0].focus(); return arguments[0].matches(':focus');",
                                    element);
            if (!Boolean.TRUE.equals(focused)) {
                throw new ZestClientFailException(this, "Unable to focus the element.");
            }
            new Actions(wd).sendKeys(keys).perform();
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
