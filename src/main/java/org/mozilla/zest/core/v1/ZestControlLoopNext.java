/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.net.MalformedURLException;

// TODO: Auto-generated Javadoc

/** This class represents a NEXT statement for a loop. */
public class ZestControlLoopNext extends ZestControl {

    @Override
    void setPrefix(String oldPrefix, String newPrefix) throws MalformedURLException {}

    @Override
    public ZestControlLoopNext deepCopy() {
        ZestControlLoopNext copy = new ZestControlLoopNext();
        copy.setEnabled(this.isEnabled());
        return copy;
    }

    @Override
    public boolean isPassive() {
        return true;
    }
}
