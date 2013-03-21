/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ZestScript extends ZestElement {

	public static final String ZEST_URL = "https://developer.mozilla.org/en-US/docs/Zest";
	public static final String ABOUT = "This is a Zest script. For more details about Zest visit " + ZEST_URL;

	private String about = ABOUT;
	private int zestVersion = 1;
	private String generatedBy;
	private String author;
	private String title;
	private String description;
	private String prefix;
	private ZestTokens tokens = new ZestTokens();
	
	private List<ZestRequest> requests = new ArrayList<ZestRequest>();
	private List<ZestAuthentication> authentication = new ArrayList<ZestAuthentication>();
	
	public ZestScript () {
	}
	
	public ZestScript (String title, String description) {
		this.title = title;
		this.description = description;
	}

	@Override
	public ZestScript deepCopy() {
		ZestScript script = new ZestScript();
		this.duplicateTo(script);
		return script;
	}
	
	public void duplicateTo(ZestScript script) {
		script.about = this.about;
		script.zestVersion = this.zestVersion;
		script.generatedBy = this.generatedBy;
		script.author = this.author;
		script.title = this.title;
		script.description = this.description;
		script.prefix = this.prefix;
		script.tokens = this.tokens.deepCopy();
		
		for (ZestRequest zr : this.getRequests()) {
			script.add(zr.deepCopy());
		}
		for (ZestAuthentication za : this.getAuthentication()) {
			script.addAuthentication((ZestAuthentication)za.deepCopy());
		}
		// Correct references in the copy... 
		script.setUpRefs();
		script.setTokens(this.getTokens().deepCopy());
	}

	private void setUpRefs() {
		for (ZestRequest zr : this.getRequests()) {
			zr.setUpRefs(this);
		}
	}

	public void add(ZestRequest req) {
		this.requests.add(req);
		req.setIndex(this.requests.indexOf(req));
		req.setUpRefs(this);
		update(req);
	}
	
	public void add(int index, ZestRequest req) {
		this.requests.add(index, req);
		this.reindex(index);
		update(req);
	}
	
	public void move(int index, ZestRequest req) {
		this.requests.remove(req);
		this.requests.add(index, req);
		this.reindex(index);
	}
	
	public void remove(ZestRequest req) {
		this.removeRequest(this.requests.indexOf(req));
	}
	
	public void removeRequest(int index) {
		this.requests.remove(index);
		this.reindex(index);
	}
	
	public ZestRequest getRequest (int index) throws IndexOutOfBoundsException {
		return this.requests.get(index);
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ZestRequest> getRequests() {
		return requests;
	}

	public void setRequests(List<ZestRequest> requests) {
		this.requests = requests;
		reindex(0);	// Probably not needed, but wont hurt
		this.setUpRefs();
	}
	
	public void addAuthentication(ZestAuthentication auth) {
		this.authentication.add(auth);
	}

	public void removeAuthentication(ZestAuthentication auth) {
		this.authentication.remove(auth);
	}

	public List<ZestAuthentication> getAuthentication() {
		return authentication;
	}

	public void setAuthentication(List<ZestAuthentication> authentication) {
		this.authentication = authentication;
	}
	
	private void reindex(int start) {
		for (int i = start; i < this.requests.size(); i++) {
			// Note this will correct the indexes of any references
			this.requests.get(i).setIndex(i);
			// TODO Shouldnt really be here...
			this.update(this.requests.get(i));
		}
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) throws MalformedURLException {
		if (this.prefix != null) {
			// Replace the prefixes
			int oldLen = this.prefix.length();
			for (ZestRequest req : this.requests) {
				String urlStr = prefix + req.getUrl().toString().substring(oldLen);
				req.setUrl(new URL(urlStr));
			}
		} else {
			// Check all current requests start with the prefix
			for (ZestRequest req : this.requests) {
				if (! req.getUrl().toString().startsWith(prefix)) {
					throw new IllegalArgumentException("Request " + req.getUrl() + " does not start with prefix " + prefix);
				}
			}
		}
		this.prefix = prefix;
	}

	public ZestTokens getTokens() {
		return this.tokens;
	}

	public void setTokens(ZestTokens tokens) {
		this.tokens = tokens;
	}

	public void update(ZestRequest request) {
		List<String> allTokens = request.getTokens(this.tokens.getTokenStart(), this.tokens.getTokenEnd());
		for (String str : allTokens) {
			// Will default if not present
			this.tokens.addToken(str);
		}
	}

	public int getZestVersion() {
		return zestVersion;
	}

	public void setZestVersion(int zestVersion) {
		if (zestVersion != 1) {
			throw new IllegalArgumentException("Version " + zestVersion + " not supported by this class");
		}
		this.zestVersion = zestVersion;
	}

	public String getGeneratedBy() {
		return generatedBy;
	}

	public void setGeneratedBy(String generatedBy) {
		this.generatedBy = generatedBy;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
}
