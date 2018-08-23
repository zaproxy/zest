/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.test.v1;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;

/** Helper class that manages a HTTP server for use during tests. */
public abstract class ServerBasedTest {

    @Rule public WireMockRule server = createServer();

    protected WireMockRule createServer() {
        return new WireMockRule(options().dynamicPort(), false);
    }

    protected String getHostPort() {
        return server.getOptions().bindAddress() + ":" + server.port();
    }

    protected String getServerUrl(String path) {
        return "http://" + getHostPort() + path;
    }
}
