/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class ZestConditionURL extends ZestConditional {
	
	private List<String> includeRegexes = new ArrayList<String>();
	private List<String> excludeRegexes = new ArrayList<String>();
	
	private transient List<Pattern> includePatterns = new ArrayList<Pattern>();
	private transient List<Pattern> excludePatterns = new ArrayList<Pattern>();

	public ZestConditionURL() {
		super();
	}
	
	public ZestConditionURL(int index) {
		super(index);
	}
	
	public ZestConditionURL(List<String> includeRegexes, List<String> excludeRegexes) {
		this.setIncludeRegexes(includeRegexes);
		this.setExcludeRegexes(excludeRegexes);
	}
	
	@Override
	public boolean isTrue (ZestResponse response) {
		String url = response.getUrl().toString();
		boolean inc = false;
		
		for (Pattern pattern: includePatterns) {
			if (pattern.matcher(url).find()) {
				inc = true;
				break;
			}
		}
		if (! inc) {
			// Not explicitly included
			return false;
		}
		for (Pattern pattern: excludePatterns) {
			if (pattern.matcher(url).find()) {
				// explicitly excluded
				return false;
			}
		}
		// Included and not excluded
		return true;
	}


	public List<String> getIncludeRegexes() {
		return includeRegexes;
	}

	public List<String> getExcludeRegexes() {
		return excludeRegexes;
	}

	public void setIncludeRegexes(List<String> includeRegexes) {
		this.includeRegexes = includeRegexes;
		
		this.includePatterns.clear();
		for (String regex: includeRegexes) {
			this.includePatterns.add(Pattern.compile(regex));
		}
	}

	public void setExcludeRegexes(List<String> excludeRegexes) {
		this.excludeRegexes = excludeRegexes;
		
		this.excludePatterns.clear();
		for (String regex: excludeRegexes) {
			this.excludePatterns.add(Pattern.compile(regex));
		}
	}

	@Override
	public ZestConditionURL deepCopy() {
		ZestConditionURL copy = new ZestConditionURL(this.getIndex());
		
		List<String> copyIncRegexes = new ArrayList<String>();
		Collections.copy(copyIncRegexes, this.includeRegexes);
		copy.setIncludeRegexes(copyIncRegexes);

		List<String> copyExcRegexes = new ArrayList<String>();
		Collections.copy(copyExcRegexes, this.excludeRegexes);
		copy.setExcludeRegexes(copyExcRegexes);

		for (ZestStatement stmt : this.getIfStatements()) {
			copy.addIf(stmt.deepCopy());
		}
		for (ZestStatement stmt : this.getElseStatements()) {
			copy.addElse(stmt.deepCopy());
		}
		return copy;
	}
	
}
