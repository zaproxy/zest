/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

import java.net.URI;

/** Unit tests for {@link ZestResponse}. */
class ZestResponseUnitTest extends BaseZestElementTests {

    @Override
    protected ZestElement getElementForSerialization() {
        try {
            return new ZestResponse(
                    new URI("https://www.example.org/").toURL(), "headers", "body", 404, 100);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
