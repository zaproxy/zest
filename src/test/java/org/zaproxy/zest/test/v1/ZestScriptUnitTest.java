/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.zaproxy.zest.core.v1.ZestAssignFieldValue;
import org.zaproxy.zest.core.v1.ZestAssignStringDelimiters;
import org.zaproxy.zest.core.v1.ZestClientLaunch;
import org.zaproxy.zest.core.v1.ZestClientWindowHandle;
import org.zaproxy.zest.core.v1.ZestConditional;
import org.zaproxy.zest.core.v1.ZestExpressionRegex;
import org.zaproxy.zest.core.v1.ZestFieldDefinition;
import org.zaproxy.zest.core.v1.ZestRequest;
import org.zaproxy.zest.core.v1.ZestScript;
import org.zaproxy.zest.core.v1.ZestStatement;
import org.zaproxy.zest.core.v1.ZestVariables;

/** */
class ZestScriptUnitTest {

    @Test
    void testSimpleIndexing() {
        ZestScript script = new ZestScript();
        ZestRequest req = new ZestRequest();
        script.add(req);
        checkOrder(new ZestStatement[] {script, req, null});

        ZestRequest req2 = new ZestRequest();
        script.add(req2);
        checkOrder(new ZestStatement[] {script, req, req2, null});

        ZestRequest req1b = new ZestRequest();
        script.add(1, req1b);
        checkOrder(new ZestStatement[] {script, req, req1b, req2, null});
    }

    @Test
    void testDepthIndexing() {
        ZestScript script = new ZestScript();
        ZestRequest req = new ZestRequest();
        script.add(req);
        // 0: script
        // 1:   req
        checkOrder(new ZestStatement[] {script, req, null});

        ZestConditional cond1 = new ZestConditional(new ZestExpressionRegex("BODY", ""));
        script.add(cond1);
        // 0: script
        // 1:   req
        // 2:   cond1
        checkOrder(new ZestStatement[] {script, req, cond1, null});

        ZestRequest req2 = new ZestRequest();
        script.add(req2);
        // 0: script
        // 1:   req
        // 2:   cond1
        // 3:   req2
        checkOrder(new ZestStatement[] {script, req, cond1, req2, null});

        ZestRequest req3 = new ZestRequest();
        cond1.addIf(req3);
        // 0: script
        // 1:   req
        // 2:   cond1
        // 3:     req3
        // 4:   req2
        checkOrder(new ZestStatement[] {script, req, cond1, req3, req2, null});

        ZestRequest req4 = new ZestRequest();
        cond1.addIf(0, req4);
        // 0: script
        // 1:   req
        // 2:   cond1
        // 3:     req4
        // 4:     req3
        // 5:   req2
        checkOrder(new ZestStatement[] {script, req, cond1, req4, req3, req2, null});
    }

    @Test
    void testDeepComplex() {
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
        ZestAssignFieldValue xfrm1 = new ZestAssignFieldValue();
        ZestAssignFieldValue xfrm2 = new ZestAssignFieldValue();

        /*
         * 0 zc1 if
         *     req1
         *     zc2 if
         *       req2
         *         xfrm1 (req1)
         *     else
         *       req3
         *       zc3 else
         *         req4
         *       req5
         *       zc4 else
         *         req6
         *           xform2 (req4)
         */
        zc1.addIf(req1);
        checkOrder(new ZestStatement[] {zc1, req1, null});
        zc1.addIf(zc2);
        checkOrder(new ZestStatement[] {zc1, req1, zc2, null});
        zc2.addIf(req2);
        checkOrder(new ZestStatement[] {zc1, req1, zc2, req2, null});
        xfrm1.setFieldDefinition(new ZestFieldDefinition(1, "xxx"));
        // assertEquals(req1.getIndex(), xfrm1.getRequestId());
        // req2.addTransformation(xfrm1);

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
        xfrm2.setFieldDefinition(new ZestFieldDefinition(1, "xxx"));
        // req6.addTransformation(xfrm2);
        // assertEquals(req4.getIndex(), xfrm2.getRequestId());
        checkOrder(
                new ZestStatement[] {zc1, req1, zc2, req2, req3, zc3, req4, req5, zc4, req6, null});
        /*
         * 0 zc1 if
         *     req1
         *     zc2 if
         *       req2
         *         xfrm1 (req1)
         *     else
         *       req3
         *       zc3 else
         *         req4
         *       req5
         *       zc4 else
         *         req6
         *           xform2 (req4)
         */
        zc1.removeIf(req3);
        checkOrder(new ZestStatement[] {zc1, req1, zc2, req2, zc3, req4, req5, zc4, req6, null});
        // Check the transforms have been updated
        // assertEquals(req1.getIndex(), xfrm1.getRequestId());
        // assertEquals(req4.getIndex(), xfrm2.getRequestId());
    }

