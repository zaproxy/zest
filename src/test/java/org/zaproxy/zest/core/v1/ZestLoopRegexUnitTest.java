/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

/** Unit tests for {@link ZestLoopRegex}. */
class ZestLoopRegexUnitTest extends BaseZestElementTests {

    @Override
    protected ZestElement getElementForSerialization() {
        var element = new ZestLoopRegex("variableName", "inputVariableName", "regex", 1, true);
        element.addStatement(new ZestComment("Comment 1"));
        element.addStatement(new ZestComment("Comment 2"));
        return element;
    }
}
