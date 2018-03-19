/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

// TODO: Auto-generated Javadoc
/**
 * this interface represents a Conditional Element, such as a simple<br>
 * Coditional (based on regex, Status Code, ..) or a list of AND/OR<br>
 * clauses
 *
 * @author Alessandro Secco: seccoale@gmail.com
 */
public interface ZestExpressionElement {

    /**
     * true if it is a Simple Conditional false otherwise.
     *
     * @return true if it is a Simple Conditional false otherwise
     */
    public boolean isLeaf();

    /**
     * the boolean value result of the expression without inverse flag.
     *
     * @param runtime the runtime
     * @return the boolean value of the expression without inverse flag
     */
    public boolean isTrue(ZestRuntime runtime);

    /**
     * the boolean value of the whole Conditional Element.
     *
     * @param runtime the runtime
     * @return the boolean value of the whole Conditional Element
     */
    public boolean evaluate(ZestRuntime runtime);

    /**
     * return true if the Conditional Element has a NOT clause.
     *
     * @return true if the Conditional Element has a NOT clause
     */
    public boolean isInverse();

    /**
     * sets if the Conditional Element has a NOT clause.
     *
     * @param not true if this Conditional Element has to contain the NOT clause
     */
    public void setInverse(boolean not);

    /**
     * Deep copy.
     *
     * @return a copy of the ZestConditionalElement
     * @see ZestElement
     */
    public ZestExpressionElement deepCopy();
}
