/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.test.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mozilla.zest.core.v1.ZestClientFailException;
import org.mozilla.zest.core.v1.ZestClientScreenshot;
import org.mozilla.zest.core.v1.ZestJSON;
import org.mozilla.zest.core.v1.ZestRuntime;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/** Unit test for {@link ZestClientScreenshot}. */
public class ZestClientScreenshotUnitTest {

    private static final byte[] SCREENSHOT_DATA = {0x00, 0x01, 0x02};
    private static final String SCREENSHOT_DATA_BASE_64 = "AAEC";

    @Rule public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void shouldBePassive() {
        // Given / When
        ZestClientScreenshot clientScreenshot = new ZestClientScreenshot();
        // Then
        assertEquals(true, clientScreenshot.isPassive());
    }

    @Test
    public void shouldNotHaveWindowHandleByDefault() {
        // Given / When
        ZestClientScreenshot clientScreenshot = new ZestClientScreenshot();
        // Then
        assertEquals(null, clientScreenshot.getWindowHandle());
    }

    @Test
    public void shouldNotHaveFilePathByDefault() {
        // Given / When
        ZestClientScreenshot clientScreenshot = new ZestClientScreenshot();
        // Then
        assertEquals(null, clientScreenshot.getFilePath());
    }

    @Test
    public void shouldNotHaveVariableNameByDefault() {
        // Given / When
        ZestClientScreenshot clientScreenshot = new ZestClientScreenshot();
        // Then
        assertEquals(null, clientScreenshot.getVariableName());
    }

    @Test
    public void shouldBeEnabledByDefault() {
        // Given / When
        ZestClientScreenshot clientScreenshot = new ZestClientScreenshot();
        // Then
        assertEquals(true, clientScreenshot.isEnabled());
    }

    @Test
    public void shouldConstructWithFilePathAndVariableName() {
        // Given
        String windowHandle = "windowHandle";
        String filePath = "/path";
        String variableName = "variableName";
        // When
        ZestClientScreenshot clientScreenshot =
                new ZestClientScreenshot(windowHandle, filePath, variableName);
        // Then
        assertEquals(windowHandle, clientScreenshot.getWindowHandle());
        assertEquals(filePath, clientScreenshot.getFilePath());
        assertEquals(variableName, clientScreenshot.getVariableName());
    }

    @Test
    public void shouldSetWindowHandle() {
        // Given
        String windowHandle = "windowHandle";
        ZestClientScreenshot clientScreenshot = new ZestClientScreenshot();
        // When
        clientScreenshot.setWindowHandle(windowHandle);
        // Then
        assertEquals(windowHandle, clientScreenshot.getWindowHandle());
    }

    @Test
    public void shouldSetFilePath() {
        // Given
        String filePath = "/path";
        ZestClientScreenshot clientScreenshot = new ZestClientScreenshot();
        // When
        clientScreenshot.setFilePath(filePath);
        // Then
        assertEquals(filePath, clientScreenshot.getFilePath());
    }

    @Test
    public void shouldSetVariableName() {
        // Given
        String variableName = "variableName";
        ZestClientScreenshot clientScreenshot = new ZestClientScreenshot();
        // When
        clientScreenshot.setVariableName("variableName");
        // Then
        assertEquals(variableName, clientScreenshot.getVariableName());
    }

