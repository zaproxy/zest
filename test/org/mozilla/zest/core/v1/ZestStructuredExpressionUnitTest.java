/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.zest.core.v1;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.mozilla.zest.core.v1.ZestExpressionAnd;
import org.mozilla.zest.core.v1.ZestExpressionElement;
import org.mozilla.zest.core.v1.ZestExpressionLength;
import org.mozilla.zest.core.v1.ZestExpressionStatusCode;

public class ZestStructuredExpressionUnitTest {
	@Test
	public void testDeepCopySingleAndSameChildrenSize() {
		ZestExpressionAnd and = new ZestExpressionAnd();
		and.addChildCondition(new ZestExpressionStatusCode(100));
		and.addChildCondition(new ZestExpressionLength(1, 1));
		ZestExpressionAnd copy = and.deepCopy();
		assertTrue(and.getChildrenCondition().size() == copy
				.getChildrenCondition().size());
	}

	@Test
	public void testDeepCopySingleAndSameChildrenClasses() {
		List<ZestExpressionElement> children = new LinkedList<>();
		for (int i = 0; i < 10; i++) {
			children.add(new ZestExpressionLength());
		}
		ZestExpressionAnd and = new ZestExpressionAnd(children);
		ZestExpressionAnd copy = and.deepCopy();
		for (int i = 0; i < children.size(); i++) {
			assertTrue(and.getChild(i).getClass() == copy.getChild(i)
					.getClass());
		}
	}

	@Test
	public void testDeepCopyComplexCondition() {
		ZestExpressionAnd and1 = new ZestExpressionAnd();
		ZestExpressionAnd and2 = new ZestExpressionAnd();
		ZestExpressionAnd and3 = new ZestExpressionAnd();
		ZestExpressionLength leaf = new ZestExpressionLength();
		and3.addChildCondition(leaf);
		and2.addChildCondition(and3);
		and1.addChildCondition(and2);
		ZestExpressionAnd copy1 = and1.deepCopy();
		ZestExpressionAnd copy2 = (ZestExpressionAnd) copy1.getChild(0);
		ZestExpressionAnd copy3 = (ZestExpressionAnd) copy2.getChild(0);
		ZestExpressionLength copyLeaf = (ZestExpressionLength) copy3
				.getChild(0);
		assertTrue(and1.getClass() == copy1.getClass()// TODO to many booleans
				&& and2.getClass() == copy2.getClass()
				&& copy3.getClass() == and3.getClass()
				&& copyLeaf instanceof ZestExpressionLength
				&& copyLeaf.isLeaf());
	}

	@Test
	public void testDeepCopyNoPointer() {
		LinkedList<ZestExpressionElement> children = new LinkedList<>();
		for (int i = 0; i < 10; i++) {
			children.add(new ZestExpressionStatusCode(i));
		}
		ZestExpressionAnd and = new ZestExpressionAnd(children);
		ZestExpressionAnd copy = and.deepCopy();
		and.removeChildCondition(0);
		assertTrue(and.getChildrenCondition().size() + 1 == copy
				.getChildrenCondition().size());// different size <=> no
												// pointers
	}

	@Test
	public void testIsLeaf() {// TODO maybe an exception raises if a
								// StructuredExpr has no child
		ZestExpressionAnd and = new ZestExpressionAnd();
		ZestExpressionAnd andChild = new ZestExpressionAnd();
		ZestExpressionLength lengthChild = new ZestExpressionLength();
		and.addChildCondition(andChild);
		and.addChildCondition(lengthChild);
		assertTrue(!and.getChild(0).isLeaf() && and.getChild(1).isLeaf());
	}

	@Test
	public void testRemoveFirstChild() {
		ZestExpressionAnd and = new ZestExpressionAnd();
		and.addChildCondition(new ZestExpressionLength());
		and.addChildCondition(new ZestExpressionStatusCode());
		and.removeChildCondition(0);
		assertTrue(and.getChild(0) instanceof ZestExpressionStatusCode);
	}

	@Test
	public void testRemoveLastChild() {
		int childrenSize = 10;// initial size
		ZestExpressionAnd and = new ZestExpressionAnd();
		for (int i = 0; i < childrenSize - 1; i++) {
			and.addChildCondition(new ZestExpressionLength());
		}
		and.addChildCondition(new ZestExpressionStatusCode());
		and.removeChildCondition(childrenSize - 1);// removes last child
		if (and.getChildrenCondition().size() == childrenSize)
			fail();
		else
			assertTrue(and.getChild(childrenSize - 2) instanceof ZestExpressionLength);// last
																						// of
																						// the
																						// remaining
	}

	@Test
	public void testRemoveMiddleChild() {
		int half = 5;
		ZestExpressionAnd and = new ZestExpressionAnd();
		for (int i = 0; i < half; i++) {
			and.addChildCondition(new ZestExpressionLength());
		}
		and.addChildCondition(new ZestExpressionStatusCode());
		for (int i = 0; i < half; i++) {
			and.addChildCondition(new ZestExpressionLength());
		}
		boolean middleElementIsCode = and.getChild(half).getClass()
				.equals(ZestExpressionStatusCode.class);
		if (!middleElementIsCode)
			fail();
		and.removeChildCondition(half);
		assertTrue(and.getChild(half) instanceof ZestExpressionLength);
	}

	@Test
	public void testRemoveAllChildren() {
		ZestExpressionAnd and = new ZestExpressionAnd();
		for (int i = 0; i < 10; i++) {
			if (i % 2 == 0)
				and.addChildCondition(new ZestExpressionLength());
			else
				and.addChildCondition(new ZestExpressionStatusCode());
		}
		LinkedList<ZestExpressionElement> toRemove = new LinkedList<>();
		for (int i = 0; i < 10; i += 2) {
			toRemove.add(and.getChild(i));
		}
		if (!and.removeAllChildren(toRemove))
			fail();
		else {
			for (int i = 0; i < and.getChildrenCondition().size(); i++) {
				assertTrue(
						"Element n°" + i,
						and.getChild(i).getClass()
								.equals(ZestExpressionStatusCode.class));
			}
		}
	}

	@Test
	public void testAddChildConditionZestExpressionElementInt() {
		ZestExpressionAnd and = new ZestExpressionAnd();
		for (int i = 0; i < 10; i++)
			and.addChildCondition(new ZestExpressionLength());
		Random random = new Random();
		int pos = random.nextInt(10);
		and.addChildCondition(new ZestExpressionStatusCode(), pos);
		if (and.getChildrenCondition().size() != 11)
			fail();
		for (int i = 0; i < 11; i++) {
			if (i != pos) {
				Class<ZestExpressionLength> expected = ZestExpressionLength.class;
				Class<ZestExpressionStatusCode> wrong = ZestExpressionStatusCode.class;
				String msg = "Pos: " + i + ". Expected " + expected.getName()
						+ "; obtained " + wrong.getName();
				assertTrue(msg, and.getChild(i).getClass().equals(expected));
			} else {
				Class<ZestExpressionLength> wrong = ZestExpressionLength.class;
				Class<ZestExpressionStatusCode> expected= ZestExpressionStatusCode.class;
				String msg = "Pos: " + i + ". Expected " + expected.getName()
						+ "; obtained " + wrong.getName();
				assertTrue(msg, and.getChild(i).getClass().equals(expected));
			}
		}
	}
}
