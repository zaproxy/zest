/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.testutils;

import java.io.IOException;
import java.util.Map;
import org.zaproxy.zest.core.v1.ZestActionFailException;
import org.zaproxy.zest.core.v1.ZestAssertFailException;
import org.zaproxy.zest.core.v1.ZestAssignFailException;
import org.zaproxy.zest.core.v1.ZestClientFailException;
import org.zaproxy.zest.core.v1.ZestInvalidCommonTestException;
import org.zaproxy.zest.core.v1.ZestRequest;
import org.zaproxy.zest.core.v1.ZestScript;
import org.zaproxy.zest.impl.ZestBasicRunner;

/** A {@link ZestBasicRunner} that quits the browsers that might have been launched. */
public class ZestTestRunner extends ZestBasicRunner {

    @Override
    public String run(ZestScript script, ZestRequest target, Map<String, String> tokens)
            throws ZestAssertFailException,
                    ZestActionFailException,
                    ZestInvalidCommonTestException,
                    IOException,
                    ZestAssignFailException,
                    ZestClientFailException {

        try {
            return super.run(script, target, tokens);
        } finally {
            getWebDrivers()
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
