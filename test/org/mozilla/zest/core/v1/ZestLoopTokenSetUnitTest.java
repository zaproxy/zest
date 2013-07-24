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
@RunWith(MockitoJUnitRunner.class)
public class ZestLoopTokenSetUnitTest {
	String[] arrayValueS={"A","B","C","D","D"};
	Integer[] arrayValueI={1,2,3,4,5,4,5};
	@Test
	public void testZestLoopTokenSetString() {
		ZestLoopTokenSet<String> set=new ZestLoopTokenSet<String>();
		assertFalse(set.getTokens()==null);
	}

	@Test
	public void testZestLoopTokenSetListOfZestLoopTokenOfTInt() {
		ZestLoopTokenSet<Integer> set=new ZestLoopTokenSet<>();
		assertFalse(set.getTokens()==null);
	}

	@Test
	public void testZestLoopTokenSetListOfZestLoopTokenOfT() {
		ZestLoopTokenSet<ZestLoopToken<?>> set=new ZestLoopTokenSet<>();
		assertFalse(set.getTokens()==null);
	}

	@Test
	public void testGetIndexStartDefault() {
		ZestLoopTokenSet<Integer> set=new ZestLoopTokenSet<>(arrayValueI);
		assertTrue(set.getIndexStart()==0);
	}
	@Test
	public void testGetIndexStart() {
		int startIndex=2;
		ZestLoopTokenSet<Integer> set=new ZestLoopTokenSet<>(arrayValueI, startIndex);
		assertTrue(set.getIndexStart()==startIndex);
	}

	@Test
	public void testGetToken() {
		ZestLoopTokenSet<String> set=new ZestLoopTokenSet<>(arrayValueS);
		for(int i=0; i<arrayValueS.length; i++){
			String msg=i+" expected "+arrayValueS[i]+", obtained "+set.getToken(i).getValue();
			assertTrue(msg, arrayValueS[i].equals(set.getToken(i).getValue()));
		}
	}

	@Test
	public void testIndexOf() {
		ZestLoopTokenSet<String> set=new ZestLoopTokenSet<>(arrayValueS);
		int indexConsidered=3;
		ZestLoopToken<String> token=new ZestLoopToken<String>(arrayValueS[indexConsidered]);
		int indexFound=set.indexOf(token);
		assertTrue(indexFound==indexConsidered);
	}

	@Test
	public void testGetFirstConsideredToken() {
		int firstConsideredToken=2;
		ZestLoopTokenSet<String> set=new ZestLoopTokenSet<>(arrayValueS, firstConsideredToken);
		assertTrue(set.getFirstConsideredToken().equals(new ZestLoopToken<String>(arrayValueS[firstConsideredToken])));
	}

	@Test
	public void testRemoveToken() {
		ZestLoopTokenSet<String> set=new ZestLoopTokenSet<>(arrayValueS);
		ZestLoopToken<String> tokenRemoved=new ZestLoopToken<String>(arrayValueS[0]);
		boolean isPresentBeforeRemove=set.indexOf(tokenRemoved)>=0;
		set.removeToken(0);
		assertTrue(isPresentBeforeRemove && set.indexOf(tokenRemoved)<0);
	}

	@Test
	public void testSetIndexStart() {
		ZestLoopTokenSet<String> set=new ZestLoopTokenSet<>(arrayValueS);
		set.setIndexStart(2);
		assertTrue(set.getIndexStart()==2);
	}

	@Test
	public void testReplace() {
		int indexOfReplace=2;
		String valueOfNewToken="CHANGED";
		ZestLoopTokenSet<String> set=new ZestLoopTokenSet<>(arrayValueS);
		ZestLoopToken<String> newToken=new ZestLoopToken<String>(valueOfNewToken);
		set.replace(indexOfReplace, newToken);
		assertTrue(set.getToken(indexOfReplace).getValue().equals(valueOfNewToken));
	}

	@Test
	public void testSize() {
		ZestLoopTokenSet<String> set=new ZestLoopTokenSet<>(arrayValueS);
		int prevSize=set.size();
		set.addToken(new ZestLoopToken<String>("ernvgqiup"));
		assertTrue(prevSize==arrayValueS.length && set.size()==prevSize+1);
	}

	@Test
	public void testDeepCopy() {
		ZestLoopTokenSet<String> set=new ZestLoopTokenSet<>(arrayValueS);
		ZestLoopTokenSet<String> copy=set.deepCopy();
		for(int i=0; i<set.size(); i++){
			ZestLoopToken<String> expected=set.getToken(i);
			ZestLoopToken<String> obtained=copy.getToken(i);
			String msg=i+" obtained "+obtained.getValue()+" instead of "+expected.getValue();
			assertTrue(msg,expected.equals(obtained));
		}
	}
}
