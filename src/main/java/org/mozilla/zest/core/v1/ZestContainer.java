/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.util.List;
import java.util.Set;

// TODO: Auto-generated Javadoc
/** The Interface ZestContainer. */
public interface ZestContainer {

    /**
     * Gets the last.
     *
     * @return the last
     */
    ZestStatement getLast();

    /**
     * Gets the statement.
     *
     * @param index the index
     * @return the statement
     */
    ZestStatement getStatement(int index);

    /**
     * Gets the index.
     *
     * @param child the child
     * @return the index
     */
    int getIndex(ZestStatement child);

    /**
     * Move.
     *
     * @param index the index
     * @param stmt the stmt
     */
    void move(int index, ZestStatement stmt);

    /**
     * Gets the child before.
     *
     * @param child the child
     * @return the child before
     */
    ZestStatement getChildBefore(ZestStatement child);

    /**
     * Returns all of the containers immediate children
     *
     * @return the children
     */
    List<ZestStatement> getChildren();

    /**
     * Returns all of the variable names defined by this staement and its children.
     *
     * @return the tokens
     */
    Set<String> getVariableNames();
}
