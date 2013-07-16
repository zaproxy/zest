/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.util.List;

public class ZestExpressionOr extends ZestStructuredExpression{
	/**
	 * Main construptor
	 * @param parent the parent of this ZestConditionalElement
	 */
	public ZestExpressionOr() {
		super();
	}
	/**
	 * Construptor
	 * @param parent the parent of this Conditional Element
	 * @param children the list of the OR clauses
	 */
	public ZestExpressionOr(List<ZestExpressionElement> children){
		super(children);
	}

	@Override
	public boolean isTrue(ZestResponse response){
		boolean toReturn = false;
		for (ZestExpressionElement con : getChildrenCondition()) {
			toReturn = toReturn || con.evaluate(response);// compute OR for each child
			if(toReturn) break;//lazy evaluation
		}
		return toReturn;
	}
	@Override
	public ZestExpressionOr deepCopy() {
		return (ZestExpressionOr)super.deepCopy();
	}
}
