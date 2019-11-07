/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

/**
 * Submit the specified client element.
 *
 * @author simon
 */
public class ZestClientElementSubmit extends ZestClientElement {

    public ZestClientElementSubmit(String sessionIdName, String type, String element) {
        super(sessionIdName, type, element);
    }

    public ZestClientElementSubmit() {
        super();
    }

    @Override
    public String invoke(ZestRuntime runtime) throws ZestClientFailException {
        this.getWebElement(runtime).submit();

        return null;
    }

    @Override
    public ZestStatement deepCopy() {
        ZestClientElementSubmit copy =
                new ZestClientElementSubmit(
                        this.getWindowHandle(), this.getType(), this.getElement());
        copy.setEnabled(this.isEnabled());
        return copy;
    }

    @Override
    public boolean isPassive() {
        return false;
    }
}
