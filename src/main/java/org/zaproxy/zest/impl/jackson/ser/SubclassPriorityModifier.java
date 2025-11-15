/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.impl.jackson.ser;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import java.util.Comparator;
import java.util.List;

/**
 * A {@link BeanSerializerModifier} that orders properties during Jackson serialization to ensure
 * properties defined in subclasses appear <b>before</b> properties defined in superclasses. In
 * Jackson's default behavior, the serialization order for inherited properties typically places the
 * superclass properties first, followed by the subclass properties.
 *
 * <p>This maintains a more logical order in the serialized output, placing more specific fields
 * first.
 */
@SuppressWarnings("serial")
public class SubclassPriorityModifier extends BeanSerializerModifier {
    /**
     * Modifies the order of properties to prioritize subclass properties over superclass
     * properties.
     *
     * <p>The sort is stable, meaning properties within the same declaring class maintain their
     * original relative order.
     *
     * @param config The serialization configuration.
     * @param beanDesc The description of the bean being serialized.
     * @param propWriters The original list of property writers.
     * @return The sorted {@code propWriters} where subclass properties are listed before superclass
     *     properties.
     */
    @Override
    public List<BeanPropertyWriter> orderProperties(
            SerializationConfig config,
            BeanDescription beanDesc,
            List<BeanPropertyWriter> propWriters) {
        propWriters.sort(
                Comparator.comparing(
                        BeanPropertyWriter::getMember,
                        (memberA, memberB) -> {
                            Class<?> classA = memberA.getDeclaringClass();
                            Class<?> classB = memberB.getDeclaringClass();

                            // Same class, keep original relative order
                            if (classA == classB) return 0;

                            // classA is super, classB is sub -> prioritize sub first (B)
                            // Return 1 to move A (super) *after* B (sub).
                            if (classA.isAssignableFrom(classB)) return 1;

                            // classB is super, classA is sub -> prioritize sub first (A)
                            // Return -1 to move A (sub) *before* B (super).
                            return -1;
                        }));

        return propWriters;
    }
}
