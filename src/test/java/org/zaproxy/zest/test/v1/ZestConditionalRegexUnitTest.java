/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.zaproxy.zest.core.v1.ZestConditional;
import org.zaproxy.zest.core.v1.ZestExpressionRegex;
import org.zaproxy.zest.core.v1.ZestRequest;
import org.zaproxy.zest.core.v1.ZestScript;
import org.zaproxy.zest.core.v1.ZestStatement;

/** */
class ZestConditionalRegexUnitTest {

    @Test
    void testAddingIfs() {
        ZestConditional zc = new ZestConditional(new ZestExpressionRegex("BODY", ""));

        ZestRequest req = new ZestRequest();
        zc.addIf(req);
        assertEquals(1, req.getIndex());
        checkOrder(new ZestStatement[] {zc, req, null});

        ZestRequest req2 = new ZestRequest();
        zc.addIf(req2);
        // 0: zc
        // 1:   req
        // 2:   req2
        checkOrder(new ZestStatement[] {zc, req, req2, null});

        ZestRequest req3 = new ZestRequest();
        zc.addIf(1, req3);
        // 0: zc
        // 1:   req
        // 2:   req3
        // 3:   req2
        checkOrder(new ZestStatement[] {zc, req, req3, req2, null});
    }

    @Test
    void testRemoveFirstIf() {
        ZestConditional zc = new ZestConditional(new ZestExpressionRegex("BODY", ""));
        ZestRequest req1 = new ZestRequest();
        ZestRequest req2 = new ZestRequest();
        ZestRequest req3 = new ZestRequest();

        zc.addIf(req1);
        zc.addIf(req2);
        zc.addIf(req3);

        zc.removeIf(req1);
        checkOrder(new ZestStatement[] {zc, req2, req3, null});
    }

    @Test
    void testRemoveMiddleIf() {

        ZestConditional zc = new ZestConditional(new ZestExpressionRegex("BODY", ""));
        ZestRequest req1 = new ZestRequest();
        ZestRequest req2 = new ZestRequest();
        ZestRequest req3 = new ZestRequest();

        zc.addIf(req1);
        zc.addIf(req2);
        zc.addIf(req3);

        zc.removeIf(req2);
        checkOrder(new ZestStatement[] {zc, req1, req3, null});
    }

    @Test
    void testRemoveLastIf() {
        ZestConditional zc = new ZestConditional(new ZestExpressionRegex("BODY", ""));
        ZestRequest req1 = new ZestRequest();
        ZestRequest req2 = new ZestRequest();
        ZestRequest req3 = new ZestRequest();

        zc.addIf(req1);
        zc.addIf(req2);
        zc.addIf(req3);

        zc.removeIf(req3);
        checkOrder(new ZestStatement[] {zc, req1, req2, null});
    }

    @Test
    void testMoveFirstIf() {
        ZestConditional zc = new ZestConditional(new ZestExpressionRegex("BODY", ""));
        ZestRequest req1 = new ZestRequest();
        ZestRequest req2 = new ZestRequest();
        ZestRequest req3 = new ZestRequest();

        zc.addIf(req1);
        zc.addIf(req2);
        zc.addIf(req3);

        zc.moveIf(2, req1);
        checkOrder(new ZestStatement[] {zc, req2, req3, req1, null});
    }

    @Test
    void testMoveSecondIf() {
        ZestConditional zc = new ZestConditional(new ZestExpressionRegex("BODY", ""));
        ZestRequest req1 = new ZestRequest();
        ZestRequest req2 = new ZestRequest();
        ZestRequest req3 = new ZestRequest();

        zc.addIf(req1);
        zc.addIf(req2);
        zc.addIf(req3);

        checkOrder(new ZestStatement[] {zc, req1, req2, req3, null});
        zc.moveIf(0, req2);
        checkOrder(new ZestStatement[] {zc, req2, req1, req3, null});
    }

    @Test
    void testMoveLastIf() {
        ZestConditional zc = new ZestConditional(new ZestExpressionRegex("BODY", ""));
        ZestRequest req1 = new ZestRequest();
        ZestRequest req2 = new ZestRequest();
        ZestRequest req3 = new ZestRequest();

        zc.addIf(req1);
        zc.addIf(req2);
        zc.addIf(req3);

        zc.moveIf(1, req3);
        checkOrder(new ZestStatement[] {zc, req1, req3, req2, null});
    }

    @Test
    void testAddingElses() {
        ZestConditional zc = new ZestConditional(new ZestExpressionRegex("BODY", ""));

        ZestRequest req = new ZestRequest();
        zc.addElse(req);
        assertEquals(1, req.getIndex());
        checkOrder(new ZestStatement[] {zc, req, null});

        ZestRequest req2 = new ZestRequest();
        zc.addElse(req2);
        // 0: zc
        // 1:   req
        // 2:   req2
        checkOrder(new ZestStatement[] {zc, req, req2, null});

        ZestRequest req3 = new ZestRequest();
        zc.addElse(1, req3);
        // 0: zc
        // 1:   req
        // 2:   req3
        // 3:   req2
        checkOrder(new ZestStatement[] {zc, req, req3, req2, null});
    }

