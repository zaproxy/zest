/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.util.regex.Pattern;


public class ZestTestBodyRegex extends ZestTest {

	private String regex;
	private boolean inverse = false;
	
	private transient Pattern pattern = null;

	public ZestTestBodyRegex() {
	}
	
	public ZestTestBodyRegex(String regex) {
		super ();
		if (regex != null) {
			this.regex = regex;
			this.pattern = Pattern.compile(regex);
		}
	}
	
	public ZestTestBodyRegex(String message, String regex, boolean inverse) {
		super (message);
		this.regex = regex;
		this.pattern = Pattern.compile(regex);
		this.inverse = inverse;
	}
	
	@Override
	public boolean test (ZestResponse response) {
		if (response.getBody() == null) {
			return false;
		}
		boolean contains = pattern.matcher(response.getBody()).find();
		return inverse == contains;
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
	public ZestTestBodyRegex deepCopy() {
		return new ZestTestBodyRegex(super.getMessage(), this.regex, this.inverse);
	}
	
}
