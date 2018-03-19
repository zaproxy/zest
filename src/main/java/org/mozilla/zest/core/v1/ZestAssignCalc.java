/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

/**
 * The Class ZestAssignString assigns a string (which can include other variables) to the specified
 * variable.
 */
public class ZestAssignCalc extends ZestAssignment {

    public static final String OPERAND_ADD = "add";
    public static final String OPERAND_SUBTRACT = "subtract";
    public static final String OPERAND_MULTIPLY = "multiply";
    public static final String OPERAND_DIVIDE = "divide";

    private String operandA = null;
    private String operandB = null;
    private String operation = null;

    /** Instantiates a new zest assign random integer. */
    public ZestAssignCalc() {}

    /**
     * Instantiates a new zest assign random integer.
     *
     * @param variableName the variable name
     */
    public ZestAssignCalc(String variableName) {
        super(variableName);
    }

    /**
     * Instantiates a new zest assign random integer.
     *
     * @param variableName the variable name
     * @param operandA the right hand operand.
     * @param operation the operation.
     * @param operandB the left hand operand.
     */
    public ZestAssignCalc(String variableName, String operandA, String operation, String operandB) {
        super(variableName);
        this.operandA = operandA;
        this.operation = operation;
        this.operandB = operandB;
    }

    @Override
    public String assign(ZestResponse response, ZestRuntime runtime)
            throws ZestAssignFailException {
        int operA;
        int operB;
        try {
            operA = Integer.parseInt(runtime.replaceVariablesInString(this.operandA, false));
        } catch (NumberFormatException e) {
            throw new ZestAssignFailException(this, "operandA not a number");
        }
        try {
            operB = Integer.parseInt(runtime.replaceVariablesInString(this.operandB, false));
        } catch (NumberFormatException e) {
            throw new ZestAssignFailException(this, "operandB not a number");
        }
        if (OPERAND_ADD.equals(operation)) {
            return Integer.toString(operA + operB);
        } else if (OPERAND_SUBTRACT.equals(operation)) {
            return Integer.toString(operA - operB);
        } else if (OPERAND_MULTIPLY.equals(operation)) {
            return Integer.toString(operA * operB);
        } else if (OPERAND_DIVIDE.equals(operation)) {
            return Integer.toString(operA / operB);
        }
        throw new ZestAssignFailException(this, "Invalid operation");
    }

    @Override
    public ZestAssignCalc deepCopy() {
        ZestAssignCalc copy =
                new ZestAssignCalc(this.getVariableName(), operandA, operation, operandB);
        copy.setEnabled(this.isEnabled());
        return copy;
    }

    public String getOperandA() {
        return operandA;
    }

    public String getOperandB() {
        return operandB;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperandA(String operandA) {
        this.operandA = operandA;
    }

    public void setOperandB(String operandB) {
        this.operandB = operandB;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
