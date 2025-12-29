/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.test.v1;

import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.zaproxy.zest.core.v1.ZestClientLaunch;
import org.zaproxy.zest.testutils.HTTPDTestServer;

/** Helper class that manages a HTTP server for use during tests. */
public abstract class ServerBasedTest {

    protected HTTPDTestServer server;

    @BeforeEach
    void startServer() throws IOException {
        server = new HTTPDTestServer(0);
        server.start();
    }

    @AfterEach
    void stopServer() {
        if (server != null) {
            server.stop();
        }
    }

    protected String getHostPort() {
        return "127.0.0.1:" + server.getListeningPort();
    }

    protected String getServerUrl(String path) {
        return "http://" + getHostPort() + path;
    }

    protected class TestClientLaunch extends ZestClientLaunch {

        TestClientLaunch(String windowHandle, String path) {
            super(windowHandle, "firefox", getServerUrl(path));
        }
    }
}
