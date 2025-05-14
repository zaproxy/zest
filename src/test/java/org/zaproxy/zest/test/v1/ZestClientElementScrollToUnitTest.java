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
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.zaproxy.zest.core.v1.ZestClientElementScrollTo;
import org.zaproxy.zest.core.v1.ZestClientLaunch;
import org.zaproxy.zest.core.v1.ZestJSON;
import org.zaproxy.zest.core.v1.ZestScript;
import org.zaproxy.zest.impl.ZestBasicRunner;

/** Unit test for {@link ZestClientElementScrollTo}. */
class ZestClientElementScrollToUnitTest extends ServerBasedTest {

    private static final String PATH_SERVER_FILE = "/test.html";

    @Test
    void shouldNotBePassive() {
        // Given / When
        ZestClientElementScrollTo clientElementScrollTo = new ZestClientElementScrollTo();
        // Then
        assertEquals(false, clientElementScrollTo.isPassive());
    }

    @Test
    void shouldNotHaveWindowHandleByDefault() {
        // Given / When
        ZestClientElementScrollTo clientElementScrollTo = new ZestClientElementScrollTo();
        // Then
        assertNull(clientElementScrollTo.getWindowHandle());
    }

    @Test
    void shouldBeEnabledByDefault() {
        // Given / When
        ZestClientElementScrollTo clientElementScrollTo = new ZestClientElementScrollTo();
        // Then
        assertEquals(true, clientElementScrollTo.isEnabled());
    }

    @Test
    void shouldSerialiseAndDeserialise() {
        // Given
        ZestClientElementScrollTo original =
                new ZestClientElementScrollTo("sessionId", "type", "element");
        original.setEnabled(false);
        // When
        String serialisation = ZestJSON.toString(original);
        ZestClientElementScrollTo deserialised =
                (ZestClientElementScrollTo) ZestJSON.fromString(serialisation);
        // Then
        assertEquals(deserialised.getElementType(), original.getElementType());
        assertEquals(deserialised.getWindowHandle(), original.getWindowHandle());
        assertEquals(deserialised.getElement(), original.getElement());
        assertEquals(deserialised.isEnabled(), original.isEnabled());
    }

    @Test
    void shouldScrollToElement() throws Exception {
        // Given
        String htmlContent =
                "<html><head></head><body style='height: 2000px;'><div style='height: 5000px;'></div><p id=\"test-id\">Paragraph</p></body></html>";
        server.stubFor(
                get(urlEqualTo(PATH_SERVER_FILE))
                        .willReturn(aResponse().withStatus(200).withBody(htmlContent)));
        ZestScript script = new ZestScript();
        ZestBasicRunner runner = new ZestBasicRunner();

        // When
        script.add(new ZestClientLaunch("windowHandle", "firefox", getServerUrl(PATH_SERVER_FILE)));
        script.add(new ZestClientElementScrollTo("windowHandle", "id", "test-id"));
        runner.run(script, null);
        WebDriver driver = runner.getWebDriver("windowHandle");
        WebElement element = driver.findElement(By.id("test-id"));
        Point location = element.getLocation();
        int viewportHeight = driver.manage().window().getSize().getHeight();
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        // Then
        long scrollY = (long) jsExecutor.executeScript("return window.scrollY;");
        boolean isInView =
                element.isDisplayed()
                        && location.getY() >= scrollY
                        && location.getY() <= scrollY + viewportHeight;
        driver.quit();
        assertEquals(true, isInView);
    }

    @Test
    void shouldNotScrollToElementWhenInView() throws Exception {
        // Given
        String htmlContent =
                "<html><head></head><body style='height: 2000px;'><div style='height: 100px;'></div><p id=\"test-id\">Paragraph</p></body></html>";
        server.stubFor(
                get(urlEqualTo(PATH_SERVER_FILE))
                        .willReturn(aResponse().withStatus(200).withBody(htmlContent)));
        ZestScript script = new ZestScript();
        ZestBasicRunner runner = new ZestBasicRunner();
        script.add(new ZestClientLaunch("windowHandle", "firefox", getServerUrl(PATH_SERVER_FILE)));
        script.add(new ZestClientElementScrollTo("windowHandle", "id", "test-id"));

        // When
        runner.run(script, null);

        // Then
        WebDriver driver = runner.getWebDriver("windowHandle");
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        long scrollY = (long) jsExecutor.executeScript("return window.scrollY;");
        driver.quit();
        // Zero means no scroll happened
        assertThat(scrollY).isZero();
    }

    @Test
    void shouldDeepCopy() {
        // Given
        ZestClientElementScrollTo original =
                new ZestClientElementScrollTo("sessionId", "type", "element");
        original.setEnabled(false);
        // When
        ZestClientElementScrollTo copy = original.deepCopy();
        // Then
        assertThat(copy).isNotSameAs(original);
        assertEquals(copy.getElementType(), original.getElementType());
        assertEquals(copy.getWindowHandle(), original.getWindowHandle());
        assertEquals(copy.getElement(), original.getElement());
        assertEquals(copy.isEnabled(), original.isEnabled());
    }
}
