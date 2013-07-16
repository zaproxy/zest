/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

public abstract class ZestExpression extends ZestElement implements
		ZestExpressionElement {
	private boolean not = false;

	public ZestExpression() {
		super();
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	public boolean isInverse() {
		return not;
	}

	public void setInverse(boolean not) {
		this.not = not;
	}

	@Override
	public boolean evaluate(ZestResponse response) {
		boolean toReturn = isTrue(response);
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

	public abstract ZestExpression deepCopy();
}
