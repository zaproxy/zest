/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zaproxy.zest.core.v1.ZestComment;
import org.zaproxy.zest.core.v1.ZestRequest;
import org.zaproxy.zest.core.v1.ZestResponse;
import org.zaproxy.zest.core.v1.ZestScript;
import org.zaproxy.zest.core.v1.ZestStatement;
import org.zaproxy.zest.impl.ZestBasicRunner;
import org.zaproxy.zest.testutils.HTTPDTestServer;
import org.zaproxy.zest.testutils.HTTPDTestServer.Request;
import org.zaproxy.zest.testutils.NanoServerHandler;

/** Unit test for {@code ZestBasicRunner}. */
class ZestBasicRunnerUnitTest extends ServerBasedTest {

    private static final String PATH_SERVER_FILE = "/test";
    private static final String PATH_SERVER_REDIRECT = "/redirect";

    private NanoServerHandler fileHandler;
    private NanoServerHandler redirectHandler;
    private HTTPDTestServer proxy;

    @BeforeEach
    void startProxy() throws IOException {
        proxy = new HTTPDTestServer(0);
        proxy.start();
    }

    @AfterEach
    void stopProxy() {
        if (proxy != null) {
            proxy.stop();
        }
    }

    @BeforeEach
    void before() {
        fileHandler =
                new NanoServerHandler(PATH_SERVER_FILE) {
                    @Override
                    protected boolean handles(IHTTPSession session) {
                        return session.getUri().endsWith(getName());
                    }

                    @Override
                    protected Response serve(IHTTPSession session) {
                        Response response =
                                newFixedLengthResponse(
                                        Response.Status.NOT_FOUND,
                                        NanoHTTPD.MIME_PLAINTEXT,
                                        "This is the response");
                        response.addHeader("Name", "value");
                        response.addHeader("Server", "abc");
                        return response;
                    }
                };
        proxy.addHandler(fileHandler);
        server.addHandler(fileHandler);

        redirectHandler =
                new NanoServerHandler(PATH_SERVER_REDIRECT) {
                    @Override
                    protected boolean handles(IHTTPSession session) {
                        return session.getUri().endsWith(getName());
                    }

                    @Override
                    protected Response serve(IHTTPSession session) {
                        Response response =
                                newFixedLengthResponse(
                                        Response.Status.REDIRECT, NanoHTTPD.MIME_PLAINTEXT, "");
                        response.addHeader("Location", PATH_SERVER_FILE);
                        return response;
                    }
                };
        proxy.addHandler(redirectHandler);
        server.addHandler(redirectHandler);
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
        URL url = createUrl(getServerUrl(PATH_SERVER_FILE));
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
                .startsWith("HTTP/1.1 404 Not Found\r\n")
                .contains("Name: value\r\n")
                .contains("Server: abc\r\n");
        assertThat(response.getBody()).isEqualTo("This is the response");
    }

    @Test
    void shouldSendRequestWithoutGeneratedUserAgentHeader() throws Exception {
        // Given
        ZestScript script = new ZestScript();
        ZestRequest request = new ZestRequest();
        request.setMethod("GET");
        request.setUrl(createUrl(getServerUrl(PATH_SERVER_FILE)));
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
        request.setUrl(createUrl(getServerUrl(PATH_SERVER_FILE)));
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
        request.setUrl(createUrl(getServerUrl(PATH_SERVER_FILE)));
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
        request.setUrl(createUrl(getServerUrl(PATH_SERVER_FILE)));
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
        URL url = createUrl(getServerUrl(PATH_SERVER_FILE));
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
        Request actualRequest = assertThat(server.getRequests()).hasSize(1).actual().get(0);
        assertThat(actualRequest.method()).isEqualTo("PUT");
        assertThat(actualRequest.body()).isEqualTo(data);
    }

