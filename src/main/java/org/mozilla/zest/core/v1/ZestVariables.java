/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/** The Class ZestTokens. */
public class ZestVariables extends ZestElement {

    /** The Request URL. */
    public static final String REQUEST_URL = "request.url";

    /** The Request URL. */
    public static final String REQUEST_METHOD = "request.method";

    /** The Request Headers. */
    public static final String REQUEST_HEADER = "request.header";

    /** The Request Body. */
    public static final String REQUEST_BODY = "request.body";

    /** The Response URL. */
    public static final String RESPONSE_URL = "response.url";

    /** The Response Headers. */
    public static final String RESPONSE_HEADER = "response.header";

    /** The Response Body. */
    public static final String RESPONSE_BODY = "response.body";

    /** The token start. */
    private String tokenStart = "{{";

    /** The token end. */
    private String tokenEnd = "}}";

    /** The tokens. */
    private Map<String, String> tokens = new LinkedHashMap<>();

    /** Instantiates a new zest tokens. */
    public ZestVariables() {
        super();
    }

    @Override
    public ZestVariables deepCopy() {
        ZestVariables zt = new ZestVariables();
        zt.setTokenStart(this.tokenStart);
        zt.setTokenEnd(this.tokenEnd);
        for (Entry<String, String> entry : tokens.entrySet()) {
            zt.setVariable(entry.getKey(), entry.getValue());
        }
        return zt;
    }

    /**
     * Gets the token start.
     *
     * @return the token start
     */
    public String getTokenStart() {
        return tokenStart;
    }

    /**
     * Sets the token start.
     *
     * @param tokenStart the new token start
     */
    public void setTokenStart(String tokenStart) {
        this.tokenStart = tokenStart;
    }

    /**
     * Gets the token end.
     *
     * @return the token end
     */
    public String getTokenEnd() {
        return tokenEnd;
    }

    /**
     * Sets the token end.
     *
     * @param tokenEnd the new token end
     */
    public void setTokenEnd(String tokenEnd) {
        this.tokenEnd = tokenEnd;
    }

    /**
     * Gets the token.
     *
     * @param name the name
     * @return the token
     */
    public String getVariable(String name) {
        return tokens.get(name);
    }

    /**
     * Sets the tokens.
     *
     * <p>It's created a copy of the given map.
     *
     * @param tokens the tokens
     */
    public void setVariable(Map<String, String> tokens) {
        this.tokens = new LinkedHashMap<>(tokens);
    }

    /**
     * Gets the tokens.
     *
     * @return the tokens
     */
    public List<String[]> getVariables() {
        List<String[]> list = new ArrayList<String[]>();
        for (Entry<String, String> entry : tokens.entrySet()) {
            list.add(new String[] {entry.getKey(), entry.getValue()});
        }
        return list;
    }

    /**
     * Adds the token.
     *
     * @param name the name
     * @param value the value
     */
    public void addVariable(String name, String value) {
        if (this.tokens.get(name) == null) {
            if (value != null) {
                this.tokens.put(name, value);
            } else {
                // Dont know of it. so add it with a default of the same name
                this.tokens.put(name, name);
            }
        }
    }

    /**
     * Adds the token.
     *
     * @param name the name
     */
    public void addVariable(String name) {
        if (this.tokens.get(name) == null) {
            // Dont know of it. so add it with a default of the same name
            this.tokens.put(name, name);
        }
    }

    /**
     * Adds the tokens.
     *
     * @param tokens the tokens
     */
    public void addVariables(Map<String, String> tokens) {
        this.tokens.putAll(tokens);
    }

    /**
     * Sets the token.
     *
     * @param name the name
     * @param value the value
     */
    public void setVariable(String name, String value) {
        this.tokens.put(name, value);
    }

    private String replaceInString(String str, boolean urlEncode, List<String> previous) {
        if (str == null) {
            return null;
        }
        boolean changed = false;
        for (String[] nvPair : getVariables()) {
            String tokenStr = getTokenStart() + nvPair[0] + getTokenEnd();
            if (str.contains(tokenStr)) {
                if (!previous.contains(nvPair[0])) {
                    // To prevent loops
                    previous.add(nvPair[0]);
                    changed = true;
                    str = str.replace(tokenStr, getReplacementValue(nvPair[1]));
                }
            }
            if (!changed && urlEncode) {
                // Try again with encoded value
                try {
                    tokenStr = URLEncoder.encode(tokenStr, "UTF-8");
                    if (str.contains(tokenStr)) {
                        if (!previous.contains(nvPair[0])) {
                            // To prevent loops
                            previous.add(nvPair[0]);
                            changed = true;
                            str = str.replace(tokenStr, getReplacementValue(nvPair[1]));
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    // Ignore
                }
            }
        }
        if (changed) {
            // keep going to handle tokens in tokens
            return this.replaceInString(str, urlEncode, previous);
        }
        return str;
    }

    /**
     * Gets the value suitable for {@link #replaceInString(String, boolean) replacement in the
     * strings}.
     *
     * <p>If the given {@code value} is {@code null} it returns an empty string, otherwise the
     * {@code value}.
     *
     * @param value the value to be used as replacement
     * @return the value suitable for replacement in strings, never {@code null}.
     */
    private String getReplacementValue(String value) {
        return (value == null) ? "" : value;
    }

    public String replaceInString(String str, boolean urlEncode) {
        List<String> prev = new ArrayList<String>();
        return this.replaceInString(str, urlEncode, prev);
    }
}
