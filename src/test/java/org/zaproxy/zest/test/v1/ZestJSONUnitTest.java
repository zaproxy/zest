/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.JsonMappingException;
import java.net.URI;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
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

class ZestJSONUnitTest {

    private static Stream<Arguments> provideZestScriptAndJson() throws Exception {
        return Stream.concat(basicScriptWithRequest(), emptyScriptWithStatementDelay());
    }

    private static Stream<Arguments> basicScriptWithRequest() throws Exception {
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

        String json =
                """
                {
                  "about": "This is a Zest script. For more details about Zest visit https://github.com/zaproxy/zest/",
                  "zestVersion": "0.8",
                  "parameters": {
                    "tokenStart": "{{",
                    "tokenEnd": "}}",
                    "tokens": {},
                    "elementType": "ZestVariables"
                  },
                  "statements": [
                    {
                      "url": "https://example.com/",
                      "data": "name\\u003D\\"Never Gonna Give You Up\\"\\u0026artist\\u003D\\"Rick Astley\\"",
                      "method": "POST",
                      "headers": "Content-Type: application/x-www-form-urlencoded",
                      "assertions": [],
                      "followRedirects": true,
                      "timestamp": 0,
                      "cookies": [],
                      "index": 1,
                      "enabled": true,
                      "elementType": "ZestRequest"
                    },
                    {
                      "rootExpression": {
                        "code": 404,
                        "not": false,
                        "elementType": "ZestExpressionStatusCode"
                      },
                      "ifStatements": [
                        {
                          "message": "Got a 404!",
                          "index": 1,
                          "enabled": true,
                          "elementType": "ZestActionPrint"
                        }
                      ],
                      "elseStatements": [
                        {
                          "message": "Didn\\u0027t get a 404!",
                          "index": 2,
                          "enabled": true,
                          "elementType": "ZestActionPrint"
                        }
                      ],
                      "index": 2,
                      "enabled": true,
                      "elementType": "ZestConditional"
                    }
                  ],
                  "authentication": [],
                  "options": {},
                  "index": 0,
                  "enabled": true,
                  "elementType": "ZestScript"
                }
                """;

        return Stream.of(Arguments.of(script, json));
    }

    private static Stream<Arguments> emptyScriptWithStatementDelay() throws Exception {
        var script = new ZestScript();
        script.setOptions(Map.of(ZestScript.STATEMENT_DELAY_MS, "10"));
        String json =
                """
                {
                  "about": "This is a Zest script. For more details about Zest visit https://github.com/zaproxy/zest/",
                  "zestVersion": "0.8",
                  "parameters": {
                    "tokenStart": "{{",
                    "tokenEnd": "}}",
                    "tokens": {},
                    "elementType": "ZestVariables"
                  },
                  "statements": [],
                  "authentication": [],
                  "options": {
                    "statementDelay": "10"
                  },
                  "index": 0,
                  "enabled": true,
                  "elementType": "ZestScript"
                }
                """;
        return Stream.of(Arguments.of(script, json));
    }

    @ParameterizedTest
    @MethodSource("provideZestScriptAndJson")
    void shouldConvertZestElementToJsonString(ZestElement element, String expectedJson) {
        // When
        String json = ZestJSON.toString(element);
        // Then
        assertThat(json.trim(), is(equalTo(expectedJson.trim())));
    }

    @ParameterizedTest
    @MethodSource("provideZestScriptAndJson")
    void shouldConvertJsonStringToZestElement(ZestElement expectedElement, String json) {
        // When
        ZestElement element = ZestJSON.fromString(json);
        // Then
        assertThat(element.getElementType().equals(expectedElement.getElementType()), is(true));
        assertThat(ZestJSON.toString(element), is(equalTo(ZestJSON.toString(expectedElement))));
    }

    @Test
    void shouldSetOptions() {
        // Given
        String json =
                """
                {
                  "about": "This is a Zest script. For more details about Zest visit https://github.com/zaproxy/zest/",
                  "zestVersion": "0.8",
                  "statements": [],
                  "authentication": [],
                  "options": {
                    "statementDelay": "10"
                  },
                  "index": 0,
                  "enabled": true,
                  "elementType": "ZestScript"
                }
                """;

        // When
        ZestScript script = (ZestScript) ZestJSON.fromString(json);
        // Then
        assertEquals(script.getStatementDelay(), 10);
    }

    @Test
    void shouldFailOnBadOptions() {
        // Given
        String json =
                """
                {
                  "about": "This is a Zest script. For more details about Zest visit https://github.com/zaproxy/zest/",
                  "zestVersion": "0.8",
                  "statements": [],
                  "authentication": [],
                  "options": {
                    "statementDelai": "10"
                  },
                  "index": 0,
                  "enabled": true,
                  "elementType": "ZestScript"
                }
                """;

        // When
        Exception e = assertThrows(RuntimeException.class, () -> ZestJSON.fromString(json));
        // Then
        assertEquals(e.getCause().getClass(), JsonMappingException.class);
        assertEquals(
                e.getCause().getMessage().startsWith("Invalid parameter: statementDelai"), true);
    }
}
