/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.util.regex.Pattern;


public class ZestAssertHeaderRegex extends ZestAssertion {

	private String regex;
	private boolean inverse = false;

	transient private Pattern pattern = null;

	public ZestAssertHeaderRegex() {
	}
	
	public ZestAssertHeaderRegex(String regex) {
		super ();
		if (regex != null) {
			this.regex = regex;
			this.pattern = Pattern.compile(regex);
		}
	}
	
	public ZestAssertHeaderRegex(String regex, boolean inverse) {
		this (regex);
		this.inverse = inverse;
	}
	
	public ZestAssertHeaderRegex deepCopy() {
		return new ZestAssertHeaderRegex(this.regex, this.inverse);
	}
	
	@Override
	public boolean isValid (ZestResponse response) {
		boolean contains = pattern.matcher(response.getHeaders()).find();
		return inverse != contains;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
		this.pattern = Pattern.compile(regex);
	}

	public boolean isInverse() {
		return inverse;
	}

	public void setInverse(boolean inverse) {
		this.inverse = inverse;
	}

	
}
