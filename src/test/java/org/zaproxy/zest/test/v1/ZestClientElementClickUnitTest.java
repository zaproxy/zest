/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.zaproxy.zest.core.v1.ZestClientElementClick;
import org.zaproxy.zest.core.v1.ZestClientFailException;
import org.zaproxy.zest.core.v1.ZestClientLaunch;
import org.zaproxy.zest.core.v1.ZestClientWindowResize;
import org.zaproxy.zest.core.v1.ZestScript;
import org.zaproxy.zest.impl.ZestBasicRunner;
import org.zaproxy.zest.testutils.HTTPDTestServer;
import org.zaproxy.zest.testutils.NanoServerHandler;

/** Unit test for {@link ZestClientElementClick}. */
class ZestClientElementClickUnitTest extends ClientBasedTest {

    private static final String WINDOW_HANDLE = "windowHandle";

    private static final String PATH_SERVER_FILE = "/test.html";
    private static final String FORM_SERVER_FILE = "/form.html";

    private static final List<String> BASE_CASES =
            List.of(
                    // Simple case.
                    """
                    <html>
                      <body>
                        <form action="/form.html" method="POST">
                          <button type="submit" id="login" />
                        </form>
                      </body>
                    </html>
                    """,
                    // Out of view.
                    """
                    <html>
                      <head>
                        <style>
                          .layer { top: 25000px; left: 25000px; position: absolute; }
                        </style>
                      </head>
                      <body>
                        <div class="layer">
                          <form action="/form.html" method="POST">
                            <button type="submit" id="login" class="layer" />
                          </form>
                        </div>
                      </body>
                    </html>
                    """);

    private HTTPDTestServer nano;

    @BeforeEach
    void startServer() throws IOException {
        nano = new HTTPDTestServer(0);
        nano.start();
    }

    @AfterEach
    void stopServer() {
        if (nano != null) {
            nano.stop();
        }
    }

    @Test
    void shouldNotClickMissingElement() {
        // Given
        nano.addHandler(
                new NanoServerHandler(PATH_SERVER_FILE) {
                    @Override
                    protected Response serve(IHTTPSession session) {
                        return newFixedLengthResponse("<html></html>");
                    }
                });
        ZestScript script = new ZestScript();
        runner = new ZestBasicRunner();
        script.add(new TestClientLaunch(WINDOW_HANDLE));
        script.add(new ZestClientElementClick(WINDOW_HANDLE, "id", "missing"));

        // When / Then
        ZestClientFailException ex =
                assertThrows(ZestClientFailException.class, () -> runner.run(script, null));
        assertThat(ex).hasMessageContaining("Unable to locate element: #missing");
    }

    static Stream<String> baseCases() {
        return BASE_CASES.stream();
    }

    @ParameterizedTest
    @MethodSource("baseCases")
    void shouldClickElement(String response) throws Exception {
        // Given
        nano.addHandler(
                new NanoServerHandler(PATH_SERVER_FILE) {
                    @Override
                    protected Response serve(IHTTPSession session) {
                        return newFixedLengthResponse(response);
                    }
                });
        AtomicReference<Boolean> formSubmitted = new AtomicReference<>(false);
        nano.addHandler(
                new NanoServerHandler(FORM_SERVER_FILE) {
                    @Override
                    protected Response serve(IHTTPSession session) {
                        formSubmitted.set(true);
                        return newFixedLengthResponse("");
                    }
                });
        ZestScript script = new ZestScript();
        runner = new ZestBasicRunner();
        script.add(new TestClientLaunch(WINDOW_HANDLE));
        script.add(new ZestClientElementClick(WINDOW_HANDLE, "id", "login"));

        // When
        runner.run(script, null);

        // Then
        assertThat(formSubmitted.get()).isTrue();
    }

    static Stream<Arguments> resizeCases() {
        List<Integer> sizes = List.of(0, 1);
        return BASE_CASES.stream()
                .flatMap(baseCase -> sizes.stream().map(size -> Arguments.of(baseCase, size)));
    }

    @ParameterizedTest
    @MethodSource("resizeCases")
    void shouldClickElementAfterWindowResize(String response, int size) throws Exception {
        // Given
        nano.addHandler(
                new NanoServerHandler(PATH_SERVER_FILE) {
                    @Override
                    protected Response serve(IHTTPSession session) {
                        return newFixedLengthResponse(response);
                    }
                });
        AtomicReference<Boolean> formSubmitted = new AtomicReference<>(false);
        nano.addHandler(
                new NanoServerHandler(FORM_SERVER_FILE) {
                    @Override
                    protected Response serve(IHTTPSession session) {
                        formSubmitted.set(true);
                        return newFixedLengthResponse("");
                    }
                });
        ZestScript script = new ZestScript();
        runner = new ZestBasicRunner();
        script.add(new TestClientLaunch(WINDOW_HANDLE));
        script.add(new ZestClientWindowResize(WINDOW_HANDLE, size, size));
        script.add(new ZestClientElementClick(WINDOW_HANDLE, "id", "login"));

        // When
        runner.run(script, null);

        // Then
        assertThat(formSubmitted.get()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"l2", "l3"})
    void shouldClickObscuredElement(String element) throws Exception {
        // Given
        nano.addHandler(
                new NanoServerHandler(PATH_SERVER_FILE) {
                    @Override
                    protected Response serve(IHTTPSession session) {
                        return newFixedLengthResponse(
                                """
                                <html>
                                  <head>
                                    <style>
                                      .layer { width: 100px; height: 25px; position: absolute; opacity: 0.5; }
                                      #l1 { background-color: #FF0000; z-index: 3; }
                                      #l2 { background-color: #00FF00; z-index: 2; }
                                      #l3 { background-color: #0000FF; z-index: 1; }
                                    </style>
                                  </head>
                                  <body>
                                    <form action="/form.html" method="POST">
                                      <button type="submit" id="login">
                                        <span class="layer" id="l1">Log</span>
                                        <span class="layer" id="l2">me</span>
                                        <span class="layer" id="l3">in</span>
                                      </button>
                                    </form>
                                  </body>
                                </html>
                                """);
                    }
                });
        AtomicReference<Boolean> formSubmitted = new AtomicReference<>(false);
        nano.addHandler(
                new NanoServerHandler(FORM_SERVER_FILE) {
                    @Override
                    protected Response serve(IHTTPSession session) {
                        formSubmitted.set(true);
                        return newFixedLengthResponse("");
                    }
                });
        ZestScript script = new ZestScript();
        runner = new ZestBasicRunner();
        script.add(new TestClientLaunch(WINDOW_HANDLE));
        script.add(new ZestClientElementClick(WINDOW_HANDLE, "id", element));

        // When
        runner.run(script, null);

        // Then
        assertThat(formSubmitted.get()).isTrue();
    }

    private class TestClientLaunch extends ZestClientLaunch {

        TestClientLaunch(String windowHandle) {
            super(
                    windowHandle,
                    "firefox",
                    "http://localhost:" + nano.getListeningPort() + PATH_SERVER_FILE);
        }
    }
}
