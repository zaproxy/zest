package org.mozilla.zest.core.v1;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
public class ZestExpressionAnd extends ZestExpression implements
		ZestConditionalElement {
	public static final String DEFAULT_NAME="default_and_";
	private static int counter=0;
	/**
	 * Main construptor
	 * 
	 * @param parent
	 *            the parent of this Conditional Element
	 */
	public ZestExpressionAnd(ZestConditionalElement parent) {
		super(parent);
		this.setName(DEFAULT_NAME+counter++);
	}

	/**
	 * Construptor
	 * 
	 * @param parent
	 *            the parent of this conditional Element
	 * @param andList
	 *            the list of AND clauses
	 */
	public ZestExpressionAnd(ZestConditionalElement parent,
			List<ZestConditionalElement> andList) {
		super(parent, andList);
		this.setName(DEFAULT_NAME+counter++);
	}

	@Override
	public ZestElement deepCopy() {
		ZestExpressionAnd copy = new ZestExpressionAnd(getParent());
		List<ZestConditionalElement> copyOfChildren = new LinkedList<>();
		for (ZestConditionalElement elem : copyOfChildren) {
			copy.addChildCondition(elem);
		}
		return copy;
	}

	@Override
	public boolean evaluate(ZestResponse response) {
		boolean toReturn = true;
		for (ZestConditionalElement con : getChildrenCondition()) {
			toReturn = toReturn && con.evaluate(response);// compute AND for each child
		}
		return isNot() ? (!toReturn) : toReturn;
	}
	@Override
	public int getCount(){
		return counter;
	}
}
