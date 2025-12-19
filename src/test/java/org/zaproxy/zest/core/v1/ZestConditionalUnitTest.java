/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

/** Unit tests for {@link ZestConditional}. */
class ZestConditionalUnitTest extends BaseZestElementTests {

    @Override
    protected ZestElement getElementForSerialization() {
        var element = new ZestConditional(new ZestExpressionEquals("variableName", "regex"));
        element.addIf(new ZestComment("Comment If"));
        element.addElse(new ZestComment("Comment Else"));
        return element;
    }
}
