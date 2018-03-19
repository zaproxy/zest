/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class ZestAssignRegexDelimiters allows you to assign a string to the specified variable from
 * the last response received. The string is delimited by the regexs specified.
 */
public class ZestAssignRegexDelimiters extends ZestAssignment {

    /** The prefix. */
    private String prefix;

    /** The postfix. */
    private String postfix;

    /** The location. */
    private String location;

    /** The Constant LOC_HEAD. */
    public static final transient String LOC_HEAD = "HEAD";

    /** The Constant LOC_BODY. */
    public static final transient String LOC_BODY = "BODY";

    /** The prefix pattern. */
    private transient Pattern prefixPattern = null;

    /** The postfix pattern. */
    private transient Pattern postfixPattern = null;

    /** The Constant LOCATIONS. */
    private static final transient Set<String> LOCATIONS =
            new HashSet<String>(Arrays.asList(new String[] {LOC_HEAD, LOC_BODY}));

    /** Instantiates a new zest action set token. */
    public ZestAssignRegexDelimiters() {
        super();
    }

    /**
     * Instantiates a new zest action set token.
     *
     * @param variableName the token name
     * @param location the location
     * @param prefix the prefix
     * @param postfix the postfix
     */
    public ZestAssignRegexDelimiters(
            String variableName, String location, String prefix, String postfix) {
        super(variableName);
        this.location = location;
        this.setPrefix(prefix);
        this.setPostfix(postfix);
    }

    /**
     * Instantiates a new zest action set token.
     *
     * @param index the index
     */
    public ZestAssignRegexDelimiters(int index) {
        super(index);
    }

    /**
     * Gets the prefix.
     *
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets the prefix.
     *
     * @param prefix the new prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
        this.prefixPattern = null;
    }

    /**
     * Gets the postfix.
     *
     * @return the postfix
     */
    public String getPostfix() {
        return postfix;
    }

    /**
     * Sets the postfix.
     *
     * @param postfix the new postfix
     */
    public void setPostfix(String postfix) {
        this.postfix = postfix;
        this.postfixPattern = null;
    }

    /**
     * Gets the location.
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location.
     *
     * @param location the new location
     */
    public void setLocation(String location) {
        if (!LOCATIONS.contains(location)) {
            throw new IllegalArgumentException("Unsupported location: " + location);
        }
        this.location = location;
    }

    @Override
    public ZestAssignRegexDelimiters deepCopy() {
        ZestAssignRegexDelimiters copy = new ZestAssignRegexDelimiters(this.getIndex());
        copy.setVariableName(this.getVariableName());
        copy.location = this.location;
        copy.setPrefix(this.prefix);
        copy.setPostfix(this.postfix);
        copy.setEnabled(this.isEnabled());
        return copy;
    }

    /**
     * Gets the token value.
     *
     * @param str the str
     * @return the token value
     */
    private String getTokenValue(String str) {
        if (str != null) {
            checkPatterns();

            Matcher prefixMatcher = this.prefixPattern.matcher(str);
            if (prefixMatcher.find()) {
                int tokenStart = prefixMatcher.end();
                String str2 = str.substring(tokenStart);
                Matcher postfixMatcher = this.postfixPattern.matcher(str2);
                if (postfixMatcher.find()) {
                    int tokenEnd = postfixMatcher.start();
                    return str2.substring(0, tokenEnd);
                }
            }
        }
        return null;
    }

    /**
     * Check if the pattern are set. They will not be set after a json deserialisation using field
     * reflexion.
     */
    private void checkPatterns() {
        if (prefixPattern == null) {
            prefixPattern = Pattern.compile(prefix);
        }
        if (postfixPattern == null) {
            postfixPattern = Pattern.compile(postfix);
        }
    }

    @Override
    public String assign(ZestResponse response, ZestRuntime runtime)
            throws ZestAssignFailException {
        if (prefix == null || prefix.length() == 0) {
            throw new ZestAssignFailException(this, "Null prefix");
        }
        if (postfix == null || postfix.length() == 0) {
            throw new ZestAssignFailException(this, "Null postfix");
        }
        if (response == null) {
            throw new ZestAssignFailException(this, "Null response");
        }
        String value;

        if (LOC_HEAD.equals(this.location)) {
            value = this.getTokenValue(response.getHeaders());
        } else if (LOC_BODY.equals(this.location)) {
            value = this.getTokenValue(response.getBody());
        } else {
            // Not specified - check in both (probably a v1 script)
            value = this.getTokenValue(response.getHeaders());
            if (value == null) {
                value = this.getTokenValue(response.getBody());
            }
        }

        if (value != null) {
            return value;
        }
        throw new ZestAssignFailException(
                this, "Failed to find value between '" + prefix + "' and '" + postfix + "'");
    }
}
