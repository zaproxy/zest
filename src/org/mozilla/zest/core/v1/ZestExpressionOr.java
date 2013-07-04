package org.mozilla.zest.core.v1;

import java.util.LinkedList;
import java.util.List;

public class ZestExpressionOr extends ZestExpression implements
		ZestConditionalElement {
	/**
	 * Main construptor
	 * @param parent the parent of this ZestConditionalElement
	 */
	public ZestExpressionOr(ZestConditionalElement parent) {
		super(parent);
	}
	/**
	 * Construptor
	 * @param parent the parent of this Conditional Element
	 * @param children the list of the OR clauses
	 */
	public ZestExpressionOr(ZestConditionalElement parent, List<ZestConditionalElement> children){
		super(parent, children);
	}

	@Override
	public boolean evaluate() {
		boolean toReturn = true;
		for (ZestConditionalElement con : getChildrenCondition()) {
			toReturn = toReturn || con.evaluate();// compute AND for each child
		}
		return isNot() ? (!toReturn) : toReturn;
	}

	@Override
	public ZestElement deepCopy() {
		ZestExpressionOr copy = new ZestExpressionOr(getParent());
		List<ZestConditionalElement> copyOfChildren = new LinkedList<>();
		for (ZestConditionalElement elem : copyOfChildren) {
			copy.addChildCondition(elem);
		}
		return copy;
	}

}
