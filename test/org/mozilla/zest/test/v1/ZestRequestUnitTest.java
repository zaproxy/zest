/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.zest.test.v1;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mozilla.zest.core.v1.ZestRequest;
import org.mozilla.zest.core.v1.ZestVariables;


/**
 */
@RunWith(MockitoJUnitRunner.class)
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
	}

}
