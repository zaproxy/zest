package org.mozilla.zest.core.v1;

import java.util.List;
/**
 * 
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * this interface represents a Conditional Element, such as a simple<br>
 * Coditional (based on regex, Status Code, ..) or a list of AND/OR<br>
 * clauses
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
public interface ZestConditionalElement {
	/**
	 * The children (if any) of this Conditional Element (null for Simple conditional)
	 * @return null if simple conditional, the children of this Conditional Element otherwise
	 */
	public List<ZestConditionalElement> getChildrenCondition();
	/**
	 * true if it is a Simple Conditional false otherwise
	 * @return true if it is a Simple Conditional false otherwise
	 */
	public boolean isLeaf();
	/**
	 * true if it has no parent (null)
	 * @return true if it has no parent (null)
	 */
	public boolean isRoot();
	/**
	 * the boolean value of the whole Conditional Element
	 * @return the boolean value of the whole Conditional Element
	 */
	public boolean evaluate();
	
	/**
	 * adds a new Condition to the children (last position)
	 * @param child the child condition to add
	 */
	public void addChildCondition(ZestConditionalElement child);
	/**
	 * adds a new Condition to the children (give position)
	 * @param child the child condition to add
	 * @param position the position where to add the condition
	 */
	public void addChildCondition(ZestConditionalElement child, int position);
	/**
	 * returns the Condition Child in position i
	 * @param index position of the child we're searching for
	 * @return  the child found at the given position
	 */
	public ZestConditionalElement getChild(int index);
	/**
	 * sets the list of Children and return the previous list
	 * @param new_list the new list of Children Condition
	 * @return the previous list of Children Condition
	 */
	public List<ZestConditionalElement> setChildrenCondition(List<ZestConditionalElement>  new_list);
	/**
	 * return the name of the Conditional Element
	 * @return the name of the Conditional Element
	 */
	public String getName();
	/**
	 * Sets the name of the Conditional Element
	 * @param new_name the new name
	 * @return the previous name
	 */
	public String setName(String new_name);
}
