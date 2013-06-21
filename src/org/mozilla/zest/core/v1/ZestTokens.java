/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ZestTokens extends ZestElement {

	private String tokenStart = "{{";
	private String tokenEnd = "}}";
	
	private Map<String, String> tokens = new HashMap<String, String>();
	
	public ZestTokens () {
		super();
	}

	@Override
	public ZestTokens deepCopy() {
		ZestTokens zt = new ZestTokens();
		zt.setTokenStart(this.tokenStart);
		zt.setTokenEnd(this.tokenEnd);
		for (Entry<String, String> entry : tokens.entrySet()) {
			zt.setToken(entry.getKey(), entry.getValue());
		}
		return zt;
	}

	public String getTokenStart() {
		return tokenStart;
	}

	public void setTokenStart(String tokenStart) {
		this.tokenStart = tokenStart;
	}

	public String getTokenEnd() {
		return tokenEnd;
	}

	public void setTokenEnd(String tokenEnd) {
		this.tokenEnd = tokenEnd;
	}

	public String getToken(String name) {
		return tokens.get(name);
	}

	public void setTokens(Map<String, String> tokens) {
		this.tokens = tokens;
	}
	
	public List<String[]> getTokens() {
		List<String[]> list = new ArrayList<String[]>();
		for (Entry<String, String> entry : tokens.entrySet()) {
			list.add(new String[] {entry.getKey(), entry.getValue()});
		}
		return list;
	}
	
	public void addToken(String name, String value) {
		if (this.tokens.get(name) == null) {
			if (value != null) {
				this.tokens.put(name, value);
			} else {
				// Dont know of it. so add it with a default of the same name
				this.tokens.put(name, name);
			}
		}
	}
	
	public void addToken(String name) {
		if (this.tokens.get(name) == null) {
			// Dont know of it. so add it with a default of the same name
			this.tokens.put(name, name);
		}
	}

	public void addTokens(Map<String, String> tokens) {
		this.tokens.putAll(tokens);
	}
	

	public void setToken(String name, String value) {
		this.tokens.put(name, value);
	}

}
