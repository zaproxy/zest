/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;
import org.zaproxy.zest.core.v1.ZestActionInvoke;
import org.zaproxy.zest.core.v1.ZestJSON;
import org.zaproxy.zest.core.v1.ZestResponse;

/** */
class ZestActionInvokeUnitTest {

    @Test
    void shouldUseArgsPassedInConstructor() {
        // Given
        String script = "script.js";
        String variable = "var";
        List<String[]> parameters = new ArrayList<>();
        String charset = StandardCharsets.UTF_8.name();
        // When
        ZestActionInvoke invokeAction = new ZestActionInvoke(script, variable, parameters, charset);
        // Then
        assertEquals(invokeAction.getScript(), script);
        assertEquals(invokeAction.getVariableName(), variable);
        assertEquals(invokeAction.getParameters(), parameters);
        assertEquals(invokeAction.getCharset(), charset);
    }

    @Test
    @EnabledForJreRange(
            disabledReason = "JavaScript engine not present.",
            min = JRE.JAVA_8,
            max = JRE.JAVA_11)
    void testSimpleJsScript() throws Exception {
        ZestActionInvoke inv = new ZestActionInvoke();
        inv.setVariableName("test");
        inv.setScript(
                ZestActionInvokeUnitTest.class.getResource("/data/simple-script.js").getPath());
        TestRuntime rt = new TestRuntime();

        ZestResponse resp =
                new ZestResponse(
                        null, "Header prefix12345postfix", "Body Prefix54321Postfix", 200, 0);
        String result = inv.invoke(resp, rt);

        assertEquals("abcde", result);
    }

    @Test
    @EnabledForJreRange(
            disabledReason = "JavaScript engine not present.",
            min = JRE.JAVA_8,
            max = JRE.JAVA_11)
    void testParamJsScript() throws Exception {
        ZestActionInvoke inv = new ZestActionInvoke();
        inv.setVariableName("test");
        inv.setScript(
                ZestActionInvokeUnitTest.class.getResource("/data/param-script.js").getPath());
        inv.setParameters(params(param("param", "PQRST")));
        TestRuntime rt = new TestRuntime();

        ZestResponse resp =
                new ZestResponse(
                        null, "Header prefix12345postfix", "Body Prefix54321Postfix", 200, 0);
        String result = inv.invoke(resp, rt);

        assertEquals("PQRST", result);
    }

    @Test
    void testSimpleZestScript() throws Exception {
        ZestActionInvoke inv = new ZestActionInvoke();
        inv.setVariableName("test");
        inv.setScript(
                ZestActionInvokeUnitTest.class.getResource("/data/simple-script.zest").getPath());
        TestRuntime rt = new TestRuntime();

        ZestResponse resp =
                new ZestResponse(
                        null, "Header prefix12345postfix", "Body Prefix54321Postfix", 200, 0);
        String result = inv.invoke(resp, rt);

        assertEquals("ABCDE", result);
    }

    @Test
    void testAssignZestScript() throws Exception {
        ZestActionInvoke inv = new ZestActionInvoke();
        inv.setVariableName("test");
        inv.setScript(
                ZestActionInvokeUnitTest.class.getResource("/data/assign-script.zest").getPath());
        TestRuntime rt = new TestRuntime();

        ZestResponse resp =
                new ZestResponse(
                        null, "Header prefix12345postfix", "Body Prefix54321Postfix", 200, 0);
        String result = inv.invoke(resp, rt);

        assertEquals("EFGHI", result);
    }

    @Test
    void testParamZestScript() throws Exception {
        ZestActionInvoke inv = new ZestActionInvoke();
        inv.setVariableName("test");
        inv.setScript(
                ZestActionInvokeUnitTest.class.getResource("/data/param-script.zest").getPath());
        inv.setParameters(params(param("param", "ZYXWV")));
        TestRuntime rt = new TestRuntime();

        ZestResponse resp =
                new ZestResponse(
                        null, "Header prefix12345postfix", "Body Prefix54321Postfix", 200, 0);
        String result = inv.invoke(resp, rt);

        assertEquals("ZYXWV", result);
        assertEquals("ZYXWV", rt.getVariable("test"));
    }

    @Test
    @EnabledForJreRange(
            disabledReason = "JavaScript engine not present.",
            min = JRE.JAVA_8,
            max = JRE.JAVA_11)
    void shouldReplaceVariablesPassedAsParameters() throws Exception {
        // Given
        String varName = "VarName";
        String varValue = "VarValue";
        TestRuntime rt = new TestRuntime();
        rt.setVariable(varName, varValue);
        ZestActionInvoke inv = new ZestActionInvoke();
        inv.setScript(
                ZestActionInvokeUnitTest.class.getResource("/data/param-script.js").getPath());
        inv.setParameters(params(param("param", "{{" + varName + "}}")));
        // When
        String result = inv.invoke(null, rt);
        // Then
        assertEquals(varValue, result);
    }

    @Test
    void testSerialization() {
        ZestActionInvoke inv = new ZestActionInvoke();
        inv.setVariableName("test");
        inv.setCharset(StandardCharsets.UTF_8.name());
        inv.setParameters(params(param("first", "AAA"), param("second", "BBB")));

        inv.setScript(
                ZestActionInvokeUnitTest.class.getResource("/data/simple-script.js").getPath());

        String str = ZestJSON.toString(inv);

        ZestActionInvoke inv2 = (ZestActionInvoke) ZestJSON.fromString(str);

        assertEquals(inv.getElementType(), inv2.getElementType());
        assertEquals(inv.getVariableName(), inv2.getVariableName());
        assertEquals(inv.getScript(), inv2.getScript());
        assertEquals(inv.getCharset(), inv2.getCharset());
        assertEquals(inv.getParameters().size(), inv2.getParameters().size());
        for (int i = 0; i < inv.getParameters().size(); i++) {
            assertEquals(inv.getParameters().get(i).length, inv2.getParameters().get(i).length);
            for (int j = 0; j < inv.getParameters().get(i).length; j++) {
                assertEquals(inv.getParameters().get(i)[j], inv2.getParameters().get(i)[j]);
            }
        }
    }

    private static String[] param(String parameterName, String parameterValue) {
        return new String[] {parameterName, parameterValue};
    }

    private static List<String[]> params(String[]... parameters) {
        if (parameters == null || parameters.length == 0) {
            return Collections.emptyList();
        }
        return Arrays.asList(parameters);
    }
}
