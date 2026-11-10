/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.zaproxy.zest.core.v1.ZestClientElementScroll;
import org.zaproxy.zest.core.v1.ZestJSON;
import org.zaproxy.zest.core.v1.ZestScript;
import org.zaproxy.zest.impl.ZestBasicRunner;
import org.zaproxy.zest.testutils.NanoServerHandler;
import org.zaproxy.zest.testutils.ShadowDomPage;

/** Unit test for {@link ZestClientElementScroll}. */
class ZestClientElementScrollUnitTest extends ClientBasedTest {

    private static final String PATH_SERVER_FILE = "/test.html";

    @Test
    void shouldNotBePassive() {
        // Given / When
        ZestClientElementScroll clientElementScroll = new ZestClientElementScroll();
        // Then
        assertEquals(false, clientElementScroll.isPassive());
    }

    @Test
    void shouldNotHaveWindowHandleByDefault() {
        // Given / When
        ZestClientElementScroll clientElementScroll = new ZestClientElementScroll();
        // Then
        assertNull(clientElementScroll.getWindowHandle());
    }

    @Test
    void shouldBeEnabledByDefault() {
        // Given / When
        ZestClientElementScroll clientElementScroll = new ZestClientElementScroll();
        // Then
        assertEquals(true, clientElementScroll.isEnabled());
    }

    @Test
    void shouldSetX() {
        // Given
        int x = 10;
        ZestClientElementScroll clientElementScroll = new ZestClientElementScroll();
        // When
        clientElementScroll.setX(x);
        // Then
        assertEquals(x, clientElementScroll.getX());
    }

    @Test
    void shouldSetY() {
        // Given
        int y = 20;
        ZestClientElementScroll clientElementScroll = new ZestClientElementScroll();
        // When
        clientElementScroll.setY(y);
        // Then
        assertEquals(y, clientElementScroll.getY());
    }

    @Test
    void shouldSerialiseAndDeserialise() {
        // Given
        ZestClientElementScroll original =
                new ZestClientElementScroll("sessionId", "type", "element", 10, 20);
        original.setEnabled(false);
        // When
        String serialisation = ZestJSON.toString(original);
        ZestClientElementScroll deserialised =
                (ZestClientElementScroll) ZestJSON.fromString(serialisation);
        // Then
        assertEquals(deserialised.getElementType(), original.getElementType());
        assertEquals(deserialised.getWindowHandle(), original.getWindowHandle());
        assertEquals(deserialised.getElement(), original.getElement());
        assertEquals(deserialised.getX(), original.getX());
        assertEquals(deserialised.getY(), original.getY());
        assertEquals(deserialised.isEnabled(), original.isEnabled());
    }

    @Test
    void shouldDeepCopy() {
        // Given
        ZestClientElementScroll original =
                new ZestClientElementScroll("sessionId", "type", "element", 10, 20);
        original.setEnabled(false);
        // When
        ZestClientElementScroll copy = original.deepCopy();
        // Then
        assertThat(copy).isNotSameAs(original);
        assertEquals(copy.getElementType(), original.getElementType());
        assertEquals(copy.getWindowHandle(), original.getWindowHandle());
        assertEquals(copy.getElement(), original.getElement());
        assertEquals(copy.getX(), original.getX());
        assertEquals(copy.getY(), original.getY());
        assertEquals(copy.isEnabled(), original.isEnabled());
    }

    @Test
    void shouldScroll() throws Exception {
        // Given
        server.addHandler(
                new NanoServerHandler(PATH_SERVER_FILE) {
                    @Override
                    protected Response serve(IHTTPSession session) {
                        return newFixedLengthResponse(
                                "<html><head></head><body style='height: 2000px;'><div style='height: 5000px;'></div></body></html>");
                    }
                });
        ZestScript script = new ZestScript();
        runner = new ZestBasicRunner();

        // When
        script.add(new TestClientLaunch("windowHandle", PATH_SERVER_FILE));
        script.add(new ZestClientElementScroll("windowHandle", "cssselector", "body", 0, 100));
        runner.run(script, null);
        JavascriptExecutor jsExecutor = (JavascriptExecutor) runner.getWebDriver("windowHandle");
        long scrollX = (long) jsExecutor.executeScript("return window.scrollX;");
        long scrollY = (long) jsExecutor.executeScript("return window.scrollY;");

        // Then
        assertEquals(0, scrollX);
        assertEquals(100, scrollY);
    }

    @Nested
    class ShadowDom {

        @ParameterizedTest(name = "{0} {1}")
        @MethodSource("org.zaproxy.zest.testutils.ShadowDomPage#cases")
        void shouldScrollElement(String browser, List<String> modes, int delayMs) throws Exception {
            // Given
            ZestClientElementScroll element =
                    new ZestClientElementScroll("", "cssSelector", "#scroller", 0, 100);
            ZestScript script =
                    ShadowDomPage.setup(
                            server,
                            ZestClientElementScrollUnitTest.this::getServerUrl,
                            browser,
                            element,
                            modes,
                            delayMs,
                            "");
            runner = new ZestBasicRunner();
            // When
            runner.run(script, null);
            // Then
            awaitResult();
            assertThat(ShadowDomPage.result(runner))
                    .startsWith(ShadowDomPage.SCROLLED_PREFIX)
                    .isNotEqualTo(ShadowDomPage.SCROLLED_PREFIX + "0");
        }

        private void awaitResult() {
            new WebDriverWait(ShadowDomPage.driver(runner), Duration.ofSeconds(5))
                    .until(d -> !ShadowDomPage.result(runner).isEmpty());
        }
    }
}
