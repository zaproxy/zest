/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.zaproxy.zest.core.v1.ZestAssignCalc;
import org.zaproxy.zest.core.v1.ZestAssignFailException;

/** Unit test for {@code ZestAssignCalc}. */
class ZestAssignCalcUnitTest {

    @Test
    void shouldBePassiveZestStatement() {
        // Given / When
        ZestAssignCalc assignCalc = new ZestAssignCalc();
        // Then
        assertTrue(assignCalc.isPassive());
    }

    @Test
    void shouldHaveNoVariableNameByDefault() {
        // Given / When
        ZestAssignCalc assignCalc = new ZestAssignCalc();
        // Then
        assertNull(assignCalc.getVariableName());
    }

    @Test
    void shouldBeEnabledByDefault() {
        // Given / When
        ZestAssignCalc assignCalc = new ZestAssignCalc();
        // Then
        assertTrue(assignCalc.isEnabled());
    }

    @Test
    void shouldHaveNoOperandsNorOperationByDefault() {
        // Given / When
        ZestAssignCalc assignCalc = new ZestAssignCalc();
        // Then
        assertNull(assignCalc.getOperandA());
        assertNull(assignCalc.getOperation());
        assertNull(assignCalc.getOperandB());
    }

    @Test
    void shouldConstructWithVariableName() {
        // Given
        String variableName = "Var1";
        // When
        ZestAssignCalc assignCalc = new ZestAssignCalc(variableName);
        // Then
        assertEquals(variableName, assignCalc.getVariableName());
    }

    @Test
    void shouldConstructWithVariableNameOperandsAndOperation() {
        // Given
        String variableName = "Var2";
        String operandA = "1";
        String operation = ZestAssignCalc.OPERAND_ADD;
        String operandB = "2";
        // When
        ZestAssignCalc assignCalc = new ZestAssignCalc(variableName, operandA, operation, operandB);
        // Then
        assertEquals(variableName, assignCalc.getVariableName());
        assertEquals(operandA, assignCalc.getOperandA());
        assertEquals(operation, assignCalc.getOperation());
        assertEquals(operandB, assignCalc.getOperandB());
    }

    @Test
    void shouldSetVariableName() {
        // Given
        String variableName = "Var3";
        ZestAssignCalc assignCalc = new ZestAssignCalc();
        // When
        assignCalc.setVariableName(variableName);
        // Then
        assertEquals(variableName, assignCalc.getVariableName());
    }

    @Test
    void shouldSetNullVariableName() {
        // Given
        String variableName = null;
        ZestAssignCalc assignCalc = new ZestAssignCalc();
        // When
        assignCalc.setVariableName(variableName);
        // Then
        assertEquals(variableName, assignCalc.getVariableName());
    }

    @Test
    void shouldSetEnabledState() {
        // Given
        boolean enabled = false;
        ZestAssignCalc assignCalc = new ZestAssignCalc();
        // When
        assignCalc.setEnabled(enabled);
        // Then
        assertEquals(enabled, assignCalc.isEnabled());
    }

    @Test
    void shouldSetOperandA() {
        // Given
        String operandA = "3";
        ZestAssignCalc assignCalc = new ZestAssignCalc();
        // When
        assignCalc.setOperandA(operandA);
        // Then
        assertEquals(operandA, assignCalc.getOperandA());
    }

    @Test
    void shouldSetKnownOperation() {
        // Given
        String operation = ZestAssignCalc.OPERAND_SUBTRACT;
        ZestAssignCalc assignCalc = new ZestAssignCalc();
        // When
        assignCalc.setOperation(operation);
        // Then
        assertEquals(operation, assignCalc.getOperation());
    }

    @Test
    void shouldSetUnknownOperation() {
        // Given
        String operation = "unknown";
        ZestAssignCalc assignCalc = new ZestAssignCalc();
        // When
        assignCalc.setOperation(operation);
        // Then
        assertEquals(operation, assignCalc.getOperation());
    }

    @Test
    void shouldSetOperandB() {
        // Given
        String operandB = "4";
        ZestAssignCalc assignCalc = new ZestAssignCalc();
        // When
        assignCalc.setOperandB(operandB);
        // Then
        assertEquals(operandB, assignCalc.getOperandB());
    }

    @Test
    void shouldDeepCopy() {
        // Given
        String variableName = "Var4";
        String operandA = "5";
        String operation = ZestAssignCalc.OPERAND_MULTIPLY;
        String operandB = "6";
        boolean enabled = false;
        ZestAssignCalc assignCalc = new ZestAssignCalc(variableName, operandA, operation, operandB);
        assignCalc.setEnabled(enabled);
        // When
        ZestAssignCalc copy = assignCalc.deepCopy();
        // Then
        assertEquals(variableName, copy.getVariableName());
        assertEquals(operandA, copy.getOperandA());
        assertEquals(operation, copy.getOperation());
        assertEquals(operandB, copy.getOperandB());
        assertEquals(enabled, copy.isEnabled());
    }

    @Test
    void shouldFailTheAssignWithoutOperandA() throws Exception {
        // Given
        ZestAssignCalc assignCalc =
                new ZestAssignCalc("Var", null, ZestAssignCalc.OPERAND_ADD, "2");
        // When / Then
        assertThrows(
                ZestAssignFailException.class, () -> assignCalc.assign(null, new TestRuntime()));
    }

