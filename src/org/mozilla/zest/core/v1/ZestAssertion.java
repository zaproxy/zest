/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;


public abstract class ZestAssertion extends ZestElement {

	public ZestAssertion() {
	}

	public boolean isValid (ZestResponse response) {
		throw new IllegalArgumentException();
	}

	@Override
	public boolean isSameSubclass(ZestElement ze) {
		return ze instanceof ZestAssertion;
	}

}
