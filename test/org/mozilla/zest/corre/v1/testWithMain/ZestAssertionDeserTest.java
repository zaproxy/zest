/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package mozilla.zest.corre.v1.testWithMain;

import org.mozilla.zest.core.v1.ZestAssertion;
import org.mozilla.zest.core.v1.ZestExpressionLength;
import org.mozilla.zest.core.v1.ZestExpressionStatusCode;
import org.mozilla.zest.core.v1.ZestJSON;

public class ZestAssertionDeserTest {
	public void main(String[] args){
		ZestExpressionStatusCode code=new ZestExpressionStatusCode(200);
		ZestExpressionLength length=new ZestExpressionLength(2096, 1);
		ZestAssertion ass1=new ZestAssertion(length);
		ZestAssertion ass2=new ZestAssertion(code);
//		String ass1String=ZestJSON.toString(ass1);
//		String ass2String=ZestJSON.toString(ass2);
//		ZestAssertion ass1COPY=(ZestAssertion) ZestJSON.fromString(ass1String);
//		ZestAssertion ass2COPY=(ZestAssertion) ZestJSON.fromString(ass2String);
//		String ass1StringCOPY=ZestJSON.toString(ass1COPY);
//		String ass2StringCOPY=ZestJSON.toString(ass2COPY);
//		System.out.println("ASS2: "+ass2String.equals(ass2StringCOPY));
//		System.out.println("ASS1: "+ass1String.equals(ass1StringCOPY));
	}
}
