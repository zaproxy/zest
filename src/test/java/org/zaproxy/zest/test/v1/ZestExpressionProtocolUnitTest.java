/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import org.junit.jupiter.api.Test;
import org.zaproxy.zest.core.v1.ZestExpressionProtocol;
import org.zaproxy.zest.core.v1.ZestJSON;
import org.zaproxy.zest.core.v1.ZestRequest;

/** Unit test for {@link ZestExpressionProtocol}. */
class ZestExpressionProtocolUnitTest {

    @Test
    void shouldNotHaveProtocolByDefault() {
        // Given / When
        ZestExpressionProtocol protocolExpression = new ZestExpressionProtocol();
        // Then
        assertNull(protocolExpression.getProtocol());
    }

    @Test
    void shouldNotBeInversedByDefault() {
        // Given / When
        ZestExpressionProtocol protocolExpression = new ZestExpressionProtocol();
        // Then
        assertFalse(protocolExpression.isInverse());
    }

    @Test
    void shouldSetProtocol() {
        // Given
        String protocol = "http";
        ZestExpressionProtocol protocolExpression = new ZestExpressionProtocol();
        // When
        protocolExpression.setProtocol(protocol);
        // Then
        assertEquals(protocol, protocolExpression.getProtocol());
    }

    @Test
    void shouldSetInverse() {
        // Given
        ZestExpressionProtocol protocolExpression = new ZestExpressionProtocol();
        // When
        protocolExpression.setInverse(true);
        // Then
        assertTrue(protocolExpression.isInverse());
    }

    @Test
    void shouldDeepCopy() {
        // Given
        ZestExpressionProtocol protocolExpression = new ZestExpressionProtocol("https");
        protocolExpression.setInverse(true);
        // When
        ZestExpressionProtocol copyProtocolExpression = protocolExpression.deepCopy();
        // Then
        assertTrue(copyProtocolExpression != protocolExpression);
        assertEquals(copyProtocolExpression.getProtocol(), protocolExpression.getProtocol());
        assertEquals(copyProtocolExpression.isInverse(), protocolExpression.isInverse());
    }

    @Test
    void shouldEvaluateToFalseIfNoProtocolSet() throws Exception {
        // Given
        ZestExpressionProtocol protocolExpression = new ZestExpressionProtocol();
        ZestRequest request = createRequest("http://localhost/");
        // When
        boolean result = protocolExpression.evaluate(new TestRuntime(request));
        // Then
        assertFalse(result);
    }

    @Test
    void shouldEvaluateToFalseIfNoRequest() {
        // Given
        ZestExpressionProtocol protocolExpression = new ZestExpressionProtocol("http");
        // When
        boolean result = protocolExpression.evaluate(new TestRuntime());
        // Then
        assertFalse(result);
    }

    @Test
    void shouldEvaluateToFalseIfRequestButNoUrl() {
        // Given
        ZestExpressionProtocol protocolExpression = new ZestExpressionProtocol("http");
        ZestRequest request = createRequest();
        // When
        boolean result = protocolExpression.evaluate(new TestRuntime(request));
        // Then
        assertFalse(result);
    }

    @Test
    void shouldEvaluateToTrueIfSameProtocol() throws Exception {
        // Given
        ZestExpressionProtocol protocolExpression = new ZestExpressionProtocol("http");
        ZestRequest request = createRequest("http://localhost/");
        // When
        boolean result = protocolExpression.evaluate(new TestRuntime(request));
        // Then
        assertTrue(result);
    }

    @Test
    void shouldEvaluateToTrueIfSameProtocolWithDifferenceCase() throws Exception {
        // Given
        ZestExpressionProtocol protocolExpression = new ZestExpressionProtocol("HTTP");
        ZestRequest request = createRequest("http://localhost/");
        // When
        boolean result = protocolExpression.evaluate(new TestRuntime(request));
        // Then
        assertTrue(result);
    }

    @Test
    void shouldEvaluateToFalseIfInverseAndSameProtocol() throws Exception {
        // Given
        ZestExpressionProtocol protocolExpression = new ZestExpressionProtocol("http");
        protocolExpression.setInverse(true);
        ZestRequest request = createRequest("http://localhost/");
        // When
        boolean result = protocolExpression.evaluate(new TestRuntime(request));
        // Then
        assertFalse(result);
    }

    @Test
    void shouldEvaluateToFalseIfNotSameProtocol() throws Exception {
        // Given
        ZestExpressionProtocol protocolExpression = new ZestExpressionProtocol("https");
        ZestRequest request = createRequest("http://localhost/");
        // When
        boolean result = protocolExpression.evaluate(new TestRuntime(request));
        // Then
        assertFalse(result);
    }

    @Test
    void shouldEvaluateToTrueIfInverseAndNotSameProtocol() throws Exception {
        // Given
        ZestExpressionProtocol protocolExpression = new ZestExpressionProtocol("https");
        protocolExpression.setInverse(true);
        ZestRequest request = createRequest("http://localhost/");
        // When
        boolean result = protocolExpression.evaluate(new TestRuntime(request));
        // Then
        assertTrue(result);
    }

    @Test
    void shouldSerialiseAndDeserialise() {
        // Given
        ZestExpressionProtocol protocolExpression = new ZestExpressionProtocol("http");
        // When
        String serialisation = ZestJSON.toString(protocolExpression);
        ZestExpressionProtocol deserialisedProtocolExpression =
                (ZestExpressionProtocol) ZestJSON.fromString(serialisation);
        // Then
        assertTrue(deserialisedProtocolExpression != protocolExpression);
        assertEquals(
                deserialisedProtocolExpression.getProtocol(), protocolExpression.getProtocol());
        assertEquals(deserialisedProtocolExpression.isInverse(), protocolExpression.isInverse());
    }

    private static ZestRequest createRequest() {
        return new ZestRequest();
    }

    private static ZestRequest createRequest(String url) throws Exception {
        ZestRequest request = createRequest();
        request.setUrl(new URL(url));
        return request;
    }
}
