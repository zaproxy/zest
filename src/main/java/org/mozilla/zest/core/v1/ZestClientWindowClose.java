/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import org.openqa.selenium.WebDriver;

/**
 * Close the specified client
 *
 * @author simon
 */
public class ZestClientWindowClose extends ZestClient {

    private String windowHandle = null;
    private int sleepInSeconds = 0;

    public ZestClientWindowClose() {
        super();
    }

    public ZestClientWindowClose(String windowHandle, int sleepInSeconds) {
        super();
        this.windowHandle = windowHandle;
        this.sleepInSeconds = sleepInSeconds;
    }

    public String getWindowHandle() {
        return windowHandle;
    }

    public void setWindowHandle(String windowHandle) {
        this.windowHandle = windowHandle;
    }

    public int getSleepInSeconds() {
        return sleepInSeconds;
    }

    public void setSleepInSeconds(int sleepInSeconds) {
        this.sleepInSeconds = sleepInSeconds;
    }

    @Override
    public ZestStatement deepCopy() {
        ZestClientWindowClose copy =
                new ZestClientWindowClose(this.getWindowHandle(), this.getSleepInSeconds());
        copy.setEnabled(this.isEnabled());
        return copy;
    }

    @Override
    public boolean isPassive() {
        return false;
    }

    @Override
    public String invoke(ZestRuntime runtime) throws ZestClientFailException {
        WebDriver wd = runtime.getWebDriver(this.getWindowHandle());

        if (wd == null) {
            // No point throwing an exception as we were going to close it anyway
            return null;
        }

        // Wait for the specified number of seconds, unless the window closes by itself
        if (this.sleepInSeconds > 0) {
            int sleepInMs = this.sleepInSeconds * 1000;
            while (sleepInMs > 0) {
                wd = runtime.getWebDriver(this.getWindowHandle());
                if (wd == null) {
                    break;
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    // Ignore
                }
                sleepInMs -= 200;
            }
        }

        if (wd != null) {
            try {
                wd.close();
            } catch (Exception e) {
                // Ignore, it might have already closed
            }
            runtime.removeWebDriver(getWindowHandle());

            if (runtime.getWebDrivers().size() == 0 && this.getNext() == null) {
                // We've closed all of the windows and this is the last statement
                // Explicitly quit - currently needed for the phantomjs driver, otherwise it never
                // returns :/
                wd.quit();
            }
        }

        return null;
    }
}
