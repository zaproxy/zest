/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import javax.script.ScriptEngineFactory;

/** The Interface ZestRunner. */
public interface ZestRunner {

    /**
     * Run.
     *
     * @param script the script
     * @throws ZestAssertFailException the zest assert fail exception
     * @throws ZestActionFailException the zest action fail exception
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ZestInvalidCommonTestException the zest invalid common test exception
     * @throws ZestAssignFailException the zest assign fail exception
     * @throws ZestClientFailException
     */
    String run(ZestScript script, Map<String, String> params)
            throws ZestAssertFailException, ZestActionFailException, IOException,
                    ZestInvalidCommonTestException, ZestAssignFailException,
                    ZestClientFailException;

    /**
     * Run script.
     *
     * @param reader the reader
     * @throws ZestAssertFailException the zest assert fail exception
     * @throws ZestActionFailException the zest action fail exception
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ZestInvalidCommonTestException the zest invalid common test exception
     * @throws ZestAssignFailException the zest assign fail exception
     * @throws ZestClientFailException
     */
    String runScript(Reader reader, Map<String, String> params)
            throws ZestAssertFailException, ZestActionFailException, IOException,
                    ZestInvalidCommonTestException, ZestAssignFailException,
                    ZestClientFailException;

    /**
     * Run script.
     *
     * @param script the script
     * @throws ZestAssertFailException the zest assert fail exception
     * @throws ZestActionFailException the zest action fail exception
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ZestInvalidCommonTestException the zest invalid common test exception
     * @throws ZestAssignFailException the zest assign fail exception
     * @throws ZestClientFailException
     */
    String runScript(String script, Map<String, String> params)
            throws ZestAssertFailException, ZestActionFailException, IOException,
                    ZestInvalidCommonTestException, ZestAssignFailException,
                    ZestClientFailException;

    /**
     * Run.
     *
     * @param script the script
     * @param target the target
     * @throws ZestAssertFailException the zest assert fail exception
     * @throws ZestActionFailException the zest action fail exception
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ZestInvalidCommonTestException the zest invalid common test exception
     * @throws ZestAssignFailException the zest assign fail exception
     * @throws ZestClientFailException
     */
    String run(ZestScript script, ZestRequest target, Map<String, String> params)
            throws ZestAssertFailException, ZestActionFailException, IOException,
                    ZestInvalidCommonTestException, ZestAssignFailException,
                    ZestClientFailException;

    /**
     * Run statement.
     *
     * @param script the script
     * @param stmt the stmt
     * @param lastResponse the last response
     * @return the zest response
     * @throws ZestAssertFailException the zest assert fail exception
     * @throws ZestActionFailException the zest action fail exception
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ZestInvalidCommonTestException the zest invalid common test exception
     * @throws ZestAssignFailException the zest assign fail exception
     * @throws ZestClientFailException
     */
    ZestResponse runStatement(ZestScript script, ZestStatement stmt, ZestResponse lastResponse)
            throws ZestAssertFailException, ZestActionFailException, IOException,
                    ZestInvalidCommonTestException, ZestAssignFailException,
                    ZestClientFailException;

    /**
     * Send.
     *
     * @param request the request
     * @return the zest response
     * @throws IOException Signals that an I/O exception has occurred.
     */
    ZestResponse send(ZestRequest request) throws IOException;

    /**
     * Handle response.
     *
     * @param request the request
     * @param response the response
     * @throws ZestAssertFailException the zest assert fail exception
     * @throws ZestActionFailException the zest action fail exception
     */
    void handleResponse(ZestRequest request, ZestResponse response)
            throws ZestAssertFailException, ZestActionFailException;

    /**
     * Handle action.
     *
     * @param script the script
     * @param action the action
     * @param lastResponse the last response
     * @return the string
     * @throws ZestActionFailException the zest action fail exception
     */
    String handleAction(ZestScript script, ZestAction action, ZestResponse lastResponse)
            throws ZestActionFailException;

    /**
     * Handle an assignment.
     *
     * @param script the script
     * @param assign the assignment
     * @param lastResponse the last response
     * @return the string
     * @throws ZestAssignFailException the zest assignment fail exception
     */
    String handleAssignment(ZestScript script, ZestAssignment assign, ZestResponse lastResponse)
            throws ZestAssignFailException;

    ZestResponse handleLoop(ZestScript script, ZestLoop<?> loop, ZestResponse lastResponse)
            throws ZestAssertFailException, ZestActionFailException, ZestInvalidCommonTestException,
                    IOException, ZestAssignFailException, ZestClientFailException;

    String handleClient(ZestScript script, ZestClient client) throws ZestClientFailException;

    /**
     * Response passed.
     *
     * @param request the request
     * @param response the response
     * @param assertion the assertion
     */
    void responsePassed(ZestRequest request, ZestResponse response, ZestAssertion assertion);

    /**
     * Response failed.
     *
     * @param request the request
     * @param response the response
     * @param assertion the assertion
     * @throws ZestAssertFailException the zest assert fail exception
     */
    void responseFailed(ZestRequest request, ZestResponse response, ZestAssertion assertion)
            throws ZestAssertFailException;

    /**
     * Response passed.
     *
     * @param request the request
     * @param response the response
     */
    void responsePassed(ZestRequest request, ZestResponse response);

    /**
     * Response failed.
     *
     * @param request the request
     * @param response the response
     * @throws ZestAssertFailException the zest assert fail exception
     */
    void responseFailed(ZestRequest request, ZestResponse response) throws ZestAssertFailException;

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
     * Sets the output writer.
     *
     * @param writer the new output writer
     */
    void setOutputWriter(Writer writer);

    /**
     * Turns debugging on or off
     *
     * @param debug
     */
    void setDebug(boolean debug);

    /**
     * Reports if debugging is enabled
     *
     * @return {@code true} if debug is enabled, {@code false} otherwise.
     */
    boolean isDebug();

    /**
     * Gets the variable.
     *
     * @param name the name
     * @return the variable
     */
    String getVariable(String name);

    /**
     * Sets the variable.
     *
     * @param name the name
     * @param value the value
     */
    void setVariable(String name, String value);

    void setScriptEngineFactory(ScriptEngineFactory factory);
}
