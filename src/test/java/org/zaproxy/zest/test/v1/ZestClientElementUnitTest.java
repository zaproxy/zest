/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.zaproxy.zest.core.v1.ZestClientElementAssign;
import org.zaproxy.zest.core.v1.ZestClientElementClear;
import org.zaproxy.zest.core.v1.ZestClientElementSendKeys;
import org.zaproxy.zest.core.v1.ZestClientFailException;
import org.zaproxy.zest.core.v1.ZestScript;
import org.zaproxy.zest.impl.ZestBasicRunner;
import org.zaproxy.zest.testutils.NanoServerHandler;

/** Unit tests for {@link org.zaproxy.zest.core.v1.ZestClientElement}. */
class ZestClientElementUnitTest extends ClientBasedTest {

    private static final String WINDOW_HANDLE = "windowHandle";
    private static final String PATH_SERVER_FILE = "/test.html";

    private static final int WAIT_FOR_MSEC = (int) TimeUnit.MILLISECONDS.toMillis(500);

    private static final String VISIBLE_INPUT_HTML =
            """
            <html><body>
              <input id="username" value="secret" />
            </body></html>
            """
                    .strip();

    /**
     * Ionic-style hidden native input: still interactable after {@code findElement}, but must not
     * be used to prove a wait timed out because {@code opacity: 0} may still satisfy some waits.
     */
    private static final String OPACITY_ZERO_INPUT_HTML =
            """
            <html><body>
              <input id="username" style="opacity: 0" value="secret" />
            </body></html>
            """
                    .strip();

    /** Fails Selenium visibility and clickability waits reliably (unlike {@code opacity: 0}). */
    private static final String VISIBILITY_HIDDEN_INPUT_HTML =
            """
            <html><body>
              <input id="username" style="visibility: hidden" value="secret" />
            </body></html>
            """
                    .strip();

    @Test
    void shouldUseFindElementAfterVisibilityWaitTimesOut() throws Exception {
        // Given
        serveHtml(VISIBLE_INPUT_HTML);
        ZestScript script = scriptWithLaunch();
        ZestClientElementClear clear =
                new ZestClientElementClearWithUnmetVisibilityWait(WINDOW_HANDLE, "id", "username");
        clear.setWaitForMsec(WAIT_FOR_MSEC);
        script.add(clear);
        script.add(assignValue("username.value", "value"));

        // When
        long timeTakenMs = runScript(script);

        // Then
        assertThat(runner.getVariable("username.value")).isEqualTo("");
        assertWaitTimedOut(timeTakenMs, "visibility");
    }

    @Test
    void shouldUseFindElementAfterClickableWaitTimesOut() throws Exception {
        // Given
        serveHtml(
                """
                <html><body>
                  <input id="username" />
                </body></html>
                """
                        .strip());
        ZestScript script = scriptWithLaunch();
        ZestClientElementSendKeys sendKeys =
                new ZestClientElementSendKeysWithUnmetClickableWait(
                        WINDOW_HANDLE, "id", "username", "test-user");
        sendKeys.setWaitForMsec(WAIT_FOR_MSEC);
        script.add(sendKeys);
        script.add(assignValue("username.value", "value"));

        // When
        long timeTakenMs = runScript(script);

        // Then
        assertThat(runner.getVariable("username.value")).isEqualTo("test-user");
        assertWaitTimedOut(timeTakenMs, "clickable");
    }

    @Test
    void shouldReadValueAfterRealVisibilityWaitTimesOut() throws Exception {
        // Given
        serveHtml(VISIBILITY_HIDDEN_INPUT_HTML);
        ZestScript script = scriptWithLaunch();
        ZestClientElementAssign assign = assignValue("username.value", "value");
        assign.setWaitForMsec(WAIT_FOR_MSEC);
        script.add(assign);

        // When
        long timeTakenMs = runScript(script);

        // Then
        assertThat(runner.getVariable("username.value")).isEqualTo("secret");
        assertWaitTimedOut(timeTakenMs, "visibility (visibility: hidden)");
    }

    @Test
    void shouldClearIonicStyleOpacityZeroInputAfterVisibilityWaitTimesOut() throws Exception {
        // Given
        serveHtml(OPACITY_ZERO_INPUT_HTML);
        ZestScript script = scriptWithLaunch();
        ZestClientElementClear clear = new ZestClientElementClear(WINDOW_HANDLE, "id", "username");
        clear.setWaitForMsec(WAIT_FOR_MSEC);
        script.add(clear);
        script.add(assignValue("username.value", "value"));

        // When
        long timeTakenMs = runScript(script);

        // Then
        assertThat(runner.getVariable("username.value")).isEqualTo("");
        assertWaitTimedOut(timeTakenMs, "visibility (opacity: 0)");
    }

