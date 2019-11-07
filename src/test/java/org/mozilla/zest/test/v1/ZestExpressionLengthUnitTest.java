/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.test.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Test;
import org.mozilla.zest.core.v1.ZestExpressionLength;
import org.mozilla.zest.core.v1.ZestResponse;

/** */
public class ZestExpressionLengthUnitTest {
    public static ZestResponse response;

    static {
        try {
            response =
                    new ZestResponse(
                            new URL("http://this.is.a.test"),
                            "header prefix12345postfix",
                            "body prefix54321postfix",
                            200,
                            100);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testZestExpressionLength() {
        ZestExpressionLength length = new ZestExpressionLength();
        assertFalse(length.isTrue(new TestRuntime(response)));
    }

    @Test
    public void testZestExpressionLengthWithParamsTrue() {
        ZestExpressionLength length = new ZestExpressionLength("response.body", 100, 100);
        assertTrue(length.isTrue(new TestRuntime(response)));
    }

    @Test
    public void testZestExpressionLengthWithParamsFalse() {
        ZestExpressionLength length = new ZestExpressionLength("response.body", 100, -1);
        assertFalse(length.isTrue(new TestRuntime(response)));
    }

    @Test
    public void testDeepCopy() {
        ZestExpressionLength length = new ZestExpressionLength("var", 100, 5, true);
        ZestExpressionLength copy = length.deepCopy();
        assertEquals(copy.getVariableName(), length.getVariableName());
        assertEquals(copy.getLength(), length.getLength());
        assertEquals(copy.getApprox(), length.getApprox());
        assertEquals(copy.isInverse(), length.isInverse());
    }

    @Test
    public void testDeepCopySameParams() {
        int len = 10;
        int approx = 20;
        ZestExpressionLength length = new ZestExpressionLength("response.body", len, approx);
        ZestExpressionLength copy = length.deepCopy();
        assertTrue(copy.getLength() == len && copy.getApprox() == approx);
    }

    @Test
    public void testGetLength() {
        int len = 10;
        int approx = 20;
        ZestExpressionLength length = new ZestExpressionLength("response.body", len, approx);
        assertTrue(len == length.getLength());
    }

    @Test
    public void testSetLength() {
        int len = 10;
        ZestExpressionLength length = new ZestExpressionLength();
        length.setLength(len);
        assertTrue(length.getLength() == len);
    }

    @Test
    public void testGetApprox() {
        int len = 10;
        int approx = 20;
        ZestExpressionLength length = new ZestExpressionLength("response.body", len, approx);
        assertTrue(approx == length.getApprox());
    }

    @Test
    public void testSetApprox() {
        int approx = 10;
        ZestExpressionLength length = new ZestExpressionLength();
        length.setApprox(approx);
        assertTrue(approx == length.getApprox());
    }

    @Test
    public void testEvaluateInverse() {
        ZestExpressionLength length = new ZestExpressionLength("response.body", 100, 100);
        length.setInverse(true);
        TestRuntime runtime = new TestRuntime(response);
        assertTrue(length.isTrue(runtime) && !length.evaluate(runtime));
    }

    @Test
    public void testIsTrueNullBody() {
        ZestResponse resp = new ZestResponse(null, null, null, 0, 0);
        ZestExpressionLength length = new ZestExpressionLength("response.body", 100, 100);
        assertFalse(length.isTrue(new TestRuntime(resp)));
    }
}
