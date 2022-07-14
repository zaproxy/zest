/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.zaproxy.zest.core.v1.ZestClientFailException;
import org.zaproxy.zest.core.v1.ZestClientWindowOpenUrl;

/** Unit test for {@link ZestClientWindowOpenUrl}. */
class ZestClientWindowOpenUrlUnitTest {

    @Test
    void shouldFailToOpenUrlIfWindowHandleNotFound() throws Exception {
        // Given
        ZestClientWindowOpenUrl openUrl =
                new ZestClientWindowOpenUrl("NoWindowHandle", "http://no.wd.localhost/");
        // When / Then
        assertThrows(ZestClientFailException.class, () -> openUrl.invoke(testRuntime()));
    }

    private static TestRuntime testRuntime() {
        return new TestRuntime();
    }
}
