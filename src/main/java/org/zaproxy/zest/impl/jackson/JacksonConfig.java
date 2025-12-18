/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.impl.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.MapperBuilder;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import java.util.Arrays;
import java.util.function.Consumer;
import org.zaproxy.zest.core.v1.ZestAction;
import org.zaproxy.zest.core.v1.ZestAssignment;
import org.zaproxy.zest.core.v1.ZestAuthentication;
import org.zaproxy.zest.core.v1.ZestElement;
import org.zaproxy.zest.core.v1.ZestExpressionElement;
import org.zaproxy.zest.core.v1.ZestLoop;
import org.zaproxy.zest.core.v1.ZestLoopState;
import org.zaproxy.zest.core.v1.ZestLoopTokenSet;
import org.zaproxy.zest.core.v1.ZestStatement;
import org.zaproxy.zest.impl.jackson.module.DateModel;
import org.zaproxy.zest.impl.jackson.ser.SubclassPriorityModifier;

public final class JacksonConfig {
    private static final String ZEST_PACKAGE = ZestElement.class.getPackageName();

    private JacksonConfig() {}

    /**
     * Configures a Jackson {@link MapperBuilder} ({@link JsonMapper.Builder}, {@link
     * YAMLMapper.Builder}, and so on) with Zest-specific settings.
     *
     * @param <M> The {@link ObjectMapper} being built
     * @param <B> The {@link MapperBuilder}
     * @param builder The builder instance to configure
     * @return The configured builder
     */
    public static <M extends ObjectMapper, B extends MapperBuilder<M, B>> B configureCommonBuilder(
            B builder) {
        // Create a module to hold a custom modifier
        SimpleModule subclassPriorityModule = new SimpleModule("SubclassPriorityModule");
        subclassPriorityModule.setSerializerModifier(new SubclassPriorityModifier());

        builder.addModules(new DateModel(), subclassPriorityModule);

        builder.defaultPropertyInclusion(
                JsonInclude.Value.construct(
                        JsonInclude.Include.NON_NULL, JsonInclude.Include.ALWAYS));

        builder.visibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
                .visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        builder.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        builder.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        // Define mix-in annotations for abstract classes
        Arrays.asList(
                        ZestAction.class,
                        ZestAssignment.class,
                        ZestAuthentication.class,
                        ZestElement.class,
                        ZestStatement.class,
                        ZestExpressionElement.class,
                        ZestLoop.class,
                        ZestLoopState.class,
                        ZestLoopTokenSet.class)
                .forEach(type -> builder.addMixIn(type, ZestElementMixIn.class));

        // Scan for the Zest package and register all ZestElement subtypes
        // TODO Remove runtime dependency on ClassGraph
        try (ScanResult scanResult = new ClassGraph().acceptPackages(ZEST_PACKAGE).scan()) {
            NamedType[] subtypes =
                    scanResult.getSubclasses(ZestElement.class).stream()
                            .mapMulti(
                                    (ClassInfo classInfo, Consumer<Class<?>> consumer) -> {
                                        final Class<?> classRef = classInfo.loadClass();
                                        if (classRef != null) {
                                            // Pass the classRef along for subtype registration
                                            consumer.accept(classRef);
                                        }
                                    })
                            .map(NamedType::new)
                            .toArray(NamedType[]::new);

            builder.registerSubtypes(subtypes);
        }

        return builder;
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.SIMPLE_NAME,
            include = JsonTypeInfo.As.EXISTING_PROPERTY,
            property = "elementType",
            visible = true)
    private interface ZestElementMixIn {}
}
