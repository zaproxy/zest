/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

// TODO: Auto-generated Javadoc
/** The Class ZestAssertion. */
public class ZestAssertion extends ZestElement {

    /** The root expression. */
    private ZestExpressionElement rootExpression;

    /** Instantiates a new zest assertion. */
    public ZestAssertion() {}

    /**
     * Instantiates a new zest assertion.
     *
     * @param rootExpr the root expr
     */
    public ZestAssertion(ZestExpressionElement rootExpr) {
        this.rootExpression = rootExpr;
    }

    /**
     * Gets the root expression.
     *
     * @return the root expression
     */
    public ZestExpressionElement getRootExpression() {
        return this.rootExpression;
    }

    /**
     * Sets the root expression.
     *
     * @param new_rootExpression the new_root expression
     * @return the zest expression element
     */
    public ZestExpressionElement setRootExpression(ZestExpressionElement new_rootExpression) {
        ZestExpressionElement old_expr = this.rootExpression;
        this.rootExpression = new_rootExpression;
        return old_expr;
    }

    /**
     * Checks if is valid.
     *
     * @param runtime the runtime
     * @return true, if is valid
     */
    public boolean isValid(ZestRuntime runtime) {
        if (rootExpression == null) {
            return false; // no condition to check!
        }
        return rootExpression.evaluate(runtime);
    }

    @Override
    public boolean isSameSubclass(ZestElement ze) {
        return ze instanceof ZestAssertion;
    }

    @Override
    public ZestElement deepCopy() {
        return new ZestAssertion(rootExpression.deepCopy());
    }
}
