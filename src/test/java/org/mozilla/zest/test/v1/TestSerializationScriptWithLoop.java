/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.test.v1;

import java.io.FileNotFoundException;
import org.mozilla.zest.core.v1.ZestJSON;
import org.mozilla.zest.core.v1.ZestLoopFile;
import org.mozilla.zest.core.v1.ZestScript;
import org.mozilla.zest.core.v1.ZestScript.Type;

public class TestSerializationScriptWithLoop {
    public static void main(String[] args) throws FileNotFoundException {
        ZestLoopFile loop = new ZestLoopFile(ZestLoopFileUnitTest.file.getAbsolutePath());
        ZestScript script =
                new ZestScript(
                        "TestSerializationZestScript",
                        "Just to test the correct serialization",
                        Type.StandAlone);
        script.add(loop);
        System.out.println("========================");
        System.out.println("        SCRIPT");
        System.out.println("========================");
        System.out.println(ZestJSON.toString(script));
        System.out.println("========================");
        System.out.println("         LOOP");
        System.out.println("========================");
        System.out.println(ZestJSON.toString(loop));
        System.out.println("========================");
        System.out.println("    LOOP FROM SCRIPT");
        System.out.println("========================");
        System.out.println(ZestJSON.toString(script.getStatement(1)));
        ZestScript copy = script.deepCopy();
        System.out.println("========================");
        System.out.println("   DEEP COPIED SCRIPT");
        System.out.println("========================");
        System.out.println(ZestJSON.toString(copy));
        //		ZestScript script2=new ZestScript("TestSerializationZestScript2", "Just to test the
        // correct serialization", Type.StandAlone);
        //		ZestConditional condition0=new ZestConditional(new ZestExpressionStatusCode(100));
        //		ZestConditional condition1=new ZestConditional(new ZestExpressionStatusCode(200));
        //		ZestConditional condition2=new ZestConditional(new ZestExpressionStatusCode(300));
        //		condition1.addIf(condition2);
        //		condition0.addIf(condition1);
        //		script2.add(condition0);
        //		System.out.println(ZestJSON.toString(script2));

    }
}
