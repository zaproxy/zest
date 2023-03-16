/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.zaproxy.zest.core.v1.ZestClientFailException;
import org.zaproxy.zest.core.v1.ZestClientWindowResize;

/** Unit test for {@link ZestClientWindowResize}. */
class ZestClientWindowResizeUnitTest {

    @Test
    void shouldFailToResizeIfWindowHandleNotFound() throws Exception {
        // Given
        ZestClientWindowResize resize = new ZestClientWindowResize("NoWindowHandle", 100, 500);
        // When / Then
        assertThrows(ZestClientFailException.class, () -> resize.invoke(testRuntime()));
    }

    private static TestRuntime testRuntime() {
        return new TestRuntime();
    }
}
