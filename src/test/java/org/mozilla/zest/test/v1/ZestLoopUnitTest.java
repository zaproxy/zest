/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.test.v1;

import static org.junit.Assert.assertTrue;

import java.util.List;
import org.junit.Test;
import org.mozilla.zest.core.v1.ZestComment;
import org.mozilla.zest.core.v1.ZestLoop;
import org.mozilla.zest.core.v1.ZestLoopTokenSet;
import org.mozilla.zest.core.v1.ZestStatement;

/** Unit test for {@link ZestLoop}. */
public class ZestLoopUnitTest {

    @Test
    public void shouldReturnEmptyListIfNoStatementsWhenCopyingStatements() throws Exception {
        // Given
        ZestLoop<String> loop = new ZestLoopTest();
        // When
        List<ZestStatement> copiedStatements = loop.copyStatements();
        // Then
        assertTrue(copiedStatements.isEmpty());
    }

    @Test
    public void shouldReturnADifferentListAndStatementsWhenCopyingStatements() throws Exception {
        // Given
        ZestLoop<String> loop = new ZestLoopTest();
        loop.addStatement(new ZestComment());
        loop.addStatement(new ZestComment());
        // When
        List<ZestStatement> copiedStatements = loop.copyStatements();
        // Then
        assertTrue(copiedStatements != loop.getStatements());
        assertTrue(copiedStatements.size() == loop.getStatements().size());
        assertTrue(copiedStatements.get(0) != loop.getStatements().get(0));
        assertTrue(copiedStatements.get(1) != loop.getStatements().get(1));
    }

    private static class ZestLoopTest extends ZestLoop<String> {

        @Override
        public ZestLoopTokenSet<String> getSet() {
            return null;
        }

        @Override
        public ZestLoop<String> deepCopy() {
            return null;
        }

        @Override
        public boolean isLastState() {
            return false;
        }

        @Override
        protected void increase() {}

        @Override
        public void toLastState() {}
    }
}
