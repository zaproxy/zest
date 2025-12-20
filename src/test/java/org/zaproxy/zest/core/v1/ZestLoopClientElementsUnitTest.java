/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

import org.apache.commons.lang3.ArrayUtils;

/** Unit tests for {@link ZestLoopClientElements}. */
class ZestLoopClientElementsUnitTest extends BaseZestElementTests {

    @Override
    protected ZestElement getElementForSerialization() {
        var element =
                new ZestLoopClientElements(
                        "variableName", "windowHandle", "xapth", "element", "attribute");
        element.addStatement(new ZestComment("Comment A"));
        element.addStatement(new ZestComment("Comment B"));
        return element;
    }

    @Override
    protected String[] getSerializationIgnoreFields() {
        return ArrayUtils.addAll(
                super.getSerializationIgnoreFields(),
                new String[] {"set.loop", "statements.previous"});
    }
}
