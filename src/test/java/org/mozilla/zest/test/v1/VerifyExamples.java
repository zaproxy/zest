/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.test.v1;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.mozilla.zest.core.v1.ZestElement;
import org.mozilla.zest.core.v1.ZestJSON;
import org.mozilla.zest.core.v1.ZestScript;

/** Verifies that the examples can be parsed. */
public class VerifyExamples {

    @Test
    public void shouldParseExamples() throws Exception {
        String[] scripts = {"BodgeIt_Register_XSS"};
        for (String script : scripts) {
            assertZestScript(script);
        }
    }

    private static void assertZestScript(String name) throws Exception {
        ZestElement zestElement = ZestJSON.fromString(readScript(name));
        assertTrue(name, zestElement instanceof ZestScript);
    }

    private static String readScript(String name) throws Exception {
        return FileUtils.readFileToString(
                new File(VerifyExamples.class.getResource("/" + name + ".zst").getPath()),
                StandardCharsets.UTF_8);
    }
}
