package mozilla.zest.core.v1;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.theories.Theory;
import org.junit.internal.runners.statements.ExpectException;
import org.mozilla.zest.core.v1.ZestExpression;
import org.mozilla.zest.core.v1.ZestExpressionAnd;
import org.mozilla.zest.core.v1.ZestExpressionElement;
import org.mozilla.zest.core.v1.ZestExpressionLength;
import org.mozilla.zest.core.v1.ZestExpressionStatusCode;

public class ZestExpressionAndUnitTest {

	@Ignore
	public void testZestExpressionAndZestConditionalElement() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testZestExpressionAndZestConditionalElementListOfZestConditionalElement() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testEvaluate() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testZestExpressionZestConditionalElement() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testZestExpressionZestConditionalElementListOfZestConditionalElement() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testGetChildrenCondition() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testIsLeaf() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testIsRoot() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testAddChildConditionZestConditionalElement() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testAddChildConditionZestConditionalElementInt() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testGetChild() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testSetChildrenCondition() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsNot() {
		ZestExpressionAnd and = new ZestExpressionAnd(null);
		assertFalse(and.isInverse());
	}

	@Test
	public void testSetNot() {
		ZestExpressionAnd and = new ZestExpressionAnd(null);
		assertFalse(and.isInverse());
		and.setInverse(true);
		assertTrue(and.isInverse());
	}
}
