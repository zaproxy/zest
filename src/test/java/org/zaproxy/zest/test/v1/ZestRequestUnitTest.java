/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.time.Instant;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.zaproxy.zest.core.v1.ZestCookie;
import org.zaproxy.zest.core.v1.ZestJSON;
import org.zaproxy.zest.core.v1.ZestRequest;
import org.zaproxy.zest.core.v1.ZestVariables;

/** */
class ZestRequestUnitTest {

    private static final String REQUEST_SERIALISATION =
            "{\n"
                    + "  \"url\": \"http://example.com/\",\n"
                    + "  \"urlToken\": \"http://{{host}}/\",\n"
                    + "  \"data\": \"a\\u003db\\u0026c\\u003dd\",\n"
                    + "  \"method\": \"POST\",\n"
                    + "  \"headers\": \"Header-A: value-a\\r\\nHeader-B: value-b\",\n"
                    + "  \"assertions\": [],\n"
                    + "  \"followRedirects\": false,\n"
                    + "  \"timestamp\": 1558960123456,\n"
                    + "  \"cookies\": [\n"
                    + "    {\n"
                    + "      \"domain\": \"example.com\",\n"
                    + "      \"name\": \"name\",\n"
                    + "      \"value\": \"value\",\n"
                    + "      \"path\": \"/path\",\n"
                    + "      \"expiry\": \"1970-01-01T00:00:00Z\",\n"
                    + "      \"secure\": true\n"
                    + "    },\n"
                    + "    {\n"
                    + "      \"domain\": \"example.com\",\n"
                    + "      \"name\": \"id\",\n"
                    + "      \"value\": \"valueId\",\n"
                    + "      \"path\": \"/path/id\",\n"
                    + "      \"expiry\": \"2019-05-27T12:28:43Z\",\n"
                    + "      \"secure\": false\n"
                    + "    }\n"
                    + "  ],\n"
                    + "  \"index\": 0,\n"
                    + "  \"enabled\": true,\n"
                    + "  \"elementType\": \"ZestRequest\"\n"
                    + "}";

    /**
     * Method testTokenReplacement.
     *
     * @throws Exception
     */
    @Test
    void testTokenReplacement() throws Exception {
        ZestRequest req = new ZestRequest();

        req.setUrl(new URL("http://www.example.com/app/{{token1}}"));
        req.setHeaders("Set-Cookie: test={{token2}}");
        req.setData("test={{token3}}&user=12{{token3}}34");
        req.addCookie(
                new ZestCookie(
                        "{{token}}.{{token3}}",
                        "12{{token3}}34",
                        "56{{token2}}78",
                        "/{{token1}}A/{{token2}}B",
                        new Date(),
                        true));

        ZestRequest req2 = req.deepCopy();

        ZestVariables tokens = new ZestVariables();
        tokens.setTokenStart("{{");
        tokens.setTokenEnd("}}");
        tokens.addVariable("token", "ABC");
        tokens.addVariable("token1", "DEFG");
        tokens.addVariable("token2", "GHI");
        tokens.addVariable("token3", "JKL");
        req2.replaceTokens(tokens);

        assertEquals("http://www.example.com/app/DEFG", req2.getUrl().toString());
        assertEquals("Set-Cookie: test=GHI", req2.getHeaders());
        assertEquals("test=JKL&user=12JKL34", req2.getData());
        assertEquals("ABC.JKL", req2.getCookies().get(0).getDomain());
        assertEquals("12JKL34", req2.getCookies().get(0).getName());
        assertEquals("56GHI78", req2.getCookies().get(0).getValue());
        assertEquals("/DEFGA/GHIB", req2.getCookies().get(0).getPath());
    }

    /**
     * Method testDeepCopy.
     *
     * @throws Exception
     */
    @Test
    void testDeepCopy() throws Exception {
        ZestRequest req = new ZestRequest();

        req.setUrl(new URL("http://www.example.com/app/{{token1}}"));
        req.setHeaders("Set-Cookie: test={{token2}}");
        req.setData("test={{token3}}&user=12{{token3}}34");
        req.setTimestamp(Instant.now().toEpochMilli());
        req.addCookie(
                new ZestCookie(
                        "{{token}}.{{token3}}",
                        "12{{token3}}34",
                        "56{{token2}}78",
                        "/{{token1}}A/{{token2}}B",
                        new Date(),
                        true));

        ZestRequest req2 = req.deepCopy();

        assertEquals(req.getHeaders(), req2.getHeaders());
        assertEquals(req.getData(), req2.getData());
        assertEquals(req.getTimestamp(), req2.getTimestamp());

        ZestCookie cookie = req.getCookies().get(0);
        ZestCookie cookie2 = req2.getCookies().get(0);

        assertTrue(cookie != cookie2, "cookies should not be reference equals");
        assertEquals(cookie.getDomain(), cookie2.getDomain());
        assertEquals(cookie.getName(), cookie2.getName());
        assertEquals(cookie.getValue(), cookie2.getValue());
        assertEquals(cookie.getPath(), cookie2.getPath());
        assertEquals(cookie.getExpiryDate(), cookie2.getExpiryDate());
        assertEquals(cookie.isSecure(), cookie2.isSecure());
    }

    @Test
    void shouldSerialise() throws Exception {
        // Given
        ZestRequest request = new ZestRequest();
        request.setUrl(new URL("http://example.com/"));
        request.setUrlToken("http://{{host}}/");
        request.setMethod("POST");
        request.setHeaders("Header-A: value-a\r\nHeader-B: value-b");
        request.setData("a=b&c=d");
        request.setFollowRedirects(false);
        request.setTimestamp(1558960123456L);
        request.addCookie("example.com", "name", "value", "/path", new Date(0L), true);
        request.addCookie(
                "example.com", "id", "valueId", "/path/id", new Date(1558960123000L), false);
        // When
        String serialisation = ZestJSON.toString(request);
        // Then
        assertThat(serialisation).isEqualTo(REQUEST_SERIALISATION);
    }

    @Test
    void shouldDeserialise() throws Exception {
        // Given
        String serialisation = REQUEST_SERIALISATION;
        // When
        ZestRequest deserialisedRequest = (ZestRequest) ZestJSON.fromString(serialisation);
        // Then
        assertThat(deserialisedRequest.getUrl()).isEqualTo(new URL("http://example.com/"));
        assertThat(deserialisedRequest.getUrlToken()).isEqualTo("http://{{host}}/");
        assertThat(deserialisedRequest.getMethod()).isEqualTo("POST");
        assertThat(deserialisedRequest.getHeaders())
                .isEqualTo("Header-A: value-a\r\nHeader-B: value-b");
        assertThat(deserialisedRequest.getData()).isEqualTo("a=b&c=d");
        assertThat(deserialisedRequest.isFollowRedirects()).isEqualTo(false);
        assertThat(deserialisedRequest.getTimestamp()).isEqualTo(1558960123456L);
        assertThat(deserialisedRequest.getCookies())
                .contains(
                        new ZestCookie("example.com", "name", "value", "/path", new Date(0L), true),
                        new ZestCookie(
                                "example.com",
                                "id",
                                "valueId",
                                "/path/id",
                                new Date(1558960123000L),
                                false));
    }
}
