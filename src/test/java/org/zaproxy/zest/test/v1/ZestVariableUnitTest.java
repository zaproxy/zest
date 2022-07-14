/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.zaproxy.zest.core.v1.ZestAssignString;
import org.zaproxy.zest.core.v1.ZestLoopFile;
import org.zaproxy.zest.core.v1.ZestScript;

/** */
class ZestVariableUnitTest {

    @Test
    void testAssign() {
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
    void testLoop() throws Exception {
        ZestScript script = new ZestScript();
        ZestLoopFile loop = new ZestLoopFile();
        loop.setVariableName("aaa");

        script.add(loop);

        Set<String> vars = script.getVariableNames();

        assertTrue(vars.contains("aaa"));
    }
}
