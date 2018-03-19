/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.util.LinkedList;
import java.util.List;

// TODO: Auto-generated Javadoc
/** The Class ZestExpressionOr. */
public class ZestExpressionOr extends ZestStructuredExpression {

    /** Main construptor. */
    public ZestExpressionOr() {
        super();
    }

    /**
     * Construptor.
     *
     * @param children the list of the OR clauses
     */
    public ZestExpressionOr(List<ZestExpressionElement> children) {
        super(children);
    }

    @Override
    public boolean isTrue(ZestRuntime runtime) {
        boolean toReturn = false;
        for (ZestExpressionElement con : getChildrenCondition()) {
            toReturn = toReturn || con.evaluate(runtime); // compute OR for each
            // child
            if (toReturn) {
                break; // lazy evaluation
            }
        }
        return toReturn;
    }

    @Override
    public ZestExpressionOr deepCopy() {
        List<ZestExpressionElement> copyChildren = new LinkedList<>();
        if (getChildrenCondition() != null) {
            for (int i = 0; i < getChildrenCondition().size(); i++) {
                copyChildren.add(getChildrenCondition().get(i).deepCopy());
            }
        }
        ZestExpressionOr copy = new ZestExpressionOr(copyChildren);
        copy.setInverse(isInverse());
        return copy;
    }

    @Override
    public String toString() {
        if (this.getChildrenCondition() == null || this.getChildrenCondition().isEmpty()) {
            return "Empty OR";
        }
        StringBuilder expression = new StringBuilder(150);
        if (isInverse()) {
            expression.append("NOT ");
        }
        expression.append('(');
        int i = 0;
        for (; i < this.getChildrenCondition().size() - 1; i++) {
            expression.append(this.getChild(i).toString()).append(" OR ");
        }
        expression.append(this.getChild(i).toString()).append(')');
        return expression.toString();
    }
}
