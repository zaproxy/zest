/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class ZestYaml {
    public static String toString(ZestElement element) {
        try {
            String jsonString = ZestJSON.toString(element);
            JsonNode jsonNodeTree = new ObjectMapper().readTree(jsonString);
            return new YAMLMapper()
                    .configure(YAMLGenerator.Feature.MINIMIZE_QUOTES, true)
                    .writeValueAsString(jsonNodeTree);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static ZestElement fromString(String str) {
        try {
            ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
            Object obj = yamlReader.readValue(str, Object.class);
            ObjectMapper jsonWriter = new ObjectMapper();
            return ZestJSON.fromString(jsonWriter.writeValueAsString(obj));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
