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
	
	public static final String REQUEST_URL 		= "request.url";
	public static final String REQUEST_HEADER 	= "request.header";
	public static final String REQUEST_BODY 	= "request.body";
	public static final String RESPONSE_URL 	= "response.url";
	public static final String RESPONSE_HEADER 	= "response.header";
	public static final String RESPONSE_BODY 	= "response.body";

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
	 * @param tokens the tokens
	 */
	public void setVariable(Map<String, String> tokens) {
		this.tokens = tokens;
	}
	
	/**
	 * Gets the tokens.
	 *
	 * @return the tokens
	 */
	public List<String[]> getVariable() {
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
	public void addVariable(Map<String, String> tokens) {
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
	
	public void setStandardVariables(ZestRequest request) {
		if (request != null) {
			if (request.getUrl()!= null) {
				this.setVariable(REQUEST_URL, request.getUrl().toString());
			}
			this.setVariable(REQUEST_HEADER, request.getHeaders());
			this.setVariable(REQUEST_BODY, request.getData());
		}
	}

	public void setStandardVariables(ZestResponse response) {
		if (response != null) {
			if (response.getUrl() != null) {
				this.setVariable(RESPONSE_URL, response.getUrl().toString());
			}
			this.setVariable(RESPONSE_HEADER, response.getHeaders());
			this.setVariable(RESPONSE_BODY, response.getBody());
		}
	}

}
