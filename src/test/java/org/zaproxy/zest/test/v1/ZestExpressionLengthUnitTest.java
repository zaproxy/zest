/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zaproxy.zest.core.v1.ZestExpressionLength;
import org.zaproxy.zest.core.v1.ZestResponse;

/** Unit test for {@link ZestExpressionLength}. */
class ZestExpressionLengthUnitTest {

    private ZestResponse response;

    @BeforeEach
    void setup() throws Exception {
        response =
                new ZestResponse(
                        new URI("http://this.is.a.test").toURL(),
                        "header prefix12345postfix",
                        "body prefix54321postfix",
                        200,
                        100);
    }

    @Test
    void testZestExpressionLength() {
        ZestExpressionLength length = new ZestExpressionLength();
        assertFalse(length.isTrue(new TestRuntime(response)));
    }

    @Test
    void testZestExpressionLengthWithParamsTrue() {
        ZestExpressionLength length = new ZestExpressionLength("response.body", 100, 100);
        assertTrue(length.isTrue(new TestRuntime(response)));
    }

    @Test
    void testZestExpressionLengthWithParamsFalse() {
        ZestExpressionLength length = new ZestExpressionLength("response.body", 100, -1);
        assertFalse(length.isTrue(new TestRuntime(response)));
    }

    @Test
    void testDeepCopy() {
        ZestExpressionLength length = new ZestExpressionLength("var", 100, 5, true);
        ZestExpressionLength copy = length.deepCopy();
        assertEquals(copy.getVariableName(), length.getVariableName());
        assertEquals(copy.getLength(), length.getLength());
        assertEquals(copy.getApprox(), length.getApprox());
        assertEquals(copy.isInverse(), length.isInverse());
    }

    @Test
    void testDeepCopySameParams() {
        int len = 10;
        int approx = 20;
        ZestExpressionLength length = new ZestExpressionLength("response.body", len, approx);
        ZestExpressionLength copy = length.deepCopy();
        assertTrue(copy.getLength() == len && copy.getApprox() == approx);
    }

    @Test
    void testGetLength() {
        int len = 10;
        int approx = 20;
        ZestExpressionLength length = new ZestExpressionLength("response.body", len, approx);
        assertEquals(len, length.getLength());
    }

    @Test
    void testSetLength() {
        int len = 10;
        ZestExpressionLength length = new ZestExpressionLength();
        length.setLength(len);
        assertEquals(length.getLength(), len);
    }

    @Test
    void testGetApprox() {
        int len = 10;
        int approx = 20;
        ZestExpressionLength length = new ZestExpressionLength("response.body", len, approx);
        assertEquals(approx, length.getApprox());
    }

    @Test
    void testSetApprox() {
        int approx = 10;
        ZestExpressionLength length = new ZestExpressionLength();
        length.setApprox(approx);
        assertEquals(approx, length.getApprox());
    }

    @Test
    void testEvaluateInverse() {
        ZestExpressionLength length = new ZestExpressionLength("response.body", 100, 100);
        length.setInverse(true);
        TestRuntime runtime = new TestRuntime(response);
        assertTrue(length.isTrue(runtime) && !length.evaluate(runtime));
    }

    @Test
    void testIsTrueNullBody() {
        ZestResponse resp = new ZestResponse(null, null, null, 0, 0);
        ZestExpressionLength length = new ZestExpressionLength("response.body", 100, 100);
        assertFalse(length.isTrue(new TestRuntime(resp)));
    }
}
