/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

/**
 * Click on the specified client element.
 *
 * @author simon
 */
public class ZestClientElementClick extends ZestClientElement {

    public ZestClientElementClick(String sessionIdName, String type, String element) {
        super(sessionIdName, type, element);
    }

    public ZestClientElementClick() {
        super();
    }

    @Override
    public String invoke(ZestRuntime runtime) throws ZestClientFailException {

        this.getWebElement(runtime).click();

        return null;
    }

    @Override
    public ZestStatement deepCopy() {
        ZestClientElementClick copy =
                new ZestClientElementClick(
                        this.getWindowHandle(), this.getType(), this.getElement());
        copy.setEnabled(this.isEnabled());
        return copy;
    }

    @Override
    public boolean isPassive() {
        return false;
    }
}
