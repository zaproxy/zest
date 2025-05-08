/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.testutils;

import fi.iki.elonen.NanoHTTPD;
import java.util.ArrayList;
import java.util.List;

public class HTTPDTestServer extends NanoHTTPD {

    private List<NanoServerHandler> handlers = new ArrayList<>();
    private List<String> requestedUris = new ArrayList<>();

    private NanoServerHandler handler404 =
            new NanoServerHandler("") {
                @Override
                protected Response serve(IHTTPSession session) {
                    consumeBody(session);
                    return newFixedLengthResponse(
                            Response.Status.NOT_FOUND,
                            MIME_HTML,
                            "<html><head><title>404</title></head><body>404 Not Found</body></html>");
                }
            };

    public HTTPDTestServer(int port) {
        super(port);
    }

    public List<String> getRequestedUris() {
        return requestedUris;
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        requestedUris.add(uri);

        for (NanoServerHandler handler : handlers) {
            if (uri.startsWith(handler.getName())) {
                return handler.serve(session);
            }
        }
        return handler404.serve(session);
    }

    public void addHandler(NanoServerHandler handler) {
        this.handlers.add(handler);
    }

    public void removeHandler(NanoServerHandler handler) {
        this.handlers.remove(handler);
    }

    public void setHandler404(NanoServerHandler handler) {
        this.handler404 = handler;
    }
}
