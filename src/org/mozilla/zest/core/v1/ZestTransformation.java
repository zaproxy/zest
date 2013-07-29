/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

// TODO: Auto-generated Javadoc
/**
 * The Class ZestTransformation.
 */
public abstract class ZestTransformation extends ZestElement {

	/**
	 * Instantiates a new zest transformation.
	 */
	public ZestTransformation() {
	}

	/**
	 * Transform.
	 *
	 * @param runner the runner
	 * @param request the request
	 * @throws ZestTransformFailException the zest transform fail exception
	 */
	public void transform (ZestRunner runner, ZestRequest request) throws ZestTransformFailException {
		throw new IllegalArgumentException();
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestElement#isSameSubclass(org.mozilla.zest.core.v1.ZestElement)
	 */
	@Override
	public boolean isSameSubclass(ZestElement ze) {
		return ze instanceof ZestTransformation;
	}

}
