/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.test.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import org.mozilla.zest.core.v1.ZestActionFail;
import org.mozilla.zest.core.v1.ZestAssignString;
import org.mozilla.zest.core.v1.ZestConditional;
import org.mozilla.zest.core.v1.ZestJSON;
import org.mozilla.zest.core.v1.ZestLoopStateString;
import org.mozilla.zest.core.v1.ZestLoopString;
import org.mozilla.zest.core.v1.ZestScript;
import org.mozilla.zest.core.v1.ZestStatement;
import org.mozilla.zest.impl.ZestBasicRunner;

/** */
public class ZestLoopStringUnitTest {
    String[] values = {"1", "2", "3", "4", "5", "6", "7"};

    @Test
    public void testZestLoopString() {
        ZestLoopString loop = new ZestLoopString(0, values);
        assertTrue(loop.getCurrentState() != null);
    }

    @Test
    public void testZestLoopStringStringArrayIntListOfZestStatement() {
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
        assertTrue("right statements position", rightStmtPos);
        assertTrue("right Set Size", rightSetSize);
    }

    @Test
    public void testLoop() {
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
        assertTrue("right index", rightIndex);
        assertTrue("right value", rightValue);
        assertFalse("not last state", state.isLastState(loop.getSet()));
    }

    @Test
    public void testEndLoop() {
        ZestLoopString loop = new ZestLoopString(0, values);
        loop.endLoop();
        assertTrue(loop.getCurrentState().isLastState(loop.getSet()));
    }

    @Test
    public void testAddStatement() {
        ZestLoopString loop1 = new ZestLoopString(values);
        ZestLoopString loop2 = new ZestLoopString(values);
        loop1.addStatement(loop2);
        assertTrue(loop1.getStatement(1).getClass().equals(ZestLoopString.class));
    }

    @Test
    public void testGetLast() {
        ZestLoopString loop = new ZestLoopString(values);
        for (int i = 0; i < 10; i++) {
            loop.addStatement(new ZestConditional());
        }
        loop.addStatement(new ZestLoopString(values));
        assertTrue(loop.getLast().getClass().equals(ZestLoopString.class));
    }

    @Test
    public void testDeepCopy() {
        ZestLoopString loop = new ZestLoopString(values);
        loop.addStatement(new ZestConditional());
        loop.addStatement(new ZestLoopString(values));
        loop.addStatement(new ZestActionFail());
        ZestLoopString copy = loop.deepCopy();
        assertTrue("same state", copy.getCurrentState().equals(loop.getCurrentState()));
    }

    @Test
    public void testGetValues() {
        ZestLoopString loop = new ZestLoopString(values);
        String[] valuesObtained = loop.getValues();
        if (valuesObtained.length != values.length) {
            fail("The two arrays do not have same length!");
        }
        for (int i = 0; i < values.length; i++) {
            assertTrue(
                    i + " expected " + values[i] + " instead of " + valuesObtained[i],
                    valuesObtained[i].equals(values[i]));
        }
    }

    @Test
    public void testSerialization() {
        ZestLoopString loop = new ZestLoopString(values);
        String str = ZestJSON.toString(loop);
        System.out.println(str);

        ZestLoopString loop2 = (ZestLoopString) ZestJSON.fromString(str);
        if (loop2.getValues().length != values.length) {
            fail("The two arrays do not have same length!");
        }
        for (int i = 0; i < values.length; i++) {
            assertTrue(
                    i + " expected " + values[i] + " instead of " + loop2.getValues()[i],
                    loop2.getValues()[i].equals(values[i]));
        }
    }

    @Test
    public void testDisable() throws Exception {
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
