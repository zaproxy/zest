/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.test.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.Date;

import org.junit.Test;
import org.mozilla.zest.core.v1.ZestCookie;
import org.mozilla.zest.core.v1.ZestRequest;
import org.mozilla.zest.core.v1.ZestVariables;


/**
 */
public class ZestRequestUnitTest {

	/**
	 * Method testTokenReplacement.
	 * @throws Exception
	 */
	@Test
	public void testTokenReplacement() throws Exception {
		ZestRequest req = new ZestRequest ();
		
		req.setUrl(new URL("http://www.example.com/app/{{token1}}"));
		req.setHeaders("Set-Cookie: test={{token2}}");
		req.setData("test={{token3}}&user=12{{token3}}34");
		req.addCookie(new ZestCookie("{{token}}.{{token3}}", "12{{token3}}34", "56{{token2}}78", "/{{token1}}A/{{token2}}B", new Date(), true));
		
		ZestRequest req2 = req.deepCopy();
		
		ZestVariables tokens = new ZestVariables();
		tokens.setTokenStart("{{");
		tokens.setTokenEnd("}}");
		tokens.addVariable("token", "ABC");
		tokens.addVariable("token1", "DEFG");
		tokens.addVariable("token2", "GHI");
		tokens.addVariable("token3", "JKL");
		req2.replaceTokens(tokens);
		
		assertEquals ("http://www.example.com/app/DEFG", req2.getUrl().toString());
		assertEquals ("Set-Cookie: test=GHI", req2.getHeaders());
		assertEquals ("test=JKL&user=12JKL34", req2.getData());
		assertEquals ("ABC.JKL", req2.getZestCookies().get(0).getDomain());
		assertEquals ("12JKL34", req2.getZestCookies().get(0).getName());
		assertEquals ("56GHI78", req2.getZestCookies().get(0).getValue());
		assertEquals ("/DEFGA/GHIB", req2.getZestCookies().get(0).getPath());
	}

}
