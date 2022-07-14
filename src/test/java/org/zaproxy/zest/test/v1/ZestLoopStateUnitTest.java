/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.zaproxy.zest.core.v1.ZestLoopStateString;
import org.zaproxy.zest.core.v1.ZestLoopTokenStringSet;

/** */
class ZestLoopStateUnitTest {
    String[] values = {"A", "B", "C", "D", "D"};
    ZestLoopTokenStringSet set = new ZestLoopTokenStringSet(values);

    @Test
    void testZestLoopState() {
        ZestLoopStateString state = new ZestLoopStateString(set);
        assertFalse(state.isLastState(set));
    }

    @Test
    void testGetCurrentToken() {
        int index = 0;
        ZestLoopStateString state = new ZestLoopStateString(set);
        assertTrue(state.getCurrentToken().equals(values[index]));
    }

    @Test
    void testGetCurrentIndex() {
        int index = 3;
        ZestLoopStateString state = new ZestLoopStateString(set);
        for (int i = 0; i < index; i++) {
            state.increase(set);
        }
        assertEquals(state.getCurrentIndex(), index);
    }

    @Test
    void testIncrease() {
        ZestLoopStateString state = new ZestLoopStateString(set);
        int currentIndex = state.getCurrentIndex();
        boolean increasable = state.increase(set);
        if (!increasable) {
            fail();
        }
        int newIndex = state.getCurrentIndex();
        assertTrue(
                currentIndex + 1 == newIndex && values[newIndex].equals(state.getCurrentToken()));
    }

    @Test
    void testToLastState() {
        ZestLoopStateString state = new ZestLoopStateString(set);
        state.toLastState(set);
        assertTrue(state.isLastState(set));
    }

    @Test
    void testIsLastState() {
        ZestLoopStateString state = new ZestLoopStateString(set);
        for (int i = 0; i < 13; i++) {
            state.increase(set);
            if (state.getCurrentIndex() == set.size()) {
                assertTrue(state.isLastState(set));
                break;
            }
        }
    }
}
