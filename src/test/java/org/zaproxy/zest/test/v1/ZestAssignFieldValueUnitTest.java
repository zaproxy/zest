/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.zaproxy.zest.core.v1.ZestAssignFailException;
import org.zaproxy.zest.core.v1.ZestAssignFieldValue;
import org.zaproxy.zest.core.v1.ZestFieldDefinition;
import org.zaproxy.zest.core.v1.ZestResponse;

/** Unit test for {@link ZestAssignFieldValue}. */
class ZestAssignFieldValueUnitTest {

    @Test
    void shouldFailTheAssignIfResponseIsNotProvided() throws Exception {
        // Given
        ZestResponse response = null;
        ZestAssignFieldValue assignFieldValue =
                new ZestAssignFieldValue("Var", new ZestFieldDefinition(0, "Field"));
        // When / Then
        assertThrows(
                ZestAssignFailException.class,
                () -> assignFieldValue.assign(response, new TestRuntime()));
    }
}
