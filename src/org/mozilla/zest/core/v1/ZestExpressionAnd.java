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
public class ZestExpressionAnd extends ZestExpression implements
		ZestExpressionElement {
	public static final String DEFAULT_NAME="default_and_";
	private static int counter=0;
	/**
	 * Main construptor
	 * 
	 * @param parent
	 *            the parent of this Conditional Element
	 */
	public ZestExpressionAnd(ZestExpressionElement parent) {
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
	public ZestExpressionAnd(ZestExpressionElement parent,
			List<ZestExpressionElement> andList) {
		super(parent, andList);
		this.setName(DEFAULT_NAME+counter++);
	}

	@Override
	public boolean evaluate(ZestResponse response) {
		boolean toReturn = true;
		for (ZestExpressionElement con : getChildrenCondition()) {
			toReturn = toReturn && con.evaluate(response);// compute AND for each child
		}
		return isInverse() ? (!toReturn) : toReturn;
	}
	@Override
	public int getCount(){
		return counter;
	}
}
