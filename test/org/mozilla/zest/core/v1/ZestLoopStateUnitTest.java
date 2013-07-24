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
import org.mozilla.zest.core.v1.ZestLoopState;
import org.mozilla.zest.core.v1.ZestLoopToken;
import org.mozilla.zest.core.v1.ZestLoopTokenSet;

@RunWith(MockitoJUnitRunner.class)
public class ZestLoopStateUnitTest {
	String[] values={"A","B","C","D","D"};
	ZestLoopTokenSet<String> set=new ZestLoopTokenSet<>(values);
	@Test
	public void testZestLoopState() {
		ZestLoopState<String> state=new ZestLoopState<>(set);
		assertFalse(state.isLastState());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testZestLoopStateNull(){
		new ZestLoopState<>(null);
		fail();
	}

	@Test
	public void testGetCurrentToken() {
		int index=0;
		set.setIndexStart(index);
		ZestLoopState<String> state=new ZestLoopState<>(set);
		assertTrue(state.getCurrentToken().equals(new ZestLoopToken<String>(values[index])));
	}

	@Test
	public void testGetCurrentIndex() {
		int index=3;
		set.setIndexStart(index);
		ZestLoopState<String> state=new ZestLoopState<>(set);
		state.increase();
		assertTrue(state.getCurrentIndex()==index+1);
	}

	@Test
	public void testIncrease() {
		set.setIndexStart(0);
		ZestLoopState<String> state=new ZestLoopState<>(set);
		int currentIndex=state.getCurrentIndex();
		boolean increasable=state.increase();
		if(!increasable){
			fail();
		}
		int newIndex=state.getCurrentIndex();
		assertTrue(currentIndex+1==newIndex && values[newIndex].equals(state.getCurrentToken().getValue()));
	}

	@Test
	public void testEndState() {
		set.setIndexStart(0);
		ZestLoopState<String> state=new ZestLoopState<>(set);
		state.endState();
		assertTrue(state.isLastState());
	}

	@Test
	public void testDeepCopy() {
		set.setIndexStart(2);
		ZestLoopState<String> state=new ZestLoopState<>(set);
		ZestLoopState<String> copy=state.deepCopy();
		if(state.getCurrentIndex()!=copy.getCurrentIndex()){
			fail("Not same index!");
		}
		for(int i=0; i<0; i++){
			ZestLoopToken<String> expected=state.getTokenSet().getToken(i);
			ZestLoopToken<String> obtained=copy.getTokenSet().getToken(i);
			String msg=i+" obtained "+obtained.getValue()+" instead of "+expected.getValue();
			assertTrue(msg, expected.equals(obtained));
		}
	}

	@Test
	public void testIsLastState() {
		set.setIndexStart(0);
		ZestLoopState<String> state=new ZestLoopState<>(set);
		for(int i=0; i<13; i++){
			state.increase();
			if(state.getCurrentIndex()==set.size()){
				assertTrue(state.isLastState());
				break;
			}
		}
	}

}
