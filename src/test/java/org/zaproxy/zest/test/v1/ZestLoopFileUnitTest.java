/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.zaproxy.zest.core.v1.ZestLoopFile;

/** */
class ZestLoopFileUnitTest {
    static final File file =
            new File(ZestLoopFileUnitTest.class.getResource("/TestLoopFile.txt").getPath());

    /**
     * Method testOpenFile.
     *
     * @throws IOException
     */
    @Test
    void testOpenFile() throws IOException {
        new ZestLoopFile(file.getAbsolutePath());
    }
}
