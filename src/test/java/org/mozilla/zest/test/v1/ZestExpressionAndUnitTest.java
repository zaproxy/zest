/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.test.v1;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;
import org.mozilla.zest.core.v1.ZestExpressionAnd;

public class ZestExpressionAndUnitTest {

    @Test
    public void testDeepCopy() {
        ZestExpressionAnd and = new ZestExpressionAnd(null);
        and.deepCopy();
    }

    @Ignore
    public void testZestExpressionAndZestConditionalElement() {
        fail("Not yet implemented");
    }

    @Ignore
    public void testZestExpressionAndZestConditionalElementListOfZestConditionalElement() {
        fail("Not yet implemented");
    }

    @Ignore
    public void testEvaluate() {
        fail("Not yet implemented");
    }

    @Ignore
    public void testZestExpressionZestConditionalElement() {
        fail("Not yet implemented");
    }

    @Ignore
    public void testZestExpressionZestConditionalElementListOfZestConditionalElement() {
        fail("Not yet implemented");
    }

    @Ignore
    public void testGetChildrenCondition() {
        fail("Not yet implemented");
    }

    @Ignore
    public void testIsLeaf() {
        fail("Not yet implemented");
    }

    @Ignore
    public void testIsRoot() {
        fail("Not yet implemented");
    }

    @Ignore
    public void testAddChildConditionZestConditionalElement() {
        fail("Not yet implemented");
    }

    @Ignore
    public void testAddChildConditionZestConditionalElementInt() {
        fail("Not yet implemented");
    }

    @Ignore
    public void testGetChild() {
        fail("Not yet implemented");
    }

    @Ignore
    public void testSetChildrenCondition() {
        fail("Not yet implemented");
    }

    @Test
    public void testIsNot() {
        ZestExpressionAnd and = new ZestExpressionAnd(null);
        assertFalse(and.isInverse());
    }

    @Test
    public void testSetNot() {
        ZestExpressionAnd and = new ZestExpressionAnd(null);
        assertFalse(and.isInverse());
        and.setInverse(true);
        assertTrue(and.isInverse());
    }
}
