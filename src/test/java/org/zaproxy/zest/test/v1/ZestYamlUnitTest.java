/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.net.URI;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.zaproxy.zest.core.v1.ZestActionPrint;
import org.zaproxy.zest.core.v1.ZestConditional;
import org.zaproxy.zest.core.v1.ZestElement;
import org.zaproxy.zest.core.v1.ZestExpressionStatusCode;
import org.zaproxy.zest.core.v1.ZestJSON;
import org.zaproxy.zest.core.v1.ZestRequest;
import org.zaproxy.zest.core.v1.ZestScript;
import org.zaproxy.zest.core.v1.ZestYaml;

class ZestYamlUnitTest {

    private static Stream<Arguments> provideZestScriptAndYaml() throws Exception {
        var script = new ZestScript();
        var request = new ZestRequest();
        request.setMethod("POST");
        request.setData("name=\"Never Gonna Give You Up\"&artist=\"Rick Astley\"");
        request.setHeaders("Content-Type: application/x-www-form-urlencoded");
        request.setUrl(new URI("https://example.com/").toURL());
        script.add(request);
        var conditional = new ZestConditional(new ZestExpressionStatusCode(404));
        conditional.addIf(new ZestActionPrint("Got a 404!"));
        conditional.addElse(new ZestActionPrint("Didn't get a 404!"));
        script.add(conditional);

        String yaml =
                "---\n"
                        + "about: This is a Zest script. For more details about Zest visit https://github.com/zaproxy/zest/\n"
                        + "zestVersion: 0.8\n"
                        + "parameters:\n"
                        + "  tokenStart: \"{{\"\n"
                        + "  tokenEnd: \"}}\"\n"
                        + "  tokens: {}\n"
                        + "  elementType: ZestVariables\n"
                        + "statements:\n"
                        + "- url: https://example.com/\n"
                        + "  data: name=\"Never Gonna Give You Up\"&artist=\"Rick Astley\"\n"
                        + "  method: POST\n"
                        + "  headers: \"Content-Type: application/x-www-form-urlencoded\"\n"
                        + "  assertions: []\n"
                        + "  followRedirects: true\n"
                        + "  timestamp: 0\n"
                        + "  cookies: []\n"
                        + "  index: 1\n"
                        + "  enabled: true\n"
                        + "  elementType: ZestRequest\n"
                        + "- rootExpression:\n"
                        + "    code: 404\n"
                        + "    not: false\n"
                        + "    elementType: ZestExpressionStatusCode\n"
                        + "  ifStatements:\n"
                        + "  - message: Got a 404!\n"
                        + "    index: 1\n"
                        + "    enabled: true\n"
                        + "    elementType: ZestActionPrint\n"
                        + "  elseStatements:\n"
                        + "  - message: Didn't get a 404!\n"
                        + "    index: 2\n"
                        + "    enabled: true\n"
                        + "    elementType: ZestActionPrint\n"
                        + "  index: 2\n"
                        + "  enabled: true\n"
                        + "  elementType: ZestConditional\n"
                        + "authentication: []\n"
                        + "index: 0\n"
                        + "enabled: true\n"
                        + "elementType: ZestScript\n";

        return Stream.of(Arguments.of(script, yaml));
    }

    @ParameterizedTest
    @MethodSource("provideZestScriptAndYaml")
    void shouldConvertZestElementToYamlString(ZestElement element, String expectedYaml) {
        // When
        String yaml = ZestYaml.toString(element);
        // Then
        assertThat(yaml, is(equalTo(expectedYaml)));
    }

    @ParameterizedTest
    @MethodSource("provideZestScriptAndYaml")
    void shouldConvertYamlStringToZestElement(ZestElement expectedElement, String yaml) {
        // When
        ZestElement element = ZestYaml.fromString(yaml);
        // Then
        assertThat(element.getElementType().equals(expectedElement.getElementType()), is(true));
        assertThat(ZestJSON.toString(element), is(equalTo(ZestJSON.toString(expectedElement))));
    }
}
