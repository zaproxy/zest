/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

/** @since 0.14 */
public interface ZestOutputWriter {

    /**
     * Outputs the specified string
     *
     * @param str
     */
    void output(String str);

    /**
     * Outputs the supplied message if debugging is turned on
     *
     * @param str
     */
    void debug(String str);
}
