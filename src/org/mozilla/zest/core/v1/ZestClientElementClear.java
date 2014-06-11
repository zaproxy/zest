/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

/**
 * Clear the specified client element.
 * @author simon
 *
 */
public class ZestClientElementClear extends ZestClientElement {

	public ZestClientElementClear(String sessionIdName, String type, String element) {
		super(sessionIdName, type, element);
	}
	
	public ZestClientElementClear() {
		super();
	}
	
	@Override
	public String invoke(ZestRuntime runtime) throws ZestClientFailException {
		this.getWebElement(runtime).clear();

		return null;
	}

	@Override
	public ZestStatement deepCopy() {
		return new ZestClientElementClear(this.getWindowHandle(), this.getElementType(), this.getElement());
	}

	@Override
	public boolean isPassive() {
		return false;
	}

}
