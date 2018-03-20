/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.impl;

import java.io.IOException;
import org.mozilla.zest.core.v1.ZestAuthentication;
import org.mozilla.zest.core.v1.ZestOutputWriter;
import org.mozilla.zest.core.v1.ZestRequest;
import org.mozilla.zest.core.v1.ZestResponse;

/** @since 0.14 */
public interface ZestHttpClient {

    void init(ZestOutputWriter zestOutputWriter);

    void addAuthentication(ZestAuthentication zestAuthentication);

    void setProxy(String host, int port);

    ZestResponse send(ZestRequest req) throws IOException;
}
