/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.zaproxy.zest.core.v1.ZestVariables;

/** Unit test for {@link ZestVariables}. */
class ZestVariablesUnitTest {

    private static final String VAR_NAME = "name";
    private static final String VAR_VALUE = "value";

    @Test
    void shouldHaveTokenStartByDefault() {
        // Given
        ZestVariables zestVars = new ZestVariables();
        // When
        String tokenStart = zestVars.getTokenStart();
        // Then
        assertEquals("{{", tokenStart);
    }

    @Test
    void shouldHaveTokenEndByDefault() {
        // Given
        ZestVariables zestVars = new ZestVariables();
        // When
        String tokenEnd = zestVars.getTokenEnd();
        // Then
        assertEquals("}}", tokenEnd);
    }

    @Test
    void shouldHaveNoVariablesByDefault() {
        // Given
        ZestVariables zestVars = new ZestVariables();
        // When
        List<String[]> variables = zestVars.getVariables();
        // Then
        assertTrue(variables.isEmpty());
    }

    @Test
    void shouldSetTokenStart() {
        // Given
        ZestVariables zestVars = new ZestVariables();
        String tokenStart = "\\|";
        // When
        zestVars.setTokenStart(tokenStart);
        // Then
        assertEquals(zestVars.getTokenStart(), tokenStart);
    }

    @Test
    void shouldSetTokenEnd() {
        // Given
        ZestVariables zestVars = new ZestVariables();
        String tokenEnd = "|//";
        // When
        zestVars.setTokenEnd(tokenEnd);
        // Then
        assertEquals(zestVars.getTokenEnd(), tokenEnd);
    }

    @Test
    void shouldAddVarWithNameAsValue() {
        // Given
        ZestVariables zestVars = new ZestVariables();
        // When
        zestVars.addVariable(VAR_NAME);
        // Then
        assertEquals(zestVars.getVariables().size(), 1);
        assertEquals(VAR_NAME, zestVars.getVariables().get(0)[0]);
        assertEquals(VAR_NAME, zestVars.getVariables().get(0)[1]);
    }

    @Test
    void shouldAddVarWithValue() {
        // Given
        ZestVariables zestVars = new ZestVariables();
        // When
        zestVars.addVariable(VAR_NAME, VAR_VALUE);
        // Then
        assertEquals(zestVars.getVariables().size(), 1);
        assertEquals(VAR_NAME, zestVars.getVariables().get(0)[0]);
        assertEquals(VAR_VALUE, zestVars.getVariables().get(0)[1]);
    }

    @Test
    void shouldAddVarWithNameAsValueIfValueIsNull() {
        // Given
        ZestVariables zestVars = new ZestVariables();
        String value = null;
        // When
        zestVars.addVariable(VAR_NAME, value);
        // Then
        assertEquals(zestVars.getVariables().size(), 1);
        assertEquals(VAR_NAME, zestVars.getVariables().get(0)[0]);
        assertEquals(VAR_NAME, zestVars.getVariables().get(0)[1]);
    }

    @Test
    void shouldNotAddVarIfAlreadyAdded() {
        // Given
        ZestVariables zestVars = new ZestVariables();
        zestVars.addVariable(VAR_NAME);
        // When
        zestVars.addVariable(VAR_NAME);
        // Then
        assertEquals(zestVars.getVariables().size(), 1);
        assertEquals(VAR_NAME, zestVars.getVariables().get(0)[0]);
        assertEquals(VAR_NAME, zestVars.getVariables().get(0)[1]);
    }

    @Test
    void shouldNotAddVarWithValueIfAlreadyAdded() {
        // Given
        ZestVariables zestVars = new ZestVariables();
        zestVars.addVariable(VAR_NAME);
        // When
        zestVars.addVariable(VAR_NAME, VAR_VALUE);
        // Then
        assertEquals(zestVars.getVariables().size(), 1);
        assertEquals(VAR_NAME, zestVars.getVariables().get(0)[0]);
        assertEquals(VAR_NAME, zestVars.getVariables().get(0)[1]);
    }

    @Test
    void shouldGetValueOfVariableAdded() {
        // Given
        ZestVariables zestVars = new ZestVariables();
        zestVars.addVariable(VAR_NAME, VAR_VALUE);
        // When
        String valueObtained = zestVars.getVariable(VAR_NAME);
        // Then
        assertEquals(valueObtained, VAR_VALUE);
    }

