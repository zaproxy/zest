/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.zaproxy.zest.core.v1.ZestAssertion;
import org.zaproxy.zest.core.v1.ZestExpressionRegex;
import org.zaproxy.zest.core.v1.ZestResponse;
import org.zaproxy.zest.core.v1.ZestVariables;

/** */
class ZestAssertBodyRegexUnitTest {

    @Test
    void testSimpleIncRegex() {
        ZestExpressionRegex regex = new ZestExpressionRegex(ZestVariables.RESPONSE_BODY, "test123");
        ZestAssertion ze = new ZestAssertion(regex);
        assertTrue(ze.isValid(new TestRuntime(new ZestResponse(null, "", "aaaatest123", 200, 0))));
    }

    @Test
    void testSimpleExcRegex() {
        ZestExpressionRegex regex = new ZestExpressionRegex(ZestVariables.RESPONSE_BODY, "test123");
        ZestAssertion ze = new ZestAssertion(regex);
        assertFalse(ze.isValid(new TestRuntime(new ZestResponse(null, "", "aaaatst123", 200, 0))));
    }

    @Test
    void testSimpleIncInvRegex() {
        ZestExpressionRegex regex =
                new ZestExpressionRegex(ZestVariables.RESPONSE_BODY, "test123", false, true);
        ZestAssertion ze = new ZestAssertion(regex);
        assertFalse(ze.isValid(new TestRuntime(new ZestResponse(null, "", "aaaatest123", 200, 0))));
    }

    @Test
    void testSimpleCaseExact() {
        ZestExpressionRegex regex =
                new ZestExpressionRegex(ZestVariables.RESPONSE_BODY, "test123", true, false);
        ZestAssertion ze = new ZestAssertion(regex);
        assertTrue(ze.isValid(new TestRuntime(new ZestResponse(null, "", "aaaatest123", 200, 0))));
        assertFalse(ze.isValid(new TestRuntime(new ZestResponse(null, "", "aaaaTest123", 200, 0))));
    }

    @Test
    void testSimpleCaseIgnore() {
        ZestExpressionRegex regex =
                new ZestExpressionRegex(ZestVariables.RESPONSE_BODY, "test123", false, false);
        ZestAssertion ze = new ZestAssertion(regex);
        assertTrue(ze.isValid(new TestRuntime(new ZestResponse(null, "", "aaaatest123", 200, 0))));
        assertTrue(ze.isValid(new TestRuntime(new ZestResponse(null, "", "aaaaTest123", 200, 0))));
    }

    @Test
    void testSimpleExcInvRegex() {
        ZestExpressionRegex regex =
                new ZestExpressionRegex(ZestVariables.RESPONSE_BODY, "test123", false, true);
        ZestAssertion ze = new ZestAssertion(regex);
        assertTrue(ze.isValid(new TestRuntime(new ZestResponse(null, "", "aaaatst123", 200, 0))));
    }
}
