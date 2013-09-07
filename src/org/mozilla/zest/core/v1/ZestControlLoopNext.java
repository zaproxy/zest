/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;

import java.net.MalformedURLException;
// TODO: Auto-generated Javadoc

/**
 * This class represents a NEXT statement for a loop.
 */
public class ZestControlLoopNext extends ZestControl {

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#setPrefix(java.lang.String, java.lang.String)
	 */
	@Override
	void setPrefix(String oldPrefix, String newPrefix)
			throws MalformedURLException {
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#deepCopy()
	 */
	@Override
	public ZestControlLoopNext deepCopy() {
		return new ZestControlLoopNext();
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#isPassive()
	 */
	@Override
	public boolean isPassive() {
		return true;
	}

}
