/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class ZestConditionRegex extends ZestConditional implements ZestConditionalElement{
	
	private transient static final String LOC_HEAD = "HEAD"; 
	private transient static final String LOC_BODY = "BODY"; 
	private transient static final Set<String> LOCATIONS = 
			new HashSet<String>(Arrays.asList(new String[] {LOC_HEAD, LOC_BODY}));
	private transient static final String DEFAULT_NAME="default_regex_";
	private static transient int counter=0;

	private String regex;
	private String location;
	
	private ZestConditionalElement parent=null;
	private boolean inverse=false;
	private String name=DEFAULT_NAME+(counter++);
	
	private transient Pattern pattern = null;

	public ZestConditionRegex() {
		super();
	}
	public ZestConditionRegex(ZestConditionalElement parent){
		super();
		this.setParent(parent);
	}
	
	public ZestConditionRegex(int index) {
		super(index);
	}
	
	public ZestConditionRegex(String location, String regex) {
		this (location, regex, false);
	}
	public ZestConditionRegex(int index, ZestConditionalElement parent) {
		this (index);
		this.setParent(parent);
	}
	public ZestConditionRegex(String location, String regex, boolean inverse) {
		super ();
		this.setLocation(location);
		this.regex = regex;
		if (regex != null) {
			this.pattern = Pattern.compile(regex);
		}
	}
	public ZestConditionRegex(String location, String regex, ZestConditionalElement parent) {
		this(location, regex, false);
		this.setParent(parent);
	}
	public ZestConditionRegex(String location, String regex, boolean inverse, ZestConditionalElement parent) {
		super ();
		this.inverse=inverse;
		this.setParent(parent);
		this.setLocation(location);
		this.regex = regex;
		if (regex != null) {
			this.pattern = Pattern.compile(regex);
		}
	}
	
	@Override
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

	@Override
	public ZestConditionRegex deepCopy() {
		ZestConditionRegex copy = new ZestConditionRegex(this.getIndex());
		copy.location = location;
		copy.regex = regex;
		for (ZestStatement stmt : this.getIfStatements()) {
			copy.addIf(stmt.deepCopy());
		}
		for (ZestStatement stmt : this.getElseStatements()) {
			copy.addElse(stmt.deepCopy());
		}
		return copy;
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public boolean isRoot() {
		return getParent()==null;
	}

	@Override
	public ZestConditionalElement getParent() {
		return this.parent;
	}

	@Override
	public ZestConditionalElement setParent(ZestConditionalElement new_parent) {
		ZestConditionalElement old_parent=this.getParent();
		this.parent=new_parent;
		return old_parent;
	}

	@Override
	public boolean evaluate(ZestResponse response) {
		return inverse?!isTrue(response):isTrue(response);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String setName(String new_name) {
		String oldName=this.getName();
		this.name=new_name;
		return oldName;
	}

	@Override
	public boolean isNot() {
		return inverse;
	}

	@Override
	public void setNot(boolean not) {
		inverse=not;
	}
	
}
