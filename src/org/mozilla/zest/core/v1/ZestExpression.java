package org.mozilla.zest.core.v1;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public abstract class ZestExpression extends ZestElement implements ZestConditionalElement{
	private List<ZestConditionalElement> children;
	private boolean not=false;
	private ZestConditionalElement parent;
	private String name;
	private static int counter=0;
	public static final String DEFAULT_NAME="default_expression_";
	public ZestExpression(ZestConditionalElement parent){
		super();
		this.name=DEFAULT_NAME+counter++;
		this.parent=parent;
		this.children=new LinkedList<>();
	}
	public ZestExpression(ZestConditionalElement parent, List<ZestConditionalElement> children){
		super();
		this.name=DEFAULT_NAME+counter++;
		this.parent=parent;
		this.children=children;
	}
	/**
	 * returns the children condition of this Zest Expression
	 */
	public List<ZestConditionalElement> getChildrenCondition() {
		return this.children;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public boolean isRoot() {
		return parent==null;
	}
	/**
	 * adds a new Condition to the children (last position)
	 * @param child the child condition to add
	 */
	public void addChildCondition(ZestConditionalElement child) {
		this.children.add(child);
	}
	/**
	 * adds a new Condition to the children (give position)
	 * @param child the child condition to add
	 * @param position the position where to add the condition
	 */
	public void addChildCondition(ZestConditionalElement child, int position) {
		this.children.add(position, child);
	}
	/**
	 * returns the Condition Child in position i
	 * @param index position of the child we're searching for
	 * @return  the child found at the given position
	 */
	public ZestConditionalElement getChild(int index) {
		return children.get(index);
	}
	/**
	 * sets the list of Children and return the previous list
	 * @param new_list the new list of Children Condition
	 * @return the previous list of Children Condition
	 */
	public List<ZestConditionalElement> setChildrenCondition(
			List<ZestConditionalElement> new_list) {
		List<ZestConditionalElement> old_children=children;
		this.children=new_list;
		return old_children;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public String setName(String new_name) {
		if(new_name==null){
			throw new IllegalArgumentException("the name cannot be null!");
		}
		String old_name=name;
		this.name=new_name;
		return old_name;
	}
	@Override
	public ZestConditionalElement getParent(){
		return this.parent;
	}
	@Override
	public ZestConditionalElement setParent(ZestConditionalElement new_parent){
		ZestConditionalElement old_parent=this.getParent();
		this.parent=new_parent;
		return old_parent;
	}
	public boolean isNot(){
		return not;
	}
	public void setNot(boolean not){
		this.not=not;
	}
	/**
	 * returns the number of ZestExpression created
	 * @return the number of the ZestExpression created
	 */
	public int getCount(){
		return counter;
	}
}
