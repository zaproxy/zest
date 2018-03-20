/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.test.v1;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mozilla.zest.core.v1.ZestAssignCalc;
import org.mozilla.zest.core.v1.ZestAssignFailException;

/** Unit test for {@code ZestAssignCalc}. */
public class ZestAssignCalcUnitTest {

    @Test
    public void shouldBePassiveZestStatement() throws Exception {
        // Given / When
        ZestAssignCalc assignCalc = new ZestAssignCalc();
        // Then
        assertTrue(assignCalc.isPassive());
    }

    @Test
    public void shouldHaveNoVariableNameByDefault() throws Exception {
        // Given / When
        ZestAssignCalc assignCalc = new ZestAssignCalc();
        // Then
        assertTrue(assignCalc.getVariableName() == null);
    }

    @Test
    public void shouldBeEnabledByDefault() throws Exception {
        // Given / When
        ZestAssignCalc assignCalc = new ZestAssignCalc();
        // Then
        assertTrue(assignCalc.isEnabled());
    }

    @Test
    public void shouldHaveNoOperandsNorOperationByDefault() throws Exception {
        // Given / When
        ZestAssignCalc assignCalc = new ZestAssignCalc();
        // Then
        assertTrue(assignCalc.getOperandA() == null);
        assertTrue(assignCalc.getOperation() == null);
        assertTrue(assignCalc.getOperandB() == null);
    }

    @Test
    public void shouldConstructWithVariableName() throws Exception {
        // Given
        String variableName = "Var1";
        // When
        ZestAssignCalc assignCalc = new ZestAssignCalc(variableName);
        // Then
        assertTrue(variableName.equals(assignCalc.getVariableName()));
    }

    @Test
    public void shouldConstructWithVariableNameOperandsAndOperation() throws Exception {
        // Given
        String variableName = "Var2";
        String operandA = "1";
        String operation = ZestAssignCalc.OPERAND_ADD;
        String operandB = "2";
        // When
        ZestAssignCalc assignCalc = new ZestAssignCalc(variableName, operandA, operation, operandB);
        // Then
        assertTrue(variableName.equals(assignCalc.getVariableName()));
        assertTrue(operandA.equals(assignCalc.getOperandA()));
        assertTrue(operation.equals(assignCalc.getOperation()));
        assertTrue(operandB.equals(assignCalc.getOperandB()));
    }

    @Test
    public void shouldSetVariableName() throws Exception {
        // Given
        String variableName = "Var3";
        ZestAssignCalc assignCalc = new ZestAssignCalc();
        // When
        assignCalc.setVariableName(variableName);
        // Then
        assertTrue(variableName.equals(assignCalc.getVariableName()));
    }

    @Test
    public void shouldSetNullVariableName() throws Exception {
        // Given
        String variableName = null;
        ZestAssignCalc assignCalc = new ZestAssignCalc();
        // When
        assignCalc.setVariableName(variableName);
        // Then
        assertTrue(variableName == assignCalc.getVariableName());
    }

    @Test
    public void shouldSetEnabledState() throws Exception {
        // Given
        boolean enabled = false;
        ZestAssignCalc assignCalc = new ZestAssignCalc();
        // When
        assignCalc.setEnabled(enabled);
        // Then
        assertTrue(enabled == assignCalc.isEnabled());
    }

    @Test
    public void shouldSetOperandA() throws Exception {
        // Given
        String operandA = "3";
        ZestAssignCalc assignCalc = new ZestAssignCalc();
        // When
        assignCalc.setOperandA(operandA);
        // Then
        assertTrue(operandA.equals(assignCalc.getOperandA()));
    }

    @Test
    public void shouldSetKnownOperation() throws Exception {
        // Given
        String operation = ZestAssignCalc.OPERAND_SUBTRACT;
        ZestAssignCalc assignCalc = new ZestAssignCalc();
        // When
        assignCalc.setOperation(operation);
        // Then
        assertTrue(operation.equals(assignCalc.getOperation()));
    }

    @Test
    public void shouldSetUnknownOperation() throws Exception {
        // Given
        String operation = "unknown";
        ZestAssignCalc assignCalc = new ZestAssignCalc();
        // When
        assignCalc.setOperation(operation);
        // Then
        assertTrue(operation.equals(assignCalc.getOperation()));
    }

    @Test
    public void shouldSetOperandB() throws Exception {
        // Given
        String operandB = "4";
        ZestAssignCalc assignCalc = new ZestAssignCalc();
        // When
        assignCalc.setOperandB(operandB);
        // Then
        assertTrue(operandB.equals(assignCalc.getOperandB()));
    }

    @Test
    public void shouldDeepCopy() throws Exception {
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
        assertTrue(variableName.equals(copy.getVariableName()));
        assertTrue(operandA.equals(copy.getOperandA()));
        assertTrue(operation.equals(copy.getOperation()));
        assertTrue(operandB.equals(copy.getOperandB()));
        assertTrue(enabled == copy.isEnabled());
    }

    @Test(expected = ZestAssignFailException.class)
    public void shouldFailTheAssignWithoutOperandA() throws Exception {
        // Given
        ZestAssignCalc assignCalc =
                new ZestAssignCalc("Var", null, ZestAssignCalc.OPERAND_ADD, "2");
        // When
        assignCalc.assign(null, new TestRuntime());
        // Then = ZestAssignFailException
    }

