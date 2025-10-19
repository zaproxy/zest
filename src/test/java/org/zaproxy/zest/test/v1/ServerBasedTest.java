/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.extension.RegisterExtension;

/** Helper class that manages a HTTP server for use during tests. */
public abstract class ServerBasedTest {

    @RegisterExtension WireMockExtension server = createServer();

    protected WireMockExtension createServer() {
        return WireMockExtension.newInstance()
                .options(options().dynamicPort())
                .failOnUnmatchedRequests(false)
                .build();
    }

    protected String getHostPort() {
        return "127.0.0.1:" + server.getPort();
    }

    protected String getServerUrl(String path) {
        return "http://" + getHostPort() + path;
    }
}
