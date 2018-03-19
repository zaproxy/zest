/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.test.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import org.junit.Test;
import org.mozilla.zest.core.v1.ZestExpressionStatusCode;
import org.mozilla.zest.core.v1.ZestJSON;
import org.mozilla.zest.core.v1.ZestResponse;

/** Unit test for {@link ZestExpressionStatusCode}. */
public class ZestExpressionStatusCodeUnitTest {

    @Test
    public void shouldHaveZeroStatusCodeByDefault() {
        // Given / When
        ZestExpressionStatusCode statusCodeExpression = new ZestExpressionStatusCode();
        // Then
        assertEquals(statusCodeExpression.getCode(), 0);
    }

    @Test
    public void shouldNotBeInversedByDefault() {
        // Given / When
        ZestExpressionStatusCode statusCodeExpression = new ZestExpressionStatusCode();
        // Then
        assertFalse(statusCodeExpression.isInverse());
    }

    @Test
    public void shouldSetStatusCode() {
        // Given
        int statusCode = 201;
        ZestExpressionStatusCode statusCodeExpression = new ZestExpressionStatusCode();
        // When
        statusCodeExpression.setCode(statusCode);
        // Then
        assertEquals(statusCode, statusCodeExpression.getCode());
    }

    @Test
    public void shouldSetInverse() {
        // Given
        ZestExpressionStatusCode statusCodeExpression = new ZestExpressionStatusCode();
        // When
        statusCodeExpression.setInverse(true);
        // Then
        assertTrue(statusCodeExpression.isInverse());
    }

    @Test
    public void shouldDeepCopy() {
        // Given
        ZestExpressionStatusCode statusCodeExpression = new ZestExpressionStatusCode(404);
        statusCodeExpression.setInverse(true);
        // When
        ZestExpressionStatusCode copyStatusCodeExpression = statusCodeExpression.deepCopy();
        // Then
        assertTrue(copyStatusCodeExpression != statusCodeExpression);
        assertEquals(copyStatusCodeExpression.getCode(), statusCodeExpression.getCode());
        assertEquals(copyStatusCodeExpression.isInverse(), statusCodeExpression.isInverse());
    }

    @Test
    public void shouldEvaluateToFalseIfNoResponse() throws Exception {
        // Given
        ZestExpressionStatusCode statusCodeExpression = new ZestExpressionStatusCode(204);
        // When
        boolean result = statusCodeExpression.evaluate(new TestRuntime());
        // Then
        assertFalse(result);
    }

    @Test
    public void shouldEvaluateToTrueIfSameStatusCode() throws Exception {
        // Given
        ZestExpressionStatusCode statusCodeExpression = new ZestExpressionStatusCode(200);
        ZestResponse response = createResponse(200);
        // When
        boolean result = statusCodeExpression.evaluate(new TestRuntime(response));
        // Then
        assertTrue(result);
    }

    @Test
    public void shouldEvaluateToFalseIfInverseAndSameStatusCode() throws Exception {
        // Given
        ZestExpressionStatusCode statusCodeExpression = new ZestExpressionStatusCode(200);
        statusCodeExpression.setInverse(true);
        ZestResponse response = createResponse(200);
        // When
        boolean result = statusCodeExpression.evaluate(new TestRuntime(response));
        // Then
        assertFalse(result);
    }

    @Test
    public void shouldEvaluateToFalseIfNotSameStatusCode() throws Exception {
        // Given
        ZestExpressionStatusCode statusCodeExpression = new ZestExpressionStatusCode(500);
        ZestResponse response = createResponse(200);
        // When
        boolean result = statusCodeExpression.evaluate(new TestRuntime(response));
        // Then
        assertFalse(result);
    }

    @Test
    public void shouldEvaluateToTrueIfInverseAndNotSameStatusCode() throws Exception {
        // Given
        ZestExpressionStatusCode statusCodeExpression = new ZestExpressionStatusCode(200);
        statusCodeExpression.setInverse(true);
        ZestResponse response = createResponse(500);
        // When
        boolean result = statusCodeExpression.evaluate(new TestRuntime(response));
        // Then
        assertTrue(result);
    }

    @Test
    public void shouldSerialiseAndDeserialise() {
        // Given
        ZestExpressionStatusCode statusCodeExpression = new ZestExpressionStatusCode(401);
        // When
        String serialisation = ZestJSON.toString(statusCodeExpression);
        ZestExpressionStatusCode deserialisedStatusCodeExpression =
                (ZestExpressionStatusCode) ZestJSON.fromString(serialisation);
        // Then
        assertTrue(deserialisedStatusCodeExpression != statusCodeExpression);
        assertEquals(deserialisedStatusCodeExpression.getCode(), statusCodeExpression.getCode());
        assertEquals(
                deserialisedStatusCodeExpression.isInverse(), statusCodeExpression.isInverse());
    }

    private static ZestResponse createResponse(int statusCode) throws Exception {
        return new ZestResponse(new URL("http://localhost/"), "", "", statusCode, 0);
    }
}
