/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public abstract class ZestAction extends ZestStatement {

	public ZestAction() {
		super();
	}

	public ZestAction(int index) {
		super(index);
	}

	@Override
	public boolean isSameSubclass(ZestElement ze) {
		return ze instanceof ZestAction;
	}
	
	@Override
	void setPrefix(String oldPrefix, String newPrefix) throws MalformedURLException {
		// Ignore
	}

	@Override
	Set<String> getTokens(String tokenStart, String tokenEnd) {
		return new HashSet<String>();
	}

	@Override
	void setUpRefs(ZestScript script) {
		// Ignore
	}

	@Override
	public List<ZestTransformation> getTransformations() {
		return new ArrayList<ZestTransformation>();
	}

	public abstract String invoke(ZestResponse response) throws ZestActionFailException;

}
