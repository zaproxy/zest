/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.zaproxy.zest.core.v1.ZestConditional;
import org.zaproxy.zest.core.v1.ZestExpression;
import org.zaproxy.zest.core.v1.ZestExpressionAnd;
import org.zaproxy.zest.core.v1.ZestExpressionElement;
import org.zaproxy.zest.core.v1.ZestExpressionLength;
import org.zaproxy.zest.core.v1.ZestExpressionOr;
import org.zaproxy.zest.core.v1.ZestExpressionRegex;
import org.zaproxy.zest.core.v1.ZestExpressionResponseTime;
import org.zaproxy.zest.core.v1.ZestExpressionStatusCode;
import org.zaproxy.zest.core.v1.ZestExpressionURL;
import org.zaproxy.zest.core.v1.ZestResponse;
import org.zaproxy.zest.core.v1.ZestRuntime;
import org.zaproxy.zest.core.v1.ZestVariables;

/** */
class ZestStructuredExpressionUnitTest {
    @Test
    void testDeepCopySingleAndSameChildrenSize() {
        ZestExpressionAnd and = new ZestExpressionAnd();
        and.addChildCondition(new ZestExpressionStatusCode(100));
        and.addChildCondition(new ZestExpressionLength("response.body", 1, 1));
        ZestExpressionAnd copy = and.deepCopy();
        assertEquals(and.getChildrenCondition().size(), copy.getChildrenCondition().size());
    }

    @Test
    void testOrDeepCopySingleAndSameChildrenSize() {
        ZestExpressionOr or = new ZestExpressionOr();
        or.addChildCondition(new ZestExpressionStatusCode(100));
        or.addChildCondition(new ZestExpressionLength("response.body", 1, 1));
        ZestExpressionOr copy = or.deepCopy();
        assertEquals(or.getChildrenCondition().size(), copy.getChildrenCondition().size());
    }

