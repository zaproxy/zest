/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

public class ZestActionSetToken extends ZestAction {

	private String tokenName;
	private String prefix;
	private String postfix;

	public ZestActionSetToken() {
		super();
	}
	
	public ZestActionSetToken(String tokenName, String prefix, String postfix) {
		super();
		this.tokenName = tokenName;
		this.prefix = prefix;
		this.postfix = postfix;
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
	}

	public String getPostfix() {
		return postfix;
	}

	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}

	@Override
	public ZestActionSetToken deepCopy() {
		ZestActionSetToken copy = new ZestActionSetToken(this.getIndex());
		copy.tokenName = this.tokenName;
		copy.prefix = this.prefix;
		copy.postfix = this.postfix;
		return copy;
	}
	
	private String getTokenValue(String str) {
		if (str != null) {
			int prefixStart = str.indexOf(prefix);
			if (prefixStart >= 0) {
				int valueStart = prefixStart + prefix.length();
				int postfixStart = str.indexOf(postfix, valueStart);
				if (postfixStart >= 0) {
					return str.substring(valueStart, postfixStart); 
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
		String value = this.getTokenValue(response.getHeaders());
		if (value == null) {
			value = this.getTokenValue(response.getBody());
		}
		if (value != null) {
			return value;
		}
		throw new ZestActionFailException(this, "Failed to find value between '" + prefix + "' and '" + postfix + "'");
	}

}
