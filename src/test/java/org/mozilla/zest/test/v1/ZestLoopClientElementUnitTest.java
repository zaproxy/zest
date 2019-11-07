/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.test.v1;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import org.junit.Before;
import org.junit.Test;
import org.mozilla.zest.core.v1.ZestActionPrint;
import org.mozilla.zest.core.v1.ZestClientLaunch;
import org.mozilla.zest.core.v1.ZestClientWindowClose;
import org.mozilla.zest.core.v1.ZestJSON;
import org.mozilla.zest.core.v1.ZestLoopClientElements;
import org.mozilla.zest.core.v1.ZestScript;
import org.mozilla.zest.impl.ZestBasicRunner;

/** */
public class ZestLoopClientElementUnitTest extends ServerBasedTest {

    private static final String PATH_SERVER_FILE = "/test";

    private static final String HTML_RESPONSE =
            "<html><head></head><body>\n"
                    + "<form name=\"something\">\n"
                    + "	<input type=\"text\" name=\"a\">Username</input>\n"
                    + "	<input type=\"password\" name=\"b\">password</input>\n"
                    + "	<select name=\"c\" id=\"c\">\n"
                    + "		<option value=\"1\">1</option>\n"
                    + "		<option value=\"2\">2</option>\n"
                    + "	</select>\n"
                    + "	<input type=\"submit\" name=\"submit\">submit</input>\n"
                    + "</form>\n"
                    + "</body></html>\n";

    @Before
    public void before() {
        server.stubFor(
                get(urlEqualTo(PATH_SERVER_FILE))
                        .willReturn(aResponse().withStatus(200).withBody(HTML_RESPONSE)));
    }

    @Test
    public void testClientXpathLoopReturningName() throws Exception {
        ZestScript script = new ZestScript();
        script.add(new ZestClientLaunch("htmlunit", "HtmlUnit", getServerUrl(PATH_SERVER_FILE)));
        ZestLoopClientElements loop =
                new ZestLoopClientElements("val", "htmlunit", "xpath", "//*", "name");
        loop.addStatement(new ZestActionPrint("{{val}}"));
        script.add(loop);
        script.add(new ZestClientWindowClose("htmlunit", 0));

        ZestBasicRunner runner = new ZestBasicRunner();
        // Uncomment this to proxy via ZAP
        // runner.setProxy("localhost", 8090);
        StringWriter sw = new StringWriter();
        runner.setOutputWriter(sw);
        runner.run(script, null);
        // Expected string split up for clarity
        assertEquals("something\n" + "a\n" + "b\n" + "c" + "\n" + "submit\n", sw.toString());
    }

    @Test
    public void testClientXpathLoopReturningType() throws Exception {
        ZestScript script = new ZestScript();
        script.add(new ZestClientLaunch("htmlunit", "HtmlUnit", getServerUrl(PATH_SERVER_FILE)));
        ZestLoopClientElements loop =
                new ZestLoopClientElements("val", "htmlunit", "xpath", "//*", "type");
        loop.addStatement(new ZestActionPrint("{{val}}"));
        script.add(loop);
        script.add(new ZestClientWindowClose("htmlunit", 0));

        ZestBasicRunner runner = new ZestBasicRunner();
        // Uncomment this to proxy via ZAP
        // runner.setProxy("localhost", 8090);
        StringWriter sw = new StringWriter();
        runner.setOutputWriter(sw);
        runner.run(script, null);

        assertEquals("text\n" + "password\n" + "select-one\n" + "submit\n", sw.toString());
    }

    @Test
    public void testClientXpathLoopReturningText() throws Exception {
        ZestScript script = new ZestScript();
        script.add(new ZestClientLaunch("htmlunit", "HtmlUnit", getServerUrl(PATH_SERVER_FILE)));
        ZestLoopClientElements loop =
                new ZestLoopClientElements(
                        "val", "htmlunit", "xpath", "//input[@type='text']", "name");
        loop.addStatement(new ZestActionPrint("{{val}}"));
        script.add(loop);
        script.add(new ZestClientWindowClose("htmlunit", 0));

        ZestBasicRunner runner = new ZestBasicRunner();
        // Uncomment this to proxy via ZAP
        // runner.setProxy("localhost", 8090);
        StringWriter sw = new StringWriter();
        runner.setOutputWriter(sw);
        runner.run(script, null);

        assertEquals("a\n", sw.toString());
    }

    @Test
    public void testSerialization() {
        ZestClientLaunch zcl1 =
                new ZestClientLaunch("htmlunit", "HtmlUnit", getServerUrl(PATH_SERVER_FILE));
        String str = ZestJSON.toString(zcl1);
        ZestClientLaunch zcl2 = (ZestClientLaunch) ZestJSON.fromString(str);

        assertEquals(zcl1.getElementType(), zcl2.getElementType());
        assertEquals(zcl1.getBrowserType(), zcl2.getBrowserType());
        assertEquals(zcl1.getWindowHandle(), zcl2.getWindowHandle());
        assertEquals(zcl1.getUrl(), zcl2.getUrl());
    }
}
