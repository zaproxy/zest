/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.zaproxy.zest.core.v1.ZestClientElementScroll;
import org.zaproxy.zest.core.v1.ZestJSON;

/** Unit test for {@link ZestClientElementScroll}. */
class ZestClientElementScrollUnitTest {

    @Test
    void shouldNotBePassive() {
        // Given / When
        ZestClientElementScroll clientElementScroll = new ZestClientElementScroll();
        // Then
        assertEquals(false, clientElementScroll.isPassive());
    }

    @Test
    void shouldNotHaveWindowHandleByDefault() {
        // Given / When
        ZestClientElementScroll clientElementScroll = new ZestClientElementScroll();
        // Then
        assertNull(clientElementScroll.getWindowHandle());
    }

    @Test
    void shouldBeEnabledByDefault() {
        // Given / When
        ZestClientElementScroll clientElementScroll = new ZestClientElementScroll();
        // Then
        assertEquals(true, clientElementScroll.isEnabled());
    }

    @Test
    void shouldSetX() {
        // Given
        int x = 10;
        ZestClientElementScroll clientElementScroll = new ZestClientElementScroll();
        // When
        clientElementScroll.setX(x);
        // Then
        assertEquals(x, clientElementScroll.getX());
    }

    @Test
    void shouldSetY() {
        // Given
        int y = 20;
        ZestClientElementScroll clientElementScroll = new ZestClientElementScroll();
        // When
        clientElementScroll.setY(y);
        // Then
        assertEquals(y, clientElementScroll.getY());
    }

    @Test
    void shouldSerialiseAndDeserialise() {
        // Given
        ZestClientElementScroll original =
                new ZestClientElementScroll("sessionId", "type", "element", 10, 20);
        original.setEnabled(false);
        // When
        String serialisation = ZestJSON.toString(original);
        ZestClientElementScroll deserialised =
                (ZestClientElementScroll) ZestJSON.fromString(serialisation);
        // Then
        assertEquals(deserialised.getElementType(), original.getElementType());
        assertEquals(deserialised.getWindowHandle(), original.getWindowHandle());
        assertEquals(deserialised.getElement(), original.getElement());
        assertEquals(deserialised.getX(), original.getX());
        assertEquals(deserialised.getY(), original.getY());
        assertEquals(deserialised.isEnabled(), original.isEnabled());
    }

    @Test
    void shouldDeepCopy() {
        // Given
        ZestClientElementScroll original =
                new ZestClientElementScroll("sessionId", "type", "element", 10, 20);
        original.setEnabled(false);
        // When
        ZestClientElementScroll copy = original.deepCopy();
        // Then
        assertThat(copy).isNotSameAs(original);
        assertEquals(copy.getElementType(), original.getElementType());
        assertEquals(copy.getWindowHandle(), original.getWindowHandle());
        assertEquals(copy.getElement(), original.getElement());
        assertEquals(copy.getX(), original.getX());
        assertEquals(copy.getY(), original.getY());
        assertEquals(copy.isEnabled(), original.isEnabled());
    }
}
