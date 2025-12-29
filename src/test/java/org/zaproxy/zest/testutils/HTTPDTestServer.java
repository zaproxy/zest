/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.testutils;

import fi.iki.elonen.NanoHTTPD;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HTTPDTestServer extends NanoHTTPD {

    private List<NanoServerHandler> handlers = new ArrayList<>();
    private List<Request> requests = new ArrayList<>();

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

    public List<Request> getRequests() {
        return requests;
    }

    @Override
    public Response serve(IHTTPSession session) {
        requests.add(
                new Request(
                        session.getUri(),
                        session.getMethod().toString(),
                        session.getHeaders(),
                        NanoServerHandler.getBody(session)));

        for (NanoServerHandler handler : handlers) {
            if (handler.handles(session)) {
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

    public static record Request(
            String uri, String method, Map<String, String> headers, String body) {}
}
