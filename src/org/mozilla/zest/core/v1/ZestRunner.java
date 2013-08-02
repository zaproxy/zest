/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

// TODO: Auto-generated Javadoc
/**
 * The Interface ZestRunner.
 */
public interface ZestRunner {

	/**
	 * Run.
	 *
	 * @param script the script
	 * @throws ZestTransformFailException the zest transform fail exception
	 * @throws ZestAssertFailException the zest assert fail exception
	 * @throws ZestActionFailException the zest action fail exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ZestInvalidCommonTestException the zest invalid common test exception
	 */
	void run (ZestScript script) 
			throws ZestTransformFailException, ZestAssertFailException, ZestActionFailException, IOException,
			ZestInvalidCommonTestException;

	/**
	 * Run script.
	 *
	 * @param reader the reader
	 * @throws ZestTransformFailException the zest transform fail exception
	 * @throws ZestAssertFailException the zest assert fail exception
	 * @throws ZestActionFailException the zest action fail exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ZestInvalidCommonTestException the zest invalid common test exception
	 */
	void runScript (Reader reader) 
			throws ZestTransformFailException, ZestAssertFailException, ZestActionFailException, IOException,
			ZestInvalidCommonTestException;
	
	/**
	 * Run script.
	 *
	 * @param script the script
	 * @throws ZestTransformFailException the zest transform fail exception
	 * @throws ZestAssertFailException the zest assert fail exception
	 * @throws ZestActionFailException the zest action fail exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ZestInvalidCommonTestException the zest invalid common test exception
	 */
	void runScript (String script) 
			throws ZestTransformFailException, ZestAssertFailException, ZestActionFailException, IOException,
			ZestInvalidCommonTestException;
	
	/**
	 * Run.
	 *
	 * @param script the script
	 * @param target the target
	 * @throws ZestTransformFailException the zest transform fail exception
	 * @throws ZestAssertFailException the zest assert fail exception
	 * @throws ZestActionFailException the zest action fail exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ZestInvalidCommonTestException the zest invalid common test exception
	 */
	void run (ZestScript script, ZestRequest target) 
			throws ZestTransformFailException, ZestAssertFailException, ZestActionFailException, IOException,
			ZestInvalidCommonTestException;

	/**
	 * Run statement.
	 *
	 * @param script the script
	 * @param stmt the stmt
	 * @param lastResponse the last response
	 * @return the zest response
	 * @throws ZestAssertFailException the zest assert fail exception
	 * @throws ZestActionFailException the zest action fail exception
	 * @throws ZestTransformFailException the zest transform fail exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ZestInvalidCommonTestException the zest invalid common test exception
	 */
	ZestResponse runStatement(ZestScript script, ZestStatement stmt, ZestResponse lastResponse) 
			throws ZestAssertFailException, ZestActionFailException, ZestTransformFailException, IOException,
			ZestInvalidCommonTestException;

	/**
	 * Send.
	 *
	 * @param request the request
	 * @return the zest response
	 * @throws ZestTransformFailException the zest transform fail exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	ZestResponse send (ZestRequest request) throws ZestTransformFailException, IOException;
	
	/**
	 * Handle response.
	 *
	 * @param request the request
	 * @param response the response
	 * @throws ZestTransformFailException the zest transform fail exception
	 * @throws ZestAssertFailException the zest assert fail exception
	 * @throws ZestActionFailException the zest action fail exception
	 */
	void handleResponse (ZestRequest request, ZestResponse response) 
			throws ZestTransformFailException, ZestAssertFailException, ZestActionFailException;
	
	/**
	 * Handle transforms.
	 *
	 * @param script the script
	 * @param request the request
	 * @param response the response
	 * @throws ZestTransformFailException the zest transform fail exception
	 */
	void handleTransforms(ZestScript script, ZestRequest request, ZestResponse response) throws ZestTransformFailException;
	
	/**
	 * Handle transform.
	 *
	 * @param request the request
	 * @param transform the transform
	 * @throws ZestTransformFailException the zest transform fail exception
	 */
	void handleTransform (ZestRequest request, ZestTransformation transform) throws ZestTransformFailException;

	/**
	 * Handle common tests.
	 *
	 * @param script the script
	 * @param request the request
	 * @param response the response
	 * @throws ZestActionFailException the zest action fail exception
	 * @throws ZestInvalidCommonTestException the zest invalid common test exception
	 */
	void handleCommonTests (ZestScript script, ZestRequest request, ZestResponse response) 
			throws ZestActionFailException, ZestInvalidCommonTestException;
	
	/**
	 * Run common test.
	 *
	 * @param stmt the stmt
	 * @param response the response
	 * @throws ZestActionFailException the zest action fail exception
	 * @throws ZestInvalidCommonTestException the zest invalid common test exception
	 */
	void runCommonTest(ZestStatement stmt, ZestResponse response) 
			throws ZestActionFailException, ZestInvalidCommonTestException;
	
	/**
	 * Handle action.
	 *
	 * @param script the script
	 * @param action the action
	 * @param lastResponse the last response
	 * @return the string
	 * @throws ZestActionFailException the zest action fail exception
	 */
	String handleAction(ZestScript script, ZestAction action, ZestResponse lastResponse) throws ZestActionFailException;

	/**
	 * Gets the replacement value.
	 *
	 * @param defn the defn
	 * @return the replacement value
	 */
	String getReplacementValue(ZestFieldDefinition defn);
	
	/**
	 * Response passed.
	 *
	 * @param request the request
	 * @param response the response
	 * @param assertion the assertion
	 */
	void responsePassed (ZestRequest request, ZestResponse response, ZestAssertion assertion);

	/**
	 * Response failed.
	 *
	 * @param request the request
	 * @param response the response
	 * @param assertion the assertion
	 * @throws ZestAssertFailException the zest assert fail exception
	 */
	void responseFailed (ZestRequest request, ZestResponse response, ZestAssertion assertion) throws ZestAssertFailException;
	
	/**
	 * Response passed.
	 *
	 * @param request the request
	 * @param response the response
	 */
	void responsePassed (ZestRequest request, ZestResponse response);

	/**
	 * Response failed.
	 *
	 * @param request the request
	 * @param response the response
	 * @throws ZestAssertFailException the zest assert fail exception
	 */
	void responseFailed (ZestRequest request, ZestResponse response) throws ZestAssertFailException;
	
	/**
	 * Sets the stop on assert fail.
	 *
	 * @param stop the new stop on assert fail
	 */
	void setStopOnAssertFail(boolean stop);
	
	/**
	 * Sets the stop on test fail.
	 *
	 * @param stop the new stop on test fail
	 */
	void setStopOnTestFail(boolean stop);

	/**
	 * Gets the stop on assert fail.
	 *
	 * @return the stop on assert fail
	 */
	boolean getStopOnAssertFail();
	
	/**
	 * Gets the stop on test fail.
	 *
	 * @return the stop on test fail
	 */
	boolean getStopOnTestFail();
	
	/**
	 * Sets the proxy.
	 *
	 * @param host the host
	 * @param port the port
	 */
	void setProxy(String host, int port);

	/**
	 * Sets the output writer.
	 *
	 * @param writer the new output writer
	 */
	void setOutputWriter(Writer writer);

}