    @Test
    void testRemoveFirstElse() {
        ZestConditional zc = new ZestConditional(new ZestExpressionRegex("BODY", ""));
        ZestRequest req1 = new ZestRequest();
        ZestRequest req2 = new ZestRequest();
        ZestRequest req3 = new ZestRequest();

        zc.addElse(req1);
        zc.addElse(req2);
        zc.addElse(req3);

        zc.removeElse(req1);
        checkOrder(new ZestStatement[] {zc, req2, req3, null});
    }

    @Test
    void testRemoveMiddleElse() {
        ZestConditional zc = new ZestConditional(new ZestExpressionRegex("BODY", ""));
        ZestRequest req1 = new ZestRequest();
        ZestRequest req2 = new ZestRequest();
        ZestRequest req3 = new ZestRequest();

        zc.addElse(req1);
        zc.addElse(req2);
        zc.addElse(req3);

        zc.removeElse(req2);
        checkOrder(new ZestStatement[] {zc, req1, req3, null});
    }

    @Test
    void testRemoveLastElse() {
        ZestConditional zc = new ZestConditional(new ZestExpressionRegex("BODY", ""));
        ZestRequest req1 = new ZestRequest();
        ZestRequest req2 = new ZestRequest();
        ZestRequest req3 = new ZestRequest();

        zc.addElse(req1);
        zc.addElse(req2);
        zc.addElse(req3);

        zc.removeElse(req3);
        checkOrder(new ZestStatement[] {zc, req1, req2, null});
    }

    @Test
    void testMoveFirstElse() {
        ZestConditional zc = new ZestConditional(new ZestExpressionRegex("BODY", ""));
        ZestRequest req1 = new ZestRequest();
        ZestRequest req2 = new ZestRequest();
        ZestRequest req3 = new ZestRequest();

        zc.addElse(req1);
        zc.addElse(req2);
        zc.addElse(req3);

        zc.moveElse(1, req1);
        checkOrder(new ZestStatement[] {zc, req2, req1, req3, null});
    }

    @Test
    void testMoveSecondElse() {
        ZestConditional zc = new ZestConditional(new ZestExpressionRegex("BODY", ""));
        ZestRequest req1 = new ZestRequest();
        ZestRequest req2 = new ZestRequest();
        ZestRequest req3 = new ZestRequest();

        zc.addElse(req1);
        zc.addElse(req2);
        zc.addElse(req3);

        zc.moveElse(2, req2);
        checkOrder(new ZestStatement[] {zc, req1, req3, req2, null});
    }

    @Test
    void testMoveLastElse() {
        ZestConditional zc = new ZestConditional(new ZestExpressionRegex("BODY", ""));
        ZestRequest req1 = new ZestRequest();
        ZestRequest req2 = new ZestRequest();
        ZestRequest req3 = new ZestRequest();

        zc.addElse(req1);
        zc.addElse(req2);
        zc.addElse(req3);

        zc.moveElse(0, req3);
        checkOrder(new ZestStatement[] {zc, req3, req1, req2, null});
    }

    @Test
    void testDeepConditionals1() {
        ZestConditional zc1 = new ZestConditional(new ZestExpressionRegex("BODY", ""));
        ZestConditional zc2 = new ZestConditional(new ZestExpressionRegex("BODY", ""));
        ZestRequest req1 = new ZestRequest();
        ZestRequest req2 = new ZestRequest();

        zc1.addIf(zc2);
        zc2.addIf(req1);
        zc1.addElse(req2);
        checkOrder(new ZestStatement[] {zc1, zc2, req1, req2, null});
    }

