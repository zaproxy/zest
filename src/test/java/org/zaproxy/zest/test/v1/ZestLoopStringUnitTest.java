/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.zaproxy.zest.core.v1.ZestActionFail;
import org.zaproxy.zest.core.v1.ZestAssignString;
import org.zaproxy.zest.core.v1.ZestConditional;
import org.zaproxy.zest.core.v1.ZestJSON;
import org.zaproxy.zest.core.v1.ZestLoopStateString;
import org.zaproxy.zest.core.v1.ZestLoopString;
import org.zaproxy.zest.core.v1.ZestScript;
import org.zaproxy.zest.core.v1.ZestStatement;
import org.zaproxy.zest.impl.ZestBasicRunner;

/** */
class ZestLoopStringUnitTest {
    String[] values = {"1", "2", "3", "4", "5", "6", "7"};

    @Test
    void testZestLoopString() {
        ZestLoopString loop = new ZestLoopString(0, values);
        assertTrue(loop.getCurrentState() != null);
    }

    @Test
    void testZestLoopStringStringArrayIntListOfZestStatement() {
        LinkedList<ZestStatement> statements = new LinkedList<>();
        int firstIndex = 3;
        for (int i = 0; i < firstIndex - 1; i++) {
            statements.add(new ZestConditional(i));
        }
        statements.add(new ZestLoopString(firstIndex, values));
        ZestLoopString loop = new ZestLoopString(firstIndex, values);
        int idx = 0;
        for (ZestStatement stmt : statements) {
            loop.addStatement(stmt);
            idx = stmt.getIndex();
        }
        boolean rightStmtPos = loop.getStatement(idx).getClass().equals(ZestLoopString.class);
        boolean rightSetSize = loop.getSet().size() == values.length;
        assertTrue(rightStmtPos, "right statements position");
        assertTrue(rightSetSize, "right Set Size");
    }

    @Test
    void testLoop() {
        List<ZestStatement> stmts = new LinkedList<>();
        stmts.add(new ZestConditional());
        ZestLoopString loop = new ZestLoopString(0, values);
        for (ZestStatement stmt : stmts) {
            loop.addStatement(stmt);
        }
        int stopIndex = 4;
        for (int i = 0; i < stopIndex; i++) {
            loop.loop();
        }
        ZestLoopStateString state = loop.getCurrentState();
        boolean rightIndex = state.getCurrentIndex() == stopIndex;
        boolean rightValue = state.getCurrentToken().equals(values[state.getCurrentIndex()]);
        assertTrue(rightIndex, "right index");
        assertTrue(rightValue, "right value");
        assertFalse(state.isLastState(loop.getSet()), "not last state");
    }

    @Test
    void testEndLoop() {
        ZestLoopString loop = new ZestLoopString(0, values);
        loop.endLoop();
        assertTrue(loop.getCurrentState().isLastState(loop.getSet()));
    }

    @Test
    void testAddStatement() {
        ZestLoopString loop1 = new ZestLoopString(values);
        ZestLoopString loop2 = new ZestLoopString(values);
        loop1.addStatement(loop2);
        assertEquals(loop1.getStatement(1).getClass(), ZestLoopString.class);
    }

    @Test
    void testGetLast() {
        ZestLoopString loop = new ZestLoopString(values);
        for (int i = 0; i < 10; i++) {
            loop.addStatement(new ZestConditional());
        }
        loop.addStatement(new ZestLoopString(values));
        assertEquals(loop.getLast().getClass(), ZestLoopString.class);
    }

    @Test
    void testDeepCopy() {
        ZestLoopString loop = new ZestLoopString(values);
        loop.addStatement(new ZestConditional());
        loop.addStatement(new ZestLoopString(values));
        loop.addStatement(new ZestActionFail());
        ZestLoopString copy = loop.deepCopy();
        assertEquals(copy.getCurrentState(), loop.getCurrentState());
    }

    @Test
    void testGetValues() {
        ZestLoopString loop = new ZestLoopString(values);
        String[] valuesObtained = loop.getValues();
        if (valuesObtained.length != values.length) {
            fail("The two arrays do not have same length!");
        }
        for (int i = 0; i < values.length; i++) {
            assertEquals(valuesObtained[i], values[i]);
        }
    }

    @Test
    void testSerialization() {
        ZestLoopString loop = new ZestLoopString(values);
        String str = ZestJSON.toString(loop);
        System.out.println(str);

        ZestLoopString loop2 = (ZestLoopString) ZestJSON.fromString(str);
        if (loop2.getValues().length != values.length) {
            fail("The two arrays do not have same length!");
        }
        for (int i = 0; i < values.length; i++) {
            assertEquals(loop2.getValues()[i], values[i]);
        }
    }

    @Test
    void testDisable() throws Exception {
        ZestScript script = new ZestScript();
        ZestAssignString zaInit = new ZestAssignString("res", "");
        ZestLoopString loop = new ZestLoopString("var", values);
        ZestAssignString zaAppendVar = new ZestAssignString("res", "{{res}} {{var}}");
        ZestAssignString zaAppendComma = new ZestAssignString("res", "{{res}},");

        script.add(zaInit);
        script.add(loop);
        loop.addStatement(zaAppendVar);
        loop.addStatement(zaAppendComma);

        ZestBasicRunner runner = new ZestBasicRunner();

        // All enabled
        runner.run(script, null);
        assertEquals(" 1, 2, 3, 4, 5, 6, 7,", runner.getVariable("res"));

        // Disable appending the var
        zaAppendVar.setEnabled(false);
        runner.run(script, null);
        assertEquals(",,,,,,,", runner.getVariable("res"));

        // Disable appending the comma
        zaAppendVar.setEnabled(true);
        zaAppendComma.setEnabled(false);
        runner.run(script, null);
        assertEquals(" 1 2 3 4 5 6 7", runner.getVariable("res"));

        // Disable the loop
        zaAppendVar.setEnabled(true);
        zaAppendComma.setEnabled(true);
        loop.setEnabled(false);

        runner.run(script, null);
        assertEquals("", runner.getVariable("res"));
    }
}
