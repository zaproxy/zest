/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.test.v1;

import org.junit.Test;
import org.mozilla.zest.core.v1.ZestClientFailException;
import org.mozilla.zest.core.v1.ZestClientWindowOpenUrl;

/** Unit test for {@link ZestClientWindowOpenUrl}. */
public class ZestClientWindowOpenUrlUnitTest {

    @Test(expected = ZestClientFailException.class)
    public void shouldFailToOpenUrlIfWindowHandleNotFound() throws Exception {
        // Given
        ZestClientWindowOpenUrl openUrl =
                new ZestClientWindowOpenUrl("NoWindowHandle", "http://no.wd.localhost/");
        // When
        openUrl.invoke(testRuntime());
        // Then = ZestClientFailException
    }

    private static TestRuntime testRuntime() {
        return new TestRuntime();
    }
}
