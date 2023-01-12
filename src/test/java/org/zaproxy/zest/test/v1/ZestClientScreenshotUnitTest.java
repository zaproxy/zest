/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.zaproxy.zest.core.v1.ZestClientFailException;
import org.zaproxy.zest.core.v1.ZestClientScreenshot;
import org.zaproxy.zest.core.v1.ZestJSON;
import org.zaproxy.zest.core.v1.ZestRuntime;

/** Unit test for {@link ZestClientScreenshot}. */
class ZestClientScreenshotUnitTest {

    private static final byte[] SCREENSHOT_DATA = {0x00, 0x01, 0x02};
    private static final String SCREENSHOT_DATA_BASE_64 = "AAEC";

    @TempDir Path folder;

    @Test
    void shouldBePassive() {
        // Given / When
        ZestClientScreenshot clientScreenshot = new ZestClientScreenshot();
        // Then
        assertEquals(true, clientScreenshot.isPassive());
    }

    @Test
    void shouldNotHaveWindowHandleByDefault() {
        // Given / When
        ZestClientScreenshot clientScreenshot = new ZestClientScreenshot();
        // Then
        assertNull(clientScreenshot.getWindowHandle());
    }

    @Test
    void shouldNotHaveFilePathByDefault() {
        // Given / When
        ZestClientScreenshot clientScreenshot = new ZestClientScreenshot();
        // Then
        assertNull(clientScreenshot.getFilePath());
    }

    @Test
    void shouldNotHaveVariableNameByDefault() {
        // Given / When
        ZestClientScreenshot clientScreenshot = new ZestClientScreenshot();
        // Then
        assertNull(clientScreenshot.getVariableName());
    }

    @Test
    void shouldBeEnabledByDefault() {
        // Given / When
        ZestClientScreenshot clientScreenshot = new ZestClientScreenshot();
        // Then
        assertEquals(true, clientScreenshot.isEnabled());
    }

