/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

import java.util.List;

/** Unit tests for {@link ZestExpressionOr}. */
class ZestExpressionOrUnitTest extends BaseZestElementTests {

    @Override
    protected ZestElement getElementForSerialization() {
        var element =
                new ZestExpressionOr(
                        List.of(
                                new ZestExpressionIsInteger("variableName1", true),
                                new ZestExpressionIsInteger("variableName2", false)));
        element.setInverse(true);
        return element;
    }
}
