/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.zaproxy.zest.core.v1.ZestClientElementScrollTo;
import org.zaproxy.zest.core.v1.ZestJSON;

/** Unit test for {@link ZestClientElementScrollTo}. */
class ZestClientElementScrollToUnitTest {

    @Test
    void shouldNotBePassive() {
        // Given / When
        ZestClientElementScrollTo clientElementScrollTo = new ZestClientElementScrollTo();
        // Then
        assertEquals(false, clientElementScrollTo.isPassive());
    }

    @Test
    void shouldNotHaveWindowHandleByDefault() {
        // Given / When
        ZestClientElementScrollTo clientElementScrollTo = new ZestClientElementScrollTo();
        // Then
        assertNull(clientElementScrollTo.getWindowHandle());
    }

    @Test
    void shouldBeEnabledByDefault() {
        // Given / When
        ZestClientElementScrollTo clientElementScrollTo = new ZestClientElementScrollTo();
        // Then
        assertEquals(true, clientElementScrollTo.isEnabled());
    }

    @Test
    void shouldSerialiseAndDeserialise() {
        // Given
        ZestClientElementScrollTo original =
                new ZestClientElementScrollTo("sessionId", "type", "element");
        original.setEnabled(false);
        // When
        String serialisation = ZestJSON.toString(original);
        ZestClientElementScrollTo deserialised =
                (ZestClientElementScrollTo) ZestJSON.fromString(serialisation);
        // Then
        assertEquals(deserialised.getElementType(), original.getElementType());
        assertEquals(deserialised.getWindowHandle(), original.getWindowHandle());
        assertEquals(deserialised.getElement(), original.getElement());
        assertEquals(deserialised.isEnabled(), original.isEnabled());
    }

    @Test
    void shouldDeepCopy() {
        // Given
        ZestClientElementScrollTo original =
                new ZestClientElementScrollTo("sessionId", "type", "element");
        original.setEnabled(false);
        // When
        ZestClientElementScrollTo copy = original.deepCopy();
        // Then
        assertThat(copy).isNotSameAs(original);
        assertEquals(copy.getElementType(), original.getElementType());
        assertEquals(copy.getWindowHandle(), original.getWindowHandle());
        assertEquals(copy.getElement(), original.getElement());
        assertEquals(copy.isEnabled(), original.isEnabled());
    }
}
