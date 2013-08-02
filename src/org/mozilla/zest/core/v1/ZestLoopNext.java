/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Set;
// TODO: Auto-generated Javadoc

/**
 * This class represents a NEXT statement for a loop.
 */
public class ZestLoopNext extends ZestStatement {

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#setPrefix(java.lang.String, java.lang.String)
	 */
	@Override
	void setPrefix(String oldPrefix, String newPrefix)
			throws MalformedURLException {
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#getTokens(java.lang.String, java.lang.String)
	 */
	@Override
	Set<String> getTokens(String tokenStart, String tokenEnd) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#setUpRefs(org.mozilla.zest.core.v1.ZestScript)
	 */
	@Override
	void setUpRefs(ZestScript script) {
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#getTransformations()
	 */
	@Override
	public List<ZestTransformation> getTransformations() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#deepCopy()
	 */
	@Override
	public ZestLoopNext deepCopy() {
		return new ZestLoopNext();
	}

}
