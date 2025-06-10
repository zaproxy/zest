/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.zaproxy.zest.core.v1.ZestClientElementSubmit;
import org.zaproxy.zest.core.v1.ZestClientFailException;
import org.zaproxy.zest.core.v1.ZestClientLaunch;
import org.zaproxy.zest.core.v1.ZestJSON;
import org.zaproxy.zest.core.v1.ZestRuntime;
import org.zaproxy.zest.core.v1.ZestScript;
import org.zaproxy.zest.impl.ZestBasicRunner;

/** Unit test for {@link ZestClientElementSubmit}. */
class ZestClientElementSubmitUnitTest extends ServerBasedTest {

    private static final String PATH_SERVER_FILE = "/test.html";

    @Test
    void shouldNotBePassive() {
        // Given / When
        ZestClientElementSubmit clientElementSubmit = new ZestClientElementSubmit();
        // Then
        assertEquals(false, clientElementSubmit.isPassive());
    }

    @Test
    void shouldNotHaveWindowHandleByDefault() {
        // Given / When
        ZestClientElementSubmit clientElementSubmit = new ZestClientElementSubmit();
        // Then
        assertNull(clientElementSubmit.getWindowHandle());
    }

    @Test
    void shouldBeEnabledByDefault() {
        // Given / When
        ZestClientElementSubmit clientElementSubmit = new ZestClientElementSubmit();
        // Then
        assertEquals(true, clientElementSubmit.isEnabled());
    }

    @Test
    void shouldSerialiseAndDeserialise() {
        // Given
        ZestClientElementSubmit original =
                new ZestClientElementSubmit("sessionId", "type", "element");
        original.setEnabled(false);
        // When
        String serialisation = ZestJSON.toString(original);
        ZestClientElementSubmit deserialised =
                (ZestClientElementSubmit) ZestJSON.fromString(serialisation);
        // Then
        assertEquals(deserialised.getElementType(), original.getElementType());
        assertEquals(deserialised.getWindowHandle(), original.getWindowHandle());
        assertEquals(deserialised.getElement(), original.getElement());
        assertEquals(deserialised.isEnabled(), original.isEnabled());
    }

    @Test
    void shouldFailIfNoSuchElement() throws Exception {
        // Given
        String htmlContent =
                "<html><head></head><body><form><input id=\"test-submit\" type=\"submit\" value=\"Submit\" />\n"
                        + "</form></body></html>";
        server.stubFor(
                get(urlEqualTo(PATH_SERVER_FILE))
                        .willReturn(aResponse().withStatus(200).withBody(htmlContent)));
        ZestScript script = new ZestScript();
        ZestBasicRunner runner = new ZestBasicRunner();

        // When
        script.add(new ZestClientLaunch("windowHandle", "firefox", getServerUrl(PATH_SERVER_FILE)));
        script.add(new ZestClientElementSubmit("windowHandle", "id", "badId"));

        Exception exception =
                assertThrows(
                        ZestClientFailException.class,
                        () -> {
                            runner.run(script, null);
                        });

        // Then
        MatcherAssert.assertThat(
                exception.getMessage(),
                containsString(
                        "org.openqa.selenium.NoSuchElementException: Unable to locate element: #badId"));
    }

    @Test
    void shouldFailIfNoSuchElementAfterWaiting() throws Exception {
        // Given
        String htmlContent =
                "<html><head></head><body><form><input id=\"test-submit\" type=\"submit\" value=\"Submit\" />\n"
                        + "</form></body></html>";
        server.stubFor(
                get(urlEqualTo(PATH_SERVER_FILE))
                        .willReturn(aResponse().withStatus(200).withBody(htmlContent)));
        ZestScript script = new ZestScript();
        ZestBasicRunner runner = new ZestBasicRunner();

        // When
        script.add(new ZestClientLaunch("windowHandle", "firefox", getServerUrl(PATH_SERVER_FILE)));
        script.add(
                newZestClientElementSubmit(
                        "windowHandle", "id", "badId", (int) TimeUnit.SECONDS.toMillis(8)));

        long startTime = System.currentTimeMillis();
        assertThrows(
                ZestClientFailException.class,
                () -> {
                    runner.run(script, null);
                });
        long timeTaken = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime);

