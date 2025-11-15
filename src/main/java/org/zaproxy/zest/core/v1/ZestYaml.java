/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.yaml.snakeyaml.LoaderOptions;
import org.zaproxy.zest.impl.jackson.JacksonConfig;

public class ZestYaml {
    private static final int DEFAULT_CODE_POINT_LIMIT = 3 * 1024 * 1024; // 3 MB

    private static YAMLMapper yamlMapper = null;
    private static int currentCodePointLimit = DEFAULT_CODE_POINT_LIMIT;

    public static String toString(ZestElement element) {
        try {
            return getYamlMapper().writeValueAsString(element);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static ZestElement fromString(String str) {
        try {
            return getYamlMapper(str.length()).readValue(str, ZestElement.class);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static YAMLMapper getYamlMapper() {
        return getYamlMapper(-1);
    }

    private static YAMLMapper getYamlMapper(int size) {
        YAMLMapper.Builder builder = null;

        // If hit the document size limitation,
        if (size > currentCodePointLimit) {
            // increase the limit by configuring YAMLFactory and constructing YAMLMapper.
            builder = createCustomBuilder(size);
        } else if (yamlMapper == null) {
            builder = YAMLMapper.builder();
        }

        if (builder != null) {
            // Apply YAML-specific features
            builder.enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
                    .disable(YAMLGenerator.Feature.USE_NATIVE_TYPE_ID);

            // Apply all common Zest configurations and build
            yamlMapper = JacksonConfig.configureCommonBuilder(builder).build();
        }

        return yamlMapper;
    }

    private static YAMLMapper.Builder createCustomBuilder(final int size) {
        double newSize = Math.pow(2, Math.ceil(Math.log(size) / Math.log(2)));
        int newLimit = (int) Math.min(newSize, Integer.MAX_VALUE);
        currentCodePointLimit = newLimit;

        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setCodePointLimit(newLimit);

        YAMLFactory yamlFactory = YAMLFactory.builder().loaderOptions(loaderOptions).build();
        return YAMLMapper.builder(yamlFactory);
    }
}
