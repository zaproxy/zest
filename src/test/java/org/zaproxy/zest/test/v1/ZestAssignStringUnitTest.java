/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.zaproxy.zest.core.v1.ZestAssignString;
import org.zaproxy.zest.core.v1.ZestJSON;
import org.zaproxy.zest.core.v1.ZestResponse;

/** */
class ZestAssignStringUnitTest {

    @Test
    void testSimpleCase() {
        ZestAssignString ast = new ZestAssignString();
        TestRuntime rt = new TestRuntime();
        ZestResponse resp =
                new ZestResponse(
                        null, "Header prefix12345postfix", "Body Prefix54321Postfix", 200, 0);

        String test1 = "fdsjik934785:P@O():*&K";
        ast.setVariableName("aaa");
        ast.setString(test1);
        assertEquals(test1, ast.assign(resp, rt));
    }

    @Test
    void testVariables() {
        ZestAssignString ast = new ZestAssignString();
        TestRuntime rt = new TestRuntime();

        // One level
        rt.setVariable("aaa", "aAaAaAaA");
        ast.setVariableName("a");
        ast.setString("bbb{{aaa}}ccc");
        assertEquals("bbbaAaAaAaAccc", ast.assign(null, rt));

        // Two levels
        rt = new TestRuntime();
        rt.setVariable("aaa", "aAaAaAaA");
        rt.setVariable("bbb", "bBb{{aaa}}CcC");
        ast.setVariableName("c");
        ast.setString("ddd{{bbb}}eee");
        assertEquals("dddbBbaAaAaAaACcCeee", ast.assign(null, rt));

        // Three levels
        rt = new TestRuntime();
        rt.setVariable("aaa", "aAaAaAaA");
        rt.setVariable("bbb", "bBb{{aaa}}CcC");
        rt.setVariable("ccc", "ddd{{bbb}}eee");
        ast.setVariableName("c");
        ast.setString("fFF{{ccc}}Hhh");
        assertEquals("fFFdddbBbaAaAaAaACcCeeeHhh", ast.assign(null, rt));
    }

    @Test
    void testRecurse() {
        ZestAssignString ast = new ZestAssignString();
        TestRuntime rt = new TestRuntime();

        rt.setVariable("aaa", "aAa{{bbb}}AaA");
        rt.setVariable("bbb", "bBb{{aaa}}CcC");
        ast.setVariableName("c");
        ast.setString("ddd{{bbb}}eee");

        // Ignores recursing variables
        assertEquals("dddbBbaAa{{bbb}}AaACcCeee", ast.assign(null, rt));
    }

    @Test
    void testSerialization() {
        ZestAssignString assign = new ZestAssignString("var", "io;hjvd9740[w9u;sdz");

        String str = ZestJSON.toString(assign);

        ZestAssignString assign2 = (ZestAssignString) ZestJSON.fromString(str);

        assertEquals(assign.getElementType(), assign2.getElementType());
        assertEquals(assign.getVariableName(), assign2.getVariableName());
        assertEquals(assign.getString(), assign2.getString());
    }
}
