/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class ZestExpressionRegex extends ZestExpression implements ZestExpressionElement{
	
	private transient static final String LOC_HEAD = "HEAD"; 
	private transient static final String LOC_BODY = "BODY"; 
	private transient static final Set<String> LOCATIONS = 
			new HashSet<String>(Arrays.asList(new String[] {LOC_HEAD, LOC_BODY}));

	private String regex;
	private String location;
	
	private ZestExpressionElement parent=null;
	private boolean inverse=false;
	
	private transient Pattern pattern = null;

	public ZestExpressionRegex(){
		this(null,null,false);
	}
	public ZestExpressionRegex(String location, String regex) {
		this(location, regex, false);
	}
	public ZestExpressionRegex(String location, String regex, boolean inverse) {
		super ();
		this.inverse=inverse;
		this.setLocation(location);
		this.regex = regex;
		if (regex != null) {
			this.pattern = Pattern.compile(regex);
		}
	}
	
	public boolean isTrue (ZestResponse response) {
		String str = null;		
		if (LOC_HEAD.equals(this.location)) {
			str = response.getHeaders();
		} else if (LOC_BODY.equals(this.location)) {
			str = response.getBody();
		}
		if (str == null) {
			return false;
		}
		if (pattern == null && regex != null) {
			this.pattern = Pattern.compile(regex);
		}
		
		return pattern.matcher(str).find();
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

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
		this.pattern = Pattern.compile(regex);
	}

//	@Override
//	public ZestExpressionRegex deepCopy() {
//		ZestExpressionRegex copy = new ZestExpressionRegex(this.getIndex());
//		copy.location = location;
//		copy.regex = regex;
//		for (ZestStatement stmt : this.getIfStatements()) {
//			copy.addIf(stmt.deepCopy());
//		}
//		for (ZestStatement stmt : this.getElseStatements()) {
//			copy.addElse(stmt.deepCopy());
//		}
//		return copy;
//	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public boolean evaluate(ZestResponse response) {
		return inverse?!isTrue(response):isTrue(response);
	}

	@Override
	public boolean isInverse() {
		return inverse;
	}

	@Override
	public void setInverse(boolean not) {
		inverse=not;
	}
	@Override
	public ZestExpression deepCopy() {
		return new ZestExpressionRegex(this.getLocation(), this.getRegex(), this.isInverse());
	}
	
}
