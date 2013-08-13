/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class ZestExpressionRegex.
 */
public class ZestExpressionRegex extends ZestExpression{
	
	/** The Constant LOC_HEAD. */
	private transient static final String LOC_HEAD = "HEAD"; 
	
	/** The Constant LOC_BODY. */
	private transient static final String LOC_BODY = "BODY"; 
	
	/** The Constant LOCATIONS. */
	private transient static final Set<String> LOCATIONS = 
			new HashSet<String>(Arrays.asList(new String[] {LOC_HEAD, LOC_BODY}));

	/** The regex. */
	private String regex;
	
	/** The location. */
	private String location;
	
	/** The inverse. */
	private boolean inverse=false;
	
	/** The pattern. */
	private transient Pattern pattern = null;

	/**
	 * Instantiates a new zest expression regex.
	 */
	public ZestExpressionRegex(){
		this(LOC_HEAD,null,false);
	}
	
	/**
	 * Instantiates a new zest expression regex.
	 *
	 * @param location the location
	 * @param regex the regex
	 */
	public ZestExpressionRegex(String location, String regex) {
		this(location, regex, false);
	}
	
	/**
	 * Instantiates a new zest expression regex.
	 *
	 * @param location the location
	 * @param regex the regex
	 * @param inverse the inverse
	 */
	public ZestExpressionRegex(String location, String regex, boolean inverse) {
		super ();
		this.inverse=inverse;
		this.setLocation(location);
		this.regex = regex;
		if (regex != null) {
			this.pattern = Pattern.compile(regex);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestExpressionElement#isTrue(org.mozilla.zest.core.v1.ZestResponse)
	 */
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

	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Sets the location.
	 *
	 * @param location the new location
	 */
	public void setLocation(String location) {
		if (! LOCATIONS.contains(location)) {
			throw new IllegalArgumentException("Unsupported location: " + location);
		}
		this.location = location;
	}

	/**
	 * Gets the regex.
	 *
	 * @return the regex
	 */
	public String getRegex() {
		return regex;
	}

	/**
	 * Sets the regex.
	 *
	 * @param regex the new regex
	 */
	public void setRegex(String regex) {
		this.regex = regex;
		this.pattern = Pattern.compile(regex);
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestExpression#isLeaf()
	 */
	@Override
	public boolean isLeaf() {
		return true;
	}


	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestExpression#isInverse()
	 */
	@Override
	public boolean isInverse() {
		return inverse;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestExpression#setInverse(boolean)
	 */
	@Override
	public void setInverse(boolean not) {
		inverse=not;
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestExpression#deepCopy()
	 */
	@Override
	public ZestExpressionRegex deepCopy() {
		return new ZestExpressionRegex(this.getLocation(), this.getRegex(), this.isInverse());
	}
	@Override
	public String toString(){
		String expression=(isInverse()?"NOT ":"")+"REGEX: "+regex+" , Location: "+location;
		return expression;
	}
	
}
