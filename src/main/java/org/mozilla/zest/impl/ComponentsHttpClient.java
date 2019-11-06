/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import javax.net.ssl.SSLContext;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.mozilla.zest.core.v1.ZestAuthentication;
import org.mozilla.zest.core.v1.ZestCookie;
import org.mozilla.zest.core.v1.ZestHttpAuthentication;
import org.mozilla.zest.core.v1.ZestOutputWriter;
import org.mozilla.zest.core.v1.ZestRequest;
import org.mozilla.zest.core.v1.ZestResponse;

/**
 * HTTP client built on Apache HttpComponents Client.
 *
 * @since 0.14.0
 */
class ComponentsHttpClient implements ZestHttpClient {

    private final HttpClient httpClient;
    private final HttpClientContext httpContext;
    private final RequestConfig defaultRequestConfig;
    private ZestOutputWriter zestOutputWriter;

    /**
     * Use the built-in HTTP client.
     *
     * @param timeoutInSeconds number of seconds for connection timeout.
     * @param skipSSLCertificateCheck skip the SSL certificate check
     */
    ComponentsHttpClient(int timeoutInSeconds, boolean skipSSLCertificateCheck) {
        this.httpClient = getHttpClient(skipSSLCertificateCheck);
        this.httpContext = HttpClientContext.create();
        this.httpContext.setCookieStore(new BasicCookieStore());
        this.defaultRequestConfig =
                RequestConfig.custom()
                        .setCircularRedirectsAllowed(true)
                        .setConnectTimeout(timeoutInSeconds * 1_000)
                        .setConnectionRequestTimeout(timeoutInSeconds * 1_000)
                        // To improve compatibility with more webapps use STANDARD,
                        // it will also become the default in following HttpClient versions.
                        .setCookieSpec(CookieSpecs.STANDARD)
                        .build();
    }

    private static HttpClient getHttpClient(boolean skipSSLCertificateCheck) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setRedirectStrategy(new LaxRedirectStrategy());

        if (skipSSLCertificateCheck) {
            SSLContext sslContext;
            try {
                sslContext =
                        SSLContexts.custom()
                                .loadTrustMaterial(TrustSelfSignedStrategy.INSTANCE)
                                .build();
            } catch (Exception e) {
                // there should be no exception as we don't load key material and the algorithm is
                // built-in
                throw new RuntimeException(
                        "Unexpected exception when configuring the HTTP client to skip the SSL certificate check.",
                        e);
            }
            SSLConnectionSocketFactory sslSocketFactory =
                    new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
            httpClientBuilder.setSSLSocketFactory(sslSocketFactory);
        }

        return httpClientBuilder.build();
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
        RequestConfig localRequestConfig;
        synchronized (defaultRequestConfig) {
            localRequestConfig =
                    RequestConfig.copy(defaultRequestConfig)
                            .setProxy(new HttpHost(host, port))
                            .build();
        }
        synchronized (httpContext) {
            httpContext.setRequestConfig(localRequestConfig);
        }
    }

    private void addHttpAuthentication(ZestHttpAuthentication zestHttpAuthentication) {
        Credentials credentials =
                new UsernamePasswordCredentials(
                        zestHttpAuthentication.getUsername(), zestHttpAuthentication.getPassword());
        // FIXME: Why is here a hard-coded port (80)?
        AuthScope authScope =
                new AuthScope(zestHttpAuthentication.getSite(), 80, AuthScope.ANY_REALM);
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(authScope, credentials);

        synchronized (httpContext) {
            httpContext.setCredentialsProvider(credentialsProvider);
        }
    }

    @Override
    public ZestResponse send(ZestRequest req) throws IOException {
        HttpRequestBase method;
        switch (req.getMethod()) {
            case "GET":
                method = new HttpGet(req.getUrl().toString());
                break;
            case "POST":
                method = new HttpPost(req.getUrl().toString());
                break;
            case "OPTIONS":
                method = new HttpOptions(req.getUrl().toString());
                break;
            case "HEAD":
                method = new HttpHead(req.getUrl().toString());
                break;
            case "PUT":
                method = new HttpPut(req.getUrl().toString());
                break;
            case "DELETE":
                method = new HttpDelete(req.getUrl().toString());
                break;
            case "TRACE":
                method = new HttpTrace(req.getUrl().toString());
                break;
            default:
                throw new IllegalArgumentException("Method not supported: " + req.getMethod());
        }

        method.setConfig(defaultRequestConfig);
        setHeaders(method, req.getHeaders());

        for (ZestCookie zestCookie : req.getCookies()) {
            BasicClientCookie cookie =
                    new BasicClientCookie(zestCookie.getName(), zestCookie.getValue());
            cookie.setDomain(zestCookie.getDomain());
            cookie.setPath(zestCookie.getPath());
            cookie.setExpiryDate(zestCookie.getExpiryDate());
            cookie.setSecure(zestCookie.isSecure());

            httpContext.getCookieStore().addCookie(cookie);
        }

        if (method instanceof HttpEntityEnclosingRequestBase) {
            HttpEntity requestBody = new StringEntity(req.getData());
            HttpEntityEnclosingRequestBase methodWithEntity =
                    (HttpEntityEnclosingRequestBase) method;
            methodWithEntity.setEntity(requestBody);
        }

        int code = 0;
        String responseHeader = null;
        String responseBody = null;
        Date start = new Date();
        req.setTimestamp(start.getTime());
        try {
            debug(req.getMethod() + ": " + req.getUrl());
            HttpResponse response = httpClient.execute(method, httpContext);
            code = response.getStatusLine().getStatusCode();

            responseHeader =
                    response.getStatusLine().toString()
                            + "\r\n"
                            + arrayToStr(response.getAllHeaders());

            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                responseBody = EntityUtils.toString(responseEntity);
            }

        } finally {
            method.releaseConnection();
        }
        // Update the headers with the ones actually sent
        req.setHeaders(arrayToStr(httpContext.getRequest().getAllHeaders()));

        return new ZestResponse(
                req.getUrl(),
                responseHeader,
                responseBody,
                code,
                new Date().getTime() - start.getTime());
    }

    private static void setHeaders(HttpRequestBase method, String headers) {
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
                    method.addHeader(header, value);
                }
            }
        }
    }

    private static String arrayToStr(Header[] headers) {
        StringBuilder sb = new StringBuilder();
        for (Header header : headers) {
            sb.append(header.toString()).append("\r\n");
        }
        return sb.toString();
    }

    private void debug(String str) {
        if (zestOutputWriter != null) {
            zestOutputWriter.debug(str);
        }
    }
}
