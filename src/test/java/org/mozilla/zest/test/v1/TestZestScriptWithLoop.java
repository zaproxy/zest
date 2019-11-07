/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.test.v1;

import java.io.IOException;
import java.net.URL;
import org.mozilla.zest.core.v1.ZestActionFailException;
import org.mozilla.zest.core.v1.ZestAssertFailException;
import org.mozilla.zest.core.v1.ZestAssignFailException;
import org.mozilla.zest.core.v1.ZestClientFailException;
import org.mozilla.zest.core.v1.ZestInvalidCommonTestException;
import org.mozilla.zest.core.v1.ZestLoopInteger;
import org.mozilla.zest.core.v1.ZestRequest;
import org.mozilla.zest.core.v1.ZestScript;
import org.mozilla.zest.impl.ZestBasicRunner;

public class TestZestScriptWithLoop {
    public static void main(String[] args)
            throws ZestAssertFailException, ZestActionFailException, IOException,
                    ZestInvalidCommonTestException, ZestAssignFailException,
                    ZestClientFailException {
        for (int i = 1; i < 9; i++) {
            ZestScript script =
                    new ZestScript(
                            "Test Zest Script with loop",
                            "this tests a Zest Script with a loop",
                            ZestScript.Type.StandAlone);
            String requestH =
                    "GET http://localhost:8080/bodgeit/ HTTP/1.1"
                            + "Host: localhost:8080"
                            + "User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0;)"
                            + "Pragma: no-cache"
                            + "Cache-control: no-cache"
                            + "Content-Type: application/x-www-form-urlencoded"
                            + "Content-length: 0";
            ZestRequest request = new ZestRequest();
            request.setMethod("GET");
            request.setHeaders(requestH);
            request.setUrl(new URL("http://localhost:8080/bodgeit/"));

            // ---- INTEGER ---

            ZestLoopInteger loopInt = new ZestLoopInteger(-10, 10);
            loopInt.setStep(i);
            loopInt.addStatement(request);
            script.add(loopInt);

            // ----FILE---

            //		 ZestLoopFile fileLoop=new ZestLoopFile(ZestLoopFileUnitTest.file);
            //		 fileLoop.addStatement(request);
            //		 script.add(fileLoop);

            // ----STRING---
            //
            //		 String[] values={"A","B","C","D"};
            //		 ZestLoopString stringLoop=new ZestLoopString(values);
            //		 stringLoop.addStatement(request);
            //		 script.add(stringLoop);

            ZestBasicRunner runner = new ZestBasicRunner();
            System.out.println("start " + i);
            runner.run(script, null);
            System.out.println("done");
        }
    }
}
