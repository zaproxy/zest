/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

public abstract class ZestTransformation extends ZestElement {

	public ZestTransformation() {
	}

	public void transform (ZestRunner runner, ZestRequest request) throws ZestTransformFailException {
		throw new IllegalArgumentException();
	}
	
	@Override
	public boolean isSameSubclass(ZestElement ze) {
		return ze instanceof ZestTransformation;
	}

}
