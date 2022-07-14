/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.zaproxy.zest.core.v1.ZestExpressionRegex;
import org.zaproxy.zest.core.v1.ZestResponse;
import org.zaproxy.zest.core.v1.ZestVariables;

/** */
class ZestExpressionRegexUnitTest {

    @Test
    void testIsLeaf() {
        ZestExpressionRegex regex = new ZestExpressionRegex(ZestVariables.RESPONSE_BODY, "");
        assertTrue(regex.isLeaf());
    }

    @Test
    void testIsInverse() {
        ZestExpressionRegex regex = new ZestExpressionRegex(ZestVariables.REQUEST_HEADER, "");
        ZestExpressionRegex copy = regex.deepCopy();
        copy.setInverse(true);
        regex.setInverse(false);
        assertTrue(copy.isInverse() && !regex.isInverse());
    }

    @Test
    void testSetInverse() {
        ZestExpressionRegex regex =
                new ZestExpressionRegex(ZestVariables.RESPONSE_HEADER, "", false, false);
        regex.setInverse(true);
        assertTrue(regex.isInverse());
    }

    @Test
    void testDeepCopySameLocation() {
        ZestExpressionRegex regex = new ZestExpressionRegex(ZestVariables.RESPONSE_HEADER, "PING");
        ZestExpressionRegex copy = regex.deepCopy();
        assertEquals(regex.getVariableName(), copy.getVariableName());
    }

    @Test
    void testDeepCopySameRegex() {
        ZestExpressionRegex regex = new ZestExpressionRegex(ZestVariables.RESPONSE_BODY, "PING");
        ZestExpressionRegex copy = regex.deepCopy();
        assertEquals(regex.getRegex(), copy.getRegex());
    }

    @Test
    void testDeepCopySameNoPointersRegex() {
        ZestExpressionRegex regex = new ZestExpressionRegex(ZestVariables.RESPONSE_HEADER, "PING");
        ZestExpressionRegex copy = regex.deepCopy();
        copy.setRegex("PONG");
        assertNotEquals(regex.getRegex(), copy.getRegex());
    }

    @Test
    void testDeepCopySameNoPointersLocation() {
        ZestExpressionRegex regex = new ZestExpressionRegex(ZestVariables.RESPONSE_HEADER, "PING");
        ZestExpressionRegex copy = regex.deepCopy();
        copy.setVariableName(ZestVariables.RESPONSE_BODY);
        assertNotEquals(regex.getVariableName(), copy.getVariableName());
    }

    @Test
    void testIsTrueHeader() {
        ZestResponse response =
                new ZestResponse(null, "123456header654321", "987654body456789", 200, 100);
        ZestExpressionRegex regexExpr =
                new ZestExpressionRegex(ZestVariables.RESPONSE_HEADER, "head");
        assertTrue(regexExpr.isTrue(new TestRuntime(response)));
    }

    @Test
    void testIsTrueBody() {
        ZestResponse response =
                new ZestResponse(null, "123456header654321", "987654body456789", 200, 100);
        ZestExpressionRegex regexExpr =
                new ZestExpressionRegex(ZestVariables.RESPONSE_BODY, "body");
        assertTrue(regexExpr.isTrue(new TestRuntime(response)));
    }

    @Test
    void testIsTrueNullBody() {
        ZestResponse response = new ZestResponse(null, null, null, 0, 0);
        ZestExpressionRegex regexExpr = new ZestExpressionRegex(ZestVariables.RESPONSE_BODY, "");
        assertFalse(regexExpr.isTrue(new TestRuntime(response)));
    }

    @Test
    void testIsTrueNullHeader() {
        ZestResponse response = new ZestResponse(null, null, null, 0, 0);
        ZestExpressionRegex regex = new ZestExpressionRegex(ZestVariables.RESPONSE_HEADER, "");
        assertFalse(regex.isTrue(new TestRuntime(response)));
    }

    @Test
    void shouldEvaluateAlwaysToFalseWithNullRegex() {
        // Given
        String nullRegex = null;
        String varName = "VarName";
        ZestExpressionRegex expressionRegex = new ZestExpressionRegex(varName, nullRegex);
        for (String varValue : Arrays.asList("", "Some value", "A\nB", "$", ".")) {
            // When
            boolean evalution = expressionRegex.evaluate(createRuntime(varName, varValue));
            // Then
            assertFalse(evalution, "String \"" + varValue + "\" evaludated to false.");
        }
    }

    @Test
    void shouldEvaluateAlwaysToTrueWithEmptyRegex() {
        // Given
        String emptyRegex = "";
        String varName = "VarName";
        ZestExpressionRegex expressionRegex = new ZestExpressionRegex(varName, emptyRegex);
        for (String varValue : Arrays.asList("", "Some value", "A\nB", "$", ".")) {
            // When
            boolean evalution = expressionRegex.evaluate(createRuntime(varName, varValue));
            // Then
            assertTrue(evalution, "String \"" + varValue + "\" evaludated to false.");
        }
    }

    @Test
    void shouldAllowToSetNullRegex() {
        // Given
        String nullRegex = null;
        ZestExpressionRegex regex = new ZestExpressionRegex("VarName", "");
        // When
        regex.setRegex(nullRegex);
        // Then
        assertNull(regex.getRegex());
    }

    private static TestRuntime createRuntime(String varName, String varValue) {
        TestRuntime runtime = new TestRuntime();
        runtime.setVariable(varName, varValue);
        return runtime;
    }
}
