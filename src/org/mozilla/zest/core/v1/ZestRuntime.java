/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.io.File;
import java.io.IOException;

// TODO: Auto-generated Javadoc
/**
 * The Interface ZestRuntime.
 */
public interface ZestRuntime {
	
	/**
	 * Run.
	 *
	 * @param zest the zest
	 * @throws ZestAssertFailException the zest assert fail exception
	 * @throws ZestActionFailException the zest action fail exception
	 */
	void run(ZestScript zest) throws ZestAssertFailException, ZestActionFailException;
	
	/**
	 * Load.
	 *
	 * @param file the file
	 * @return the zest script
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	ZestScript load(File file) throws IOException;
	
	/**
	 * Save.
	 *
	 * @param zest the zest
	 * @param file the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void save(ZestScript zest, File file) throws IOException;
		
	/**
	 * Make request.
	 *
	 * @param req the req
	 * @return the zest response
	 */
	ZestResponse makeRequest(ZestRequest req);
	
}
