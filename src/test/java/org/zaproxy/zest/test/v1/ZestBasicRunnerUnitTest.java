/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.zaproxy.zest.core.v1.ZestRequest;
import org.zaproxy.zest.core.v1.ZestResponse;
import org.zaproxy.zest.core.v1.ZestScript;
import org.zaproxy.zest.impl.ZestBasicRunner;

/** Unit test for {@code ZestBasicRunner}. */
class ZestBasicRunnerUnitTest extends ServerBasedTest {

    private static final String PATH_SERVER_FILE = "/test";
    private static final String PATH_SERVER_REDIRECT = "/redirect";

    @RegisterExtension
    public WireMockExtension proxy =
            WireMockExtension.newInstance()
                    .options(options().dynamicPort().enableBrowserProxying(true))
                    .failOnUnmatchedRequests(false)
                    .build();

    @BeforeEach
    void before() {
        server.stubFor(
                post(urlEqualTo(PATH_SERVER_FILE))
                        .willReturn(
                                aResponse()
                                        .withStatus(404)
                                        .withHeader("Content-Type", "text/plain")
                                        .withHeader("Name", "value")
                                        .withHeader("Server", "abc")
                                        .withBody("This is the response")));
        server.stubFor(
                get(urlEqualTo(PATH_SERVER_REDIRECT))
                        .willReturn(
                                aResponse()
                                        .withStatus(301)
                                        .withHeader("Location", PATH_SERVER_FILE)));
    }

    @Test
    void shouldFailToRunNonPassiveStatementsInPassiveScripts() throws Exception {
        // Given
        ZestScript script = new ZestScript();
        script.setType(ZestScript.Type.Passive);
        script.add(new ZestRequest());
        ZestBasicRunner runner = new ZestBasicRunner();
        // When / Then
        assertThrows(
                IllegalArgumentException.class,
                () -> runner.run(script, new HashMap<String, String>()));
    }

