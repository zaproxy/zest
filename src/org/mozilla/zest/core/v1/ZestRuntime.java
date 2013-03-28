/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.io.File;
import java.io.IOException;

public interface ZestRuntime {
	
	void run(ZestScript zest) throws ZestAssertFailException, ZestActionFailException;
	
	ZestScript load(File file) throws IOException;
	
	void save(ZestScript zest, File file) throws IOException;
		
	ZestResponse makeRequest(ZestRequest req);
	
}
