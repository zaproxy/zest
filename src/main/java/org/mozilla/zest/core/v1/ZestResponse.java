/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.net.URL;

/** The Class ZestResponse. */
public class ZestResponse extends ZestElement {

    /** The url. */
    private URL url;

    /** The headers. */
    private String headers;

    /** The body. */
    private String body;

    /** The status code. */
    private int statusCode;

    /** The response time in ms. */
    private long responseTimeInMs;

    /**
     * Instantiates a new zest response.
     *
     * @param url the url
     * @param headers the headers
     * @param body the body
     * @param statusCode the status code
     * @param responseTimeInMs the response time in ms
     */
    public ZestResponse(
            URL url, String headers, String body, int statusCode, long responseTimeInMs) {
        this.url = url;
        this.headers = headers;
        this.body = body;
        this.statusCode = statusCode;
        this.responseTimeInMs = responseTimeInMs;
    }

    @Override
    public ZestResponse deepCopy() {
        ZestResponse zr =
                new ZestResponse(
                        this.url, this.headers, this.body, this.statusCode, this.responseTimeInMs);
        return zr;
    }

    /**
     * Gets the url.
     *
     * @return the url
     */
    public URL getUrl() {
        return url;
    }

    /**
     * Gets the headers.
     *
     * @return the headers
     */
    public String getHeaders() {
        return headers;
    }

    /**
     * Gets the body.
     *
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * Set the headers returned
     *
     * @param headers
     */
    public void setHeaders(String headers) {
        this.headers = headers;
    }

    /**
     * Set the body returned
     *
     * @param body
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Gets the status code.
     *
     * @return the status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Gets the response time in ms.
     *
     * @return the response time in ms
     */
    public long getResponseTimeInMs() {
        return responseTimeInMs;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setResponseTimeInMs(long responseTimeInMs) {
        this.responseTimeInMs = responseTimeInMs;
    }
}
