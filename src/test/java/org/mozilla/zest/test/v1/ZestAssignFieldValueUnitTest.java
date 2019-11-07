/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.test.v1;

import org.junit.Test;
import org.mozilla.zest.core.v1.ZestAssignFailException;
import org.mozilla.zest.core.v1.ZestAssignFieldValue;
import org.mozilla.zest.core.v1.ZestFieldDefinition;
import org.mozilla.zest.core.v1.ZestResponse;

/** Unit test for {@link ZestAssignFieldValue}. */
public class ZestAssignFieldValueUnitTest {

    @Test(expected = ZestAssignFailException.class)
    public void shouldFailTheAssignIfResponseIsNotProvided() throws Exception {
        // Given
        ZestResponse response = null;
        ZestAssignFieldValue assignFieldValue =
                new ZestAssignFieldValue("Var", new ZestFieldDefinition(0, "Field"));
        // When
        assignFieldValue.assign(response, new TestRuntime());
        // Then = ZestAssignFailException
    }
}
