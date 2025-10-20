/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import org.junit.jupiter.api.AfterEach;
import org.zaproxy.zest.impl.ZestBasicRunner;

/** Quits the browsers that might have been launched using the {@link #runner} */
abstract class ClientBasedTest extends ServerBasedTest {

    protected ZestBasicRunner runner;

    @AfterEach
    void cleanUp() {
        if (runner != null) {
            runner.getWebDrivers()
                    .forEach(
                            wd -> {
                                try {
                                    wd.quit();
                                } catch (Exception ignore) {
                                    // Nothing to do, finished running.
                                }
                            });
        }
    }
}
