/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

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
    public ZestClientElementClick deepCopy() {
        return this.deepCopy(ZestClientElementClick::new);
    }

    @Override
    public boolean isPassive() {
        return false;
    }
}
