/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.test.v1;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mozilla.zest.core.v1.ZestAssertion;
import org.mozilla.zest.core.v1.ZestExpressionRegex;
import org.mozilla.zest.core.v1.ZestResponse;
import org.mozilla.zest.core.v1.ZestVariables;

/** */
public class ZestAssertBodyRegexUnitTest {

    /**
     * Method testSimpleIncRegex.
     *
     * @throws Exception
     */
    @Test
    public void testSimpleIncRegex() throws Exception {
        ZestExpressionRegex regex = new ZestExpressionRegex(ZestVariables.RESPONSE_BODY, "test123");
        ZestAssertion ze = new ZestAssertion(regex);
        assertTrue(ze.isValid(new TestRuntime(new ZestResponse(null, "", "aaaatest123", 200, 0))));
    }

    /**
     * Method testSimpleExcRegex.
     *
     * @throws Exception
     */
    @Test
    public void testSimpleExcRegex() throws Exception {
        ZestExpressionRegex regex = new ZestExpressionRegex(ZestVariables.RESPONSE_BODY, "test123");
        ZestAssertion ze = new ZestAssertion(regex);
        assertFalse(ze.isValid(new TestRuntime(new ZestResponse(null, "", "aaaatst123", 200, 0))));
    }

    /**
     * Method testSimpleIncInvRegex.
     *
     * @throws Exception
     */
    @Test
    public void testSimpleIncInvRegex() throws Exception {
        ZestExpressionRegex regex =
                new ZestExpressionRegex(ZestVariables.RESPONSE_BODY, "test123", false, true);
        ZestAssertion ze = new ZestAssertion(regex);
        assertFalse(ze.isValid(new TestRuntime(new ZestResponse(null, "", "aaaatest123", 200, 0))));
    }

    @Test
    public void testSimpleCaseExact() throws Exception {
        ZestExpressionRegex regex =
                new ZestExpressionRegex(ZestVariables.RESPONSE_BODY, "test123", true, false);
        ZestAssertion ze = new ZestAssertion(regex);
        assertTrue(ze.isValid(new TestRuntime(new ZestResponse(null, "", "aaaatest123", 200, 0))));
        assertFalse(ze.isValid(new TestRuntime(new ZestResponse(null, "", "aaaaTest123", 200, 0))));
    }

    @Test
    public void testSimpleCaseIgnore() throws Exception {
        ZestExpressionRegex regex =
                new ZestExpressionRegex(ZestVariables.RESPONSE_BODY, "test123", false, false);
        ZestAssertion ze = new ZestAssertion(regex);
        assertTrue(ze.isValid(new TestRuntime(new ZestResponse(null, "", "aaaatest123", 200, 0))));
        assertTrue(ze.isValid(new TestRuntime(new ZestResponse(null, "", "aaaaTest123", 200, 0))));
    }

    /**
     * Method testSimpleExcInvRegex.
     *
     * @throws Exception
     */
    @Test
    public void testSimpleExcInvRegex() throws Exception {
        ZestExpressionRegex regex =
                new ZestExpressionRegex(ZestVariables.RESPONSE_BODY, "test123", false, true);
        ZestAssertion ze = new ZestAssertion(regex);
        assertTrue(ze.isValid(new TestRuntime(new ZestResponse(null, "", "aaaatst123", 200, 0))));
    }
}
