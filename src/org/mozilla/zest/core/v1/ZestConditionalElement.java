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
	 * returns the parent of this Conditional Element (null if root)
	 * @return the parent of this Conditional Element (null if root)
	 */
	public ZestConditionalElement getParent();
	
	/**
	 * returns the old parent and sets the new one
	 * @param new_parent the ew parent
	 * @return the old parent
	 */
	public ZestConditionalElement setParent(ZestConditionalElement new_parent);
	/**
	 * the boolean value of the whole Conditional Element
	 * @return the boolean value of the whole Conditional Element
	 */
	public boolean evaluate(ZestResponse response);
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
	/**
	 * return true if the Conditional Element has a NOT clause
	 * @return true if the Conditional Element has a NOT clause
	 */
	public boolean isNot();
	/**
	 * sets if the Conditional Element has a NOT clause
	 * @param not true if this Conditional Element has to contain the NOT clause
	 */
	public void setNot(boolean not);
}
