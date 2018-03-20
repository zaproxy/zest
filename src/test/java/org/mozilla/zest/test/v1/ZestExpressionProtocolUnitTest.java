/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.test.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import org.junit.Test;
import org.mozilla.zest.core.v1.ZestExpressionProtocol;
import org.mozilla.zest.core.v1.ZestJSON;
import org.mozilla.zest.core.v1.ZestRequest;

/** Unit test for {@link ZestExpressionProtocol}. */
public class ZestExpressionProtocolUnitTest {

    @Test
    public void shouldNotHaveProtocolByDefault() {
        // Given / When
        ZestExpressionProtocol protocolExpression = new ZestExpressionProtocol();
        // Then
        assertTrue(protocolExpression.getProtocol() == null);
    }

    @Test
    public void shouldNotBeInversedByDefault() {
        // Given / When
        ZestExpressionProtocol protocolExpression = new ZestExpressionProtocol();
        // Then
        assertFalse(protocolExpression.isInverse());
    }

    @Test
    public void shouldSetProtocol() {
        // Given
        String protocol = "http";
        ZestExpressionProtocol protocolExpression = new ZestExpressionProtocol();
        // When
        protocolExpression.setProtocol(protocol);
        // Then
        assertEquals(protocol, protocolExpression.getProtocol());
    }

    @Test
    public void shouldSetInverse() {
        // Given
        ZestExpressionProtocol protocolExpression = new ZestExpressionProtocol();
        // When
        protocolExpression.setInverse(true);
        // Then
        assertTrue(protocolExpression.isInverse());
    }

    @Test
    public void shouldDeepCopy() {
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
    public void shouldEvaluateToFalseIfNoProtocolSet() throws Exception {
        // Given
        ZestExpressionProtocol protocolExpression = new ZestExpressionProtocol();
        ZestRequest request = createRequest("http://localhost/");
        // When
        boolean result = protocolExpression.evaluate(new TestRuntime(request));
        // Then
        assertFalse(result);
    }

    @Test
    public void shouldEvaluateToFalseIfNoRequest() throws Exception {
        // Given
        ZestExpressionProtocol protocolExpression = new ZestExpressionProtocol("http");
        // When
        boolean result = protocolExpression.evaluate(new TestRuntime());
        // Then
        assertFalse(result);
    }

    @Test
    public void shouldEvaluateToFalseIfRequestButNoUrl() throws Exception {
        // Given
        ZestExpressionProtocol protocolExpression = new ZestExpressionProtocol("http");
        ZestRequest request = createRequest();
        // When
        boolean result = protocolExpression.evaluate(new TestRuntime(request));
        // Then
        assertFalse(result);
    }

    @Test
    public void shouldEvaluateToTrueIfSameProtocol() throws Exception {
        // Given
        ZestExpressionProtocol protocolExpression = new ZestExpressionProtocol("http");
        ZestRequest request = createRequest("http://localhost/");
        // When
        boolean result = protocolExpression.evaluate(new TestRuntime(request));
        // Then
        assertTrue(result);
    }

    @Test
    public void shouldEvaluateToTrueIfSameProtocolWithDifferenceCase() throws Exception {
        // Given
        ZestExpressionProtocol protocolExpression = new ZestExpressionProtocol("HTTP");
        ZestRequest request = createRequest("http://localhost/");
        // When
        boolean result = protocolExpression.evaluate(new TestRuntime(request));
        // Then
        assertTrue(result);
    }

    @Test
    public void shouldEvaluateToFalseIfInverseAndSameProtocol() throws Exception {
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
    public void shouldEvaluateToFalseIfNotSameProtocol() throws Exception {
        // Given
        ZestExpressionProtocol protocolExpression = new ZestExpressionProtocol("https");
        ZestRequest request = createRequest("http://localhost/");
        // When
        boolean result = protocolExpression.evaluate(new TestRuntime(request));
        // Then
        assertFalse(result);
    }

    @Test
    public void shouldEvaluateToTrueIfInverseAndNotSameProtocol() throws Exception {
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
    public void shouldSerialiseAndDeserialise() {
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
