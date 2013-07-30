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
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mozilla.zest.core.v1.ZestLoopToken;

@RunWith(MockitoJUnitRunner.class)
public class ZestLoopTokenUnitTest{

	@Test
	public void testZestLoopTokenString() {
		String token="A";
		ZestLoopToken<String> lts=new ZestLoopToken<String>(token);
		assertTrue(token.equals(lts.getValue()));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testZestLoopTokenStringNullValue(){
		new ZestLoopToken<String>(null);
		fail();
	}
	@Test
	public void testDeepCopyEquals() {
		String token="ABBA";
		ZestLoopToken<String> lts=new ZestLoopToken<String>(token);
		ZestLoopToken<String> copy=lts.deepCopy();
		assertTrue(copy.equals(lts));
	}
	
	@Test
	public void testEqualsZestLoopTokenOfT() {
		String token="ABBA";
		ZestLoopToken<String> lts=new ZestLoopToken<String>(token);
		ZestLoopToken<String> ltGENERIC=new ZestLoopToken<String>(token);
		assertTrue(lts.equals(ltGENERIC));
	}

	@Test
	public void testGetValue() {
		String token="ABBA";
		ZestLoopToken<String> lts=new ZestLoopToken<String>(token);
		assertTrue(token.equals(lts.getValue()));
	}
	
	@Test
	public void testZestLoopTokenGenericDeepCopy(){
		String token="ABBa";
		ZestLoopToken<String> zltG=new ZestLoopToken<>(token);
		ZestLoopToken<String> copy=zltG.deepCopy();
		assertTrue(zltG.equals(copy));
	}
	int value=100;
	@Test
	public void testZestLoopTokenIntegerGetValue() {
		ZestLoopToken<Integer> lti=new ZestLoopToken<Integer>(value);
		assertTrue(value==lti.getValue());
	}

	@Test
	public void testDeepCopy() {
		ZestLoopToken<Integer> lti=new ZestLoopToken<Integer>(value);
		ZestLoopToken<Integer> copy=lti.deepCopy();
		assertTrue(lti.getValue()==copy.getValue());
	}

//	@Test
//	public void testEqualsZestLoopTokenOfT() {
//		ZestLoopToken<Integer> lti=new ZestLoopToken<Integer>(value);
//		ZestLoopToken<Integer> ltG=new ZestLoopToken<>(value);
//		assertTrue(lti.getValue()==ltG.getValue());
//	}

	@Test
	public void testDeepCopyIntegerToGeneric() {
		ZestLoopToken<Integer> lti=new ZestLoopToken<Integer>(value);
		ZestLoopToken<Integer> ltG=lti.deepCopy();
		assertTrue(lti.equals(ltG));
	}
	@Test(expected=IllegalArgumentException.class)
	public void testZestLoopTokenIntegerNullValue(){
		new ZestLoopToken<Integer>(null);
	}
	@Test
	public void testDeepCopyGenericToInteger(){
		ZestLoopToken<Integer> ltG=new ZestLoopToken<Integer>(value);
		ZestLoopToken<Integer> lti=ltG.deepCopy();
		assertTrue(ltG.equals(lti));
	}
	@Test
	public void testEqualsNoTypeMismatch(){
		ZestLoopToken<Integer> lti=new ZestLoopToken<Integer>(0);
		ZestLoopToken<String> lts=new ZestLoopToken<String>("0");
		assertFalse(lti.equals(lts));
	}
}
