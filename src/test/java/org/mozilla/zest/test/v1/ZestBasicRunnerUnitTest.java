/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.test.v1;

import java.util.HashMap;
import org.junit.Test;
import org.mozilla.zest.core.v1.ZestRequest;
import org.mozilla.zest.core.v1.ZestScript;
import org.mozilla.zest.impl.ZestBasicRunner;

/** Unit test for {@code ZestBasicRunner}. */
public class ZestBasicRunnerUnitTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToRunNonPassiveStatementsInPassiveScripts() throws Exception {
        // Given
        ZestScript script = new ZestScript();
        script.setType(ZestScript.Type.Passive);
        script.add(new ZestRequest());
        ZestBasicRunner runner = new ZestBasicRunner();
        // When
        runner.run(script, new HashMap<String, String>());
        // Then = IllegalArgumentException
    }
}
