/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

/** Exits the script returning a string. */
public class ZestControlReturn extends ZestControl {

    private String value;

    public ZestControlReturn() {}

    public ZestControlReturn(int index) {
        super(index);
    }

    public ZestControlReturn(String value) {
        this.value = value;
    }

    @Override
    void setPrefix(String oldPrefix, String newPrefix) {}

    @Override
    public ZestControlReturn deepCopy() {
        ZestControlReturn copy = new ZestControlReturn(value);
        copy.setEnabled(this.isEnabled());
        return copy;
    }

    @Override
    public boolean isPassive() {
        return true;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
