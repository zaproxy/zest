/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import org.zaproxy.zest.core.v1.ZestConditional;
import org.zaproxy.zest.core.v1.ZestExpressionAnd;
import org.zaproxy.zest.core.v1.ZestExpressionLength;
import org.zaproxy.zest.core.v1.ZestExpressionOr;
import org.zaproxy.zest.core.v1.ZestExpressionResponseTime;
import org.zaproxy.zest.core.v1.ZestExpressionStatusCode;
import org.zaproxy.zest.core.v1.ZestLoopFile;
import org.zaproxy.zest.core.v1.ZestLoopInteger;
import org.zaproxy.zest.core.v1.ZestLoopString;
import org.zaproxy.zest.core.v1.ZestStatement;
import org.zaproxy.zest.impl.ZestPrinter;

/** */
public class TestPrint {
    /**
     * Method main.
     *
     * @param args String[]
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("---------------------");
        System.out.println("ZestComplexExpression");
        System.out.println("---------------------");
        ZestExpressionOr or = new ZestExpressionOr();
        or.addChildCondition(new ZestExpressionLength("response.body", 10, 20));
        or.addChildCondition(new ZestExpressionStatusCode(200));
        ZestExpressionAnd and = new ZestExpressionAnd();
        and.addChildCondition(new ZestExpressionLength("response.body", 100, 200));
        and.addChildCondition(new ZestExpressionResponseTime(1000));
        and.addChildCondition(or);
        and.addChildCondition(and.deepCopy());
        ZestPrinter.printExpression(and, -1);
        System.out.println("---------------------");
        System.out.println("    ZestLoopString");
        System.out.println("---------------------");
        String[] values = {"a", "b", "c"};
        List<ZestStatement> statements = new LinkedList<>();
        statements.add(new ZestConditional(or));
        statements.add(new ZestConditional(and));
        ZestLoopString loopString = new ZestLoopString(values);
        for (ZestStatement stmt : statements) {
            loopString.addStatement(stmt);
        }
        ZestPrinter.list(loopString, -1);
        System.out.println("---------------------");
        System.out.println("    ZestLoopFile");
        System.out.println("---------------------");
        ZestLoopFile loopFile = new ZestLoopFile(ZestLoopFileUnitTest.file.getAbsolutePath());
        for (ZestStatement stmt : statements) {
            loopFile.addStatement(stmt);
        }
        ZestPrinter.list(loopFile, -1);
        System.out.println("---------------------");
        System.out.println("   ZestLoopInteger");
        System.out.println("---------------------");
        ZestLoopInteger loopInteger = new ZestLoopInteger(0, 1458);
        for (ZestStatement stmt : statements) {
            loopInteger.addStatement(stmt);
        }
        ZestPrinter.list(loopInteger, -1);
    }
}
