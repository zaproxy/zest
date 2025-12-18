/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.Separators;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.zaproxy.zest.impl.jackson.HtmlSafeCharacterEscapes;
import org.zaproxy.zest.impl.jackson.JacksonConfig;

/** The Class ZestJSON. */
public class ZestJSON {

    /** The object mapper. */
    private static JsonMapper jsonMapper = null;

    /**
     * To string.
     *
     * @param element the element
     * @return the string
     */
    public static String toString(ZestElement element) {
        try {
            return getJsonMapper().writeValueAsString(element);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize ZestElement", e);
        }
    }

    /**
     * From string.
     *
     * @param str the str
     * @return the zest element
     */
    public static ZestElement fromString(String str) {
        ZestElement ze;
        try {
            ze = getJsonMapper().readValue(str, ZestElement.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize ZestElement", e);
        }

        if (ze instanceof ZestStatement zs) {
            zs.init();
        }

        return ze;
    }

    private static JsonMapper getJsonMapper() {
        if (jsonMapper == null) {
            JsonFactory factory =
                    ((JsonFactoryBuilder) JsonFactory.builder())
                            .characterEscapes(HtmlSafeCharacterEscapes.instance())
                            .build();

            JsonMapper.Builder builder =
                    JsonMapper.builder(factory)
                            .defaultPrettyPrinter(createPrettyPrinter())
                            // Equivalent to setPrettyPrinting()
                            .enable(SerializationFeature.INDENT_OUTPUT);

            jsonMapper = JacksonConfig.configureCommonBuilder(builder).build();
        }

        return jsonMapper;
    }

    private static DefaultPrettyPrinter createPrettyPrinter() {
        Separators separators =
                new Separators(
                        Separators.DEFAULT_ROOT_VALUE_SEPARATOR,
                        PrettyPrinter.DEFAULT_SEPARATORS.getObjectFieldValueSeparator(),
                        Separators.Spacing.AFTER,
                        PrettyPrinter.DEFAULT_SEPARATORS.getObjectEntrySeparator(),
                        Separators.Spacing.NONE,
                        "",
                        PrettyPrinter.DEFAULT_SEPARATORS.getArrayValueSeparator(),
                        Separators.Spacing.NONE,
                        "");

        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(separators);

        var indenter = DefaultIndenter.SYSTEM_LINEFEED_INSTANCE.withLinefeed("\n");
        prettyPrinter.indentArraysWith(indenter);
        prettyPrinter.indentObjectsWith(indenter);

        return prettyPrinter;
    }
}
