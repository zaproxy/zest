/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The Class ZestTokens.
 */
public class ZestVariables extends ZestElement {

	/** The token start. */
	private String tokenStart = "{{";
	
	/** The token end. */
	private String tokenEnd = "}}";
	
	/** The tokens. */
	private Map<String, String> tokens = new HashMap<String, String>();
	
	/**
	 * Instantiates a new zest tokens.
	 */
	public ZestVariables () {
		super();
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestElement#deepCopy()
	 */
	@Override
	public ZestVariables deepCopy() {
		ZestVariables zt = new ZestVariables();
		zt.setTokenStart(this.tokenStart);
		zt.setTokenEnd(this.tokenEnd);
		for (Entry<String, String> entry : tokens.entrySet()) {
			zt.setToken(entry.getKey(), entry.getValue());
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
	public String getToken(String name) {
		return tokens.get(name);
	}

	/**
	 * Sets the tokens.
	 *
	 * @param tokens the tokens
	 */
	public void setTokens(Map<String, String> tokens) {
		this.tokens = tokens;
	}
	
	/**
	 * Gets the tokens.
	 *
	 * @return the tokens
	 */
	public List<String[]> getTokens() {
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
	
	/**
	 * Adds the token.
	 *
	 * @param name the name
	 */
	public void addToken(String name) {
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
	public void addTokens(Map<String, String> tokens) {
		this.tokens.putAll(tokens);
	}
	

	/**
	 * Sets the token.
	 *
	 * @param name the name
	 * @param value the value
	 */
	public void setToken(String name, String value) {
		this.tokens.put(name, value);
	}

}
