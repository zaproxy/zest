package org.mozilla.zest.core.v1;


public abstract class ZestExpression extends ZestElement implements
		ZestExpressionElement {
	private boolean not = false;
//	private static int counter = 0;

	public ZestExpression() {
		super();
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public boolean isRoot() {
		return false;
	}
	public boolean isInverse() {
		return not;
	}

	public void setInverse(boolean not) {
		this.not = not;
	}

//	/**
//	 * returns the number of ZestExpression created
//	 * 
//	 * @return the number of the ZestExpression created
//	 */
//	public int getCount() {
//		return counter;
//	}

	public abstract ZestExpression deepCopy();
}
