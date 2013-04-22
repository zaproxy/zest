/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZestActionSetToken extends ZestAction {

	private String tokenName;
	private String prefix;
	private String postfix;
	private String location;

	transient public static final String LOC_HEAD = "HEAD"; 
	transient public static final String LOC_BODY = "BODY"; 

	transient private Pattern prefixPattern = null;
	transient private Pattern postfixPattern = null;
	transient private static final Set<String> LOCATIONS = 
			new HashSet<String>(Arrays.asList(new String[] {LOC_HEAD, LOC_BODY}));

	public ZestActionSetToken() {
		super();
	}
	
	public ZestActionSetToken(String tokenName, String location, String prefix, String postfix) {
		super();
		this.tokenName = tokenName;
		this.location = location;
		this.setPrefix(prefix);
		this.setPostfix(postfix);
	}
	
	public ZestActionSetToken(int index) {
		super(index);
	}

	public String getTokenName() {
		return tokenName;
	}

	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
		if (prefix != null) {
			this.prefixPattern = Pattern.compile(prefix);
		}
	}

	public String getPostfix() {
		return postfix;
	}

	public void setPostfix(String postfix) {
		this.postfix = postfix;
		if (postfix != null) {
			this.postfixPattern = Pattern.compile(postfix);
		}
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		if (! LOCATIONS.contains(location)) {
			throw new IllegalArgumentException("Unsupported location: " + location);
		}
		this.location = location;
	}

	@Override
	public ZestActionSetToken deepCopy() {
		ZestActionSetToken copy = new ZestActionSetToken(this.getIndex());
		copy.tokenName = this.tokenName;
		copy.location = this.location;
		copy.setPrefix(this.prefix);
		copy.setPostfix(this.postfix);
		return copy;
	}
	
	private String getTokenValue(String str) {
		if (str != null) {
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

	
	@Override
	public String invoke(ZestResponse response) throws ZestActionFailException {
		if (prefix == null || prefix.length() == 0) {
			throw new ZestActionFailException(this, "Null prefix");
		}
		if (postfix == null || postfix.length() == 0) {
			throw new ZestActionFailException(this, "Null postfix");
		}
		if (response == null) {
			throw new ZestActionFailException(this, "Null response");
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
		throw new ZestActionFailException(this, "Failed to find value between '" + prefix + "' and '" + postfix + "'");
	}

}
