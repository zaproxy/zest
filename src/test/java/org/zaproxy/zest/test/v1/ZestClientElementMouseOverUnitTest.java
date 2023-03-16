/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.zaproxy.zest.core.v1.ZestClientElementMouseOver;
import org.zaproxy.zest.core.v1.ZestJSON;

/** Unit test for {@link ZestClientElementMouseOver}. */
class ZestClientElementMouseOverUnitTest {

    @Test
    void shouldNotBePassive() {
        // Given / When
        ZestClientElementMouseOver clientElementMouseOver = new ZestClientElementMouseOver();
        // Then
        assertEquals(false, clientElementMouseOver.isPassive());
    }

    @Test
    void shouldNotHaveWindowHandleByDefault() {
        // Given / When
        ZestClientElementMouseOver clientElementMouseOver = new ZestClientElementMouseOver();
        // Then
        assertNull(clientElementMouseOver.getWindowHandle());
    }

    @Test
    void shouldBeEnabledByDefault() {
        // Given / When
        ZestClientElementMouseOver clientElementMouseOver = new ZestClientElementMouseOver();
        // Then
        assertEquals(true, clientElementMouseOver.isEnabled());
    }

    @Test
    void shouldSerialiseAndDeserialise() {
        // Given
        ZestClientElementMouseOver original =
                new ZestClientElementMouseOver("sessionId", "type", "element");
        original.setEnabled(false);
        // When
        String serialisation = ZestJSON.toString(original);
        ZestClientElementMouseOver deserialised =
                (ZestClientElementMouseOver) ZestJSON.fromString(serialisation);
        // Then
        assertEquals(deserialised.getElementType(), original.getElementType());
        assertEquals(deserialised.getWindowHandle(), original.getWindowHandle());
        assertEquals(deserialised.getElement(), original.getElement());
        assertEquals(deserialised.isEnabled(), original.isEnabled());
    }

    @Test
    void shouldDeepCopy() {
        // Given
        ZestClientElementMouseOver original =
                new ZestClientElementMouseOver("sessionId", "type", "element");
        original.setEnabled(false);
        // When
        ZestClientElementMouseOver copy = original.deepCopy();
        // Then
        assertThat(copy).isNotSameAs(original);
        assertEquals(copy.getElementType(), original.getElementType());
        assertEquals(copy.getWindowHandle(), original.getWindowHandle());
        assertEquals(copy.getElement(), original.getElement());
        assertEquals(copy.isEnabled(), original.isEnabled());
    }
}
