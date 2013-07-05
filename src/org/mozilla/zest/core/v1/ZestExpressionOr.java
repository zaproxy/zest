package org.mozilla.zest.core.v1;

import java.util.LinkedList;
import java.util.List;

public class ZestExpressionOr extends ZestExpression implements
		ZestConditionalElement {
	public static final String DEFAULT_NAME="default_or_";
		private static int counter=0;
	/**
	 * Main construptor
	 * @param parent the parent of this ZestConditionalElement
	 */
	public ZestExpressionOr(ZestConditionalElement parent) {
		super(parent);
		this.setName(DEFAULT_NAME+counter++);
	}
	/**
	 * Construptor
	 * @param parent the parent of this Conditional Element
	 * @param children the list of the OR clauses
	 */
	public ZestExpressionOr(ZestConditionalElement parent, List<ZestConditionalElement> children){
		super(parent, children);
		this.setName(DEFAULT_NAME+counter++);
	}

	@Override
	public boolean evaluate(ZestResponse response) {
		boolean toReturn = true;
		for (ZestConditionalElement con : getChildrenCondition()) {
			toReturn = toReturn || con.evaluate(response);// compute AND for each child
		}
		return isInverse() ? (!toReturn) : toReturn;
	}
	@Override
	public int getCount(){
		return this.counter;
	}
}
