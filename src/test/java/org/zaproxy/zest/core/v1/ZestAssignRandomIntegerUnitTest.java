/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

import org.apache.commons.lang3.ArrayUtils;

/** Unit tests for {@link ZestAssignRandomInteger}. */
class ZestAssignRandomIntegerUnitTest extends BaseZestElementTests {

    @Override
    protected ZestElement getElementForSerialization() {
        return new ZestAssignRandomInteger("variableName", 4, 2);
    }

    @Override
    protected String[] getSerializationIgnoreFields() {
        return ArrayUtils.addAll(super.getSerializationIgnoreFields(), new String[] {"rnd"});
    }
}