    @Test
    void testDeepConditionals2() {
        ZestConditional zc1 = new ZestConditional(new ZestExpressionRegex("BODY", ""));
        ZestConditional zc2 = new ZestConditional(new ZestExpressionRegex("BODY", ""));
        ZestConditional zc3 = new ZestConditional(new ZestExpressionRegex("BODY", ""));
        ZestConditional zc4 = new ZestConditional(new ZestExpressionRegex("BODY", ""));
        ZestRequest req1 = new ZestRequest();
        ZestRequest req2 = new ZestRequest();
        ZestRequest req3 = new ZestRequest();
        ZestRequest req4 = new ZestRequest();
        ZestRequest req5 = new ZestRequest();
        ZestRequest req6 = new ZestRequest();

        /*
         * 0 zc1 if
         *     req1
         *     zc2 if
         *       req2
         *     else
         *       req3
         *       zc3 else
         *         req4
         *       req5
         *       zc4 else
         *         req6
         */
        zc1.addIf(req1);
        checkOrder(new ZestStatement[] {zc1, req1, null});
        zc1.addIf(zc2);
        checkOrder(new ZestStatement[] {zc1, req1, zc2, null});
        zc2.addIf(req2);
        checkOrder(new ZestStatement[] {zc1, req1, zc2, req2, null});
        zc2.addElse(req3);
        checkOrder(new ZestStatement[] {zc1, req1, zc2, req2, req3, null});
        zc2.addElse(zc3);
        checkOrder(new ZestStatement[] {zc1, req1, zc2, req2, req3, zc3, null});
        zc3.addElse(req4);
        checkOrder(new ZestStatement[] {zc1, req1, zc2, req2, req3, zc3, req4, null});
        zc2.addElse(req5);
        checkOrder(new ZestStatement[] {zc1, req1, zc2, req2, req3, zc3, req4, req5, null});
        zc2.addElse(zc4);
        checkOrder(new ZestStatement[] {zc1, req1, zc2, req2, req3, zc3, req4, req5, zc4, null});
        zc4.addElse(req6);
        checkOrder(
                new ZestStatement[] {zc1, req1, zc2, req2, req3, zc3, req4, req5, zc4, req6, null});

        /*
         * 0 zc1 if
         *     req1
         *     zc2 if
         *       req2
         *     else
         *       req3
         *       req5		<-
         *       zc4 else
         *         req6
         */
        zc1.removeElse(zc3);
        checkOrder(new ZestStatement[] {zc1, req1, zc2, req2, req3, req5, zc4, req6, null});
    }

    @Test
    void testMovingIfStatements() {
        ZestScript zs = new ZestScript();
        ZestConditional zc1 = new ZestConditional(new ZestExpressionRegex("BODY", ""));
        ZestRequest req1 = new ZestRequest();
        ZestRequest req2 = new ZestRequest();

        zs.add(zc1);
        zc1.addIf(req1);
        zc1.addIf(req2);

        zc1.moveIf(0, req2);

        checkOrder(new ZestStatement[] {zs, zc1, req2, req1, null});
    }

    @Test
    void testMovingElseStatements() {
        ZestConditional zc1 = new ZestConditional(new ZestExpressionRegex("BODY", ""));
        ZestRequest req1 = new ZestRequest();
        ZestRequest req2 = new ZestRequest();

        zc1.addElse(req1);
        zc1.addElse(req2);

        zc1.moveElse(0, req2);

        checkOrder(new ZestStatement[] {zc1, req2, req1, null});
    }

    /*
    	@Test
    	void testDepthIndexing() {
    System.out.println("Failing test .........");
    		ZestScript script = new ZestScript();
    		ZestRequest req = new ZestRequest();
    		script.add(req);
    		// 0: script
    		// 1:   req
    		assertEquals(1, req.getIndex());

    		ZestConditionRegex cond1 = new ZestConditionRegex();
    		script.add(cond1);
    		// 0: script
    		// 1:   req
    		// 2:   cond1
    		assertEquals(1, req.getIndex());
    		assertEquals(2, cond1.getIndex());

    		ZestRequest req2 = new ZestRequest();
    System.out.println("Add req2... " + req2);
    		script.add(req2);
    		// 0: script
    		// 1:   req
    		// 2:   cond1
    		// 3:   req2
    		assertEquals(1, req.getIndex());
    		assertEquals(2, cond1.getIndex());
    		assertEquals(3, req2.getIndex());

    		ZestRequest req3 = new ZestRequest();
    System.out.println("Add req3... " + req3);
    		cond1.addIf(req3);
    		// 0: script
    		// 1:   req
    		// 2:   cond1
    		// 3:     req3
    		// 4:   req2
    		assertEquals(1, req.getIndex());
    		assertEquals(2, cond1.getIndex());
    		assertEquals(3, req3.getIndex());
    		assertEquals(4, req2.getIndex());

    		ZestRequest req4 = new ZestRequest();
    System.out.println("Add req4... " + req4);
    		cond1.addIf(0, req4);
    		// 0: script
    		// 1:   req
    		// 2:   cond1
    		// 3:     req4
    		// 4:     req3
    		// 5:   req2
    		assertEquals(1, req.getIndex());
    		assertEquals(2, cond1.getIndex());
    		assertEquals(3, req4.getIndex());
    		assertEquals(4, req3.getIndex());
    		assertEquals(5, req2.getIndex());


    	}
    */

    /**
     * Method checkOrder.
     *
     * @param stmts ZestStatement[]
     */
    private void checkOrder(ZestStatement[] stmts) {
        for (int i = 0; i < stmts.length; i++) {
            if (stmts[i] != null) {
                assertEquals(i, stmts[i].getIndex());
            }
            if (i > 0 && stmts[i] != null) {
                assertEquals(stmts[i - 1], stmts[i].getPrevious());
            }
            if (i < stmts.length - 1) {
                assertEquals(stmts[i].getNext(), stmts[i + 1]);
            }
        }
    }
}
