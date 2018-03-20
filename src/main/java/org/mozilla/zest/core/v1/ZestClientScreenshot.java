/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * A {@link ZestClient} action that takes a screenshot.
 *
 * <p>The screenshot can be saved to a file and/or to a variable (for the latter Base64 encoded).
 *
 * @since 0.14
 */
public class ZestClientScreenshot extends ZestClient {

    private String windowHandle;
    private String filePath;
    private String variableName;

    /** Constructs a {@code ZestClientScreenshot} with no file path nor variable name. */
    public ZestClientScreenshot() {
        super();
    }

    /**
     * Constructs a {@code ZestClientScreenshot} with the given file path and variable name.
     *
     * @param windowHandle the window handle to obtain the client.
     * @param filePath the path to the file where to save the screenshot, might be {@code null}.
     * @param variableName the name of the variable where to save the screenshot (Base64 encoded),
     *     might be {@code null}.
     */
    public ZestClientScreenshot(String windowHandle, String filePath, String variableName) {
        super();
        this.windowHandle = windowHandle;
        this.filePath = filePath;
        this.variableName = variableName;
    }

    /**
     * Gets the window handle.
     *
     * @return the window handle, might be {@code null}.
     */
    public String getWindowHandle() {
        return windowHandle;
    }

    /**
     * Sets the window handle.
     *
     * @param windowHandle the new window handle.
     */
    public void setWindowHandle(String windowHandle) {
        this.windowHandle = windowHandle;
    }

    /**
     * Gets the path to the file.
     *
     * @return the path to the file, might be {@code null}.
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Sets the path to the file.
     *
     * <p>Variables are replaced before using the path.
     *
     * @param filePath the path to the file.
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Gets the name of the variable.
     *
     * @return the name of the variable, might be {@code null}.
     */
    public String getVariableName() {
        return variableName;
    }

    /**
     * Sets the name of the variable.
     *
     * @param variableName the name of the variable.
     */
    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public String invoke(ZestRuntime runtime) throws ZestClientFailException {
        WebDriver wd = runtime.getWebDriver(this.getWindowHandle());
        if (wd == null) {
            throw new ZestClientFailException(
                    this, "No client: " + runtime.getVariable(getWindowHandle()));
        }
        if (!(wd instanceof TakesScreenshot)) {
            throw new ZestClientFailException(
                    this,
                    "Client does not take screenshots: " + runtime.getVariable(getWindowHandle()));
        }

        boolean saveToVariable = variableName != null && !variableName.isEmpty();
        boolean saveToFile = filePath != null && !filePath.isEmpty();
        if (!saveToVariable && !saveToFile) {
            throw new ZestClientFailException(this, "No file nor variable specified.");
        }

        byte[] screenshot = ((TakesScreenshot) wd).getScreenshotAs(OutputType.BYTES);

        if (saveToVariable) {
            runtime.setVariable(variableName, Base64.getEncoder().encodeToString(screenshot));
        }

        if (saveToFile) {
            String finalFilePath = runtime.replaceVariablesInString(filePath, false);
            try {
                Files.copy(new ByteArrayInputStream(screenshot), Paths.get(finalFilePath));
            } catch (IOException e) {
                throw new ZestClientFailException(
                        this, "Failed to save the screenshot to file.", e);
            }
        }

        return null;
    }

    @Override
    public ZestClientScreenshot deepCopy() {
        ZestClientScreenshot copy =
                new ZestClientScreenshot(getWindowHandle(), getFilePath(), getVariableName());
        copy.setEnabled(isEnabled());
        return copy;
    }

    @Override
    public boolean isPassive() {
        return true;
    }
}