    @Test
    void testDeepMiscOrder() {
        ZestScript script = new ZestScript();
        ZestConditional zc1 = new ZestConditional(new ZestExpressionRegex("BODY", ""));
        ZestConditional zc2 = new ZestConditional(new ZestExpressionRegex("BODY", ""));
        ZestConditional zc3 = new ZestConditional(new ZestExpressionRegex("BODY", ""));
        ZestRequest req1 = new ZestRequest();
        ZestRequest req2 = new ZestRequest();
        ZestRequest req3 = new ZestRequest();
        ZestRequest req4 = new ZestRequest();
        ZestRequest req5 = new ZestRequest();
        ZestRequest req6 = new ZestRequest();
        ZestRequest req7 = new ZestRequest();

        script.add(req1);
        checkOrder(new ZestStatement[] {script, req1, null});

        script.add(zc1);
        checkOrder(new ZestStatement[] {script, req1, zc1, null});

        script.add(req2);
        checkOrder(new ZestStatement[] {script, req1, zc1, req2, null});

        zc1.addElse(req3);
        checkOrder(new ZestStatement[] {script, req1, zc1, req3, req2, null});

        zc1.addIf(req4);
        checkOrder(new ZestStatement[] {script, req1, zc1, req4, req3, req2, null});

        script.add(0, req5);
        checkOrder(new ZestStatement[] {script, req5, req1, zc1, req4, req3, req2, null});

        script.add(1, zc2);
        checkOrder(new ZestStatement[] {script, req5, zc2, req1, zc1, req4, req3, req2, null});

        zc2.addElse(req6);
        checkOrder(
                new ZestStatement[] {script, req5, zc2, req6, req1, zc1, req4, req3, req2, null});

        zc2.addElse(zc3);
        checkOrder(
                new ZestStatement[] {
                    script, req5, zc2, req6, zc3, req1, zc1, req4, req3, req2, null
                });

        zc2.addIf(req7);
        checkOrder(
                new ZestStatement[] {
                    script, req5, zc2, req7, req6, zc3, req1, zc1, req4, req3, req2, null
                });
        // Useful for debugging
        // ZestPrinter.list(script);
    }

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

    @Test
    void testVariableNames() {
        ZestScript script = new ZestScript();
        // Check the default ones
        assertTrue(script.getVariableNames().contains(ZestVariables.REQUEST_HEADER));
        assertTrue(script.getVariableNames().contains(ZestVariables.REQUEST_BODY));
        assertTrue(script.getVariableNames().contains(ZestVariables.REQUEST_METHOD));
        assertTrue(script.getVariableNames().contains(ZestVariables.REQUEST_URL));
        assertTrue(script.getVariableNames().contains(ZestVariables.RESPONSE_HEADER));
        assertTrue(script.getVariableNames().contains(ZestVariables.RESPONSE_BODY));
        assertTrue(script.getVariableNames().contains(ZestVariables.RESPONSE_URL));

        script.add(new ZestRequest());
        script.add(
                new ZestAssignStringDelimiters(
                        "test.var.1", ZestAssignStringDelimiters.LOC_HEAD, "AA", "BB"));
        assertEquals(8, script.getVariableNames().size());
        assertTrue(script.getVariableNames().contains("test.var.1"));

        ZestConditional c1 = new ZestConditional();
        script.add(c1);
        c1.addIf(
                new ZestAssignStringDelimiters(
                        "test.var.2", ZestAssignStringDelimiters.LOC_HEAD, "AA", "BB"));
        assertEquals(9, script.getVariableNames().size());
        assertTrue(script.getVariableNames().contains("test.var.2"));

        // TODO check more containers
    }

    @Test
    void shouldGetClientWindowHandles() {
        // Given
        ZestScript script = new ZestScript();
        script.add(createClientLaunch("handleA"));
        ZestClientWindowHandle stmtB = new ZestClientWindowHandle();
        stmtB.setWindowHandle("handleB");
        script.add(stmtB);
        script.add(createClientLaunch("handleC"));
        ZestConditional stmtConditional = new ZestConditional();
        script.add(stmtConditional);
        stmtConditional.addIf(createClientLaunch("conditional-if"));
        stmtConditional.addElse(createClientLaunch("conditional-else"));

        // When
        Set<String> handles = script.getClientWindowHandles();

        // Then
        assertThat(handles)
                .contains("handleA", "handleB", "handleC", "conditional-if", "conditional-else");
    }

    @Test
    void shouldDuplicateScriptOptions() {
        // Given
        ZestScript script1 = new ZestScript();
        script1.setOptions(Map.of(ZestScript.STATEMENT_DELAY_MS, "10"));
        ZestScript script2 = new ZestScript();

        // When
        script1.duplicateTo(script2);

        // Then
        assertEquals(1, script2.getOptions().size());
        assertThat(script2.getOptions()).containsEntry(ZestScript.STATEMENT_DELAY_MS, "10");
    }

    private static ZestClientLaunch createClientLaunch(String handle) {
        ZestClientLaunch stmt = new ZestClientLaunch();
        stmt.setWindowHandle(handle);
        return stmt;
    }
}