    @Test
    void shouldFailWhenElementNotInDomAfterWaitTimesOut() {
        // Given
        serveHtml("<html><body></body></html>");
        ZestScript script = scriptWithLaunch();
        ZestClientElementClear clear = new ZestClientElementClear(WINDOW_HANDLE, "id", "missing");
        clear.setWaitForMsec(WAIT_FOR_MSEC);
        script.add(clear);

        // When
        long startTime = System.currentTimeMillis();
        ZestClientFailException ex =
                assertThrows(ZestClientFailException.class, () -> runner.run(script, null));
        long timeTakenMs = System.currentTimeMillis() - startTime;

        // Then
        assertThat(ex).hasMessageContaining("Unable to locate element: #missing");
        assertWaitTimedOut(timeTakenMs, "visibility");
    }

    @Test
    void shouldClearVisibleInputQuicklyWhenVisibilityWaitSucceeds() throws Exception {
        // Given
        serveHtml(VISIBLE_INPUT_HTML);
        ZestScript script = scriptWithLaunch();
        ZestClientElementClear clear = new ZestClientElementClear(WINDOW_HANDLE, "id", "username");
        clear.setWaitForMsec((int) TimeUnit.MINUTES.toMillis(2));
        script.add(clear);
        script.add(assignValue("username.value", "value"));

        // When
        long timeTakenMs = runScript(script);

        // Then
        assertThat(runner.getVariable("username.value")).isEqualTo("");
        assertTrue(
                timeTakenMs < TimeUnit.SECONDS.toMillis(10),
                "Expected visibility wait to succeed without waiting the full timeout: "
                        + timeTakenMs);
    }

    private void serveHtml(String html) {
        server.addHandler(
                new NanoServerHandler(PATH_SERVER_FILE) {
                    @Override
                    protected Response serve(IHTTPSession session) {
                        return newFixedLengthResponse(html);
                    }
                });
    }

    private ZestScript scriptWithLaunch() {
        ZestScript script = new ZestScript();
        runner = new ZestBasicRunner();
        script.add(new TestClientLaunch(WINDOW_HANDLE, PATH_SERVER_FILE));
        return script;
    }

    private static ZestClientElementAssign assignValue(String variableName, String attribute) {
        return new ZestClientElementAssign(
                variableName, WINDOW_HANDLE, "id", "username", attribute);
    }

    private long runScript(ZestScript script) throws Exception {
        long startTime = System.currentTimeMillis();
        runner.run(script, null);
        return System.currentTimeMillis() - startTime;
    }

    private static void assertWaitTimedOut(long timeTakenMs, String waitDescription) {
        assertTrue(
                timeTakenMs >= WAIT_FOR_MSEC,
                "Expected "
                        + waitDescription
                        + " wait to time out before findElement fallback: "
                        + timeTakenMs);
    }

    /**
     * Uses the same visibility condition as {@link ZestClientElementClear}, but forces the wait to
     * expire so the test proves {@code findElement} is used afterwards.
     */
    private static final class ZestClientElementClearWithUnmetVisibilityWait
            extends ZestClientElementClear {

        ZestClientElementClearWithUnmetVisibilityWait(
                String windowHandle, String type, String element) {
            super(windowHandle, type, element);
        }

        @Override
        protected ExpectedCondition<WebElement> getExpectedCondition(By by) {
            return unmetCondition(super.getExpectedCondition(by));
        }
    }

    /**
     * Uses the same clickability condition as {@link ZestClientElementSendKeys}, but forces the
     * wait to expire so the test proves {@code findElement} is used afterwards.
     */
    private static final class ZestClientElementSendKeysWithUnmetClickableWait
            extends ZestClientElementSendKeys {

        ZestClientElementSendKeysWithUnmetClickableWait(
                String windowHandle, String type, String element, String value) {
            super(windowHandle, type, element, value);
        }

        @Override
        protected ExpectedCondition<WebElement> getExpectedCondition(By by) {
            return unmetCondition(super.getExpectedCondition(by));
        }
    }

    private static ExpectedCondition<WebElement> unmetCondition(
            ExpectedCondition<WebElement> delegate) {
        return new ExpectedCondition<>() {
            @Override
            public WebElement apply(WebDriver driver) {
                delegate.apply(driver);
                return null;
            }
        };
    }
}
