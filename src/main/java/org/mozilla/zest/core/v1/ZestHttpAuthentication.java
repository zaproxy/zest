/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

// TODO: Auto-generated Javadoc
/** The Class ZestHttpAuthentication. */
public class ZestHttpAuthentication extends ZestAuthentication {

    /** The site. */
    private String site;

    /** The realm. */
    private String realm;

    /** The username. */
    private String username;

    /** The password. */
    private String password;

    /** Instantiates a new zest http authentication. */
    public ZestHttpAuthentication() {
        super();
    }

    /**
     * Instantiates a new zest http authentication.
     *
     * @param site the site
     * @param realm the realm
     * @param username the username
     * @param password the password
     */
    public ZestHttpAuthentication(String site, String realm, String username, String password) {
        super();
        this.site = site;
        this.realm = realm;
        this.username = username;
        this.password = password;
    }

    @Override
    public ZestHttpAuthentication deepCopy() {
        return new ZestHttpAuthentication(this.site, this.realm, this.username, this.password);
    }

    /**
     * Gets the site.
     *
     * @return the site
     */
    public String getSite() {
        return site;
    }

    /**
     * Sets the site.
     *
     * @param site the new site
     */
    public void setSite(String site) {
        this.site = site;
    }

    /**
     * Gets the realm.
     *
     * @return the realm
     */
    public String getRealm() {
        return realm;
    }

    /**
     * Sets the realm.
     *
     * @param realm the new realm
     */
    public void setRealm(String realm) {
        this.realm = realm;
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
