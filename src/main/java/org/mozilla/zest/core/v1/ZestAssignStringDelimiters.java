/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * The Class ZestAssignStringDelimiters allows you to assign a string to the specified variable from
 * the last response received. The string is delimited by the strings specified.
 */
public class ZestAssignStringDelimiters extends ZestAssignment {

    /** The prefix. */
    private String prefix;

    /** The postfix. */
    private String postfix;

    /** The location. */
    private String location;

    /** The location constant which represents the Head of a response. */
    public static final transient String LOC_HEAD = "HEAD";

    /** The location constant which represents the Body of a response. */
    public static final transient String LOC_BODY = "BODY";

    /** The set of valid locations. */
    private static final transient Set<String> LOCATIONS =
            new HashSet<String>(Arrays.asList(new String[] {LOC_HEAD, LOC_BODY}));

    /** Instantiates a new zest action set variable. */
    public ZestAssignStringDelimiters() {
        super();
    }

    /**
     * Instantiates a new zest action set variable.
     *
     * @param variableName the variable name
     * @param location the location
     * @param prefix the prefix
     * @param postfix the postfix
     */
    public ZestAssignStringDelimiters(
            String variableName, String location, String prefix, String postfix) {
        super(variableName);
        this.location = location;
        this.setPrefix(prefix);
        this.setPostfix(postfix);
    }

    /**
     * Instantiates a new zest action set variable.
     *
     * @param index the index
     */
    public ZestAssignStringDelimiters(int index) {
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
    public ZestAssignStringDelimiters deepCopy() {
        ZestAssignStringDelimiters copy = new ZestAssignStringDelimiters(this.getIndex());
        copy.setVariableName(this.getVariableName());
        copy.location = this.location;
        copy.setPrefix(this.prefix);
        copy.setPostfix(this.postfix);
        copy.setEnabled(this.isEnabled());
        return copy;
    }

    /**
     * Gets the variable value.
     *
     * @param str the str
     * @return the variable value
     */
    private String getVariableValue(String str) {
        if (str != null && prefix != null && postfix != null) {
            int startIndex = str.indexOf(prefix);
            if (startIndex >= 0) {
                startIndex += prefix.length();
                int endIndex = str.indexOf(postfix, startIndex);
                if (endIndex >= 0) {
                    return str.substring(startIndex, endIndex);
                }
            }
        }
        return null;
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
            value = this.getVariableValue(response.getHeaders());
        } else if (LOC_BODY.equals(this.location)) {
            value = this.getVariableValue(response.getBody());
        } else {
            // Not specified - check in both (probably a v1 script)
            value = this.getVariableValue(response.getHeaders());
            if (value == null) {
                value = this.getVariableValue(response.getBody());
            }
        }

        if (value != null) {
            return value;
        }
        throw new ZestAssignFailException(
                this, "Failed to find value between '" + prefix + "' and '" + postfix + "'");
    }
}
