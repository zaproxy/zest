/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.net.MalformedURLException;

/**
 * Exits the script returning a string.
 */
public class ZestComment extends ZestStatement {
	
	private String comment;
	
	public ZestComment () {
	}
	
	public ZestComment (int index) {
		super(index);
	}
	
	public ZestComment (String comment) {
		this.comment = comment;
	}

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
	public ZestComment deepCopy() {
		return new ZestComment(comment);
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#isPassive()
	 */
	@Override
	public boolean isPassive() {
		return true;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
