/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.zaproxy.zest.core.v1.ZestExpressionIsInteger;

/** */
class ZestExpressionIsIntegerUnitTest {

    @Test
    void testZestExpressionIsIntegerBadVar() {
        ZestExpressionIsInteger ast = new ZestExpressionIsInteger();
        TestRuntime rt = new TestRuntime();
        ast.setVariableName("aaa");
        assertFalse(ast.evaluate(rt));
    }

    @Test
    void testZestExpressionIsIntegerTrue() {
        ZestExpressionIsInteger ast = new ZestExpressionIsInteger();
        TestRuntime rt = new TestRuntime();
        rt.setVariable("aaa", "12");
        ast.setVariableName("aaa");
        assertTrue(ast.evaluate(rt));
    }

    @Test
    void testZestExpressionIsIntegerFalse() {
        ZestExpressionIsInteger ast = new ZestExpressionIsInteger();
        TestRuntime rt = new TestRuntime();
        rt.setVariable("aaa", " 12b ");
        ast.setVariableName("aaa");
        assertFalse(ast.evaluate(rt));
    }

    @Test
    void testZestExpressionIsIntegerCopy() {
        ZestExpressionIsInteger ast = new ZestExpressionIsInteger("aaa", true);
        ZestExpressionIsInteger ast2 = ast.deepCopy();
        assertEquals(ast.getVariableName(), ast2.getVariableName());
        assertEquals(ast.isInverse(), ast2.isInverse());
    }
}
