/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;


public class ZestAssertion extends ZestElement implements ZestExpressionElement{
	private ZestExpressionElement rootExpression;
	public ZestAssertion() {
	}
	
	public ZestAssertion(ZestExpressionElement rootExpr){
		this.rootExpression=rootExpr;
	}
	public ZestExpressionElement getRootExpression(){
		return this.rootExpression;
	}
	public ZestExpressionElement setRootExpression(ZestExpressionElement new_rootExpression){
		ZestExpressionElement old_expr=this.rootExpression;
		this.rootExpression=new_rootExpression;
		return old_expr;
	}
	public boolean isValid (ZestResponse response) {
		if(rootExpression==null)
			return true;//no condition to check!
		return rootExpression.evaluate(response);
	}

	@Override
	public boolean isSameSubclass(ZestElement ze) {
		return ze instanceof ZestAssertion;
	}

	@Override
	public ZestElement deepCopy() {
		ZestExpressionElement copy_root_expr=rootExpression;
		return new ZestAssertion(copy_root_expr);
	}

	@Override
	public boolean isLeaf() {
		return rootExpression==null;
	}

	@Override
	public boolean isRoot() {
		return true;//always root
	}

	@Override
	public ZestExpressionElement getParent() {
		return null;//always root
	}

	@Override
	public ZestExpressionElement setParent(ZestExpressionElement new_parent) {
		return null;//no parent
	}

	@Override
	public boolean evaluate(ZestResponse response) {
		return isValid(response);
	}

	@Override
	public String getName() {
		return rootExpression!=null?rootExpression.getName():null;
	}

	@Override
	public String setName(String new_name) {
		return rootExpression!=null?rootExpression.setName(new_name):null;
	}

	@Override
	public boolean isInverse() {
		return rootExpression!=null?rootExpression.isInverse():false;
	}

	@Override
	public void setInverse(boolean not) {
		if(rootExpression!=null)
			rootExpression.setInverse(not);
	}

}