    @Test
    void shouldConstructWithFilePathAndVariableName() {
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
    void shouldSetWindowHandle() {
        // Given
        String windowHandle = "windowHandle";
        ZestClientScreenshot clientScreenshot = new ZestClientScreenshot();
        // When
        clientScreenshot.setWindowHandle(windowHandle);
        // Then
        assertEquals(windowHandle, clientScreenshot.getWindowHandle());
    }

    @Test
    void shouldSetFilePath() {
        // Given
        String filePath = "/path";
        ZestClientScreenshot clientScreenshot = new ZestClientScreenshot();
        // When
        clientScreenshot.setFilePath(filePath);
        // Then
        assertEquals(filePath, clientScreenshot.getFilePath());
    }

    @Test
    void shouldSetVariableName() {
        // Given
        String variableName = "variableName";
        ZestClientScreenshot clientScreenshot = new ZestClientScreenshot();
        // When
        clientScreenshot.setVariableName("variableName");
        // Then
        assertEquals(variableName, clientScreenshot.getVariableName());
    }

    @Test
    void shouldSetEnabled() {
        // Given
        boolean enabled = false;
        ZestClientScreenshot clientScreenshot = new ZestClientScreenshot();
        // When
        clientScreenshot.setEnabled(enabled);
        // Then
        assertEquals(enabled, clientScreenshot.isEnabled());
    }

    @Test
    void shouldFailWhenInvokingWithNullRuntime() throws Exception {
        // Given
        ZestClientScreenshot clientScreenshot =
                new ZestClientScreenshot("handle", "/path", "variableName");
        ZestRuntime runtime = null;
        // When / Then
        assertThrows(NullPointerException.class, () -> clientScreenshot.invoke(runtime));
    }

    @Test
    void shouldFailToInvokeIfWindowHandleNotAvailable() throws Exception {
        // Given
        ZestClientScreenshot clientScreenshot =
                new ZestClientScreenshot("handle", "/path", "variableName");
        ZestRuntime runtime = runtime();
        // When / Then
        assertThrows(ZestClientFailException.class, () -> clientScreenshot.invoke(runtime));
    }

    @Test
    void shouldFailToInvokeIfClientDoesNotTakeScreenshots() throws Exception {
        // Given
        String windowHandle = "handle";
        ZestClientScreenshot clientScreenshot =
                new ZestClientScreenshot(windowHandle, "/path", "variableName");
        ZestRuntime runtime = runtimeWithWebDriver(windowHandle);
        // When / Then
        assertThrows(ZestClientFailException.class, () -> clientScreenshot.invoke(runtime));
    }

    @Test
    void shouldFailToInvokeIfClientTakeScreenshotsButFileAndVariableAreNull() throws Exception {
        // Given
        String windowHandle = "windowHandle";
        ZestClientScreenshot clientScreenshot = new ZestClientScreenshot(windowHandle, null, null);
        ZestRuntime runtime = runtimeWithWebDriverThatTakesScreenshot(windowHandle);
        // When / Then
        assertThrows(ZestClientFailException.class, () -> clientScreenshot.invoke(runtime));
    }

    @Test
    void shouldFailToInvokeIfClientTakeScreenshotsButFileAndVariableAreEmpty() throws Exception {
        // Given
        String windowHandle = "windowHandle";
        ZestClientScreenshot clientScreenshot = new ZestClientScreenshot(windowHandle, "", "");
        ZestRuntime runtime = runtimeWithWebDriverThatTakesScreenshot(windowHandle);
        // When / Then
        assertThrows(ZestClientFailException.class, () -> clientScreenshot.invoke(runtime));
    }

    @Test
    void shouldFailToSaveScreenshotToFileAlreadyExists() throws Exception {
        // Given
        Path path = Files.createTempFile(folder, "junit", "");
        String windowHandle = "windowHandle";
        String filePath = path.toString();
        ZestClientScreenshot clientScreenshot =
                new ZestClientScreenshot(windowHandle, filePath, null);
        ZestRuntime runtime =
                runtimeWithWebDriverThatTakesScreenshot(windowHandle, SCREENSHOT_DATA);
        when(runtime.replaceVariablesInString(filePath, false)).thenReturn(filePath);
        // When / Then
        assertThrows(ZestClientFailException.class, () -> clientScreenshot.invoke(runtime));
    }

    @Test
    void shouldFailToSaveScreenshotToFileDirectory() throws Exception {
        // Given
        Path path = Files.createTempDirectory(folder, "");
        String windowHandle = "windowHandle";
        String filePath = path.toString();
        ZestClientScreenshot clientScreenshot =
                new ZestClientScreenshot(windowHandle, filePath, null);
        ZestRuntime runtime =
                runtimeWithWebDriverThatTakesScreenshot(windowHandle, SCREENSHOT_DATA);
        when(runtime.replaceVariablesInString(filePath, false)).thenReturn(filePath);
        // When / Then
        assertThrows(ZestClientFailException.class, () -> clientScreenshot.invoke(runtime));
    }

    @Test
    void shouldSaveScreenshotToFile() throws Exception {
        // Given
        Path path = folder.resolve("zcs-file-only.png");
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
        assertThat(path).hasBinaryContent(SCREENSHOT_DATA);
    }

    @Test
    void shouldSaveScreenshotToVariable() throws Exception {
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
    void shouldSaveScreenshotToFileAndVariable() throws Exception {
        // Given
        Path path = folder.resolve("zcs-file-var.png");
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
        assertThat(path).hasBinaryContent(SCREENSHOT_DATA);
    }

    @Test
    void shouldSerialiseAndDeserialise() {
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
    void shouldDeepCopy() {
        // Given
        ZestClientScreenshot original =
                new ZestClientScreenshot("windowHandle", "/path", "variableName");
        original.setEnabled(false);
        // When
        ZestClientScreenshot copy = original.deepCopy();
        // Then
        assertThat(copy).isNotSameAs(original);
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
