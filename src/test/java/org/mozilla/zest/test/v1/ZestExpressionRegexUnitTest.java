/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.test.v1;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import org.junit.Test;
import org.mozilla.zest.core.v1.ZestExpressionRegex;
import org.mozilla.zest.core.v1.ZestResponse;
import org.mozilla.zest.core.v1.ZestVariables;

/** */
public class ZestExpressionRegexUnitTest {

    @Test
    public void testIsLeaf() {
        ZestExpressionRegex regex = new ZestExpressionRegex(ZestVariables.RESPONSE_BODY, "");
        assertTrue(regex.isLeaf());
    }

    @Test
    public void testIsInverse() {
        ZestExpressionRegex regex = new ZestExpressionRegex(ZestVariables.REQUEST_HEADER, "");
        ZestExpressionRegex copy = regex.deepCopy();
        copy.setInverse(true);
        regex.setInverse(false);
        assertTrue(copy.isInverse() && !regex.isInverse());
    }

    @Test
    public void testSetInverse() {
        ZestExpressionRegex regex =
                new ZestExpressionRegex(ZestVariables.RESPONSE_HEADER, "", false, false);
        regex.setInverse(true);
        assertTrue(regex.isInverse());
    }

    @Test
    public void testDeepCopySameLocation() {
        ZestExpressionRegex regex = new ZestExpressionRegex(ZestVariables.RESPONSE_HEADER, "PING");
        ZestExpressionRegex copy = regex.deepCopy();
        assertTrue(regex.getVariableName().equals(copy.getVariableName()));
    }

    @Test
    public void testDeepCopySameRegex() {
        ZestExpressionRegex regex = new ZestExpressionRegex(ZestVariables.RESPONSE_BODY, "PING");
        ZestExpressionRegex copy = regex.deepCopy();
        assertTrue(regex.getRegex().equals(copy.getRegex()));
    }

    @Test
    public void testDeepCopySameNoPointersRegex() {
        ZestExpressionRegex regex = new ZestExpressionRegex(ZestVariables.RESPONSE_HEADER, "PING");
        ZestExpressionRegex copy = regex.deepCopy();
        copy.setRegex("PONG");
        assertFalse(regex.getRegex().equals(copy.getRegex()));
    }

    @Test
    public void testDeepCopySameNoPointersLocation() {
        ZestExpressionRegex regex = new ZestExpressionRegex(ZestVariables.RESPONSE_HEADER, "PING");
        ZestExpressionRegex copy = regex.deepCopy();
        copy.setVariableName(ZestVariables.RESPONSE_BODY);
        assertFalse(regex.getVariableName().equals(copy.getVariableName()));
    }

    @Test
    public void testIsTrueHeader() {
        ZestResponse response =
                new ZestResponse(null, "123456header654321", "987654body456789", 200, 100);
        ZestExpressionRegex regexExpr =
                new ZestExpressionRegex(ZestVariables.RESPONSE_HEADER, "head");
        assertTrue(regexExpr.isTrue(new TestRuntime(response)));
    }

    @Test
    public void testIsTrueBody() {
        ZestResponse response =
                new ZestResponse(null, "123456header654321", "987654body456789", 200, 100);
        ZestExpressionRegex regexExpr =
                new ZestExpressionRegex(ZestVariables.RESPONSE_BODY, "body");
        assertTrue(regexExpr.isTrue(new TestRuntime(response)));
    }

    @Test
    public void testIsTrueNullBody() {
        ZestResponse response = new ZestResponse(null, null, null, 0, 0);
        ZestExpressionRegex regexExpr = new ZestExpressionRegex(ZestVariables.RESPONSE_BODY, "");
        assertFalse(regexExpr.isTrue(new TestRuntime(response)));
    }

    @Test
    public void testIsTrueNullHeader() {
        ZestResponse response = new ZestResponse(null, null, null, 0, 0);
        ZestExpressionRegex regex = new ZestExpressionRegex(ZestVariables.RESPONSE_HEADER, "");
        assertFalse(regex.isTrue(new TestRuntime(response)));
    }

    @Test
    public void shouldEvaluateAlwaysToFalseWithNullRegex() {
        // Given
        String nullRegex = null;
        String varName = "VarName";
        ZestExpressionRegex expressionRegex = new ZestExpressionRegex(varName, nullRegex);
        for (String varValue : Arrays.asList("", "Some value", "A\nB", "$", ".")) {
            // When
            boolean evalution = expressionRegex.evaluate(createRuntime(varName, varValue));
            // Then
            assertFalse("String \"" + varValue + "\" evaludated to false.", evalution);
        }
    }

    @Test
    public void shouldEvaluateAlwaysToTrueWithEmptyRegex() {
        // Given
        String emptyRegex = "";
        String varName = "VarName";
        ZestExpressionRegex expressionRegex = new ZestExpressionRegex(varName, emptyRegex);
        for (String varValue : Arrays.asList("", "Some value", "A\nB", "$", ".")) {
            // When
            boolean evalution = expressionRegex.evaluate(createRuntime(varName, varValue));
            // Then
            assertTrue("String \"" + varValue + "\" evaludated to false.", evalution);
        }
    }

    @Test
    public void shouldAllowToSetNullRegex() {
        // Given
        String nullRegex = null;
        ZestExpressionRegex regex = new ZestExpressionRegex("VarName", "");
        // When
        regex.setRegex(nullRegex);
        // Then
        assertTrue(regex.getRegex() == null);
    }

    private static TestRuntime createRuntime(String varName, String varValue) {
        TestRuntime runtime = new TestRuntime();
        runtime.setVariable(varName, varValue);
        return runtime;
    }
}
