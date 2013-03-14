/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

public class ZestHttpAuthentication extends ZestAuthentication {

	private String site;
	private String realm;
	private String username;
	private String password;
	
	public ZestHttpAuthentication() {
		super();
	}
	
	public ZestHttpAuthentication(String site, String realm, String username,
			String password) {
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
	
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	
}
