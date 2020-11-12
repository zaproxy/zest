/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

// TODO: Auto-generated Javadoc

/** This class represents a NEXT statement for a loop. */
public class ZestControlLoopNext extends ZestControl {

    @Override
    void setPrefix(String oldPrefix, String newPrefix) {}

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
