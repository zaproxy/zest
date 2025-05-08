/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.testutils;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import java.io.IOException;
import org.apache.commons.io.IOUtils;

public abstract class NanoServerHandler {

    private String name;

    public NanoServerHandler(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    protected abstract Response serve(IHTTPSession session);

    /**
     * Consumes the request body.
     *
     * @param session the session that has the request
     */
    protected void consumeBody(IHTTPSession session) {
        try {
            session.getInputStream().skip(getBodySize(session));
        } catch (IOException e) {
            System.err.println("Failed to consume body:");
            e.printStackTrace();
        }
    }

    /**
     * Gets the size of the request body.
     *
     * @param session the session that has the request
     * @return the size of the body
     */
    protected int getBodySize(IHTTPSession session) {
        String contentLengthHeader = session.getHeaders().get("content-length");
        if (contentLengthHeader == null) {
            return 0;
        }

        int contentLength = 0;
        try {
            contentLength = Integer.parseInt(contentLengthHeader);
        } catch (NumberFormatException e) {
            System.err.println("Failed to parse content-length value: " + contentLengthHeader);
            e.printStackTrace();
            return 0;
        }

        if (contentLength <= 0) {
            return 0;
        }
        return contentLength;
    }

    /**
     * Gets the request body.
     *
     * @param session the session that has the request
     * @return the body
     */
    public String getBody(IHTTPSession session) {
        int contentLength = getBodySize(session);
        if (contentLength == 0) {
            return "";
        }

        byte[] bytes = new byte[contentLength];
        try {
            IOUtils.readFully(session.getInputStream(), bytes);
        } catch (IOException e) {
            System.err.println("Failed to read the body:");
            e.printStackTrace();
            return "";
        }
        return new String(bytes);
    }

    /**
     * Gets the first parameter which is contained in the parameters list
     *
     * @param session the session that has the request
     * @param param the parameter name
     * @return the first value of the parameters
     */
    protected static String getFirstParamValue(IHTTPSession session, String param) {
        return session.getParameters().get(param) != null
                ? session.getParameters().get(param).get(0)
                : null;
    }
}