    @Test
    void testDeepCopySingleAndSameChildrenClasses() {
        List<ZestExpressionElement> children = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            children.add(new ZestExpressionLength());
        }
        ZestExpressionAnd and = new ZestExpressionAnd(children);
        ZestExpressionAnd copy = and.deepCopy();
        for (int i = 0; i < children.size(); i++) {
            assertEquals(and.getChild(i).getClass(), copy.getChild(i).getClass());
        }
    }

    @Test
    void testOrDeepCopySingleAndSameChildrenClasses() {
        List<ZestExpressionElement> children = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            children.add(new ZestExpressionLength());
        }
        ZestExpressionOr or = new ZestExpressionOr(children);
        ZestExpressionOr copy = or.deepCopy();
        for (int i = 0; i < children.size(); i++) {
            assertEquals(or.getChild(i).getClass(), copy.getChild(i).getClass());
        }
    }

    @Test
    void testDeepCopyComplexCondition() {
        ZestExpressionAnd and1 = new ZestExpressionAnd();
        ZestExpressionAnd and2 = new ZestExpressionAnd();
        ZestExpressionAnd and3 = new ZestExpressionAnd();
        ZestExpressionLength leaf = new ZestExpressionLength();
        and3.addChildCondition(leaf);
        and2.addChildCondition(and3);
        and1.addChildCondition(and2);
        ZestExpressionAnd copy1 = and1.deepCopy();
        ZestExpressionAnd copy2 = (ZestExpressionAnd) copy1.getChild(0);
        ZestExpressionAnd copy3 = (ZestExpressionAnd) copy2.getChild(0);
        ZestExpressionLength copyLeaf = (ZestExpressionLength) copy3.getChild(0);
        assertTrue(
                and1.getClass() == copy1.getClass() // TODO to many booleans
                        && and2.getClass() == copy2.getClass()
                        && copy3.getClass() == and3.getClass()
                        && copyLeaf instanceof ZestExpressionLength
                        && copyLeaf.isLeaf());
    }

    @Test
    void testDeepCopyNoPointer() {
        LinkedList<ZestExpressionElement> children = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            children.add(new ZestExpressionStatusCode(i));
        }
        ZestExpressionAnd and = new ZestExpressionAnd(children);
        ZestExpressionAnd copy = and.deepCopy();
        and.removeChildCondition(0);
        assertEquals(
                and.getChildrenCondition().size() + 1,
                copy.getChildrenCondition().size()); // different size <=> no
        // pointers
    }

    @Test
    void testIsLeaf() { // TODO maybe an exception raises if a
        // StructuredExpr has no child
        ZestExpressionAnd and = new ZestExpressionAnd();
        ZestExpressionAnd andChild = new ZestExpressionAnd();
        ZestExpressionLength lengthChild = new ZestExpressionLength();
        and.addChildCondition(andChild);
        and.addChildCondition(lengthChild);
        assertTrue(!and.getChild(0).isLeaf() && and.getChild(1).isLeaf());
    }

    @Test
    void testRemoveFirstChild() {
        ZestExpressionAnd and = new ZestExpressionAnd();
        and.addChildCondition(new ZestExpressionLength());
        and.addChildCondition(new ZestExpressionStatusCode());
        and.removeChildCondition(0);
        assertTrue(and.getChild(0) instanceof ZestExpressionStatusCode);
    }

    @Test
    void testRemoveLastChild() {
        int childrenSize = 10; // initial size
        ZestExpressionAnd and = new ZestExpressionAnd();
        for (int i = 0; i < childrenSize - 1; i++) {
            and.addChildCondition(new ZestExpressionLength());
        }
        and.addChildCondition(new ZestExpressionStatusCode());
        and.removeChildCondition(childrenSize - 1); // removes last child
        if (and.getChildrenCondition().size() == childrenSize) fail();
        else assertTrue(and.getChild(childrenSize - 2) instanceof ZestExpressionLength); // last
        // of
        // the
        // remaining
    }

    @Test
    void testRemoveMiddleChild() {
        int half = 5;
        ZestExpressionAnd and = new ZestExpressionAnd();
        for (int i = 0; i < half; i++) {
            and.addChildCondition(new ZestExpressionLength());
        }
        and.addChildCondition(new ZestExpressionStatusCode());
        for (int i = 0; i < half; i++) {
            and.addChildCondition(new ZestExpressionLength());
        }
        boolean middleElementIsCode =
                and.getChild(half).getClass().equals(ZestExpressionStatusCode.class);
        if (!middleElementIsCode) fail();
        and.removeChildCondition(half);
        assertTrue(and.getChild(half) instanceof ZestExpressionLength);
    }

    @Test
    void testRemoveAllChildren() {
        ZestExpressionAnd and = new ZestExpressionAnd();
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) and.addChildCondition(new ZestExpressionLength());
            else and.addChildCondition(new ZestExpressionStatusCode());
        }
        LinkedList<ZestExpressionElement> toRemove = new LinkedList<>();
        for (int i = 0; i < 10; i += 2) {
            toRemove.add(and.getChild(i));
        }
        if (!and.removeAllChildren(toRemove)) fail();
        else {
            for (int i = 0; i < and.getChildrenCondition().size(); i++) {
                assertEquals(and.getChild(i).getClass(), ZestExpressionStatusCode.class);
            }
        }
    }

    @Test
    void testAddChildConditionZestExpressionElementInt() {
        ZestExpressionAnd and = new ZestExpressionAnd();
        for (int i = 0; i < 10; i++) and.addChildCondition(new ZestExpressionLength());
        Random random = new Random();
        int pos = random.nextInt(10);
        and.addChildCondition(new ZestExpressionStatusCode(), pos);
        if (and.getChildrenCondition().size() != 11) fail();
        for (int i = 0; i < 11; i++) {
            if (i != pos) {
                Class<ZestExpressionLength> expected = ZestExpressionLength.class;
                Class<ZestExpressionStatusCode> wrong = ZestExpressionStatusCode.class;
                String msg =
                        "Pos: "
                                + i
                                + ". Expected "
                                + expected.getName()
                                + "; obtained "
                                + wrong.getName();
                assertEquals(and.getChild(i).getClass(), expected);
            } else {
                Class<ZestExpressionLength> wrong = ZestExpressionLength.class;
                Class<ZestExpressionStatusCode> expected = ZestExpressionStatusCode.class;
                String msg =
                        "Pos: "
                                + i
                                + ". Expected "
                                + expected.getName()
                                + "; obtained "
                                + wrong.getName();
                assertEquals(and.getChild(i).getClass(), expected);
            }
        }
    }

    @Test
    void testComplexCondition() {
        ZestExpressionAnd and = new ZestExpressionAnd();
        ZestExpressionOr or = new ZestExpressionOr();
        try {
            ZestResponse resp =
                    new ZestResponse(
                            new URL("http://this.is.a.test"),
                            "Header prefix12345postfix",
                            "Body Prefix54321Postfix",
                            200,
                            1000);
            ZestExpressionLength length =
                    new ZestExpressionLength(ZestVariables.RESPONSE_BODY, 0, 1);
            length.setInverse(true);
            ZestExpressionStatusCode code = new ZestExpressionStatusCode(200);
            ZestExpressionRegex regex =
                    new ZestExpressionRegex(ZestVariables.RESPONSE_BODY, "54321");
            ZestExpressionResponseTime time = new ZestExpressionResponseTime(100);
            LinkedList<String> includeRegex = new LinkedList<>();
            LinkedList<String> excludeRegex = new LinkedList<>();
            excludeRegex.add("");
            ZestExpressionURL url = new ZestExpressionURL(includeRegex, excludeRegex);
            url.setInverse(true);
            time.setGreaterThan(true);
            ZestExpression genericExp =
                    new ZestExpression() {

                        @Override
                        public ZestExpression deepCopy() {
                            return null;
                        }

                        @Override
                        public boolean isTrue(ZestRuntime runtime) {
                            return false;
                        }
                    };
            or.addChildCondition(genericExp);
            and.addChildCondition(length);
            and.addChildCondition(code);
            and.addChildCondition(regex);
            and.addChildCondition(time);
            and.addChildCondition(url);
            or.addChildCondition(and);
            ZestConditional cond = new ZestConditional(or);
            assertTrue(cond.isTrue(new TestRuntime(resp)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testSetChildrenCondition() {
        List<ZestExpressionElement> children = new LinkedList<>();
        children.add(new ZestExpressionAnd());
        children.add(new ZestExpressionLength("response.body", 1, 2));
        children.add(new ZestExpressionStatusCode(200));
        ZestExpressionAnd root = new ZestExpressionAnd();
        root.setChildrenCondition(children);
        for (int i = 0; i < children.size(); i++) {
            String expected = children.get(i).getClass().getName();
            String obtained = root.getChild(i).getClass().getName();
            String msg = "[" + i + "] - Obtained " + obtained + " instead of " + expected;
            assertEquals(children.get(i).getClass(), root.getChild(i).getClass());
        }
    }

    @Test
    void testDeepCopyOr() {
        ZestExpressionOr or = new ZestExpressionOr();
        ZestExpressionOr copy = or.deepCopy();
        assertEquals(or.getClass(), copy.getClass());
    }

    @Test
    void testClearChildren() {
        ZestExpressionAnd and = new ZestExpressionAnd();
        LinkedList<ZestExpressionElement> children = new LinkedList<>();
        children.add(new ZestExpressionAnd());
        children.add(new ZestExpressionLength());
        children.add(new ZestExpressionOr());
        ZestExpressionOr or = new ZestExpressionOr(children);
        and.setChildrenCondition(children);
        and.addChildCondition(or);
        and.clearChildren();
        assertTrue(and.getChildrenCondition().isEmpty());
    }

    @Test
    void testDeepCopyNullChildren() {
        ZestExpressionAnd and = new ZestExpressionAnd(null);
        ZestExpressionAnd copy = and.deepCopy();
        assertEquals(copy.getClass(), and.getClass());
    }

    @Test
    void testOrDeepCopyNullChildren() {
        ZestExpressionOr or = new ZestExpressionOr(null);
        ZestExpressionOr copy = or.deepCopy();
        assertEquals(copy.getClass(), or.getClass());
    }

    @Test
    void testRemoveChildCondition() {
        ZestExpressionAnd and = new ZestExpressionAnd();
        ZestExpressionLength lengthExpr = new ZestExpressionLength("response.body", 10, 20);
        and.addChildCondition(lengthExpr);
        and.removeChildCondition(lengthExpr);
        assertTrue(and.getChildrenCondition().isEmpty());
    }

    @Test
    void testRemoveChildConditionReturnValue() {
        ZestExpressionAnd and = new ZestExpressionAnd();
        ZestExpressionLength lengthExpr = new ZestExpressionLength("response.body", 10, 20);
        and.addChildCondition(lengthExpr);
        assertEquals(and.removeChildCondition(lengthExpr), lengthExpr);
    }

    @Test
    void testRemoveChildNotPresent() {
        ZestExpressionAnd and = new ZestExpressionAnd();
        assertNull(and.removeChildCondition(new ZestExpressionLength()));
    }

    @Test
    void testZestExpressionAndLazyEvaluation() {
        ZestExpressionAnd and = new ZestExpressionAnd();
        ZestExpressionLength lengthExpr = new ZestExpressionLength("response.body", 100, 100, true);
        ZestExpression expectException =
                new ZestExpression() {

                    @Override
                    public boolean isTrue(ZestRuntime runtime) {
                        throw new IllegalAccessError(
                                "This has not to be thrown cause of the Lazy evaluation!");
                    }

                    @Override
                    public ZestExpression deepCopy() {
                        return null;
                    }
                };
        and.addChildCondition(lengthExpr);
        and.addChildCondition(expectException);
        ZestResponse response = new ZestResponse(null, "", "", 200, 100);
        assertFalse(and.evaluate(new TestRuntime(response)));
    }

    @Test
    void testZestExpressionOrLazyEvaluation() {
        ZestExpressionOr or = new ZestExpressionOr();
        ZestExpressionLength lengthExpr = new ZestExpressionLength("response.body", 100, 100);
        ZestExpression expectedException =
                new ZestExpression() {

                    @Override
                    public boolean isTrue(ZestRuntime runtime) {
                        throw new IllegalAccessError(
                                "This has not to be thrown cause of the Lazy evaluation!");
                    }

                    @Override
                    public ZestExpression deepCopy() {
                        return null;
                    }
                };
        or.addChildCondition(lengthExpr);
        or.addChildCondition(expectedException);
        ZestResponse response = new ZestResponse(null, "", "", 200, 100);
        assertTrue(or.evaluate(new TestRuntime(response)));
    }
}