    @Test
    public void shouldSetEnabled() {
        // Given
        boolean enabled = false;
        ZestClientScreenshot clientScreenshot = new ZestClientScreenshot();
        // When
        clientScreenshot.setEnabled(enabled);
        // Then
        assertEquals(enabled, clientScreenshot.isEnabled());
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailWhenInvokingWithNullRuntime() throws Exception {
        // Given
        ZestClientScreenshot clientScreenshot =
                new ZestClientScreenshot("handle", "/path", "variableName");
        ZestRuntime runtime = null;
        // When
        clientScreenshot.invoke(runtime);
        // Then = NullPointerException
    }

    @Test(expected = ZestClientFailException.class)
    public void shouldFailToInvokeIfWindowHandleNotAvailable() throws Exception {
        // Given
        ZestClientScreenshot clientScreenshot =
                new ZestClientScreenshot("handle", "/path", "variableName");
        ZestRuntime runtime = runtime();
        // When
        clientScreenshot.invoke(runtime);
        // Then = ZestClientFailException
    }

    @Test(expected = ZestClientFailException.class)
    public void shouldFailToInvokeIfClientDoesNotTakeScreenshots() throws Exception {
        // Given
        String windowHandle = "handle";
        ZestClientScreenshot clientScreenshot =
                new ZestClientScreenshot(windowHandle, "/path", "variableName");
        ZestRuntime runtime = runtimeWithWebDriver(windowHandle);
        // When
        clientScreenshot.invoke(runtime);
        // Then = ZestClientFailException
    }

    @Test(expected = ZestClientFailException.class)
    public void shouldFailToInvokeIfClientTakeScreenshotsButFileAndVariableAreNull()
            throws Exception {
        // Given
        String windowHandle = "windowHandle";
        ZestClientScreenshot clientScreenshot = new ZestClientScreenshot(windowHandle, null, null);
        ZestRuntime runtime = runtimeWithWebDriverThatTakesScreenshot(windowHandle);
        // When
        clientScreenshot.invoke(runtime);
        // Then = ZestClientFailException
    }

    @Test(expected = ZestClientFailException.class)
    public void shouldFailToInvokeIfClientTakeScreenshotsButFileAndVariableAreEmpty()
            throws Exception {
        // Given
        String windowHandle = "windowHandle";
        ZestClientScreenshot clientScreenshot = new ZestClientScreenshot(windowHandle, "", "");
        ZestRuntime runtime = runtimeWithWebDriverThatTakesScreenshot(windowHandle);
        // When
        clientScreenshot.invoke(runtime);
        // Then = ZestClientFailException
    }

    @Test(expected = ZestClientFailException.class)
    public void shouldFailToSaveScreenshotToFileAlreadyExists() throws Exception {
        // Given
        Path path = folder.newFile().toPath();
        String windowHandle = "windowHandle";
        String filePath = path.toString();
        ZestClientScreenshot clientScreenshot =
                new ZestClientScreenshot(windowHandle, filePath, null);
        ZestRuntime runtime =
                runtimeWithWebDriverThatTakesScreenshot(windowHandle, SCREENSHOT_DATA);
        when(runtime.replaceVariablesInString(filePath, false)).thenReturn(filePath);
        // When
        clientScreenshot.invoke(runtime);
        // Then = ZestClientFailException
    }

    @Test(expected = ZestClientFailException.class)
    public void shouldFailToSaveScreenshotToFileDirectory() throws Exception {
        // Given
        Path path = folder.newFolder().toPath();
        String windowHandle = "windowHandle";
        String filePath = path.toString();
        ZestClientScreenshot clientScreenshot =
                new ZestClientScreenshot(windowHandle, filePath, null);
        ZestRuntime runtime =
                runtimeWithWebDriverThatTakesScreenshot(windowHandle, SCREENSHOT_DATA);
        when(runtime.replaceVariablesInString(filePath, false)).thenReturn(filePath);
        // When
        clientScreenshot.invoke(runtime);
        // Then = ZestClientFailException
    }

    @Test
    public void shouldSaveScreenshotToFile() throws Exception {
        // Given
        Path path = folder.getRoot().toPath().resolve("zcs-file-only.png");
        String windowHandle = "windowHandle";
        String filePath = path.toString();
        ZestClientScreenshot clientScreenshot =
                new ZestClientScreenshot(windowHandle, filePath, null);
        ZestRuntime runtime =
                runtimeWithWebDriverThatTakesScreenshot(windowHandle, SCREENSHOT_DATA);
        when(runtime.replaceVariablesInString(filePath, false)).thenReturn(filePath);
        // When
        clientScreenshot.invoke(runtime);
        // Then
        assertTrue(Arrays.equals(SCREENSHOT_DATA, Files.readAllBytes(path)));
    }

    @Test
    public void shouldSaveScreenshotToVariable() throws Exception {
        // Given
        String windowHandle = "windowHandle";
        String variableName = "variableName";
        ZestClientScreenshot clientScreenshot =
                new ZestClientScreenshot(windowHandle, null, variableName);
        ZestRuntime runtime =
                runtimeWithWebDriverThatTakesScreenshot(windowHandle, SCREENSHOT_DATA);
        // When
        clientScreenshot.invoke(runtime);
        // Then
        verify(runtime).setVariable(variableName, SCREENSHOT_DATA_BASE_64);
    }

    @Test
    public void shouldSaveScreenshotToFileAndVariable() throws Exception {
        // Given
        Path path = folder.getRoot().toPath().resolve("zcs-file-var.png");
        String windowHandle = "windowHandle";
        String filePath = path.toString();
        String variableName = "variableName";
        ZestClientScreenshot clientScreenshot =
                new ZestClientScreenshot(windowHandle, filePath, variableName);
        ZestRuntime runtime =
                runtimeWithWebDriverThatTakesScreenshot(windowHandle, SCREENSHOT_DATA);
        when(runtime.replaceVariablesInString(filePath, false)).thenReturn(filePath);
        // When
        clientScreenshot.invoke(runtime);
        // Then
        verify(runtime).setVariable(variableName, SCREENSHOT_DATA_BASE_64);
        assertTrue(Arrays.equals(SCREENSHOT_DATA, Files.readAllBytes(path)));
    }

    @Test
    public void shouldSerialiseAndDeserialise() {
        // Given
        ZestClientScreenshot original =
                new ZestClientScreenshot("windowHandle", "/path", "variableName");
        original.setEnabled(false);
        // When
        String serialisation = ZestJSON.toString(original);
        ZestClientScreenshot deserialised =
                (ZestClientScreenshot) ZestJSON.fromString(serialisation);
        // Then
        assertEquals(deserialised.getElementType(), original.getElementType());
        assertEquals(deserialised.getWindowHandle(), original.getWindowHandle());
        assertEquals(deserialised.getFilePath(), original.getFilePath());
        assertEquals(deserialised.getVariableName(), original.getVariableName());
        assertEquals(deserialised.isEnabled(), original.isEnabled());
    }

    @Test
    public void shouldDeepCopy() throws Exception {
        // Given
        ZestClientScreenshot original =
                new ZestClientScreenshot("windowHandle", "/path", "variableName");
        original.setEnabled(false);
        // When
        ZestClientScreenshot copy = original.deepCopy();
        // Then
        assertTrue(copy != original);
        assertEquals(copy.getElementType(), original.getElementType());
        assertEquals(copy.getWindowHandle(), original.getWindowHandle());
        assertEquals(copy.getFilePath(), original.getFilePath());
        assertEquals(copy.getVariableName(), original.getVariableName());
        assertEquals(copy.isEnabled(), original.isEnabled());
    }

    private static ZestRuntime runtime() {
        return mock(ZestRuntime.class);
    }

    private static ZestRuntime runtimeWithWebDriver(String windowHandle) {
        ZestRuntime runtime = runtime();
        when(runtime.getWebDriver(windowHandle)).thenReturn(mock(WebDriver.class));
        return runtime;
    }

    private static ZestRuntime runtimeWithWebDriverThatTakesScreenshot(String windowHandle) {
        ZestRuntime runtime = runtime();
        when(runtime.getWebDriver(windowHandle))
                .thenReturn(
                        mock(
                                WebDriver.class,
                                withSettings().extraInterfaces(TakesScreenshot.class)));
        return runtime;
    }

    private static ZestRuntime runtimeWithWebDriverThatTakesScreenshot(
            String windowHandle, byte[] screenshotData) {
        ZestRuntime runtime = runtimeWithWebDriverThatTakesScreenshot(windowHandle);
        when(((TakesScreenshot) runtime.getWebDriver(windowHandle)).getScreenshotAs(any()))
                .thenReturn(screenshotData);
        return runtime;
    }
}
