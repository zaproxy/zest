/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.net.MalformedURLException;

/** An abstract class that all client related statements extend. */
public abstract class ZestClient extends ZestStatement {

    /** Instantiates a new zest action. */
    public ZestClient() {
        super();
    }

    /**
     * Instantiates a new zest client.
     *
     * @param index the index
     */
    public ZestClient(int index) {
        super(index);
    }

    public abstract String invoke(ZestRuntime runtime) throws ZestClientFailException;

    @Override
    public boolean isSameSubclass(ZestElement ze) {
        return ze instanceof ZestClient;
    }

    @Override
    void setPrefix(String oldPrefix, String newPrefix) throws MalformedURLException {
        // Ignore
    }
}
