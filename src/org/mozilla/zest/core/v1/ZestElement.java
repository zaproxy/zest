/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.security.InvalidParameterException;

public abstract class ZestElement {

	public static final String JSON_ELEMENT_TYPE = "type";

	private String elementType;
	
	public ZestElement() {
		this.setElementType(this.getClass().getSimpleName());
	}
	
	public String getElementType() {
		return this.elementType;
	}

	public void setElementType(String type) {
		// Just here to force the field to be output!
		this.elementType = type;
		if (! this.elementType.equals(this.getClass().getSimpleName())) {
			throw new InvalidParameterException("Bad type: got " + this.elementType + " expected " + this.getClass().getSimpleName());
		}
	}
	
	public boolean isSameSubclass(ZestElement ze) {
		// Abstract subclasses should override this
		return this.getClass().equals(ze.getClass());
	}
	
	abstract ZestElement deepCopy();
	
	// abstract String toDisplayString();
	
}
