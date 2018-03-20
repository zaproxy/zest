/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.test.v1;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.mozilla.zest.core.v1.ZestVariables;

/** Unit test for {@link ZestVariables}. */
public class ZestVariablesUnitTest {

    private static final String VAR_NAME = "name";
    private static final String VAR_VALUE = "value";

    @Test
    public void shouldHaveTokenStartByDefault() throws Exception {
        // Given
        ZestVariables zestVars = new ZestVariables();
        // When
        String tokenStart = zestVars.getTokenStart();
        // Then
        assertTrue("{{".equals(tokenStart));
    }

    @Test
    public void shouldHaveTokenEndByDefault() throws Exception {
        // Given
        ZestVariables zestVars = new ZestVariables();
        // When
        String tokenEnd = zestVars.getTokenEnd();
        // Then
        assertTrue("}}".equals(tokenEnd));
    }

    @Test
    public void shouldHaveNoVariablesByDefault() throws Exception {
        // Given
        ZestVariables zestVars = new ZestVariables();
        // When
        List<String[]> variables = zestVars.getVariables();
        // Then
        assertTrue(variables.isEmpty());
    }

    @Test
    public void shouldSetTokenStart() throws Exception {
        // Given
        ZestVariables zestVars = new ZestVariables();
        String tokenStart = "\\|";
        // When
        zestVars.setTokenStart(tokenStart);
        // Then
        assertTrue(zestVars.getTokenStart().equals(tokenStart));
    }

    @Test
    public void shouldSetTokenEnd() throws Exception {
        // Given
        ZestVariables zestVars = new ZestVariables();
        String tokenEnd = "|//";
        // When
        zestVars.setTokenEnd(tokenEnd);
        // Then
        assertTrue(zestVars.getTokenEnd().equals(tokenEnd));
    }

    @Test
    public void shouldAddVarWithNameAsValue() throws Exception {
        // Given
        ZestVariables zestVars = new ZestVariables();
        // When
        zestVars.addVariable(VAR_NAME);
        // Then
        assertTrue(zestVars.getVariables().size() == 1);
        assertTrue(VAR_NAME.equals(zestVars.getVariables().get(0)[0]));
        assertTrue(VAR_NAME.equals(zestVars.getVariables().get(0)[1]));
    }

    @Test
    public void shouldAddVarWithValue() throws Exception {
        // Given
        ZestVariables zestVars = new ZestVariables();
        // When
        zestVars.addVariable(VAR_NAME, VAR_VALUE);
        // Then
        assertTrue(zestVars.getVariables().size() == 1);
        assertTrue(VAR_NAME.equals(zestVars.getVariables().get(0)[0]));
        assertTrue(VAR_VALUE.equals(zestVars.getVariables().get(0)[1]));
    }

    @Test
    public void shouldAddVarWithNameAsValueIfValueIsNull() throws Exception {
        // Given
        ZestVariables zestVars = new ZestVariables();
        String value = null;
        // When
        zestVars.addVariable(VAR_NAME, value);
        // Then
        assertTrue(zestVars.getVariables().size() == 1);
        assertTrue(VAR_NAME.equals(zestVars.getVariables().get(0)[0]));
        assertTrue(VAR_NAME.equals(zestVars.getVariables().get(0)[1]));
    }

    @Test
    public void shouldNotAddVarIfAlreadyAdded() throws Exception {
        // Given
        ZestVariables zestVars = new ZestVariables();
        zestVars.addVariable(VAR_NAME);
        // When
        zestVars.addVariable(VAR_NAME);
        // Then
        assertTrue(zestVars.getVariables().size() == 1);
        assertTrue(VAR_NAME.equals(zestVars.getVariables().get(0)[0]));
        assertTrue(VAR_NAME.equals(zestVars.getVariables().get(0)[1]));
    }

    @Test
    public void shouldNotAddVarWithValueIfAlreadyAdded() throws Exception {
        // Given
        ZestVariables zestVars = new ZestVariables();
        zestVars.addVariable(VAR_NAME);
        // When
        zestVars.addVariable(VAR_NAME, VAR_VALUE);
        // Then
        assertTrue(zestVars.getVariables().size() == 1);
        assertTrue(VAR_NAME.equals(zestVars.getVariables().get(0)[0]));
        assertTrue(VAR_NAME.equals(zestVars.getVariables().get(0)[1]));
    }

    @Test
    public void shouldGetValueOfVariableAdded() throws Exception {
        // Given
        ZestVariables zestVars = new ZestVariables();
        zestVars.addVariable(VAR_NAME, VAR_VALUE);
        // When
        String valueObtained = zestVars.getVariable(VAR_NAME);
        // Then
        assertTrue(valueObtained.equals(VAR_VALUE));
    }

    @Test
    public void shouldGetNullValueIfVariableWasNotAdded() throws Exception {
        // Given
        ZestVariables zestVars = new ZestVariables();
        // When
        String valueObtained = zestVars.getVariable(VAR_NAME);
        // Then
        assertTrue(valueObtained == null);
    }