    @Test
    void shouldFailTheAssignWithEmptyOperandA() throws Exception {
        // Given
        ZestAssignCalc assignCalc = new ZestAssignCalc("Var", "", ZestAssignCalc.OPERAND_ADD, "2");
        // When / Then
        assertThrows(
                ZestAssignFailException.class, () -> assignCalc.assign(null, new TestRuntime()));
    }

    @Test
    void shouldFailTheAssignWithNonNumericOperandA() throws Exception {
        // Given
        ZestAssignCalc assignCalc =
                new ZestAssignCalc("Var", "NotANumber", ZestAssignCalc.OPERAND_ADD, "2");
        // When / Then
        assertThrows(
                ZestAssignFailException.class, () -> assignCalc.assign(null, new TestRuntime()));
    }

    @Test
    void shouldFailTheAssignWithoutOperandB() throws Exception {
        // Given
        ZestAssignCalc assignCalc =
                new ZestAssignCalc("Var", "1", ZestAssignCalc.OPERAND_ADD, null);
        // When / Then
        assertThrows(
                ZestAssignFailException.class, () -> assignCalc.assign(null, new TestRuntime()));
    }

    @Test
    void shouldFailTheAssignWithEmptyOperandB() throws Exception {
        // Given
        ZestAssignCalc assignCalc = new ZestAssignCalc("Var", "1", ZestAssignCalc.OPERAND_ADD, "");
        // When / Then
        assertThrows(
                ZestAssignFailException.class, () -> assignCalc.assign(null, new TestRuntime()));
    }

    @Test
    void shouldFailTheAssignWithNonNumericOperandB() throws Exception {
        // Given
        ZestAssignCalc assignCalc =
                new ZestAssignCalc("Var", "1", ZestAssignCalc.OPERAND_ADD, "NotANumber");
        // When / Then
        assertThrows(
                ZestAssignFailException.class, () -> assignCalc.assign(null, new TestRuntime()));
    }

    @Test
    void shouldFailTheAssignWithoutOperation() throws Exception {
        // Given
        ZestAssignCalc assignCalc = new ZestAssignCalc("Var", "1", null, "2");
        // When / Then
        assertThrows(
                ZestAssignFailException.class, () -> assignCalc.assign(null, new TestRuntime()));
    }

    @Test
    void shouldFailTheAssignWithEmptyOperation() throws Exception {
        // Given
        ZestAssignCalc assignCalc = new ZestAssignCalc("Var", "1", "", "2");
        // When / Then
        assertThrows(
                ZestAssignFailException.class, () -> assignCalc.assign(null, new TestRuntime()));
    }

    @Test
    void shouldAddAssign() throws Exception {
        // Given
        ZestAssignCalc assignCalc = new ZestAssignCalc("Var", "1", ZestAssignCalc.OPERAND_ADD, "2");
        // When
        String result = assignCalc.assign(null, new TestRuntime());
        // Then
        assertEquals("3", result);
    }

    @Test
    void shouldSubtractAssign() throws Exception {
        // Given
        ZestAssignCalc assignCalc =
                new ZestAssignCalc("Var", "2", ZestAssignCalc.OPERAND_SUBTRACT, "1");
        // When
        String result = assignCalc.assign(null, new TestRuntime());
        // Then
        assertEquals("1", result);
    }

    @Test
    void shouldMinusAssign() throws Exception {
        // Given
        ZestAssignCalc assignCalc =
                new ZestAssignCalc("Var", "4", ZestAssignCalc.OPERAND_MULTIPLY, "2");
        // When
        String result = assignCalc.assign(null, new TestRuntime());
        // Then
        assertEquals("8", result);
    }

    @Test
    void shouldDivideAssign() throws Exception {
        // Given
        ZestAssignCalc assignCalc =
                new ZestAssignCalc("Var", "6", ZestAssignCalc.OPERAND_DIVIDE, "2");
        // When
        String result = assignCalc.assign(null, new TestRuntime());
        // Then
        assertEquals("3", result);
    }

    @Test
    void shouldReplaceVariableInOperandA() throws Exception {
        // Given
        TestRuntime runtime = new TestRuntime();
        runtime.setVariable("A", "4");
        ZestAssignCalc assignCalc =
                new ZestAssignCalc("Var", "{{A}}", ZestAssignCalc.OPERAND_ADD, "2");
        // When
        String result = assignCalc.assign(null, runtime);
        // Then
        assertEquals("6", result);
    }

    @Test
    void shouldReplaceVariableInOperandB() throws Exception {
        // Given
        TestRuntime runtime = new TestRuntime();
        runtime.setVariable("B", "4");
        ZestAssignCalc assignCalc =
                new ZestAssignCalc("Var", "2", ZestAssignCalc.OPERAND_ADD, "{{B}}");
        // When
        String result = assignCalc.assign(null, runtime);
        // Then
        assertEquals("6", result);
    }

    @Test
    void shouldConvertResultIntoInteger() throws Exception {
        // Given
        ZestAssignCalc assignCalc =
                new ZestAssignCalc("Var", "1", ZestAssignCalc.OPERAND_DIVIDE, "2");
        // When
        String result = assignCalc.assign(null, new TestRuntime());
        // Then
        assertEquals("0", result);
    }
}
