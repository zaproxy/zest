package org.mozilla.zest.core.v1;

import java.util.List;

/**
 * 
 /* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file, You
 * can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * This class represent a List of AND clauses
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
public class ZestExpressionAnd extends ZestStructuredExpression implements
		ZestExpressionElement {
	/**
	 * Main construptor
	 * 
	 * @param parent
	 *            the parent of this Conditional Element
	 */
	public ZestExpressionAnd() {
		super();
	}

	/**
	 * Construptor
	 * 
	 * @param parent
	 *            the parent of this conditional Element
	 * @param andList
	 *            the list of AND clauses
	 */
	public ZestExpressionAnd(List<ZestExpressionElement> andList) {
		super(andList);
	}

	@Override
	public boolean evaluate(ZestResponse response) {
		boolean toReturn = true;
		for (ZestExpressionElement con : getChildrenCondition()) {
			toReturn = toReturn && con.evaluate(response);// compute AND for each child
			if(!toReturn) break;//lazy evaluation
		}
		return isInverse() ? (!toReturn) : toReturn;
	}

	@Override
	public ZestExpressionAnd deepCopy() {
		ZestExpressionAnd copy=new ZestExpressionAnd();
		for(ZestExpressionElement child:getChildrenCondition())
			copy.addChildCondition((ZestExpression) child.deepCopy());
		return copy;
	}
}
