/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.zaproxy.zest.impl.ZestBasicRunner;
import org.zaproxy.zest.test.v1.ClientBasedTest;
import org.zaproxy.zest.testutils.ShadowDomPage;

/** Unit tests for {@link ZestClientElementClear}. */
class ZestClientElementClearUnitTest extends BaseZestElementTests {

    @Override
    protected ZestElement getElementForSerialization() {
        var element = new ZestClientElementClear("windowHandle", "xpath", "element");
        element.setWaitForMsec(1);
        return element;
    }

    @Nested
    class ShadowDom extends ClientBasedTest {

        @ParameterizedTest(name = "{0} {1}")
        @MethodSource("org.zaproxy.zest.testutils.ShadowDomPage#cases")
        void shouldClearElement(String browser, List<String> modes, int delayMs) throws Exception {
            // Given
            ZestClientElementClear element =
                    new ZestClientElementClear("", "cssSelector", "#field");
            ZestScript script =
                    ShadowDomPage.setup(
                            server,
                            this::getServerUrl,
                            browser,
                            element,
                            modes,
                            delayMs,
                            "initial");
            runner = new ZestBasicRunner();
            // When
            runner.run(script, null);
            // Then
            assertThat(ShadowDomPage.result(runner)).isEqualTo(ShadowDomPage.CLEARED);
        }
    }
}
