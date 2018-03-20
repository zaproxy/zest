/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.test.v1;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import org.mozilla.zest.core.v1.ZestConditional;
import org.mozilla.zest.core.v1.ZestExpressionResponseTime;
import org.mozilla.zest.core.v1.ZestExpressionStatusCode;
import org.mozilla.zest.core.v1.ZestJSON;
import org.mozilla.zest.core.v1.ZestLoopFile;
import org.mozilla.zest.core.v1.ZestLoopInteger;
import org.mozilla.zest.core.v1.ZestLoopString;
import org.mozilla.zest.core.v1.ZestStatement;

public class ZestLoopSerializationUnitTest {
    String[] values = {"a", "b", "c"};
    List<ZestStatement> statements = new LinkedList<>();

    {
        statements.add(new ZestConditional(new ZestExpressionStatusCode(100)));
        statements.add(new ZestConditional(new ZestExpressionResponseTime(1024)));
    }

    @Test
    public void testSerializationLoopString() {
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
        assertTrue(copyString.equals(loopString));
    }

    @Test
    public void testSerializationLoopInteger() {
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
        assertTrue(copyString.equals(loopString));
    }

    @Test
    public void testSerializationLoopFile() throws FileNotFoundException {
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
        assertTrue(loopString.equals(copyString));
    }
}
