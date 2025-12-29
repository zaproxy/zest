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
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.zaproxy.zest.core.v1.ZestClientElementMouseOver;
import org.zaproxy.zest.core.v1.ZestJSON;
import org.zaproxy.zest.core.v1.ZestScript;
import org.zaproxy.zest.impl.ZestBasicRunner;
import org.zaproxy.zest.testutils.NanoServerHandler;

/** Unit test for {@link ZestClientElementMouseOver}. */
class ZestClientElementMouseOverUnitTest extends ClientBasedTest {

    private static final String PATH_SERVER_FILE = "/test.html";

    @Test
    void shouldNotBePassive() {
        // Given / When
        ZestClientElementMouseOver clientElementMouseOver = new ZestClientElementMouseOver();
        // Then
        assertEquals(false, clientElementMouseOver.isPassive());
    }

    @Test
    void shouldNotHaveWindowHandleByDefault() {
        // Given / When
        ZestClientElementMouseOver clientElementMouseOver = new ZestClientElementMouseOver();
        // Then
        assertNull(clientElementMouseOver.getWindowHandle());
    }

    @Test
    void shouldBeEnabledByDefault() {
        // Given / When
        ZestClientElementMouseOver clientElementMouseOver = new ZestClientElementMouseOver();
        // Then
        assertEquals(true, clientElementMouseOver.isEnabled());
    }

    @Test
    void shouldSerialiseAndDeserialise() {
        // Given
        ZestClientElementMouseOver original =
                new ZestClientElementMouseOver("sessionId", "type", "element");
        original.setEnabled(false);
        // When
        String serialisation = ZestJSON.toString(original);
        ZestClientElementMouseOver deserialised =
                (ZestClientElementMouseOver) ZestJSON.fromString(serialisation);
        // Then
        assertEquals(deserialised.getElementType(), original.getElementType());
        assertEquals(deserialised.getWindowHandle(), original.getWindowHandle());
        assertEquals(deserialised.getElement(), original.getElement());
        assertEquals(deserialised.isEnabled(), original.isEnabled());
    }

    @Test
    void shouldMouseOverElement() throws Exception {
        // Given
        server.addHandler(
                new NanoServerHandler(PATH_SERVER_FILE) {
                    @Override
                    protected Response serve(IHTTPSession session) {
                        return newFixedLengthResponse(
                                "<html><head><style>div:hover {color: red;}</style></head><body><div id=\"test-id\">Hover over me to see my color change.</div></body></html>");
                    }
                });
        ZestScript script = new ZestScript();
        runner = new ZestBasicRunner();
        // When
        script.add(new TestClientLaunch("windowHandle", PATH_SERVER_FILE));
        script.add(new ZestClientElementMouseOver("windowHandle", "id", "test-id"));
        runner.run(script, null);
        WebDriver driver = runner.getWebDriver("windowHandle");
        WebElement element = driver.findElement(By.id("test-id"));
        // Then
        assertEquals("rgb(255, 0, 0)", element.getCssValue("color"));
        driver.quit();
    }

    @Test
    void shouldDeepCopy() {
        // Given
        ZestClientElementMouseOver original =
                new ZestClientElementMouseOver("sessionId", "type", "element");
        original.setEnabled(false);
        // When
        ZestClientElementMouseOver copy = original.deepCopy();
        // Then
        assertThat(copy).isNotSameAs(original);
        assertEquals(copy.getElementType(), original.getElementType());
        assertEquals(copy.getWindowHandle(), original.getWindowHandle());
        assertEquals(copy.getElement(), original.getElement());
        assertEquals(copy.isEnabled(), original.isEnabled());
    }
}