    @Test
    void shouldSendRequestThroughConfiguredProxy() throws Exception {
        ZestScript script = new ZestScript();
        ZestRequest request = new ZestRequest();
        URL url = createUrl(getServerUrl(PATH_SERVER_FILE));
        String method = "GET";
        request.setMethod(method);
        request.setUrl(url);
        script.add(request);
        ZestBasicRunner runner = new ZestBasicRunner();
        runner.setProxy("localhost", proxy.getListeningPort());
        // When
        runner.run(script, new HashMap<String, String>());
        // Then
        request = runner.getLastRequest();
        assertThat(request).isNotNull();
        assertThat(request.getMethod()).isEqualTo(method);
        assertThat(request.getUrl()).isEqualTo(url);
        Request actualRequest = assertThat(proxy.getRequests()).hasSize(1).actual().get(0);
        assertThat(actualRequest.headers()).contains(Map.entry("host", getHostPort()));
    }

    @Test
    void shouldNoLongerSendRequestThroughProxyIfUnset() throws Exception {
        ZestScript script = new ZestScript();
        ZestRequest request = new ZestRequest();
        URL url = createUrl(getServerUrl(PATH_SERVER_FILE));
        String method = "GET";
        request.setMethod(method);
        request.setUrl(url);
        script.add(request);
        ZestBasicRunner runner = new ZestBasicRunner();
        // When
        runner.setProxy("localhost", proxy.getListeningPort());
        runner.run(script, Collections.emptyMap());
        runner.setProxy("", 0);
        runner.run(script, Collections.emptyMap());
        // Then
        Request actualRequest = assertThat(proxy.getRequests()).hasSize(1).actual().get(0);
        assertThat(actualRequest.headers()).contains(Map.entry("host", getHostPort()));
        actualRequest = assertThat(server.getRequests()).hasSize(1).actual().get(0);
        assertThat(actualRequest.headers()).contains(Map.entry("host", getHostPort()));
    }

    @Test
    void shouldFollowRedirectsByDefault() throws Exception {
        ZestScript script = new ZestScript();
        ZestRequest request = new ZestRequest();
        URL url = createUrl(getServerUrl(PATH_SERVER_REDIRECT));
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
        var requests = assertThat(server.getRequests()).hasSize(2).actual();
        assertThat(requests.get(0).uri()).endsWith(PATH_SERVER_REDIRECT);
        assertThat(requests.get(1).uri()).endsWith(PATH_SERVER_FILE);
    }

    @Test
    void shouldNotFollowRedirectsIfDisabled() throws Exception {
        ZestScript script = new ZestScript();
        ZestRequest request = new ZestRequest();
        URL url = createUrl(getServerUrl(PATH_SERVER_REDIRECT));
        String method = "GET";
        request.setMethod(method);
        request.setUrl(url);
        request.setFollowRedirects(false);
        script.add(request);
        ZestBasicRunner runner = new ZestBasicRunner();
        // When
        runner.run(script, Collections.emptyMap());
        // Then
        request = runner.getLastRequest();
        assertThat(request).isNotNull();
        assertThat(request.getMethod()).isEqualTo(method);
        assertThat(request.getUrl()).isEqualTo(url);
        Request serverRequest = assertThat(server.getRequests()).hasSize(1).actual().get(0);
        assertThat(serverRequest.uri()).isEqualTo(PATH_SERVER_REDIRECT);
    }

    @Test
    void shouldFollowRedirectsThroughConfiguredProxy() throws Exception {
        ZestScript script = new ZestScript();
        ZestRequest request = new ZestRequest();
        URL url = createUrl(getServerUrl(PATH_SERVER_REDIRECT));
        String method = "GET";
        request.setMethod(method);
        request.setUrl(url);
        script.add(request);
        ZestBasicRunner runner = new ZestBasicRunner();
        runner.setProxy("localhost", proxy.getListeningPort());
        // When
        runner.run(script, Collections.emptyMap());
        // Then
        request = runner.getLastRequest();
        assertThat(request).isNotNull();
        assertThat(request.getMethod()).isEqualTo(method);
        assertThat(request.getUrl()).isEqualTo(url);
        var requests = assertThat(proxy.getRequests()).hasSize(2).actual();
        Request actualRequest = requests.get(0);
        assertThat(actualRequest.uri()).endsWith(PATH_SERVER_REDIRECT);
        assertThat(actualRequest.headers()).contains(Map.entry("host", getHostPort()));
        actualRequest = requests.get(1);
        assertThat(actualRequest.uri()).endsWith(PATH_SERVER_FILE);
        assertThat(actualRequest.headers()).contains(Map.entry("host", getHostPort()));
    }

