package org.mozilla.zest.core.v1;

import java.util.LinkedList;
import java.util.List;

public abstract class ZestExpression extends ZestElement implements
		ZestExpressionElement {
	private List<ZestExpressionElement> children;
	private boolean not = false;
	private ZestExpressionElement parent;
	private String name;
	private static int counter = 0;
	public static final String DEFAULT_NAME = "default_expression_";

	public ZestExpression(ZestExpressionElement parent) {
		super();
		this.name = DEFAULT_NAME + counter++;
		this.parent = parent;
		this.children = new LinkedList<>();
	}

	public ZestExpression(ZestExpressionElement parent,
			List<ZestExpressionElement> children) {
		super();
		this.name = DEFAULT_NAME + counter++;
		this.parent = parent;
		this.children = children;
	}

	/**
	 * returns the children condition of this Zest Expression
	 */
	public List<ZestExpressionElement> getChildrenCondition() {
		return this.children;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public boolean isRoot() {
		return parent == null;
	}

	/**
	 * adds a new Condition to the children (last position)
	 * 
	 * @param child
	 *            the child condition to add
	 */
	public void addChildCondition(ZestExpressionElement child) {
		this.children.add(child);
	}

	/**
	 * adds a new Condition to the children (give position)
	 * 
	 * @param child
	 *            the child condition to add
	 * @param position
	 *            the position where to add the condition
	 */
	public void addChildCondition(ZestExpressionElement child, int position) {
		this.children.add(position, child);
	}

	/**
	 * returns the Condition Child in position i
	 * 
	 * @param index
	 *            position of the child we're searching for
	 * @return the child found at the given position
	 */
	public ZestExpressionElement getChild(int index) {
		return children.get(index);
	}

	/**
	 * sets the list of Children and return the previous list
	 * 
	 * @param new_list
	 *            the new list of Children Condition
	 * @return the previous list of Children Condition
	 */
	public List<ZestExpressionElement> setChildrenCondition(
			List<ZestExpressionElement> new_list) {
		List<ZestExpressionElement> old_children = children;
		this.children = new_list;
		return old_children;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String setName(String new_name) {
		if (new_name == null) {
			throw new IllegalArgumentException("the name cannot be null!");
		}
		String old_name = name;
		this.name = new_name;
		return old_name;
	}

	@Override
	public ZestExpressionElement getParent() {
		return this.parent;
	}

	@Override
	public ZestExpressionElement setParent(ZestExpressionElement new_parent) {
		ZestExpressionElement old_parent = this.getParent();
		this.parent = new_parent;
		return old_parent;
	}

	public boolean isInverse() {
		return not;
	}

	public void setInverse(boolean not) {
		this.not = not;
	}

	/**
	 * returns the number of ZestExpression created
	 * 
	 * @return the number of the ZestExpression created
	 */
	public int getCount() {
		return counter;
	}

	public ZestExpression deepCopy(){//TODO
		ZestExpression copy=null;
		return copy;
	}
}
