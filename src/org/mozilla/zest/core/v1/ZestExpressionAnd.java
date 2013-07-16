/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.util.LinkedList;
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
public class ZestExpressionAnd extends ZestStructuredExpression{
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
	public ZestExpressionAnd deepCopy() {
		List<ZestExpressionElement> copyChildren = new LinkedList<>();
		if (getChildrenCondition()!=null) {
			for (int i = 0; i < getChildrenCondition().size(); i++) {
				copyChildren.add(getChildrenCondition().get(i).deepCopy());
			}
		}
		ZestExpressionAnd copy=new ZestExpressionAnd(copyChildren);
		return copy;
	}

	@Override
	public boolean isTrue(ZestResponse response) {
		if(getChildrenCondition().isEmpty()){
			return false;
		}
		boolean toReturn = true;
		for (ZestExpressionElement con : getChildrenCondition()) {
			toReturn = toReturn && con.evaluate(response);// compute AND for each child
			if(!toReturn) {
				break;//lazy evaluation
			}
		}
		return toReturn;
	}
}
