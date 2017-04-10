/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

// TODO: Auto-generated Javadoc
/**
 * The Class ZestExpression.
 */
public abstract class ZestExpression extends ZestElement implements
		ZestExpressionElement {
	
	/** The not. */
	private boolean not = false;

	/**
	 * Instantiates a new zest expression.
	 */
	public ZestExpression() {
		super();
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public boolean isInverse() {
		return not;
	}

	@Override
	public void setInverse(boolean not) {
		this.not = not;
	}

	@Override
	public boolean evaluate(ZestRuntime runtime) {
		boolean toReturn = isTrue(runtime);
		return isInverse() ? !toReturn : toReturn;
	}

	// /**
	// * returns the number of ZestExpression created
	// *
	// * @return the number of the ZestExpression created
	// */
	// public int getCount() {
	// return counter;
	// }

	@Override
	public abstract ZestExpression deepCopy();
}