    @Test
    public void shouldSetVariables() throws Exception {
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
        assertTrue(zestVars.getVariables().size() == 3);
        assertTrue(varName1.equals(zestVars.getVariables().get(0)[0]));
        assertTrue(varValue1.equals(zestVars.getVariables().get(0)[1]));
        assertTrue(varName2.equals(zestVars.getVariables().get(1)[0]));
        assertTrue(varValue2.equals(zestVars.getVariables().get(1)[1]));
        assertTrue(varName3.equals(zestVars.getVariables().get(2)[0]));
        assertTrue(zestVars.getVariables().get(2)[1] == varValue3);
    }

    @Test
    public void shouldSetVariable() throws Exception {
        // Given
        ZestVariables zestVars = new ZestVariables();
        // When
        zestVars.setVariable(VAR_NAME, VAR_NAME);
        // Then
        assertTrue(zestVars.getVariables().size() == 1);
        assertTrue(VAR_NAME.equals(zestVars.getVariables().get(0)[0]));
        assertTrue(VAR_NAME.equals(zestVars.getVariables().get(0)[1]));
    }

    @Test
    public void shouldSetVariableWithNullName() throws Exception {
        // Given
        ZestVariables zestVars = new ZestVariables();
        String name = null;
        // When
        zestVars.setVariable(name, VAR_VALUE);
        // Then
        assertTrue(zestVars.getVariables().size() == 1);
        assertTrue(zestVars.getVariables().get(0)[0] == null);
        assertTrue(zestVars.getVariables().get(0)[1] == VAR_VALUE);
    }

    @Test
    public void shouldSetVariableWithNullValue() throws Exception {
        // Given
        ZestVariables zestVars = new ZestVariables();
        String value = null;
        // When
        zestVars.setVariable(VAR_NAME, value);
        // Then
        assertTrue(zestVars.getVariables().size() == 1);
        assertTrue(VAR_NAME.equals(zestVars.getVariables().get(0)[0]));
        assertTrue(zestVars.getVariables().get(0)[1] == value);
    }

    @Test
    public void shouldAddVariables() throws Exception {
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
        assertTrue(zestVars.getVariables().size() == 3);
        assertTrue(zestVars.getVariables().get(0)[0] == varName1);
        assertTrue(varValue1.equals(zestVars.getVariables().get(0)[1]));
        assertTrue(varName3.equals(zestVars.getVariables().get(1)[0]));
        assertTrue(zestVars.getVariables().get(1)[1] == varValue3);
        assertTrue(varName2.equals(zestVars.getVariables().get(2)[0]));
        assertTrue(zestVars.getVariables().get(2)[1] == varValue2);
    }

    @Test
    public void shouldReturnNullStringIfReplacingVariablesInNullString() throws Exception {
        // Given
        ZestVariables zestVars = new ZestVariables();
        // When
        String finalString = zestVars.replaceInString(null, false);
        // Then
        assertTrue(finalString == null);
    }

    @Test
    public void shouldReplaceVariablesInString() throws Exception {
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
        assertTrue("0 < 1 != []".equals(finalString));
    }

    @Test
    public void shouldReplaceVariablesInVariablesInString() throws Exception {
        // Given
        ZestVariables zestVars = new ZestVariables();
        zestVars.setVariable("Var1", "-1 < " + token(zestVars, "Var2"));
        zestVars.setVariable("Var2", token(zestVars, "Var3"));
        zestVars.setVariable("Var3", "1");
        String string = token(zestVars, "Var1") + " <= " + token(zestVars, "Var3");
        // When
        String finalString = zestVars.replaceInString(string, false);
        // Then
        assertTrue("-1 < 1 <= 1".equals(finalString));
    }

    @Test
    public void shouldReplaceLoopingVariablesInVariablesInString() throws Exception {
        // Given
        ZestVariables zestVars = new ZestVariables();
        zestVars.setVariable("Var1", "No Loop: " + token(zestVars, "Var2"));
        zestVars.setVariable("Var2", "[" + token(zestVars, "Var3") + "]");
        zestVars.setVariable("Var3", token(zestVars, "Var2"));
        String string = token(zestVars, "Var1") + " | " + token(zestVars, "Var3");
        // When
        String finalString = zestVars.replaceInString(string, false);
        // Then
        assertTrue(
                ("No Loop: [" + token(zestVars, "Var2") + "] | " + token(zestVars, "Var2"))
                        .equals(finalString));
    }

    @Test
    public void shouldReplaceEncodedVariablesInString() throws Exception {
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
        assertTrue("0 < 1 != []".equals(finalString));
    }

    @Test
    public void shouldReplaceVariablesInEncodedVariablesInString() throws Exception {
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
        assertTrue("-1 < 1 <= 1".equals(finalString));
    }

    @Test
    public void shouldReplaceLoopingVariablesInEncodedVariablesInString() throws Exception {
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
        assertTrue(
                ("No Loop: ["
                                + token(zestVars, "Var2")
                                + "] | "
                                + urlencoded(token(zestVars, "Var3")))
                        .equals(finalString));
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
