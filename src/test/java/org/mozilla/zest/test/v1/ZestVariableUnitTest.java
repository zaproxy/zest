/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.test.v1;

import static org.junit.Assert.assertTrue;

import java.util.Set;
import org.junit.Test;
import org.mozilla.zest.core.v1.ZestAssignString;
import org.mozilla.zest.core.v1.ZestLoopFile;
import org.mozilla.zest.core.v1.ZestScript;

/** */
public class ZestVariableUnitTest {

    /**
     * Method testTokenReplacement.
     *
     * @throws Exception
     */
    @Test
    public void testAssign() throws Exception {
        ZestScript script = new ZestScript();
        ZestAssignString ast = new ZestAssignString();

        String test1 = "fdsjik934785:P@O():*&K";
        ast.setVariableName("aaa");
        ast.setString(test1);

        script.add(ast);

        Set<String> vars = script.getVariableNames();

        assertTrue(vars.contains("aaa"));
    }

    @Test
    public void testLoop() throws Exception {
        ZestScript script = new ZestScript();
        ZestLoopFile loop = new ZestLoopFile();
        loop.setVariableName("aaa");

        script.add(loop);

        Set<String> vars = script.getVariableNames();

        assertTrue(vars.contains("aaa"));
    }
}
