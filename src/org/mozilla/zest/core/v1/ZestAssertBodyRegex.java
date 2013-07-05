/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.util.regex.Pattern;

@Deprecated
public class ZestAssertBodyRegex extends ZestAssertion {

	private String regex;
	private boolean inverse = false;
	
	transient private Pattern pattern = null;

	public ZestAssertBodyRegex() {
	}
	
	public ZestAssertBodyRegex(String regex) {
		super ();
		if (regex != null) {
			this.regex = regex;
			this.pattern = Pattern.compile(regex);
		}
	}
	
	public ZestAssertBodyRegex(String regex, boolean inverse) {
		this (regex);
		this.inverse = inverse;
	}
	
	@Override
	public boolean isValid (ZestResponse response) {
		boolean contains = pattern.matcher(response.getBody()).find();
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

	@Override
	public ZestAssertBodyRegex deepCopy() {
		return new ZestAssertBodyRegex(this.regex, this.inverse);
	}

}
