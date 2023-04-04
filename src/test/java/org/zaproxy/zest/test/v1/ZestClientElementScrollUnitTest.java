/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.zaproxy.zest.core.v1.ZestClientElementScroll;
import org.zaproxy.zest.core.v1.ZestClientLaunch;
import org.zaproxy.zest.core.v1.ZestJSON;
import org.zaproxy.zest.core.v1.ZestScript;
import org.zaproxy.zest.impl.ZestBasicRunner;

/** Unit test for {@link ZestClientElementScroll}. */
class ZestClientElementScrollUnitTest extends ServerBasedTest {

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
        String htmlContent =
                "<html><head></head><body style='height: 2000px;'><div style='height: 5000px;'></div></body></html>";
        server.stubFor(
                get(urlEqualTo(PATH_SERVER_FILE))
                        .willReturn(aResponse().withStatus(200).withBody(htmlContent)));
        ZestScript script = new ZestScript();
        ZestBasicRunner runner = new ZestBasicRunner();

        // When
        script.add(new ZestClientLaunch("windowHandle", "firefox", getServerUrl(PATH_SERVER_FILE)));
        script.add(new ZestClientElementScroll("windowHandle", "cssselector", "body", 0, 100));
        runner.run(script, null);
        WebDriver driver = runner.getWebDriver("windowHandle");
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        long scrollX = (long) jsExecutor.executeScript("return window.scrollX;");
        long scrollY = (long) jsExecutor.executeScript("return window.scrollY;");
        runner.removeWebDriver("windowHandle");

        // Then
        assertEquals(0, scrollX);
        assertEquals(100, scrollY);
    }
}
