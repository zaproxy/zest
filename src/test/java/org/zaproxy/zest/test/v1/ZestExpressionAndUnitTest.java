/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.zaproxy.zest.core.v1.ZestExpressionAnd;

class ZestExpressionAndUnitTest {

    @Test
    void testDeepCopy() {
        ZestExpressionAnd and = new ZestExpressionAnd(null);
        and.deepCopy();
    }

    @Disabled
    void testZestExpressionAndZestConditionalElement() {
        fail("Not yet implemented");
    }

    @Disabled
    void testZestExpressionAndZestConditionalElementListOfZestConditionalElement() {
        fail("Not yet implemented");
    }

    @Disabled
    void testEvaluate() {
        fail("Not yet implemented");
    }

    @Disabled
    void testZestExpressionZestConditionalElement() {
        fail("Not yet implemented");
    }

    @Disabled
    void testZestExpressionZestConditionalElementListOfZestConditionalElement() {
        fail("Not yet implemented");
    }

    @Disabled
    void testGetChildrenCondition() {
        fail("Not yet implemented");
    }

    @Disabled
    void testIsLeaf() {
        fail("Not yet implemented");
    }

    @Disabled
    void testIsRoot() {
        fail("Not yet implemented");
    }

    @Disabled
    void testAddChildConditionZestConditionalElement() {
        fail("Not yet implemented");
    }

    @Disabled
    void testAddChildConditionZestConditionalElementInt() {
        fail("Not yet implemented");
    }

    @Disabled
    void testGetChild() {
        fail("Not yet implemented");
    }

    @Disabled
    void testSetChildrenCondition() {
        fail("Not yet implemented");
    }

    @Test
    void testIsNot() {
        ZestExpressionAnd and = new ZestExpressionAnd(null);
        assertFalse(and.isInverse());
    }

    @Test
    void testSetNot() {
        ZestExpressionAnd and = new ZestExpressionAnd(null);
        assertFalse(and.isInverse());
        and.setInverse(true);
        assertTrue(and.isInverse());
    }
}
