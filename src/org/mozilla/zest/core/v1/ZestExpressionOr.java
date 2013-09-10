/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class ZestExpressionOr.
 */
public class ZestExpressionOr extends ZestStructuredExpression {

	private static Pattern pattern = null;

	/**
	 * Main construptor.
	 * 
	 */
	public ZestExpressionOr() {
		super();
	}

	/**
	 * Construptor.
	 * 
	 * @param children
	 *            the list of the OR clauses
	 */
	public ZestExpressionOr(List<ZestExpressionElement> children) {
		super(children);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mozilla.zest.core.v1.ZestExpressionElement#isTrue(org.mozilla.zest
	 * .core.v1.ZestResponse)
	 */
	@Override
	public boolean isTrue(ZestRuntime runtime) {
		boolean toReturn = false;
		for (ZestExpressionElement con : getChildrenCondition()) {
			toReturn = toReturn || con.evaluate(runtime);// compute OR for each
															// child
			if (toReturn) {
				break;// lazy evaluation
			}
		}
		return toReturn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.zest.core.v1.ZestExpression#deepCopy()
	 */
	@Override
	public ZestExpressionOr deepCopy() {
		List<ZestExpressionElement> copyChildren = new LinkedList<>();
		if (getChildrenCondition() != null) {
			for (int i = 0; i < getChildrenCondition().size(); i++) {
				copyChildren.add(getChildrenCondition().get(i).deepCopy());
			}
		}
		ZestExpressionOr copy = new ZestExpressionOr(copyChildren);
		return copy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (this.getChildrenCondition().isEmpty()) {
			return "Empty OR";
		} else {
			String expression = (isInverse() ? "NOT ( " : "( ");
			for (int i = 0; i < this.getChildrenCondition().size() - 1; i++) {
				expression += " " + this.getChild(i).toString() + " ) OR ( ";
			}
			expression += this.getChild(this.getChildrenCondition().size() - 1)
					.toString() + " )";
			return expression;
		}
	}

	public static boolean isLiteralInstance(String literal) {
		return getPattern().matcher(literal).matches();
	}

	public static Pattern getPattern() {
		if (pattern == null) {
			pattern=Pattern.compile("(((NOT\\s)?.*(OR.*)+)|(Empty\\sOR))");
		}
		return pattern;
	}
}
