/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

/** Unit tests for {@link ZestClientElementSendKeys}. */
class ZestClientElementSendKeysUnitTest extends BaseZestElementTests {

    @Override
    protected ZestElement getElementForSerialization() {
        var element = new ZestClientElementSendKeys("windowHandle", "xpath", "element", "value");
        element.setWaitForMsec(2);
        return element;
    }
}
