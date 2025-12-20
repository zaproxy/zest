/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Unit tests for {@link ZestLoopTokenFileSet}. */
class ZestLoopTokenFileSetUnitTest extends BaseZestElementTests {

    @Override
    protected ZestElement getElementForSerialization() {
        try {
            Path file = Files.createTempFile("ZestLoopTokenFileSetUnitTest", "");
            return new ZestLoopTokenFileSet(file.toAbsolutePath().toString());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
