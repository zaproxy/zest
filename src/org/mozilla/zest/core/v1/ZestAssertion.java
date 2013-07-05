/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;


public class ZestAssertion extends ZestElement {
	private ZestConditionalElement rootExpression;
	public ZestAssertion() {
	}
	
	public ZestAssertion(ZestConditionalElement reeotCond){
		this.rootExpression=rootExpression;
	}
	public ZestConditionalElement getRootExpression(){
		return this.rootExpression;
	}
	public ZestConditionalElement setRootExpression(ZestConditionalElement new_rootExpression){
		ZestConditionalElement old_expr=this.rootExpression;
		this.rootExpression=new_rootExpression;
		return old_expr;
	}
	public boolean isValid (ZestResponse response) {
		return rootExpression.evaluate(response);
	}

	@Override
	public boolean isSameSubclass(ZestElement ze) {
		return ze instanceof ZestAssertion;
	}

	@Override
	public ZestElement deepCopy() {
		ZestConditionalElement copy_root_expr=rootExpression;
		return new ZestAssertion(copy_root_expr);
	}

}