    @Test
    void shouldGetNullValueIfVariableWasNotAdded() {
        // Given
        ZestVariables zestVars = new ZestVariables();
        // When
        String valueObtained = zestVars.getVariable(VAR_NAME);
        // Then
        assertNull(valueObtained);
    }

    @Test
    void shouldSetVariables() {
        // Given
        ZestVariables zestVars = new ZestVariables();
        Map<String, String> vars = new HashMap<>();
        String varName1 = "Var1";
        String varValue1 = "Value1";
        vars.put(varName1, varValue1);
        String varName2 = "Var2";
        String varValue2 = "Value2";
        vars.put(varName2, varValue2);
        String varName3 = "Var3";
        String varValue3 = null;
        vars.put(varName3, varValue3);
        // When
        zestVars.setVariable(vars);
        // Then
        assertEquals(zestVars.getVariables().size(), 3);
        assertEquals(varName1, zestVars.getVariables().get(0)[0]);
        assertEquals(varValue1, zestVars.getVariables().get(0)[1]);
        assertEquals(varName2, zestVars.getVariables().get(1)[0]);
        assertEquals(varValue2, zestVars.getVariables().get(1)[1]);
        assertEquals(varName3, zestVars.getVariables().get(2)[0]);
        assertEquals(zestVars.getVariables().get(2)[1], varValue3);
    }

    @Test
    void shouldSetVariable() {
        // Given
        ZestVariables zestVars = new ZestVariables();
        // When
        zestVars.setVariable(VAR_NAME, VAR_NAME);
        // Then
        assertEquals(zestVars.getVariables().size(), 1);
        assertEquals(VAR_NAME, zestVars.getVariables().get(0)[0]);
        assertEquals(VAR_NAME, zestVars.getVariables().get(0)[1]);
    }

    @Test
    void shouldSetVariableWithNullName() {
        // Given
        ZestVariables zestVars = new ZestVariables();
        String name = null;
        // When
        zestVars.setVariable(name, VAR_VALUE);
        // Then
        assertEquals(zestVars.getVariables().size(), 1);
        assertNull(zestVars.getVariables().get(0)[0]);
        assertEquals(zestVars.getVariables().get(0)[1], VAR_VALUE);
    }

    @Test
    void shouldSetVariableWithNullValue() {
        // Given
        ZestVariables zestVars = new ZestVariables();
        String value = null;
        // When
        zestVars.setVariable(VAR_NAME, value);
        // Then
        assertEquals(zestVars.getVariables().size(), 1);
        assertEquals(VAR_NAME, zestVars.getVariables().get(0)[0]);
        assertEquals(zestVars.getVariables().get(0)[1], value);
    }

    @Test
    void shouldAddVariables() {
        // Given
        ZestVariables zestVars = new ZestVariables();
        Map<String, String> vars = new HashMap<>();
        String varName1 = null;
        String varValue1 = "Value1";
        vars.put(varName1, varValue1);
        String varName2 = "Var2";
        String varValue2 = "Value2";
        vars.put(varName2, varValue2);
        String varName3 = "Var3";
        String varValue3 = null;
        vars.put(varName3, varValue3);
        zestVars.addVariable(varName1, "123");
        zestVars.addVariable(varName3, "abc");
        // When
        zestVars.addVariables(vars);
        // Then
        assertEquals(zestVars.getVariables().size(), 3);
        assertEquals(zestVars.getVariables().get(0)[0], varName1);
        assertEquals(varValue1, zestVars.getVariables().get(0)[1]);
        assertEquals(varName3, zestVars.getVariables().get(1)[0]);
        assertEquals(zestVars.getVariables().get(1)[1], varValue3);
        assertEquals(varName2, zestVars.getVariables().get(2)[0]);
        assertEquals(zestVars.getVariables().get(2)[1], varValue2);
    }

    @Test
    void shouldReturnNullStringIfReplacingVariablesInNullString() {
        // Given
        ZestVariables zestVars = new ZestVariables();
        // When
        String finalString = zestVars.replaceInString(null, false);
        // Then
        assertNull(finalString);
    }

