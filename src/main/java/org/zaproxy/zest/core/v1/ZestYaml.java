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
    private static final int CODE_POINT_LIMIT = 10 * 1024 * 1024; // 10 MB

    private static YAMLMapper yamlMapper = null;

    public static String toString(ZestElement element) {
        try {
            return getYamlMapper().writeValueAsString(element);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static ZestElement fromString(String str) {
        try {
            return getYamlMapper().readValue(str, ZestElement.class);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static YAMLMapper getYamlMapper() {
        if (yamlMapper == null) {
            LoaderOptions loaderOptions = new LoaderOptions();
            loaderOptions.setCodePointLimit(CODE_POINT_LIMIT);

            YAMLFactory yamlFactory = YAMLFactory.builder().loaderOptions(loaderOptions).build();
            YAMLMapper.Builder builder = YAMLMapper.builder(yamlFactory);

            builder.enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
                    .disable(YAMLGenerator.Feature.USE_NATIVE_TYPE_ID);

            yamlMapper = JacksonConfig.configureCommonBuilder(builder).build();
        }

        return yamlMapper;
    }
}
