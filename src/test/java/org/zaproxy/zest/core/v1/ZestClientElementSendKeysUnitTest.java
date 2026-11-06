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

/** Unit tests for {@link ZestClientElementSendKeys}. */
class ZestClientElementSendKeysUnitTest extends BaseZestElementTests {

    @Override
    protected ZestElement getElementForSerialization() {
        var element = new ZestClientElementSendKeys("windowHandle", "xpath", "element", "value");
        element.setWaitForMsec(2);
        return element;
    }

    @Nested
    class ShadowDom extends ClientBasedTest {

        private static final String VALUE = "shadow value";

        @ParameterizedTest(name = "{0} {1}")
        @MethodSource("org.zaproxy.zest.testutils.ShadowDomPage#cases")
        void shouldTypeInElement(String browser, List<String> modes, int delayMs) throws Exception {
            // Given
            ZestClientElementSendKeys element =
                    new ZestClientElementSendKeys("", "cssSelector", "#field", VALUE);
            ZestScript script =
                    ShadowDomPage.setup(
                            server, this::getServerUrl, browser, element, modes, delayMs, "");
            runner = new ZestBasicRunner();
            // When
            runner.run(script, null);
            // Then
            assertThat(ShadowDomPage.result(runner)).isEqualTo(ShadowDomPage.TYPED_PREFIX + VALUE);
        }
    }
}
