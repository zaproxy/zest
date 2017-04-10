/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.test.v1;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mozilla.zest.core.v1.ZestActionInvoke;
import org.mozilla.zest.core.v1.ZestJSON;
import org.mozilla.zest.core.v1.ZestResponse;


/**
 */
public class ZestActionInvokeUnitTest {

	/**
	 * Method testSimpleJsScript.
	 * @throws Exception
	 */
	@Test
	public void testSimpleJsScript() throws Exception {
		ZestActionInvoke inv = new ZestActionInvoke();
		inv.setVariableName("test");
		inv.setScript(ZestActionInvokeUnitTest.class.getResource("/data/simple-script.js").getPath());
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
		inv.setScript(ZestActionInvokeUnitTest.class.getResource("/data/param-script.js").getPath());
		List<String[]> params = new ArrayList<String[]>();
		params.add(new String [] {"param", "PQRST"});
		inv.setParameters(params);
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
		inv.setScript(ZestActionInvokeUnitTest.class.getResource("/data/simple-script.zest").getPath());
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
		inv.setScript(ZestActionInvokeUnitTest.class.getResource("/data/assign-script.zest").getPath());
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
		inv.setScript(ZestActionInvokeUnitTest.class.getResource("/data/param-script.zest").getPath());
		List<String[]> params = new ArrayList<String[]>();
		params.add(new String [] {"param", "ZYXWV"});
		inv.setParameters(params);
		TestRuntime rt = new TestRuntime();
		
		ZestResponse resp = new ZestResponse(null, "Header prefix12345postfix", "Body Prefix54321Postfix", 200, 0);
		String result = inv.invoke(resp, rt);

		assertEquals ("ZYXWV", result);
		assertEquals ("ZYXWV", rt.getVariable("test"));
		
	}

	@Test
	public void testSerialization() {
		ZestActionInvoke inv = new ZestActionInvoke();
		inv.setVariableName("test");
		List<String[]> params = new ArrayList<String[]>();
		params.add(new String [] {"first", "AAA"});
		params.add(new String [] {"second", "BBB"});
		inv.setParameters(params);
		
		inv.setScript(ZestActionInvokeUnitTest.class.getResource("/data/simple-script.js").getPath());
		
		String str = ZestJSON.toString(inv);
		
		ZestActionInvoke inv2 = (ZestActionInvoke) ZestJSON.fromString(str);
		
		assertEquals(inv.getElementType(), inv2.getElementType());
		assertEquals(inv.getVariableName(), inv2.getVariableName());
		assertEquals(inv.getScript(), inv2.getScript());
		assertEquals(inv.getParameters().size(), inv2.getParameters().size());
		for (int i=0; i < inv.getParameters().size(); i++) {
			assertEquals(inv.getParameters().get(i).length, inv2.getParameters().get(i).length);
			for (int j=0; j < inv.getParameters().get(i).length; j++) {
				assertEquals(inv.getParameters().get(i)[j], inv2.getParameters().get(i)[j]);
			}
		}
	}

}
