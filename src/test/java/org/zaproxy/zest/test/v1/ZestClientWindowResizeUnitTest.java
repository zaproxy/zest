/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.zaproxy.zest.core.v1.ZestClientFailException;
import org.zaproxy.zest.core.v1.ZestClientWindowResize;
import org.zaproxy.zest.core.v1.ZestJSON;
import org.zaproxy.zest.core.v1.ZestScript;
import org.zaproxy.zest.impl.ZestBasicRunner;
import org.zaproxy.zest.testutils.NanoServerHandler;

/** Unit test for {@link ZestClientWindowResize}. */
class ZestClientWindowResizeUnitTest extends ClientBasedTest {

    private static final String PATH_SERVER_FILE = "/test.html";

    @Test
    void shouldNotBePassive() {
        // Given / When
        ZestClientWindowResize resize = new ZestClientWindowResize();
        // Then
        assertEquals(false, resize.isPassive());
    }

    @Test
    void shouldNotHaveWindowHandleByDefault() {
        // Given / When
        ZestClientWindowResize resize = new ZestClientWindowResize();
        // Then
        assertNull(resize.getWindowHandle());
    }

    @Test
    void shouldBeEnabledByDefault() {
        // Given / When
        ZestClientWindowResize resize = new ZestClientWindowResize();
        // Then
        assertEquals(true, resize.isEnabled());
    }

    @Test
    void shouldSetX() {
        // Given
        int x = 10;
        ZestClientWindowResize resize = new ZestClientWindowResize();
        // When
        resize.setX(x);
        // Then
        assertEquals(x, resize.getX());
    }

    @Test
    void shouldSetY() {
        // Given
        int y = 20;
        ZestClientWindowResize resize = new ZestClientWindowResize();
        // When
        resize.setY(y);
        // Then
        assertEquals(y, resize.getY());
    }

    @Test
    void shouldSerialiseAndDeserialise() {
        // Given
        ZestClientWindowResize original = new ZestClientWindowResize("windowHandle", 100, 500);
        original.setEnabled(false);
        // When
        String serialisation = ZestJSON.toString(original);
        ZestClientWindowResize deserialised =
                (ZestClientWindowResize) ZestJSON.fromString(serialisation);
        // Then
        assertEquals(deserialised.getElementType(), original.getElementType());
        assertEquals(deserialised.getWindowHandle(), original.getWindowHandle());
        assertEquals(deserialised.getX(), original.getX());
        assertEquals(deserialised.getY(), original.getY());
        assertEquals(deserialised.isEnabled(), original.isEnabled());
    }

    @Test
    void shouldDeepCopy() {
        // Given
        ZestClientWindowResize original = new ZestClientWindowResize("windowHandle", 100, 500);
        original.setEnabled(false);
        // When
        ZestClientWindowResize copy = original.deepCopy();
        // Then
        assertThat(copy).isNotSameAs(original);
        assertEquals(copy.getElementType(), original.getElementType());
        assertEquals(copy.getWindowHandle(), original.getWindowHandle());
        assertEquals(copy.getX(), original.getX());
        assertEquals(copy.getY(), original.getY());
        assertEquals(copy.isEnabled(), original.isEnabled());
    }

    @Test
    void shouldFailToResizeIfWindowHandleNotFound() throws Exception {
        // Given
        ZestClientWindowResize resize = new ZestClientWindowResize("NoWindowHandle", 100, 500);
        // When / Then
        assertThrows(ZestClientFailException.class, () -> resize.invoke(testRuntime()));
    }

    @Test
    void shouldResizeWindow() throws Exception {
        // Given
        server.addHandler(
                new NanoServerHandler(PATH_SERVER_FILE) {
                    @Override
                    protected Response serve(IHTTPSession session) {
                        return newFixedLengthResponse("<html><head></head><body></body></html>");
                    }
                });
        ZestScript script = new ZestScript();
        runner = new ZestBasicRunner();
        // When
        script.add(new TestClientLaunch("windowHandle", PATH_SERVER_FILE));
        script.add(new ZestClientWindowResize("windowHandle", 700, 500));
        runner.run(script, null);
        WebDriver driver = runner.getWebDriver("windowHandle");
        Dimension size = driver.manage().window().getSize();
        // Then
        assertEquals(700, size.width);
        assertEquals(500, size.height);
    }

    private static TestRuntime testRuntime() {
        return new TestRuntime();
    }
}
