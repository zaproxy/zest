/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package mozilla.zest.core.v1;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mozilla.zest.core.v1.ZestLoopToken;
import org.mozilla.zest.core.v1.ZestLoopTokenSet;
import org.mozilla.zest.core.v1.ZestLoopTokenStringSet;
@RunWith(MockitoJUnitRunner.class)
public class ZestLoopTokenSetUnitTest {
	String[] arrayValueS={"A","B","C","D","D"};
	Integer[] arrayValueI={1,2,3,4,5,4,5};
	@Test
	public void testZestLoopTokenSetString() {
		ZestLoopTokenStringSet set=new ZestLoopTokenStringSet();
		assertFalse(set.getTokens()==null);
	}


	@Test
	public void testGetToken() {
		ZestLoopTokenStringSet set=new ZestLoopTokenStringSet(arrayValueS);
		for(int i=0; i<arrayValueS.length; i++){
			String msg=i+" expected "+arrayValueS[i]+", obtained "+set.getToken(i).getValue();
			assertTrue(msg, arrayValueS[i].equals(set.getToken(i).getValue()));
		}
	}

	@Test
	public void testIndexOf() {
		ZestLoopTokenStringSet set=new ZestLoopTokenStringSet(arrayValueS);
		int indexConsidered=3;
		ZestLoopToken<String> token=set.getToken(indexConsidered);
		int indexFound=set.indexOf(token);
		assertTrue(indexFound==indexConsidered);
	}

	@Test
	public void testRemoveToken() {
		ZestLoopTokenStringSet set=new ZestLoopTokenStringSet(arrayValueS);
		ZestLoopToken<String> tokenRemoved=set.getFirstConsideredToken();
		boolean isPresentBeforeRemove=set.indexOf(tokenRemoved)>=0;
		set.removeToken(0);
		assertTrue(isPresentBeforeRemove && set.indexOf(tokenRemoved)<0);
	}

	@Test
	public void testSetIndexStart() {
		ZestLoopTokenStringSet set=new ZestLoopTokenStringSet(arrayValueS);
		set.setIndexStart(2);
		assertTrue(set.getIndexStart()==2);
	}

	@Test
	public void testReplace() {
		int indexOfReplace=2;
		String valueOfNewToken="CHANGED";
		ZestLoopTokenStringSet set=new ZestLoopTokenStringSet(arrayValueS);
		ZestLoopToken<String> newToken=new ZestLoopToken<String>(valueOfNewToken);
		set.replace(indexOfReplace, newToken);
		assertTrue(set.getToken(indexOfReplace).getValue().equals(valueOfNewToken));
	}

	@Test
	public void testSize() {
		ZestLoopTokenStringSet set=new ZestLoopTokenStringSet(arrayValueS);
		int prevSize=set.size();
		set.addToken(new ZestLoopToken<String>("ernvgqiup"));
		assertTrue(prevSize==arrayValueS.length && set.size()==prevSize+1);
	}

	@Test
	public void testDeepCopy() {
		ZestLoopTokenStringSet set=new ZestLoopTokenStringSet(arrayValueS);
		ZestLoopTokenSet<String> copy=set.deepCopy();
		for(int i=0; i<set.size(); i++){
			ZestLoopToken<String> expected=set.getToken(i);
			ZestLoopToken<String> obtained=copy.getToken(i);
			String msg=i+" obtained "+obtained.getValue()+" instead of "+expected.getValue();
			assertTrue(msg,expected.equals(obtained));
		}
	}
}
