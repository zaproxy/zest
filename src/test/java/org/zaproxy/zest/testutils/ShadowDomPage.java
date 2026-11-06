/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.testutils;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.zaproxy.zest.core.v1.ZestClientElement;
import org.zaproxy.zest.core.v1.ZestClientLaunch;
import org.zaproxy.zest.core.v1.ZestRuntime;
import org.zaproxy.zest.core.v1.ZestScript;

public final class ShadowDomPage {

    public static final String CLICKED = "clicked";
    public static final String TYPED_PREFIX = "typed:";
    public static final String CLEARED = "cleared";
    public static final String SUBMITTED = "submitted";
    public static final String HOVERED = "hovered";
    public static final String SCROLLED_PREFIX = "scrolled:";

    private static final List<String> BROWSERS = List.of("Firefox", "Chrome");

    private static final List<List<String>> MODES =
            List.of(
                    List.of("open"),
                    List.of("closed"),
                    List.of("open", "open"),
                    List.of("closed", "closed"),
                    List.of("open", "closed"),
                    List.of("closed", "open"));

    private static final List<Integer> DELAYS = List.of(0, 250);

    private static final String WINDOW_HANDLE = "windowHandle";
    private static final String PATH_SERVER_FILE = "/test.html";

    private ShadowDomPage() {}

    public static Stream<Arguments> cases() {
        return BROWSERS.stream()
                .flatMap(
                        browser ->
                                MODES.stream()
                                        .flatMap(
                                                modes ->
                                                        DELAYS.stream()
                                                                .map(
                                                                        delay ->
                                                                                Arguments.of(
                                                                                        browser,
                                                                                        modes,
                                                                                        delay))));
    }

    public static List<String> hosts(List<String> modes) {
        return IntStream.range(0, modes.size()).mapToObj(i -> "#host" + i).toList();
    }

    public static String html(List<String> modes, int delayMs) {
        return html(modes, delayMs, "");
    }

    public static String html(List<String> modes, int delayMs, String initialValue) {
        String modesJs = modes.stream().map(m -> "'" + m + "'").toList().toString();
        return """
                <html>
                  <body>
                    <div id="app"></div>
                    <div id="result"></div>
                    <script>
                      function build() {
                        let parent = document.getElementById('app');
                        %s.forEach((mode, i) => {
                          const host = document.createElement('div');
                          host.id = 'host' + i;
                          parent.appendChild(host);
                          parent = host.attachShadow({mode});
                        });
                        const result = document.getElementById('result');
                        const input = document.createElement('input');
                        input.type = 'text';
                        input.id = 'field';
                        input.value = '%s';
                        input.addEventListener('input', () => {
                          result.textContent = '%s' + input.value;
                        });
                        const button = document.createElement('button');
                        button.id = 'button';
                        button.textContent = 'Click';
                        button.addEventListener('click', () => {
                          result.textContent = '%s';
                        });
                        input.addEventListener('change', () => {
                          if (input.value === '') {
                            result.textContent = '%s';
                          }
                        });
                        input.addEventListener('keydown', (e) => {
                          if (e.key === 'Enter') {
                            result.textContent = '%s';
                          }
                        });
                        button.addEventListener('mouseover', () => {
                          result.textContent = '%s';
                        });
                        const scroller = document.createElement('div');
                        scroller.id = 'scroller';
                        scroller.style.cssText = 'height: 50px; overflow: auto;';
                        scroller.innerHTML = '<div style="height: 500px;"></div>';
                        scroller.addEventListener('scroll', () => {
                          result.textContent = '%s' + scroller.scrollTop;
                        });
                        const spacer = document.createElement('div');
                        spacer.style.height = '3000px';
                        const target = document.createElement('div');
                        target.id = 'target';
                        target.textContent = 'Target';
                        parent.append(input, button, scroller, spacer, target);
                      }
                      setTimeout(build, %d);
                    </script>
                  </body>
                </html>
                """
                .formatted(
                        modesJs,
                        initialValue,
                        TYPED_PREFIX,
                        CLICKED,
                        CLEARED,
                        SUBMITTED,
                        HOVERED,
                        SCROLLED_PREFIX,
                        delayMs);
    }

    public static ZestScript setup(
            HTTPDTestServer server,
            Function<String, String> urlProvider,
            String browser,
            ZestClientElement element,
            List<String> modes,
            int delayMs,
            String value) {
        String html = html(modes, delayMs, value);
        server.addHandler(
                new NanoServerHandler(PATH_SERVER_FILE) {
                    @Override
                    protected Response serve(IHTTPSession session) {
                        return newFixedLengthResponse(html);
                    }
                });
        ZestScript script = new ZestScript();
        script.add(
                new ZestClientLaunch(WINDOW_HANDLE, browser, urlProvider.apply(PATH_SERVER_FILE)));

        element.setWaitForMsec(delayMs + 100);
        element.setWindowHandle(WINDOW_HANDLE);
        element.setShadowPath(ShadowDomPage.hosts(modes));

        script.add(element);

        return script;
    }

    public static WebDriver driver(ZestRuntime runtime) {
        return runtime.getWebDrivers().iterator().next();
    }

    public static String result(ZestRuntime runtime) {
        return driver(runtime).findElement(By.id("result")).getText();
    }
}