    @Test
    void shouldNotFollowRedirectsIfDisabledThroughConfiguredProxy() throws Exception {
        ZestScript script = new ZestScript();
        ZestRequest request = new ZestRequest();
        URL url = createUrl(getServerUrl(PATH_SERVER_REDIRECT));
        String method = "GET";
        request.setMethod(method);
        request.setUrl(url);
        request.setFollowRedirects(false);
        script.add(request);
        ZestBasicRunner runner = new ZestBasicRunner();
        runner.setProxy("localhost", proxy.getListeningPort());
        // When
        runner.run(script, Collections.emptyMap());
        // Then
        request = runner.getLastRequest();
        assertThat(request).isNotNull();
        assertThat(request.getMethod()).isEqualTo(method);
        assertThat(request.getUrl()).isEqualTo(url);
        Request actualRequest = assertThat(proxy.getRequests()).hasSize(1).actual().get(0);
        assertThat(actualRequest.uri()).endsWith(PATH_SERVER_REDIRECT);
        assertThat(actualRequest.headers()).contains(Map.entry("host", getHostPort()));
    }

    @Test
    void shouldDefaultToNoStatementDelay() throws Exception {
        // Given
        ZestScript script = new ZestScript();
        script.add(new ZestComment("1"));
        script.add(new ZestComment("2"));
        script.add(new ZestComment("3"));
        ZestBasicRunner runner = new ZestBasicRunner();
        // When
        long startTime = System.currentTimeMillis();
        runner.run(script, Collections.emptyMap());
        long endTime = System.currentTimeMillis();
        // Then
        assertThat(script.getStatementDelay()).isEqualTo(0);
        assertTrue(endTime - startTime < 50);
    }

    @Test
    void shouldApplyStatementDelay() throws Exception {
        // Given
        ZestScript script = new ZestScript();
        script.add(new ZestComment("1"));
        script.add(new ZestComment("2"));
        script.add(new ZestComment("3"));
        ZestBasicRunner runner = new ZestBasicRunner();
        script.setOptions(Map.of(ZestScript.STATEMENT_DELAY_MS, "100"));
        // When
        long startTime = System.currentTimeMillis();
        runner.run(script, Collections.emptyMap());
        long endTime = System.currentTimeMillis();
        // Then
        assertThat(script.getStatementDelay()).isEqualTo(100);
        assertTrue(endTime - startTime >= 300);
    }

    @Test
    void shouldNotApplyStatementDelayToDisabledStatements() throws Exception {
        // Given
        ZestScript script = new ZestScript();
        script.add(disable(new ZestComment("1")));
        script.add(disable(new ZestComment("2")));
        script.add(disable(new ZestComment("3")));
        ZestBasicRunner runner = new ZestBasicRunner();
        script.setOptions(Map.of(ZestScript.STATEMENT_DELAY_MS, "100"));
        // When
        long startTime = System.currentTimeMillis();
        runner.run(script, Collections.emptyMap());
        long endTime = System.currentTimeMillis();
        // Then
        assertThat(script.getStatementDelay()).isEqualTo(100);
        assertTrue(endTime - startTime < 50);
    }

    private static ZestStatement disable(ZestStatement stmt) {
        stmt.setEnabled(false);
        return stmt;
    }

    private static URL createUrl(String value) throws MalformedURLException, URISyntaxException {
        return new URI(value).toURL();
    }
}
