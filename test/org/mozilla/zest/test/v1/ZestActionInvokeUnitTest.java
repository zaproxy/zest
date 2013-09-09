/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.test.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mozilla.zest.core.v1.ZestActionInvoke;
import org.mozilla.zest.core.v1.ZestJSON;
import org.mozilla.zest.core.v1.ZestResponse;


/**
 */
@RunWith(MockitoJUnitRunner.class)
public class ZestActionInvokeUnitTest {

	/**
	 * Method testSimpleJsScript.
	 * @throws Exception
	 */
	@Test
	public void testSimpleJsScript() throws Exception {
		ZestActionInvoke inv = new ZestActionInvoke();
		inv.setVariableName("test");
		inv.setScript("test/data/simple-script.js");
		TestRuntime rt = new TestRuntime();
		
		ZestResponse resp = new ZestResponse(null, "Header prefix12345postfix", "Body Prefix54321Postfix", 200, 0);
		String result = inv.invoke(resp, rt);

		assertEquals ("abcde", result);
	}

	/**
	 * Method testSimpleJsScript.
	 * @throws Exception
	 */
	@Test
	public void testParamJsScript() throws Exception {
		ZestActionInvoke inv = new ZestActionInvoke();
		inv.setVariableName("test");
		inv.setScript("test/data/param-script.js");
		inv.setParameters(new String[]{"param=PQRST"});
		TestRuntime rt = new TestRuntime();
		
		ZestResponse resp = new ZestResponse(null, "Header prefix12345postfix", "Body Prefix54321Postfix", 200, 0);
		String result = inv.invoke(resp, rt);

		assertEquals ("PQRST", result);
	}

	/**
	 * Method testSimpleZestScript.
	 * @throws Exception
	 */
	@Test
	public void testSimpleZestScript() throws Exception {
		ZestActionInvoke inv = new ZestActionInvoke();
		inv.setVariableName("test");
		inv.setScript("test/data/simple-script.zest");
		TestRuntime rt = new TestRuntime();
		
		ZestResponse resp = new ZestResponse(null, "Header prefix12345postfix", "Body Prefix54321Postfix", 200, 0);
		String result = inv.invoke(resp, rt);

		assertEquals ("ABCDE", result);
	}

	/**
	 * Method testAssignZestScript.
	 * @throws Exception
	 */
	@Test
	public void testAssignZestScript() throws Exception {
		ZestActionInvoke inv = new ZestActionInvoke();
		inv.setVariableName("test");
		inv.setScript("test/data/assign-script.zest");
		TestRuntime rt = new TestRuntime();
		
		ZestResponse resp = new ZestResponse(null, "Header prefix12345postfix", "Body Prefix54321Postfix", 200, 0);
		String result = inv.invoke(resp, rt);

		assertEquals ("EFGHI", result);
	}

	/**
	 * Method testParamZestScript.
	 * @throws Exception
	 */
	@Test
	public void testParamZestScript() throws Exception {
		ZestActionInvoke inv = new ZestActionInvoke();
		inv.setVariableName("test");
		inv.setScript("test/data/param-script.zest");
		inv.setParameters(new String[]{"param=ZYXWV"});
		TestRuntime rt = new TestRuntime();
		
		ZestResponse resp = new ZestResponse(null, "Header prefix12345postfix", "Body Prefix54321Postfix", 200, 0);
		String result = inv.invoke(resp, rt);

		assertEquals ("ZYXWV", result);
		assertEquals ("ZYXWV", rt.getVariable("test"));
		
	}

	/**
	 * Method testBadParamZestScript.
	 * @throws Exception
	 */
	@Test
	public void testBadParamZestScript() throws Exception {
		ZestActionInvoke inv = new ZestActionInvoke();
		inv.setVariableName("test");
		inv.setScript("test/data/param-script.zest");
		inv.setParameters(new String[]{"paramZYXWV"});
		TestRuntime rt = new TestRuntime();
		
		ZestResponse resp = new ZestResponse(null, "Header prefix12345postfix", "Body Prefix54321Postfix", 200, 0);
		try {
			inv.invoke(resp, rt);
			fail("Expected an exception");
		} catch (Exception e) {
			// Expected
		}
	}

	@Test
	public void testSerialization() {
		ZestActionInvoke inv = new ZestActionInvoke();
		inv.setVariableName("test");
		inv.setParameters(new String[]{"first=AAA", "second=BBB"});
		
		inv.setScript("test/data/simple-script.js");
		
		String str = ZestJSON.toString(inv);
		
		ZestActionInvoke inv2 = (ZestActionInvoke) ZestJSON.fromString(str);
		
		assertEquals(inv.getElementType(), inv2.getElementType());
		assertEquals(inv.getVariableName(), inv2.getVariableName());
		assertEquals(inv.getScript(), inv2.getScript());
		assertEquals(inv.getParameters().length, inv2.getParameters().length);
		for (int i=0; i < inv.getParameters().length; i++) {
			assertEquals(inv.getParameters()[i], inv2.getParameters()[i]);
		}
	}

}
