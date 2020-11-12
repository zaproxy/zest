/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

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
    void setPrefix(String oldPrefix, String newPrefix) {
        // Ignore
    }
}
