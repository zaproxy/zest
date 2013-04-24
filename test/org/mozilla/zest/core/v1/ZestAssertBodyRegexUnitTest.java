/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.zest.core.v1;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mozilla.zest.core.v1.ZestAssertBodyRegex;
import org.mozilla.zest.core.v1.ZestResponse;

@RunWith(MockitoJUnitRunner.class)
public class ZestAssertBodyRegexUnitTest {

	@Test
	public void testSimpleIncRegex() throws Exception {
		ZestAssertBodyRegex ze = new ZestAssertBodyRegex();
		ze.setRegex("test123");
		assertTrue(ze.isValid(new ZestResponse(null, "", "aaaatest123", 200, 0)));
	}

	@Test
	public void testSimpleExcRegex() throws Exception {
		ZestAssertBodyRegex ze = new ZestAssertBodyRegex();
		ze.setRegex("test123");
		assertFalse(ze.isValid(new ZestResponse(null, "", "aaaatst123", 200, 0)));
	}

	@Test
	public void testSimpleIncInvRegex() throws Exception {
		ZestAssertBodyRegex ze = new ZestAssertBodyRegex();
		ze.setRegex("test123");
		ze.setInverse(true);
		assertFalse(ze.isValid(new ZestResponse(null, "", "aaaatest123", 200, 0)));
	}

	@Test
	public void testSimpleExcInvRegex() throws Exception {
		ZestAssertBodyRegex ze = new ZestAssertBodyRegex();
		ze.setRegex("test123");
		ze.setInverse(true);
		assertTrue(ze.isValid(new ZestResponse(null, "", "aaaatst123", 200, 0)));
	}

}
