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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zaproxy.zest.core.v1.ZestActionSleep;
import org.zaproxy.zest.core.v1.ZestClientFailException;
import org.zaproxy.zest.core.v1.ZestClientLaunch;
import org.zaproxy.zest.core.v1.ZestClientWindowClose;
import org.zaproxy.zest.core.v1.ZestJSON;
import org.zaproxy.zest.core.v1.ZestScript;
import org.zaproxy.zest.impl.ZestBasicRunner;
import org.zaproxy.zest.testutils.NanoServerHandler;

/** */
class ZestClientLaunchUnitTest extends ClientBasedTest {

    private static final String PATH_SERVER_FILE = "/test";

    private boolean urlAccessed;

    @BeforeEach
    void before() {
        server.addHandler(
                new NanoServerHandler(PATH_SERVER_FILE) {
                    @Override
                    protected Response serve(IHTTPSession session) {
                        urlAccessed = true;
                        return newFixedLengthResponse("This is the response");
                    }
                });
    }

    @Test
    void shouldUseArgsPassedInConstructor() {
        // Given
        String windowHandle = "windowHandle";
        String browserType = "browserType";
        String url = "url";
        // When
        ZestClientLaunch invokeAction = new ZestClientLaunch(windowHandle, browserType, url);
        // Then
        assertEquals(invokeAction.getWindowHandle(), windowHandle);
        assertEquals(invokeAction.getBrowserType(), browserType);
        assertEquals(invokeAction.getUrl(), url);
        assertNull(invokeAction.getCapabilities());
    }

    @Test
    void shouldUseArgsPassedInConstructorWithCapabilities() {
        // Given
        String windowHandle = "windowHandle";
        String browserType = "browserType";
        String url = "url";
        String capabilities = "capability=value";
        // When
        ZestClientLaunch invokeAction =
                new ZestClientLaunch(windowHandle, browserType, url, capabilities);
        // Then
        assertEquals(invokeAction.getWindowHandle(), windowHandle);
        assertEquals(invokeAction.getBrowserType(), browserType);
        assertEquals(invokeAction.getUrl(), url);
        assertEquals(invokeAction.getCapabilities(), capabilities);
    }

    @Test
    void testHtmlUnitLaunch() throws Exception {
        ZestScript script = new ZestScript();
        script.add(
                new ZestClientLaunch(
                        "htmlunit", "HtmlUnit", getServerUrl(PATH_SERVER_FILE), false));
        script.add(new ZestClientWindowClose("htmlunit", 0));
        script.add(new ZestActionSleep(1));

        runner = new ZestBasicRunner();
        // Uncomment this to proxy via ZAP
        // runner.setProxy("localhost", 8090);
        runner.run(script, null);

        assertThat(urlAccessed).isTrue();
    }

    @Test
    void testHtmlUnitByClassLaunch() throws Exception {
        ZestScript script = new ZestScript();
        ZestClientLaunch cl =
                new ZestClientLaunch(
                        "htmlunit",
                        "org.openqa.selenium.htmlunit.HtmlUnitDriver",
                        getServerUrl(PATH_SERVER_FILE));
        cl.setCapabilities("browserName=htmlunit");
        script.add(cl);
        script.add(new ZestClientWindowClose("htmlunit", 0));

        runner = new ZestBasicRunner();
        // Uncomment this to proxy via ZAP
        // runner.setProxy("localhost", 8090);
        runner.run(script, null);

        assertThat(urlAccessed).isTrue();
    }

    @Test
    void testInvalidName() throws Exception {
        ZestScript script = new ZestScript();
        script.add(new ZestClientLaunch("bad", "baddriver", getServerUrl(PATH_SERVER_FILE)));
        script.add(new ZestClientWindowClose("bad", 0));

        runner = new ZestBasicRunner();
        assertThrows(ZestClientFailException.class, () -> runner.run(script, null));
    }

    @Test
    void testSerialization() {
        ZestClientLaunch zcl1 =
                new ZestClientLaunch(
                        "htmlunit", "HtmlUnit", getServerUrl(PATH_SERVER_FILE), false, "/profile");
        String str = ZestJSON.toString(zcl1);
        ZestClientLaunch zcl2 = (ZestClientLaunch) ZestJSON.fromString(str);

        assertEquals(zcl1.getElementType(), zcl2.getElementType());
        assertEquals(zcl1.getBrowserType(), zcl2.getBrowserType());
        assertEquals(zcl1.getWindowHandle(), zcl2.getWindowHandle());
        assertEquals(zcl1.getUrl(), zcl2.getUrl());
        assertEquals(zcl1.isHeadless(), zcl2.isHeadless());
        assertEquals(zcl1.getProfilePath(), zcl2.getProfilePath());
    }

    @Test
    void shouldDeepCopy() {
        // Given
        ZestClientLaunch original =
                new ZestClientLaunch("handle", "browser", "url", "capabilities", false, "/profile");
        original.setEnabled(false);
        // When
        ZestClientLaunch copy = (ZestClientLaunch) original.deepCopy();
        // Then
        assertEquals(original.getElementType(), copy.getElementType());
        assertEquals(original.getBrowserType(), copy.getBrowserType());
        assertEquals(original.getWindowHandle(), copy.getWindowHandle());
        assertEquals(original.getUrl(), copy.getUrl());
        assertEquals(original.getCapabilities(), copy.getCapabilities());
        assertEquals(original.isHeadless(), copy.isHeadless());
        assertEquals(original.getProfilePath(), copy.getProfilePath());
        assertEquals(original.isEnabled(), copy.isEnabled());
    }
}