    @Test
    void shouldReplaceVariablesInString() {
        // Given
        ZestVariables zestVars = new ZestVariables();
        zestVars.setVariable("Var1", "0");
        zestVars.setVariable(null, "1");
        zestVars.setVariable("Var3", null);
        String string =
                token(zestVars, "Var1")
                        + " < "
                        + token(zestVars, null)
                        + " != ["
                        + token(zestVars, "Var3")
                        + "]";
        // When
        String finalString = zestVars.replaceInString(string, false);
        // Then
        assertEquals("0 < 1 != []", finalString);
    }

    @Test
    void shouldReplaceVariablesInVariablesInString() {
        // Given
        ZestVariables zestVars = new ZestVariables();
        zestVars.setVariable("Var1", "-1 < " + token(zestVars, "Var2"));
        zestVars.setVariable("Var2", token(zestVars, "Var3"));
        zestVars.setVariable("Var3", "1");
        String string = token(zestVars, "Var1") + " <= " + token(zestVars, "Var3");
        // When
        String finalString = zestVars.replaceInString(string, false);
        // Then
        assertEquals("-1 < 1 <= 1", finalString);
    }

    @Test
    void shouldReplaceLoopingVariablesInVariablesInString() {
        // Given
        ZestVariables zestVars = new ZestVariables();
        zestVars.setVariable("Var1", "No Loop: " + token(zestVars, "Var2"));
        zestVars.setVariable("Var2", "[" + token(zestVars, "Var3") + "]");
        zestVars.setVariable("Var3", token(zestVars, "Var2"));
        String string = token(zestVars, "Var1") + " | " + token(zestVars, "Var3");
        // When
        String finalString = zestVars.replaceInString(string, false);
        // Then
        assertEquals(
                ("No Loop: [" + token(zestVars, "Var2") + "] | " + token(zestVars, "Var2")),
                finalString);
    }

    @Test
    void shouldReplaceEncodedVariablesInString() {
        // Given
        ZestVariables zestVars = new ZestVariables();
        zestVars.setVariable("Var%", "0");
        zestVars.setVariable(null, "1");
        zestVars.setVariable("Var&", null);
        String string =
                urlencoded(token(zestVars, "Var%"))
                        + " < "
                        + token(zestVars, null)
                        + " != ["
                        + urlencoded(token(zestVars, "Var&"))
                        + "]";
        boolean encode = true;
        // When
        String finalString = zestVars.replaceInString(string, encode);
        // Then
        assertEquals("0 < 1 != []", finalString);
    }

    @Test
    void shouldReplaceVariablesInEncodedVariablesInString() {
        // Given
        ZestVariables zestVars = new ZestVariables();
        zestVars.setVariable("Var%", "-1 < " + token(zestVars, "Var&"));
        zestVars.setVariable("Var&", urlencoded(token(zestVars, "Var?")));
        zestVars.setVariable("Var?", "1");
        String string =
                urlencoded(token(zestVars, "Var%")) + " <= " + urlencoded(token(zestVars, "Var?"));
        boolean encode = true;
        // When
        String finalString = zestVars.replaceInString(string, encode);
        // Then
        assertEquals("-1 < 1 <= 1", finalString);
    }

    @Test
    void shouldReplaceLoopingVariablesInEncodedVariablesInString() {
        // Given
        ZestVariables zestVars = new ZestVariables();
        zestVars.setVariable("Var1", "No Loop: " + token(zestVars, "Var2"));
        zestVars.setVariable("Var2", "[" + token(zestVars, "Var3") + "]");
        zestVars.setVariable("Var3", token(zestVars, "Var2"));
        String string =
                urlencoded(token(zestVars, "Var1")) + " | " + urlencoded(token(zestVars, "Var3"));
        boolean encode = true;
        // When
        String finalString = zestVars.replaceInString(string, encode);
        // Then
        assertEquals(
                ("No Loop: ["
                        + token(zestVars, "Var2")
                        + "] | "
                        + urlencoded(token(zestVars, "Var3"))),
                finalString);
    }

    private static String token(ZestVariables zestVars, String string) {
        return zestVars.getTokenStart() + string + zestVars.getTokenEnd();
    }

    private static String urlencoded(String string) {
        try {
            return URLEncoder.encode(string, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