        // Then
        assertTrue(timeTaken >= 8, "Took less time than expected: " + timeTaken);
        assertTrue(timeTaken < 20, "Took more time than expected: " + timeTaken);
    }

    @Test
    void shouldSubmitExistingElement() throws Exception {
        // Given
        String htmlContent =
                "<html><head></head><body><form>"
                        + "<input id=\"test-submit\" type=\"submit\" value=\"Submit\" "
                        + "onSubmit=\"console.log('test');\"/>\n"
                        + "</form></body></html>";
        server.stubFor(
                get(urlEqualTo(PATH_SERVER_FILE))
                        .willReturn(aResponse().withStatus(200).withBody(htmlContent)));
        ZestScript script = new ZestScript();
        ZestBasicRunner runner = new ZestBasicRunner();

        // When / Then
        script.add(new ZestClientLaunch("windowHandle", "firefox", getServerUrl(PATH_SERVER_FILE)));
        script.add(new ZestClientElementSubmit("windowHandle", "id", "test-submit"));
        runner.run(script, null);

        // Have not yet found a good way to detect that the button was actually submitted
    }

    @Test
    void shouldSubmitExistingElementQuicklyWithLongWait() throws Exception {
        // Given
        String htmlContent =
                "<html><head></head><body><form>"
                        + "<input id=\"test-submit\" type=\"submit\" value=\"Submit\" "
                        + "onSubmit=\"console.log('test');\"/>\n"
                        + "</form></body></html>";
        server.stubFor(
                get(urlEqualTo(PATH_SERVER_FILE))
                        .willReturn(aResponse().withStatus(200).withBody(htmlContent)));
        ZestScript script = new ZestScript();
        ZestBasicRunner runner = new ZestBasicRunner();

        // When
        script.add(new ZestClientLaunch("windowHandle", "firefox", getServerUrl(PATH_SERVER_FILE)));
        script.add(
                newZestClientElementSubmit(
                        "windowHandle", "id", "test-submit", (int) TimeUnit.MINUTES.toMillis(2)));
        long startTime = System.currentTimeMillis();
        runner.run(script, null);
        long timeTaken = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime);

        // Then
        assertTrue(timeTaken < 10, "Took more time than expected: " + timeTaken);
    }

    @Test
    void shouldFailIfElementAddedAfterWaiting() {
        // Given
        String htmlContent = "<html><head></head><body><form id=\"form\"></form></body></html>";
        server.stubFor(
                get(urlEqualTo(PATH_SERVER_FILE))
                        .willReturn(aResponse().withStatus(200).withBody(htmlContent)));
        ZestScript script = new ZestScript();
        ZestBasicRunner runner = new ZestBasicRunner();
        TestClientLaunch clientLaunch =
                new TestClientLaunch(
                        "windowHandle",
                        """
                        setTimeout(() => {
                            var el = document.createElement('input');
                            el.id = 'test-submit';
                            el.type = 'submit';
                            document.getElementById('form').appendChild(el);
                        }, "15000");
                        """);

        script.add(clientLaunch);
        script.add(
                newZestClientElementSubmit(
                        "windowHandle", "id", "test-submit", (int) TimeUnit.SECONDS.toMillis(5)));

        // When / Then
        ZestClientFailException ex =
                assertThrows(ZestClientFailException.class, () -> runner.run(script, null));
        assertThat(ex)
                .hasMessageContaining(
                        "Expected condition failed: waiting for element to be clickable: By.id: test-submit");
    }

    @Test
    void shouldFailIfElementEnabledAfterWaiting() {
        // Given
        String htmlContent =
                """
                <html><head></head><body>
                    <form id="form">
                        <input id="test-submit" type="submit" disabled="true" />
                    </form>
                </body></html>
                """;
        server.stubFor(
                get(urlEqualTo(PATH_SERVER_FILE))
                        .willReturn(aResponse().withStatus(200).withBody(htmlContent)));
        ZestScript script = new ZestScript();
        ZestBasicRunner runner = new ZestBasicRunner();
        TestClientLaunch clientLaunch =
                new TestClientLaunch(
                        "windowHandle",
                        """
                        setTimeout(() => {
                            document.getElementById("test-submit").disabled = false;
                        }, "10000");
                        """);
        script.add(clientLaunch);
        script.add(
                newZestClientElementSubmit(
                        "windowHandle", "id", "test-submit", (int) TimeUnit.SECONDS.toMillis(5)));

        // When / Then
        ZestClientFailException ex =
                assertThrows(ZestClientFailException.class, () -> runner.run(script, null));
        assertThat(ex)
                .hasMessageContaining(
                        "Expected condition failed: waiting for element to be clickable: By.id: test-submit");
    }

    @Test
    void shouldSubmitElementAddedWithinWait() throws Exception {
        // Given
        String htmlContent = "<html><head></head><body><form id=\"form\"></form></body></html>";
        server.stubFor(
                get(urlEqualTo(PATH_SERVER_FILE))
                        .willReturn(aResponse().withStatus(200).withBody(htmlContent)));
        ZestScript script = new ZestScript();
        ZestBasicRunner runner = new ZestBasicRunner();
        TestClientLaunch clientLaunch =
                new TestClientLaunch(
                        "windowHandle",
                        """
                        setTimeout(() => {
                            var el = document.createElement("input");
                            el.id = "test-submit";
                            el.type = "submit";
                            document.getElementById("form").appendChild(el);
                        }, "3000");
                        """);

        script.add(clientLaunch);
        script.add(
                newZestClientElementSubmit(
                        "windowHandle", "id", "test-submit", (int) TimeUnit.SECONDS.toMillis(10)));

        // When
        runner.run(script, null);

        // Then
        long timeTaken =
                TimeUnit.MILLISECONDS.toSeconds(
                        System.currentTimeMillis() - clientLaunch.getStartTime());
        assertTrue(timeTaken < 12, "Took more time than expected: " + timeTaken);
    }

    @Test
    void shouldSubmitElementEnabledWithinWait() throws Exception {
        // Given
        String htmlContent =
                """
                <html><head></head><body>
                    <form id="form">
                        <input id="test-submit" type="submit" disabled="true" />
                    </form>
                </body></html>
                """;
        server.stubFor(
                get(urlEqualTo(PATH_SERVER_FILE))
                        .willReturn(aResponse().withStatus(200).withBody(htmlContent)));
        ZestScript script = new ZestScript();
        ZestBasicRunner runner = new ZestBasicRunner();
        TestClientLaunch clientLaunch =
                new TestClientLaunch(
                        "windowHandle",
                        """
                        setTimeout(() => {
                            document.getElementById("test-submit").disabled = false;
                        }, "3000");
                        """);
        script.add(clientLaunch);
        script.add(
                newZestClientElementSubmit(
                        "windowHandle", "id", "test-submit", (int) TimeUnit.SECONDS.toMillis(10)));

        // When
        runner.run(script, null);

        // Then
        long timeTaken =
                TimeUnit.MILLISECONDS.toSeconds(
                        System.currentTimeMillis() - clientLaunch.getStartTime());
        assertTrue(timeTaken < 12, "Took more time than expected: " + timeTaken);
    }

    @Test
    void shouldSubmitIfInputElementNotInForm() throws Exception {
        // Given
        String htmlContent =
                "<html><head></head><body><input id=\"test-submit\" type=\"submit\" value=\"Submit\" /></body></html>";
        server.stubFor(
                get(urlEqualTo(PATH_SERVER_FILE))
                        .willReturn(aResponse().withStatus(200).withBody(htmlContent)));
        ZestScript script = new ZestScript();
        ZestBasicRunner runner = new ZestBasicRunner();

        // When / Then
        script.add(new ZestClientLaunch("windowHandle", "firefox", getServerUrl(PATH_SERVER_FILE)));
        script.add(new ZestClientElementSubmit("windowHandle", "id", "test-submit"));
        runner.run(script, null);
    }

    @Test
    void shouldDeepCopy() {
        // Given
        ZestClientElementSubmit original =
                new ZestClientElementSubmit("sessionId", "type", "element");
        original.setEnabled(false);
        // When
        ZestClientElementSubmit copy = original.deepCopy();
        // Then
        assertThat(copy).isNotSameAs(original);
        assertEquals(copy.getElementType(), original.getElementType());
        assertEquals(copy.getWindowHandle(), original.getWindowHandle());
        assertEquals(copy.getElement(), original.getElement());
        assertEquals(copy.isEnabled(), original.isEnabled());
    }

    private ZestClientElementSubmit newZestClientElementSubmit(
            String sessionIdName, String type, String element, int waitForMsec) {
        ZestClientElementSubmit el = new ZestClientElementSubmit(sessionIdName, type, element);
        el.setWaitForMsec(waitForMsec);
        return el;
    }

    private class TestClientLaunch extends ZestClientLaunch {

        private final String script;
        private Long startTime;

        TestClientLaunch(String windowHandle, String script) {
            super(windowHandle, "firefox", getServerUrl(PATH_SERVER_FILE));

            this.script = script;
        }

        @Override
        public String invoke(ZestRuntime runtime) throws ZestClientFailException {
            String handle = super.invoke(runtime);

            ((JavascriptExecutor) runtime.getWebDriver(handle)).executeScript(script);
            startTime = System.currentTimeMillis();

            return handle;
        }

        public Long getStartTime() {
            return startTime;
        }
    }
}
