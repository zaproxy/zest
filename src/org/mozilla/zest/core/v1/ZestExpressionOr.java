package org.mozilla.zest.core.v1;

import java.util.List;

public class ZestExpressionOr extends ZestExpression implements
		ZestExpressionElement {
	public static final String DEFAULT_NAME="default_or_";
		private static int counter=0;
	/**
	 * Main construptor
	 * @param parent the parent of this ZestConditionalElement
	 */
	public ZestExpressionOr(ZestExpressionElement parent) {
		super(parent);
		this.setName(DEFAULT_NAME+counter++);
	}
	/**
	 * Construptor
	 * @param parent the parent of this Conditional Element
	 * @param children the list of the OR clauses
	 */
	public ZestExpressionOr(ZestExpressionElement parent, List<ZestExpressionElement> children){
		super(parent, children);
		this.setName(DEFAULT_NAME+counter++);
	}

	@Override
	public boolean evaluate(ZestResponse response) {
		boolean toReturn = true;
		for (ZestExpressionElement con : getChildrenCondition()) {
			toReturn = toReturn || con.evaluate(response);// compute AND for each child
		}
		return isInverse() ? (!toReturn) : toReturn;
	}
	@Override
	public int getCount(){
		return counter;
	}
}
