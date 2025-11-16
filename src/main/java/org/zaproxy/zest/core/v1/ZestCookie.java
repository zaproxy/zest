/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.Date;
import java.util.Objects;

/**
 * @since 0.14.0
 */
public class ZestCookie {

    private String domain;
    private String name;
    private String value;
    private String path;
    private Date expiry;
    private boolean secure;

    @JsonCreator
    public ZestCookie(
            @JsonProperty("domain") String domain,
            @JsonProperty("name") String name,
            @JsonProperty("value") String value,
            @JsonProperty("path") String path,
            @JsonProperty("expiry") Date expiry,
            @JsonProperty("secure") boolean secure) {
        this.domain = domain;
        this.name = name;
        this.value = value;
        this.path = path;
        this.expiry = expiry;
        this.secure = secure;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @JsonGetter("expiry")
    public Date getExpiryDate() {
        return expiry;
    }

    @JsonSetter("expiry")
    public void setExpiryDate(Date expiry) {
        this.expiry = expiry;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, expiry, name, path, secure, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ZestCookie other = (ZestCookie) obj;
        return secure == other.secure
                && Objects.equals(domain, other.domain)
                && Objects.equals(expiry, other.expiry)
                && Objects.equals(name, other.name)
                && Objects.equals(path, other.path)
                && Objects.equals(value, other.value);
    }
}
