/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

/** Common tests for {@link ZestElement} implementations. */
abstract class BaseZestElementTests {

    private static final String[] IGNORED_FIELDS = {"previous", "next"};

    /**
     * Gets the Zest element to use for (de)serialization tests.
     *
     * @return the Zest element.
     * @see #shouldSerializeAndDeserializeJson()
     */
    protected abstract ZestElement getElementForSerialization();

    @Test
    void shouldSerializeAndDeserializeJson() {
        // Given
        ZestElement element = getElementForSerialization();
        // When
        String json = assertDoesNotThrow(() -> ZestJSON.toString(element));
        ZestElement convertedElement = ZestJSON.fromString(json);
        // Then
        assertThat(ZestJSON.toString(convertedElement)).isEqualTo(json);
        assertThat(convertedElement)
                .usingRecursiveComparison()
                .ignoringFields(getSerializationIgnoreFields())
                .isEqualTo(element);
    }

    protected String[] getSerializationIgnoreFields() {
        return IGNORED_FIELDS;
    }
}