    @Test
    void shouldSendAndReceiveHttpMessage() throws Exception {
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
        assertThat(request.getTimestamp()).isCloseTo(System.currentTimeMillis(), byLessThan(2000L));
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

    @Test
    void shouldSendRequestWithoutGeneratedUserAgentHeader() throws Exception {
        // Given
        ZestScript script = new ZestScript();
        ZestRequest request = new ZestRequest();
        request.setMethod("GET");
        request.setUrl(new URL(getServerUrl(PATH_SERVER_FILE)));
        script.add(request);
        ZestBasicRunner runner = new ZestBasicRunner();
        // When
        runner.run(script, new HashMap<String, String>());
        // Then
        assertThat(runner.getLastRequest().getHeaders()).doesNotContain("User-Agent");
    }

    @Test
    void shouldSendRequestWithoutGeneratedContentTypeHeader() throws Exception {
        // Given
        ZestScript script = new ZestScript();
        ZestRequest request = new ZestRequest();
        request.setMethod("POST");
        request.setUrl(new URL(getServerUrl(PATH_SERVER_FILE)));
        request.setData("Content Request Body");
        script.add(request);
        ZestBasicRunner runner = new ZestBasicRunner();
        // When
        runner.run(script, new HashMap<String, String>());
        // Then
        assertThat(runner.getLastRequest().getHeaders()).doesNotContain("Content-Type");
    }

    @Test
    void shouldSendRequestWithoutGeneratedAcceptEncodingHeader() throws Exception {
        // Given
        ZestScript script = new ZestScript();
        ZestRequest request = new ZestRequest();
        request.setMethod("GET");
        request.setUrl(new URL(getServerUrl(PATH_SERVER_FILE)));
        script.add(request);
        ZestBasicRunner runner = new ZestBasicRunner();
        // When
        runner.run(script, new HashMap<String, String>());
        // Then
        assertThat(runner.getLastRequest().getHeaders()).doesNotContain("Accept-Encoding");
    }

    @Test
    void shouldSendRequestWithoutGeneratedConnectionHeader() throws Exception {
        // Given
        ZestScript script = new ZestScript();
        ZestRequest request = new ZestRequest();
        request.setMethod("GET");
        request.setUrl(new URL(getServerUrl(PATH_SERVER_FILE)));
        script.add(request);
        ZestBasicRunner runner = new ZestBasicRunner();
        // When
        runner.run(script, new HashMap<String, String>());
        // Then
        assertThat(runner.getLastRequest().getHeaders()).doesNotContain("Connection");
    }

    @Test
    void shouldSendPutRequestWithBody() throws Exception {
        // Given
        ZestScript script = new ZestScript();
        ZestRequest request = new ZestRequest();
        URL url = new URL(getServerUrl(PATH_SERVER_FILE));
        String method = "PUT";
        request.setMethod(method);
        request.setUrl(url);
        String data = "Content Request Body";
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
        assertThat(request.getData()).isEqualTo(data);
        server.verify(
                putRequestedFor(urlMatching(PATH_SERVER_FILE)).withRequestBody(equalTo(data)));
    }

    @Test
    void shouldSendRequestThroughConfiguredProxy() throws Exception {
        ZestScript script = new ZestScript();
        ZestRequest request = new ZestRequest();
        URL url = new URL(getServerUrl(PATH_SERVER_FILE));
        String method = "GET";
        request.setMethod(method);
        request.setUrl(url);
        script.add(request);
        ZestBasicRunner runner = new ZestBasicRunner();
        runner.setProxy("localhost", proxy.getPort());
        // When
        runner.run(script, new HashMap<String, String>());
        // Then
        request = runner.getLastRequest();
        assertThat(request).isNotNull();
        assertThat(request.getMethod()).isEqualTo(method);
        assertThat(request.getUrl()).isEqualTo(url);
        proxy.verify(
                getRequestedFor(urlMatching(PATH_SERVER_FILE))
                        .withHeader("Host", matching(getHostPort())));
        server.verify(
                getRequestedFor(urlMatching(PATH_SERVER_FILE))
                        .withHeader("Host", matching(getHostPort())));
    }

    @Test
    void shouldNoLongerSendRequestThroughProxyIfUnset() throws Exception {
        ZestScript script = new ZestScript();
        ZestRequest request = new ZestRequest();
        URL url = new URL(getServerUrl(PATH_SERVER_FILE));
        String method = "GET";
        request.setMethod(method);
        request.setUrl(url);
        script.add(request);
        ZestBasicRunner runner = new ZestBasicRunner();
        // When
        runner.setProxy("localhost", proxy.getPort());
        runner.run(script, Collections.emptyMap());
        runner.setProxy("", 0);
        runner.run(script, Collections.emptyMap());
        // Then
        proxy.verify(
                1,
                getRequestedFor(urlMatching(PATH_SERVER_FILE))
                        .withHeader("Host", matching(getHostPort())));
        server.verify(
                2,
                getRequestedFor(urlMatching(PATH_SERVER_FILE))
                        .withHeader("Host", matching(getHostPort())));
    }

    @Test
    void shouldFollowRedirectsByDefault() throws Exception {
        ZestScript script = new ZestScript();
        ZestRequest request = new ZestRequest();
        URL url = new URL(getServerUrl(PATH_SERVER_REDIRECT));
        String method = "GET";
        request.setMethod(method);
        request.setUrl(url);
        script.add(request);
        ZestBasicRunner runner = new ZestBasicRunner();
        // When
        runner.run(script, Collections.emptyMap());
        // Then
        request = runner.getLastRequest();
        assertThat(request).isNotNull();
        assertThat(request.getMethod()).isEqualTo(method);
        assertThat(request.getUrl()).isEqualTo(url);
        server.verify(getRequestedFor(urlMatching(PATH_SERVER_REDIRECT)));
        server.verify(getRequestedFor(urlMatching(PATH_SERVER_FILE)));
    }

    @Test
    void shouldNotFollowRedirectsIfDisabled() throws Exception {
        ZestScript script = new ZestScript();
        ZestRequest request = new ZestRequest();
        URL url = new URL(getServerUrl(PATH_SERVER_REDIRECT));
        String method = "GET";
        request.setMethod(method);
        request.setUrl(url);
        request.setFollowRedirects(false);
        script.add(request);
        ZestBasicRunner runner = new ZestBasicRunner();
        runner.setProxy("localhost", proxy.getPort());
        // When
        runner.run(script, Collections.emptyMap());
        // Then
        request = runner.getLastRequest();
        assertThat(request).isNotNull();
        assertThat(request.getMethod()).isEqualTo(method);
        assertThat(request.getUrl()).isEqualTo(url);
        server.verify(getRequestedFor(urlMatching(PATH_SERVER_REDIRECT)));
        server.verify(0, getRequestedFor(urlMatching(PATH_SERVER_FILE)));
    }

    @Test
    void shouldFollowRedirectsThroughConfiguredProxy() throws Exception {
        ZestScript script = new ZestScript();
        ZestRequest request = new ZestRequest();
        URL url = new URL(getServerUrl(PATH_SERVER_REDIRECT));
        String method = "GET";
        request.setMethod(method);
        request.setUrl(url);
        script.add(request);
        ZestBasicRunner runner = new ZestBasicRunner();
        runner.setProxy("localhost", proxy.getPort());
        // When
        runner.run(script, Collections.emptyMap());
        // Then
        request = runner.getLastRequest();
        assertThat(request).isNotNull();
        assertThat(request.getMethod()).isEqualTo(method);
        assertThat(request.getUrl()).isEqualTo(url);
        proxy.verify(
                getRequestedFor(urlMatching(PATH_SERVER_FILE))
                        .withHeader("Host", matching(getHostPort())));
        server.verify(
                getRequestedFor(urlMatching(PATH_SERVER_FILE))
                        .withHeader("Host", matching(getHostPort())));
    }

    @Test
    void shouldNotFollowRedirectsIfDisabledThroughConfiguredProxy() throws Exception {
        ZestScript script = new ZestScript();
        ZestRequest request = new ZestRequest();
        URL url = new URL(getServerUrl(PATH_SERVER_REDIRECT));
        String method = "GET";
        request.setMethod(method);
        request.setUrl(url);
        request.setFollowRedirects(false);
        script.add(request);
        ZestBasicRunner runner = new ZestBasicRunner();
        runner.setProxy("localhost", proxy.getPort());
        // When
        runner.run(script, Collections.emptyMap());
        // Then
        request = runner.getLastRequest();
        assertThat(request).isNotNull();
        assertThat(request.getMethod()).isEqualTo(method);
        assertThat(request.getUrl()).isEqualTo(url);
        proxy.verify(
                getRequestedFor(urlMatching(PATH_SERVER_REDIRECT))
                        .withHeader("Host", matching(getHostPort())));
        server.verify(0, getRequestedFor(urlMatching(PATH_SERVER_FILE)));
    }
}
