/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.test.v1;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mozilla.zest.core.v1.ZestActionFail;
import org.mozilla.zest.core.v1.ZestConditional;
import org.mozilla.zest.core.v1.ZestLoopStateString;
import org.mozilla.zest.core.v1.ZestLoopString;
import org.mozilla.zest.core.v1.ZestStatement;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class ZestLoopStringUnitTest {
	String[] values = { "1", "2", "3", "4", "5", "6", "7" };

	@Test
	public void testZestLoopString() {
		ZestLoopString loop = new ZestLoopString(0, values);
		assertTrue(loop.getCurrentState() != null);
	}

	@Test
	public void testZestLoopStringStringArrayIntListOfZestStatement() {
		LinkedList<ZestStatement> statements = new LinkedList<>();
		int firstIndex = 3;
		for (int i = 0; i < firstIndex - 1; i++) {
			statements.add(new ZestConditional(i));
		}
		statements.add(new ZestLoopString(firstIndex, values));
		ZestLoopString loop = new ZestLoopString(firstIndex, values, statements);
		boolean rightStmtPos = loop.getStatement(firstIndex).getClass()
				.equals(ZestLoopString.class);
		ZestLoopStateString state = (ZestLoopStateString) loop
				.getCurrentState();
		boolean rightSetSize = state.getTokenSet().size() == values.length;
		assertTrue("right statements position", rightStmtPos);
		assertTrue("right Set Size", rightSetSize);
	}

	@Test
	public void testLoop() {
		List<ZestStatement> stmts = new LinkedList<>();
		stmts.add(new ZestConditional());
		ZestLoopString loop = new ZestLoopString(0, values, stmts);
		int stopIndex = 4;
		for (int i = 0; i < stopIndex; i++) {
			loop.loop();
		}
		ZestLoopStateString state = (ZestLoopStateString) loop
				.getCurrentState();
		boolean rightIndex = state.getCurrentIndex() == stopIndex;
		boolean rightValue = state.getCurrentToken().equals(
				values[state.getCurrentIndex()]);
		assertTrue("right index", rightIndex);
		assertTrue("right value", rightValue);
		assertFalse("not last state", state.isLastState());
	}

	@Test
	public void testEndLoop() {
		ZestLoopString loop = new ZestLoopString(0, values,
				new LinkedList<ZestStatement>());
		loop.endLoop();
		assertTrue(loop.getCurrentState().isLastState());
	}

	@Test
	public void testAddStatement() {
		ZestLoopString loop = new ZestLoopString(values);
		loop.addStatement(new ZestLoopString(values));
		assertTrue(loop.getStatement(0).getClass().equals(ZestLoopString.class));
	}

	@Test
	public void testGetLast() {
		ZestLoopString loop = new ZestLoopString(values);
		for (int i = 0; i < 10; i++) {
			loop.addStatement(new ZestConditional());
		}
		loop.addStatement(new ZestLoopString(values));
		assertTrue(loop.getLast().getClass().equals(ZestLoopString.class));
	}

	@Test
	public void testDeepCopy() {
		ZestLoopString loop = new ZestLoopString(values);
		loop.addStatement(new ZestConditional());
		loop.addStatement(new ZestLoopString(values));
		loop.addStatement(new ZestActionFail());
		ZestLoopString copy = (ZestLoopString) loop.deepCopy();
		assertTrue("same state",
				copy.getCurrentState().equals(loop.getCurrentState()));
	}

	@Test
	public void testGetValues() {
		ZestLoopString loop = new ZestLoopString(values);
		String[] valuesObtained = loop.getValues();
		if (valuesObtained.length != values.length) {
			fail("The two arrays do not have same length!");
		}
		for (int i = 0; i < values.length; i++) {
			assertTrue(i + " expected " + values[i] + " instead of "
					+ valuesObtained[i], valuesObtained[i].equals(values[i]));
		}
	}
}
