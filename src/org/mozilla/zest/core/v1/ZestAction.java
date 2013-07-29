/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


// TODO: Auto-generated Javadoc
/**
 * The Class ZestAction.
 */
public abstract class ZestAction extends ZestStatement {

	/**
	 * Instantiates a new zest action.
	 */
	public ZestAction() {
		super();
	}

	/**
	 * Instantiates a new zest action.
	 *
	 * @param index the index
	 */
	public ZestAction(int index) {
		super(index);
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#isSameSubclass(org.mozilla.zest.core.v1.ZestElement)
	 */
	@Override
	public boolean isSameSubclass(ZestElement ze) {
		return ze instanceof ZestAction;
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#setPrefix(java.lang.String, java.lang.String)
	 */
	@Override
	void setPrefix(String oldPrefix, String newPrefix) throws MalformedURLException {
		// Ignore
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#getTokens(java.lang.String, java.lang.String)
	 */
	@Override
	Set<String> getTokens(String tokenStart, String tokenEnd) {
		return new HashSet<String>();
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#setUpRefs(org.mozilla.zest.core.v1.ZestScript)
	 */
	@Override
	void setUpRefs(ZestScript script) {
		// Ignore
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#getTransformations()
	 */
	@Override
	public List<ZestTransformation> getTransformations() {
		return new ArrayList<ZestTransformation>();
	}

	/**
	 * Invoke.
	 *
	 * @param response the response
	 * @return the string
	 * @throws ZestActionFailException the zest action fail exception
	 */
	public abstract String invoke(ZestResponse response) throws ZestActionFailException;

}
