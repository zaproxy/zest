/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.zaproxy.zest.core.v1.ZestActionPrint;
import org.zaproxy.zest.core.v1.ZestConditional;
import org.zaproxy.zest.core.v1.ZestControlLoopBreak;
import org.zaproxy.zest.core.v1.ZestControlLoopNext;
import org.zaproxy.zest.core.v1.ZestLoopInteger;
import org.zaproxy.zest.core.v1.ZestLoopString;
import org.zaproxy.zest.core.v1.ZestStatement;

/** */
class ZestLoopIntegerUnitTest {
    static String[] values = {"a", "b", "c"};
    static List<ZestStatement> statements = new LinkedList<>();

    {
        statements.add(new ZestConditional());
        statements.add(new ZestLoopString(values));
    }

    @Test
    void testLoop() {
        int maxIt = 10;
        ZestLoopInteger loop = new ZestLoopInteger(0, maxIt);
        for (ZestStatement stmt : statements) {
            loop.addStatement(stmt);
        }
        int iteration = loop.getCurrentToken();
        while (loop.loop()) {
            ++iteration;
            assertEquals(iteration, loop.getCurrentToken());
        }
        assertEquals(loop.getCurrentToken(), maxIt - 1); // start is inclusive, end
        // is exclusive!
    }

    @Test
    void testEndLoop() {
        ZestLoopInteger loop = new ZestLoopInteger(0, 10);
        for (ZestStatement stmt : statements) {
            loop.addStatement(stmt);
        }
        loop.endLoop();
        assertTrue(loop.getCurrentState().isLastState(loop.getSet())); // it
        // recognize
        // the
        // last state
        assertFalse(loop.loop()); // it returs false if the method loop is called
        // again
    }

    @Test
    void testDeepCopy() {
        ZestLoopInteger loop = new ZestLoopInteger(0, 10);
        for (ZestStatement stmt : statements) {
            loop.addStatement(stmt);
        }
        loop.loop();
        ZestLoopInteger copy = loop.deepCopy();
        assertEquals(loop.getCurrentState(), copy.getCurrentState());
    }

    @Test
    void testHasMoreElements() {
        int numOfToken = 10;
        ZestLoopInteger loop = new ZestLoopInteger(0, numOfToken);
        for (ZestStatement stmt : statements) {
            loop.addStatement(stmt);
        }
        int counter = 0;
        while (loop.hasMoreElements()) {
            ZestStatement stmt = loop.nextElement();
            assertEquals(
                    stmt.getClass(),
                    statements
                            .get(counter % statements.size())
                            .getClass()); // check if the classes of the
            // statements are equals
            counter++;
        }
        assertEquals(counter, (numOfToken) * statements.size()); // include start, exclude end!
    }

    @Test
    void testZestLoopBreak() {
        statements.clear();
        for (int i = 0; i < 10; i++) {
            statements.add(new ZestActionPrint("" + i));
        }
        List<ZestStatement> statements2 = new LinkedList<>(statements);
        statements2.add(new ZestControlLoopBreak());
        ZestLoopInteger loop = new ZestLoopInteger(0, 1000000);
        for (ZestStatement stmt : statements2) {
            loop.addStatement(stmt);
        }
        int counterIteration = 0;
        while (loop.hasMoreElements()) {
            loop.nextElement().getElementType();
            counterIteration++;
        }
        assertEquals(counterIteration, statements2.size() - 1);
    }

    @Test
    void testZestLoopNext() {
        LinkedList<ZestStatement> statements2 = new LinkedList<>(statements);
        statements2.add(0, new ZestControlLoopNext());
        ZestLoopInteger loop = new ZestLoopInteger(0, 10);
        for (ZestStatement stmt : statements2) {
            loop.addStatement(stmt);
        }
        int counter = 0;
        while (loop.hasMoreElements()) {
            ZestStatement tmp = loop.nextElement();
            assertTrue(tmp instanceof ZestControlLoopNext, "iteration " + counter);
            counter++;
        }
    }

    @Test
    void testZestLoopDifferentStep() {
        ZestLoopInteger loop = new ZestLoopInteger("with step = 7", 0, 100);
        for (ZestStatement stmt : statements) {
            loop.addStatement(stmt);
        }
        loop.setStep(7);
        int counter = 0;
        while (loop.hasMoreElements()) {
            if (loop.loop()) {
                ++counter;
                assertEquals(loop.getCurrentToken(), counter * 7);
            } else {
                assertEquals(loop.getCurrentToken(), loop.getEnd() - 1);
            }
        }
    }
}
