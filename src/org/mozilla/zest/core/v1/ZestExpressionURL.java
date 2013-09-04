/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class ZestExpressionURL.
 */
public class ZestExpressionURL extends ZestExpression {

	/** The include regexes. */
	private List<String> includeRegexes = new ArrayList<String>();
	
	/** The exclude regexes. */
	private List<String> excludeRegexes = new ArrayList<String>();

	/** The include patterns. */
	private transient List<Pattern> includePatterns = new ArrayList<Pattern>();
	
	/** The exclude patterns. */
	private transient List<Pattern> excludePatterns = new ArrayList<Pattern>();

	/**
	 * Instantiates a new zest expression url.
	 */
	public ZestExpressionURL() {
		super();
	}

	/**
	 * Instantiates a new zest expression url.
	 *
	 * @param includeRegexes the include regexes
	 * @param excludeRegexes the exclude regexes
	 */
	public ZestExpressionURL(List<String> includeRegexes,
			List<String> excludeRegexes) {
		this.setIncludeRegexes(includeRegexes);
		this.setExcludeRegexes(excludeRegexes);
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestExpressionElement#isTrue(org.mozilla.zest.core.v1.ZestResponse)
	 */
	public boolean isTrue(ZestRuntime runtime) {
		ZestRequest req = runtime.getLastRequest();
		if (req == null) {
			return false;
		}
		String url = req.getUrl().toString();
		boolean inc = false;

		for (Pattern pattern : includePatterns) {
			if (pattern.matcher(url).find()) {
				inc = true;
				break;
			}
		}
		if (!inc) {
			// Not explicitly included
			return false;
		}
		for (Pattern pattern : excludePatterns) {
			if (pattern.matcher(url).find()) {
				// explicitly excluded
				return false;
			}
		}
		// Included and not excluded
		return true;
	}

	/**
	 * Gets the include regexes.
	 *
	 * @return the include regexes
	 */
	public List<String> getIncludeRegexes() {
		return includeRegexes;
	}

	/**
	 * Gets the exclude regexes.
	 *
	 * @return the exclude regexes
	 */
	public List<String> getExcludeRegexes() {
		return excludeRegexes;
	}

	/**
	 * Sets the include regexes.
	 *
	 * @param includeRegexes the new include regexes
	 */
	public void setIncludeRegexes(List<String> includeRegexes) {
		this.includeRegexes = includeRegexes;

		this.includePatterns.clear();
		if (includeRegexes != null) {
			for (String regex : includeRegexes) {
				this.includePatterns.add(Pattern.compile(regex));
			}
		}
	}

	/**
	 * Sets the exclude regexes.
	 *
	 * @param excludeRegexes the new exclude regexes
	 */
	public void setExcludeRegexes(List<String> excludeRegexes) {
		this.excludeRegexes = excludeRegexes;

		this.excludePatterns.clear();
		if (excludeRegexes != null) {
			for (String regex : excludeRegexes) {
				this.excludePatterns.add(Pattern.compile(regex));
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestExpression#deepCopy()
	 */
	@Override
	public ZestExpressionURL deepCopy() {
		ZestExpressionURL copy = new ZestExpressionURL();
		List<String> copyIncludeRegex = new ArrayList<>(includeRegexes);
		List<String> copyExcludeRegex = new ArrayList<>(excludeRegexes);
		copy.includeRegexes = copyIncludeRegex;
		copy.excludeRegexes = copyExcludeRegex;
		return copy;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		String expression=(isInverse()?"NOT ":"")+"URL: ACCEPT:";
		for(String s:includeRegexes){
			expression+=" "+s;
		}
		expression+=", EXCLUDE:";
		for(String s:excludeRegexes){
			expression+=s+" ";
		}
		return expression;
	}
}
