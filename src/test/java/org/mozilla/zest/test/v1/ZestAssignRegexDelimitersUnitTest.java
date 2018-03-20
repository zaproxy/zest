/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.test.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.mozilla.zest.core.v1.ZestAssignFailException;
import org.mozilla.zest.core.v1.ZestAssignRegexDelimiters;
import org.mozilla.zest.core.v1.ZestJSON;
import org.mozilla.zest.core.v1.ZestRequest;
import org.mozilla.zest.core.v1.ZestResponse;
import org.mozilla.zest.core.v1.ZestScript;
import org.mozilla.zest.impl.ZestBasicRunner;

/** */
public class ZestAssignRegexDelimitersUnitTest {

    private TestRuntime rt = new TestRuntime();

    /**
     * Method testSimpleCase.
     *
     * @throws Exception
     */
    @Test
    public void testSimpleCase() throws Exception {
        ZestAssignRegexDelimiters ast = new ZestAssignRegexDelimiters();
        ZestResponse resp =
                new ZestResponse(
                        null, "Header prefix12345postfix", "Body Prefix54321Postfix", 200, 0);

        ast.setVariableName("aaa");
        ast.setPrefix("prefix");
        ast.setPostfix("postfix");
        assertEquals("12345", ast.assign(resp, rt));

        ast.setVariableName("aaa");
        ast.setPrefix("Prefix");
        ast.setPostfix("Postfix");
        assertEquals("54321", ast.assign(resp, rt));
    }

    /**
     * Method testRegexes.
     *
     * @throws Exception
     */
    @Test
    public void testRegexes() throws Exception {
        ZestAssignRegexDelimiters ast = new ZestAssignRegexDelimiters();
        ZestResponse resp =
                new ZestResponse(
                        null, "Header prefix12345postfix", "Body Prefix54321Postfix", 200, 0);

        ast.setVariableName("aaa");
        ast.setPrefix("^");
        ast.setPostfix("$");
        ast.setLocation(ZestAssignRegexDelimiters.LOC_HEAD);
        assertEquals("Header prefix12345postfix", ast.assign(resp, rt));

        ast.setVariableName("aaa");
        ast.setPrefix("^");
        ast.setPostfix("$");
        ast.setLocation(ZestAssignRegexDelimiters.LOC_BODY);
        assertEquals("Body Prefix54321Postfix", ast.assign(resp, rt));
    }

    /**
     * Method testExceptions.
     *
     * @throws Exception
     */
    @Test
    public void testExceptions() throws Exception {
        ZestAssignRegexDelimiters ast = new ZestAssignRegexDelimiters();
        ZestResponse resp = new ZestResponse(null, "aaaa", "bbbb", 200, 0);

        ast.setVariableName("aaa");
        ast.setPrefix("bbb");
        ast.setPostfix("ccc");
        try {
            ast.assign(null, rt);
            fail("Should have caused an exception");
        } catch (ZestAssignFailException e) {
            // Expected
        }

        ast.setVariableName("aaa");
        ast.setPrefix(null);
        ast.setPostfix("ccc");
        try {
            ast.assign(resp, rt);
            fail("Should have caused an exception");
        } catch (ZestAssignFailException e) {
            // Expected
        }

        ast.setVariableName("aaa");
        ast.setPrefix("bbb");
        ast.setPostfix(null);
        try {
            ast.assign(resp, rt);
            fail("Should have caused an exception");
        } catch (ZestAssignFailException e) {
            // Expected
        }

        ast.setVariableName("aaa");
        ast.setPrefix("xxx");
        ast.setPostfix("yyy");
        try {
            ast.assign(resp, rt);
            fail("Should have caused an exception");
        } catch (ZestAssignFailException e) {
            // Expected
        }
    }

    /**
     * Method testAssignRegexDelimitersZestScript. Test a zest script. Uses ZestJSON and
     * ZestBasicRunner directly as ZestActionInvoke don't use the ZestResponse now.
     *
     * @throws Exception
     */
    @Test
    public void testAssignRegexDelimitersZestScript() throws Exception {
        ZestResponse resp =
                new ZestResponse(
                        null,
                        "Server: Apache-Coyote/1.1\r\nLocation: http://some.url.com\r\nExpires: Wed, 12 Jul 2017 11:26:32 GMT",
                        "Body Prefix54321Postfix",
                        302,
                        0);
        ZestRequest req = new ZestRequest();
        req.setResponse(resp);

        String zestString =
                IOUtils.toString(
                        getClass().getResource("/data/assignRegexDelimiters-script.zest"),
                        StandardCharsets.UTF_8);
        ZestScript zestScript = (ZestScript) ZestJSON.fromString(zestString);

        Map<String, String> map = new HashMap<>();
        ZestBasicRunner runner = new ZestBasicRunner();
        String result = runner.run(zestScript, req, map);

        assertEquals("http://some.url.com", result);
    }
}
