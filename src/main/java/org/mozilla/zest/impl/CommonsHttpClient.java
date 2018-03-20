/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.OptionsMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.TraceMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.mozilla.zest.core.v1.ZestAuthentication;
import org.mozilla.zest.core.v1.ZestCookie;
import org.mozilla.zest.core.v1.ZestHttpAuthentication;
import org.mozilla.zest.core.v1.ZestOutputWriter;
import org.mozilla.zest.core.v1.ZestRequest;
import org.mozilla.zest.core.v1.ZestResponse;

/**
 * The Class CommonsHttpClient
 *
 * @since 0.14
 */
class CommonsHttpClient implements ZestHttpClient {

    private ZestOutputWriter zestOutputWriter;
    private HttpClient httpclient = new HttpClient();

    public CommonsHttpClient(HttpClient httpclient) {
        this.httpclient = httpclient;
    }

    public void setParams(HttpClientParams params) {
        httpclient.setParams(params);
    }

    @Override
    public void init(ZestOutputWriter zestOutputWriter) {
        this.zestOutputWriter = zestOutputWriter;
    }

    @Override
    public void addAuthentication(ZestAuthentication zestAuthentication) {
        if (zestAuthentication instanceof ZestHttpAuthentication) {
            addHttpAuthentication((ZestHttpAuthentication) zestAuthentication);
        }
    }

    @Override
    public void setProxy(String host, int port) {
        httpclient.getHostConfiguration().setProxy(host, port);
    }

    private void addHttpAuthentication(ZestHttpAuthentication zestHttpAuthentication) {
        Credentials defaultCredentials =
                new UsernamePasswordCredentials(
                        zestHttpAuthentication.getUsername(), zestHttpAuthentication.getPassword());
        AuthScope authScope =
                new AuthScope(zestHttpAuthentication.getSite(), 80, AuthScope.ANY_REALM);
        httpclient.getState().setCredentials(authScope, defaultCredentials);
    }

    @Override
    public ZestResponse send(ZestRequest req) throws IOException {
        HttpMethod method;
        URI uri = new URI(req.getUrl().toString(), false);

        switch (req.getMethod()) {
            case "GET":
                method = new GetMethod(uri.toString());
                // Can only redirect on GETs
                method.setFollowRedirects(req.isFollowRedirects());
                break;
            case "POST":
                method = new PostMethod(uri.toString());
                break;
            case "OPTIONS":
                method = new OptionsMethod(uri.toString());
                break;
            case "HEAD":
                method = new HeadMethod(uri.toString());
                break;
            case "PUT":
                method = new PutMethod(uri.toString());
                break;
            case "DELETE":
                method = new DeleteMethod(uri.toString());
                break;
            case "TRACE":
                method = new TraceMethod(uri.toString());
                break;
            default:
                throw new IllegalArgumentException("Method not supported: " + req.getMethod());
        }

        setHeaders(method, req.getHeaders());

        for (ZestCookie zestCookie : req.getZestCookies()) {
            Cookie cookie =
                    new Cookie(
                            zestCookie.getDomain(),
                            zestCookie.getName(),
                            zestCookie.getValue(),
                            zestCookie.getPath(),
                            zestCookie.getExpiryDate(),
                            zestCookie.isSecure());
            httpclient.getState().addCookie(cookie);
        }

        if (method instanceof EntityEnclosingMethod) {
            // The setRequestEntity call trashes any Content-Type specified, so record it and
            // reapply it after
            Header contentType = method.getRequestHeader("Content-Type");
            RequestEntity requestEntity = new StringRequestEntity(req.getData(), null, null);

            ((EntityEnclosingMethod) method).setRequestEntity(requestEntity);

            if (contentType != null) {
                method.setRequestHeader(contentType);
            }
        }

        int code = 0;
        String responseHeader = null;
        String responseBody = null;
        Date start = new Date();
        try {
            code = httpclient.executeMethod(method);

            responseHeader =
                    method.getStatusLine().toString()
                            + "\r\n"
                            + arrayToStr(method.getResponseHeaders());
            responseBody = method.getResponseBodyAsString();

        } finally {
            method.releaseConnection();
        }
        // Update the headers with the ones actually sent
        req.setHeaders(arrayToStr(method.getRequestHeaders()));

        if (method.getStatusCode() == 302
                && req.isFollowRedirects()
                && !req.getMethod().equals("GET")) {
            // Follow the redirect 'manually' as the httpclient lib only supports them for GET
            // requests
            method = new GetMethod(method.getResponseHeader("Location").getValue());
            // Just in case there are multiple redirects
            method.setFollowRedirects(req.isFollowRedirects());

            try {
                this.debug(req.getMethod() + " : " + req.getUrl());
                code = httpclient.executeMethod(method);

                responseHeader =
                        method.getStatusLine().toString()
                                + "\r\n"
                                + arrayToStr(method.getResponseHeaders());
                responseBody = method.getResponseBodyAsString();

            } finally {
                method.releaseConnection();
            }
        }

        return new ZestResponse(
                req.getUrl(),
                responseHeader,
                responseBody,
                code,
                new Date().getTime() - start.getTime());
    }

    private void setHeaders(HttpMethod method, String headers) {
        if (headers == null) {
            return;
        }
        String[] headerArray = headers.split("\r\n");
        String header;
        String value;
        for (String line : headerArray) {
            int colonIndex = line.indexOf(":");
            if (colonIndex > 0) {
                header = line.substring(0, colonIndex);
                value = line.substring(colonIndex + 1).trim();
                String lcHeader = header.toLowerCase(Locale.ROOT);
                if (!lcHeader.startsWith("cookie") && !lcHeader.startsWith("content-length")) {
                    method.addRequestHeader(new Header(header, value));
                }
            }
        }
    }

    private String arrayToStr(Header[] headers) {
        StringBuilder sb = new StringBuilder();
        for (Header header : headers) {
            sb.append(header.toString());
        }
        return sb.toString();
    }

    private void debug(String str) {
        if (zestOutputWriter != null) {
            zestOutputWriter.debug(str);
        }
    }
}
