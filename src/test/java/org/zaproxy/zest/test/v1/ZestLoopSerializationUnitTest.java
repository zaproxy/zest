/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.zaproxy.zest.core.v1.ZestConditional;
import org.zaproxy.zest.core.v1.ZestExpressionResponseTime;
import org.zaproxy.zest.core.v1.ZestExpressionStatusCode;
import org.zaproxy.zest.core.v1.ZestJSON;
import org.zaproxy.zest.core.v1.ZestLoopFile;
import org.zaproxy.zest.core.v1.ZestLoopInteger;
import org.zaproxy.zest.core.v1.ZestLoopString;
import org.zaproxy.zest.core.v1.ZestStatement;

class ZestLoopSerializationUnitTest {
    String[] values = {"a", "b", "c"};
    List<ZestStatement> statements = new LinkedList<>();

    {
        statements.add(new ZestConditional(new ZestExpressionStatusCode(100)));
        statements.add(new ZestConditional(new ZestExpressionResponseTime(1024)));
    }

    @Test
    void testSerializationLoopString() {
        ZestLoopString loop = new ZestLoopString(values);
        for (ZestStatement stmt : statements) {
            loop.addStatement(stmt);
        }
        String loopString = ZestJSON.toString(loop);
        // System.out.println(loopString);
        ZestLoopString copy = (ZestLoopString) ZestJSON.fromString(loopString);
        String copyString = ZestJSON.toString(copy);
        // System.out.println("===============================");
        // System.out.println("          LOOP STRING");
        // System.out.println("===============================");
        // System.out.println(copyString);
        assertEquals(copyString, loopString);
    }

    @Test
    void testSerializationLoopInteger() {
        ZestLoopInteger loop = new ZestLoopInteger(0, 1235);
        for (ZestStatement stmt : statements) {
            loop.addStatement(stmt);
        }
        String loopString = ZestJSON.toString(loop);
        ZestLoopInteger copy = (ZestLoopInteger) ZestJSON.fromString(loopString);
        String copyString = ZestJSON.toString(copy);
        // System.out.println("===============================");
        // System.out.println("          LOOP INTEGER");
        // System.out.println("===============================");
        // System.out.println(copyString);
        assertEquals(copyString, loopString);
    }

    @Test
    void testSerializationLoopFile() throws FileNotFoundException {
        ZestLoopFile loop = new ZestLoopFile(ZestLoopFileUnitTest.file.getAbsolutePath());
        for (ZestStatement stmt : statements) {
            loop.addStatement(stmt);
        }
        String loopString = ZestJSON.toString(loop);
        ZestLoopFile copy = (ZestLoopFile) ZestJSON.fromString(loopString);
        String copyString = ZestJSON.toString(copy);
        // System.out.println("===============================");
        // System.out.println("           LOOP FILE");
        // System.out.println("===============================");
        // System.out.println(copyString);
        assertEquals(loopString, copyString);
    }
}