    @Test(expected = ZestAssignFailException.class)
    public void shouldFailTheAssignWithEmptyOperandA() throws Exception {
        // Given
        ZestAssignCalc assignCalc = new ZestAssignCalc("Var", "", ZestAssignCalc.OPERAND_ADD, "2");
        // When
        assignCalc.assign(null, new TestRuntime());
        // Then = ZestAssignFailException
    }

    @Test(expected = ZestAssignFailException.class)
    public void shouldFailTheAssignWithNonNumericOperandA() throws Exception {
        // Given
        ZestAssignCalc assignCalc =
                new ZestAssignCalc("Var", "NotANumber", ZestAssignCalc.OPERAND_ADD, "2");
        // When
        assignCalc.assign(null, new TestRuntime());
        // Then = ZestAssignFailException
    }

    @Test(expected = ZestAssignFailException.class)
    public void shouldFailTheAssignWithoutOperandB() throws Exception {
        // Given
        ZestAssignCalc assignCalc =
                new ZestAssignCalc("Var", "1", ZestAssignCalc.OPERAND_ADD, null);
        // When
        assignCalc.assign(null, new TestRuntime());
        // Then = ZestAssignFailException
    }

    @Test(expected = ZestAssignFailException.class)
    public void shouldFailTheAssignWithEmptyOperandB() throws Exception {
        // Given
        ZestAssignCalc assignCalc = new ZestAssignCalc("Var", "1", ZestAssignCalc.OPERAND_ADD, "");
        // When
        assignCalc.assign(null, new TestRuntime());
        // Then = ZestAssignFailException
    }

    @Test(expected = ZestAssignFailException.class)
    public void shouldFailTheAssignWithNonNumericOperandB() throws Exception {
        // Given
        ZestAssignCalc assignCalc =
                new ZestAssignCalc("Var", "1", ZestAssignCalc.OPERAND_ADD, "NotANumber");
        // When
        assignCalc.assign(null, new TestRuntime());
        // Then = ZestAssignFailException
    }

    @Test(expected = ZestAssignFailException.class)
    public void shouldFailTheAssignWithoutOperation() throws Exception {
        // Given
        ZestAssignCalc assignCalc = new ZestAssignCalc("Var", "1", null, "2");
        // When
        assignCalc.assign(null, new TestRuntime());
        // Then = ZestAssignFailException
    }

    @Test(expected = ZestAssignFailException.class)
    public void shouldFailTheAssignWithEmptyOperation() throws Exception {
        // Given
        ZestAssignCalc assignCalc = new ZestAssignCalc("Var", "1", "", "2");
        // When
        assignCalc.assign(null, new TestRuntime());
        // Then = ZestAssignFailException
    }

    @Test
    public void shouldAddAssign() throws Exception {
        // Given
        ZestAssignCalc assignCalc = new ZestAssignCalc("Var", "1", ZestAssignCalc.OPERAND_ADD, "2");
        // When
        String result = assignCalc.assign(null, new TestRuntime());
        // Then
        assertTrue("3".equals(result));
    }

    @Test
    public void shouldSubtractAssign() throws Exception {
        // Given
        ZestAssignCalc assignCalc =
                new ZestAssignCalc("Var", "2", ZestAssignCalc.OPERAND_SUBTRACT, "1");
        // When
        String result = assignCalc.assign(null, new TestRuntime());
        // Then
        assertTrue("1".equals(result));
    }

    @Test
    public void shouldMinusAssign() throws Exception {
        // Given
        ZestAssignCalc assignCalc =
                new ZestAssignCalc("Var", "4", ZestAssignCalc.OPERAND_MULTIPLY, "2");
        // When
        String result = assignCalc.assign(null, new TestRuntime());
        // Then
        assertTrue("8".equals(result));
    }

    @Test
    public void shouldDivideAssign() throws Exception {
        // Given
        ZestAssignCalc assignCalc =
                new ZestAssignCalc("Var", "6", ZestAssignCalc.OPERAND_DIVIDE, "2");
        // When
        String result = assignCalc.assign(null, new TestRuntime());
        // Then
        assertTrue("3".equals(result));
    }

    @Test
    public void shouldReplaceVariableInOperandA() throws Exception {
        // Given
        TestRuntime runtime = new TestRuntime();
        runtime.setVariable("A", "4");
        ZestAssignCalc assignCalc =
                new ZestAssignCalc("Var", "{{A}}", ZestAssignCalc.OPERAND_ADD, "2");
        // When
        String result = assignCalc.assign(null, runtime);
        // Then
        assertTrue("6".equals(result));
    }

    @Test
    public void shouldReplaceVariableInOperandB() throws Exception {
        // Given
        TestRuntime runtime = new TestRuntime();
        runtime.setVariable("B", "4");
        ZestAssignCalc assignCalc =
                new ZestAssignCalc("Var", "2", ZestAssignCalc.OPERAND_ADD, "{{B}}");
        // When
        String result = assignCalc.assign(null, runtime);
        // Then
        assertTrue("6".equals(result));
    }

    @Test
    public void shouldConvertResultIntoInteger() throws Exception {
        // Given
        ZestAssignCalc assignCalc =
                new ZestAssignCalc("Var", "1", ZestAssignCalc.OPERAND_DIVIDE, "2");
        // When
        String result = assignCalc.assign(null, new TestRuntime());
        // Then
        assertTrue("0".equals(result));
    }
}
