/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.security.InvalidParameterException;

// TODO: Auto-generated Javadoc
/** The Class ZestElement. */
public abstract class ZestElement {

    /** The Constant JSON_ELEMENT_TYPE. */
    public static final String JSON_ELEMENT_TYPE = "type";

    /** The element type. */
    private String elementType;

    /** Instantiates a new zest element. */
    public ZestElement() {
        this.setElementType(this.getClass().getSimpleName());
    }

    /**
     * Gets the element type.
     *
     * @return the element type
     */
    public String getElementType() {
        return this.elementType;
    }

    /**
     * Sets the element type.
     *
     * @param type the new element type
     */
    public void setElementType(String type) {
        // Just here to force the field to be output!
        this.elementType = type;
        if (!this.elementType.equals(this.getClass().getSimpleName())) {
            throw new InvalidParameterException(
                    "Bad type: got "
                            + this.elementType
                            + " expected "
                            + this.getClass().getSimpleName());
        }
    }

    /**
     * Checks if is same subclass.
     *
     * @param ze the ze
     * @return true, if is same subclass
     */
    public boolean isSameSubclass(ZestElement ze) {
        // Abstract subclasses should override this
        return this.getClass().equals(ze.getClass());
    }

    /**
     * Deep copy.
     *
     * @return the zest element
     */
    public abstract ZestElement deepCopy();

    // abstract String toDisplayString();

}
