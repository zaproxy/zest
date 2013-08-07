/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.test.v1;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mozilla.zest.core.v1.ZestAssertion;
import org.mozilla.zest.core.v1.ZestExpressionRegex;
import org.mozilla.zest.core.v1.ZestResponse;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class ZestAssertBodyRegexUnitTest {

	/**
	 * Method testSimpleIncRegex.
	 * @throws Exception
	 */
	@Test
	public void testSimpleIncRegex() throws Exception {
		ZestExpressionRegex regex=new ZestExpressionRegex("BODY", "test123");
		ZestAssertion ze=new ZestAssertion(regex);
		assertTrue(ze.isValid(new ZestResponse(null, "", "aaaatest123", 200, 0)));
	}

	/**
	 * Method testSimpleExcRegex.
	 * @throws Exception
	 */
	@Test
	public void testSimpleExcRegex() throws Exception {
		ZestExpressionRegex regex=new ZestExpressionRegex("BODY", "test123");
		ZestAssertion ze=new ZestAssertion(regex);
		assertFalse(ze.isValid(new ZestResponse(null, "", "aaaatst123", 200, 0)));
	}

	/**
	 * Method testSimpleIncInvRegex.
	 * @throws Exception
	 */
	@Test
	public void testSimpleIncInvRegex() throws Exception {
		ZestExpressionRegex regex=new ZestExpressionRegex("BODY", "test123", true);
		ZestAssertion ze=new ZestAssertion(regex);
		assertFalse(ze.isValid(new ZestResponse(null, "", "aaaatest123", 200, 0)));
	}

	/**
	 * Method testSimpleExcInvRegex.
	 * @throws Exception
	 */
	@Test
	public void testSimpleExcInvRegex() throws Exception {
		ZestExpressionRegex regex=new ZestExpressionRegex("BODY", "test123",true);
		ZestAssertion ze=new ZestAssertion(regex);
		assertTrue(ze.isValid(new ZestResponse(null, "", "aaaatst123", 200, 0)));
	}

}
