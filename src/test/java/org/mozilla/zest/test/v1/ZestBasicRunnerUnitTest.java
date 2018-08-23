/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.test.v1;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.URL;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import org.mozilla.zest.core.v1.ZestRequest;
import org.mozilla.zest.core.v1.ZestResponse;
import org.mozilla.zest.core.v1.ZestScript;
import org.mozilla.zest.impl.ZestBasicRunner;

/** Unit test for {@code ZestBasicRunner}. */
public class ZestBasicRunnerUnitTest extends ServerBasedTest {

    private static final String PATH_SERVER_FILE = "/test";

    @Before
    public void before() {
        server.stubFor(
                post(urlEqualTo(PATH_SERVER_FILE))
                        .willReturn(
                                aResponse()
                                        .withStatus(404)
                                        .withHeader("Content-Type", "text/plain")
                                        .withHeader("Name", "value")
                                        .withHeader("Server", "abc")
                                        .withBody("This is the response")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToRunNonPassiveStatementsInPassiveScripts() throws Exception {
        // Given
        ZestScript script = new ZestScript();
        script.setType(ZestScript.Type.Passive);
        script.add(new ZestRequest());
        ZestBasicRunner runner = new ZestBasicRunner();
        // When
        runner.run(script, new HashMap<String, String>());
        // Then = IllegalArgumentException
    }

    @Test
    public void shouldSendAndReceiveHttpMessage() throws Exception {
        // Given
        ZestScript script = new ZestScript();
        ZestRequest request = new ZestRequest();
        URL url = new URL(getServerUrl(PATH_SERVER_FILE));
        String method = "POST";
        request.setMethod(method);
        request.setUrl(url);
        String headers =
                "User-Agent: xyz\r\n"
                        + "Content-Type: application/x-www-form-urlencoded\r\n"
                        + "Host: "
                        + getHostPort()
                        + "\r\n"
                        + "Content-Length: 7\r\n";
        request.setHeaders(headers);
        String data = "a=b&c=d";
        request.setData(data);
        script.add(request);
        ZestBasicRunner runner = new ZestBasicRunner();
        // When
        runner.run(script, new HashMap<String, String>());
        // Then
        request = runner.getLastRequest();
        assertThat(request).isNotNull();
        assertThat(request.getMethod()).isEqualTo(method);
        assertThat(request.getUrl()).isEqualTo(url);
        assertThat(request.getHeaders()).isEqualTo(headers);
        assertThat(request.getData()).isEqualTo(data);

        ZestResponse response = runner.getLastResponse();
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(404);
        assertThat(response.getUrl()).isEqualTo(url);
        assertThat(response.getHeaders())
                .isEqualTo(
                        "HTTP/1.1 404 Not Found\r\n"
                                + "Content-Type: text/plain\r\n"
                                + "Name: value\r\n"
                                + "Server: abc\r\n"
                                + "Transfer-Encoding: chunked\r\n");
        assertThat(response.getBody()).isEqualTo("This is the response");
    }
}
