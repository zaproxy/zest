/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

// TODO: Auto-generated Javadoc
// TODO: Auto-generated Javadoc
/*
 * The Class ZestStructuredExpression.
 */

// TODO: Auto-generated Javadoc
/** The Class ZestStructuredExpression. */
public abstract class ZestStructuredExpression extends ZestExpression {

    /** The children. */
    private List<ZestExpressionElement> children = new LinkedList<>();

    /** Instantiates a new zest structured expression. */
    public ZestStructuredExpression() {
        super();
    }

    /**
     * Instantiates a new zest structured expression.
     *
     * @param childrenExpression the children expression
     */
    public ZestStructuredExpression(List<ZestExpressionElement> childrenExpression) {
        super();
        this.children = childrenExpression;
    }

    /**
     * sets the list of Children and return the previous list.
     *
     * @param new_list the new list of Children Condition
     * @return the previous list of Children Condition
     */
    public List<ZestExpressionElement> setChildrenCondition(List<ZestExpressionElement> new_list) {
        List<ZestExpressionElement> old_children = children;
        this.children = new_list;
        return old_children;
    }

    /**
     * returns the children condition of this Zest Expression.
     *
     * @return the children condition
     */
    public List<ZestExpressionElement> getChildrenCondition() {
        return this.children;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    /**
     * adds a new Condition to the children (last position).
     *
     * @param child the child condition to add
     */
    public void addChildCondition(ZestExpressionElement child) {
        children.add(child);
    }

    /**
     * removes a child from the list of children condition.
     *
     * @param child the Expression to remove
     * @return the expression removed if any, null otherwise.
     */
    public ZestExpressionElement removeChildCondition(ZestExpressionElement child) {
        if (children.remove(child)) {
            return child;
        } else {
            return null;
        }
    }

    /**
     * removes an element from the list of children condition.
     *
     * @param index the index of the element to remove
     * @return the element removed if any.
     */
    public ZestExpressionElement removeChildCondition(int index) {
        return children.remove(index);
    }

    /**
     * Removes a collection of Expressions.
     *
     * @param childrenToRemove the Expressions, children of this StructuredExpression, which has to
     *     be removed
     * @return true if list of children condition changed as a result of the call
     */
    public boolean removeAllChildren(Collection<ZestExpressionElement> childrenToRemove) {
        return children.removeAll(childrenToRemove);
    }

    /** clears the list of children expression. */
    public void clearChildren() {
        children.clear();
    }

    /**
     * adds a new Condition to the children (give position).
     *
     * @param child the child condition to add
     * @param position the position where to add the condition
     */
    public void addChildCondition(ZestExpressionElement child, int position) {
        this.children.add(position, child);
    }

    /**
     * returns the Condition Child in position i.
     *
     * @param index position of the child we're searching for
     * @return the child found at the given position
     */
    public ZestExpressionElement getChild(int index) {
        return children.get(index);
    }
}
