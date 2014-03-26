/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.script.ScriptEngineFactory;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.OptionsMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.TraceMethod;
import org.mozilla.zest.core.v1.ZestAction;
import org.mozilla.zest.core.v1.ZestActionFailException;
import org.mozilla.zest.core.v1.ZestAssertFailException;
import org.mozilla.zest.core.v1.ZestAssertion;
import org.mozilla.zest.core.v1.ZestAssignFailException;
import org.mozilla.zest.core.v1.ZestAssignment;
import org.mozilla.zest.core.v1.ZestAuthentication;
import org.mozilla.zest.core.v1.ZestComment;
import org.mozilla.zest.core.v1.ZestConditional;
import org.mozilla.zest.core.v1.ZestControlLoopBreak;
import org.mozilla.zest.core.v1.ZestControlLoopNext;
import org.mozilla.zest.core.v1.ZestHttpAuthentication;
import org.mozilla.zest.core.v1.ZestInvalidCommonTestException;
import org.mozilla.zest.core.v1.ZestJSON;
import org.mozilla.zest.core.v1.ZestLoop;
import org.mozilla.zest.core.v1.ZestRequest;
import org.mozilla.zest.core.v1.ZestResponse;
import org.mozilla.zest.core.v1.ZestControlReturn;
import org.mozilla.zest.core.v1.ZestRunner;
import org.mozilla.zest.core.v1.ZestRuntime;
import org.mozilla.zest.core.v1.ZestScript;
import org.mozilla.zest.core.v1.ZestStatement;
import org.mozilla.zest.core.v1.ZestVariables;

public class ZestBasicRunner implements ZestRunner, ZestRuntime {

	private ScriptEngineFactory scriptEngineFactory = null;
	private HttpClient httpclient = new HttpClient();
	private boolean stopOnAssertFail = true;
	private boolean stopOnTestFail = true;
	private Writer outputWriter = null;
	private boolean debug = false;

	private ZestVariables variables;
	private ZestRequest lastRequest = null;
	private ZestResponse lastResponse = null;
	private String result = null;

	private Stack<ZestLoop<?>> loops = new Stack<>();
	private boolean skipStatements = false;

	public ZestBasicRunner() {
	}

	public ZestBasicRunner(ScriptEngineFactory factory) {
		this.scriptEngineFactory = factory;
	}

	@Override
	public String run(ZestScript script, Map<String, String> params)
			throws ZestAssertFailException, ZestActionFailException,
			IOException, ZestInvalidCommonTestException,
			ZestAssignFailException {

		return this.run(script, null, params);
	}

	@Override
	public String run(ZestScript script, ZestRequest target,
			Map<String, String> tokens) throws ZestAssertFailException,
			ZestActionFailException, ZestInvalidCommonTestException,
			IOException, ZestAssignFailException {
		List<ZestAuthentication> auth = script.getAuthentication();
		if (auth != null) {
			for (ZestAuthentication za : auth) {
				if (za instanceof ZestHttpAuthentication) {
					ZestHttpAuthentication zha = (ZestHttpAuthentication) za;

					Credentials defaultcreds = new UsernamePasswordCredentials(
							zha.getUsername(), zha.getPassword());
					httpclient.getState().setCredentials(
							new AuthScope(zha.getSite(), 80,
									AuthScope.ANY_REALM), defaultcreds);
				}
			}
		}

		variables = script.getParameters().deepCopy();

		lastRequest = target;

		if (target != null) {
			this.setStandardVariables(lastRequest);

			// used in passive tests
			lastResponse = target.getResponse();
			this.setStandardVariables(lastResponse);
		} else {
			lastResponse = null;
		}

		if (tokens != null) {
			for (Map.Entry<String, String> token : tokens.entrySet()) {
				this.setVariable(token.getKey(), token.getValue());
			}
		}

		for (ZestStatement stmt : script.getStatements()) {
			lastResponse = this.runStatement(script, stmt, lastResponse);
			if (result != null) {
				// A return statement has been used, return the value it set
				return result;
			}
		}

		return null;

	}

	@Override
	public ZestResponse runStatement(ZestScript script, ZestStatement stmt,
			ZestResponse lastRes) throws ZestAssertFailException,
			ZestActionFailException, ZestInvalidCommonTestException,
			IOException, ZestAssignFailException {
		if (skipStatements) {
			return lastRes;
		}
		if (ZestScript.Type.Passive.equals(script.getType())
				&& !stmt.isPassive()) {
			throw new IllegalArgumentException(stmt.getElementType()
					+ " not allowed in passive scripts");
		}

		if (stmt instanceof ZestRequest) {
			this.lastRequest = ((ZestRequest) stmt).deepCopy();
			this.lastRequest.replaceTokens(this.variables);
			this.lastResponse = send(this.lastRequest);
			
			// Set up the 'standard' variables
			this.setStandardVariables(lastRequest);
			this.setStandardVariables(lastResponse);

			handleResponse(this.lastRequest, this.lastResponse);
			return this.lastResponse;

		} else if (stmt instanceof ZestConditional) {
			ZestConditional zc = (ZestConditional) stmt;
			if (zc.isTrue(this)) {
				this.debug(stmt.getIndex() + " Conditional TRUE: " + zc.getClass().getName());
				for (ZestStatement ifStmt : zc.getIfStatements()) {
					lastResponse = this.runStatement(script, ifStmt,
							lastResponse);
				}
			} else {
				this.debug(stmt.getIndex() + " Conditional FALSE: " + zc.getClass().getName());
				for (ZestStatement elseStmt : zc.getElseStatements()) {
					lastResponse = this.runStatement(script, elseStmt,
							lastResponse);
				}
			}
		} else if (stmt instanceof ZestAction) {
			handleAction(script, (ZestAction) stmt, lastResponse);
		} else if (stmt instanceof ZestAssignment) {
			handleAssignment(script, (ZestAssignment) stmt, lastResponse);
		} else if (stmt instanceof ZestLoop) {
			lastResponse = handleLoop(script, (ZestLoop<?>) stmt, lastResponse);
		} else if (stmt instanceof ZestControlLoopBreak) {
			debug(stmt.getIndex() + " Break");
			handleControlLoopBreak();
		} else if (stmt instanceof ZestControlLoopNext) {
			debug(stmt.getIndex() + " Next");
			handleControlLoopNext();
		} else if (stmt instanceof ZestControlReturn) {
			// Exits the script
			ZestControlReturn zr = (ZestControlReturn) stmt;
			result = this.variables.replaceInString(zr.getValue(), false);
			debug(stmt.getIndex() + " Return " + result);
			return null;
		} else if (stmt instanceof ZestComment) {
			// Nothing to do
			debug(stmt.getIndex() + " Comment " + ((ZestComment)stmt).getComment());
		}
		return lastResponse;
	}

	@Override
	public String handleAction(ZestScript script, ZestAction action,
			ZestResponse lastResponse) throws ZestActionFailException {
		this.debug(action.getIndex() + " Action invoke: " + action.getClass().getName());
		String result = action.invoke(lastResponse, this);
		if (result != null) {
			this.debug(action.getIndex() + " Action result: " + result);
		}
		return result;
	}

	@Override
	public String handleAssignment(ZestScript script, ZestAssignment assign,
			ZestResponse lastResponse) throws ZestAssignFailException {
		String result = assign.assign(lastResponse, this);
		this.setVariable(assign.getVariableName(), result);
		if (result != null) {
			this.debug(assign.getIndex() + " Assign: " + assign.getVariableName() + " = " + result);
		}
		return result;
	}

	@Override
	public ZestResponse handleLoop(ZestScript script, ZestLoop<?> loop,
			ZestResponse lastResponse) throws ZestAssertFailException,
			ZestActionFailException, ZestInvalidCommonTestException,
			IOException, ZestAssignFailException {
		loop.init();
		String token = "";
		this.setVariable(loop.getVariableName(), loop.getCurrentToken()
				.toString());
		loops.push(loop);
		while (loop.hasMoreElements()) {
			String loopOutput = loop.getIndex() + " Loop " + loop.getVariableName()
					+ " iteration: " + loop.getCurrentIndex();
			if (!token.equals(loop.getCurrentToken().toString())) {
				token = loop.getCurrentToken().toString();
				loopOutput += ", Current Token: " + token;
				this.setVariable(loop.getVariableName(), token);
			}
			this.debug(loopOutput);
			lastResponse = this.runStatement(script, loop.nextElement(),
					lastResponse);
			if (skipStatements) {// a LoopControl occurred
				skipStatements = false;
			}
		}
		if (!loops.isEmpty() && loops.peek().equals(loop)) {
			loops.pop();
		}
		this.setVariable(loop.getVariableName(), "");
		return lastResponse;
	}

	public void handleControlLoopBreak() {
		ZestLoop<?> lastLoop = loops.pop();
		lastLoop.onControlBreak();
		this.skipStatements = true;
	}

	public void handleControlLoopNext() {
		loops.peek().onControlNext();
		this.skipStatements = true;
	}

	public void output(String str) {
		if (this.outputWriter != null) {
			try {
				this.outputWriter.append(str);
				this.outputWriter.append("\n");
				this.outputWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void debug(String str) {
		if (debug && this.outputWriter != null) {
			try {
				this.outputWriter.append("DEBUG: ");
				this.outputWriter.append(str);
				this.outputWriter.append("\n");
				this.outputWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public ZestResponse send(ZestRequest request) throws IOException {
		return send(httpclient, request);
	}

	@Override
	public void handleResponse(ZestRequest request, ZestResponse response)
			throws ZestAssertFailException, ZestActionFailException {
		boolean passed = true;
		for (ZestAssertion za : request.getAssertions()) {
			if (za.isValid(this)) {
				this.responsePassed(request, response, za);
			} else {
				passed = false;
				this.responseFailed(request, response, za);
			}
		}

		if (passed) {
			this.responsePassed(request, response);
		} else {
			this.responseFailed(request, response);
		}

	}

	@Override
	public void responsePassed(ZestRequest request, ZestResponse response,
			ZestAssertion assertion) {
		this.debug("Assertion PASSED: " + assertion.getClass().getName());
	}

	@Override
	public void responseFailed(ZestRequest request, ZestResponse response,
			ZestAssertion assertion) throws ZestAssertFailException {
		this.debug(request.getIndex() + " Assertion FAILED: " + assertion.getClass().getName());
		if (this.getStopOnAssertFail()) {
			throw new ZestAssertFailException(assertion);
		}
	}

	@Override
	public void responsePassed(ZestRequest request, ZestResponse response) {
		this.debug(request.getIndex() + " Response PASSED");
	}

	@Override
	public void responseFailed(ZestRequest request, ZestResponse response)
			throws ZestAssertFailException {
		this.debug(request.getIndex() + " Response FAILED");
	}

	@Override
	public String runScript(Reader reader, Map<String, String> params)
			throws ZestAssertFailException, ZestActionFailException,
			IOException, ZestInvalidCommonTestException,
			ZestAssignFailException {
		BufferedReader fr = new BufferedReader(reader);
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = fr.readLine()) != null) {
			sb.append(line);
		}
		fr.close();
		return run((ZestScript) ZestJSON.fromString(sb.toString()), params);
	}

	@Override
	public String runScript(String script, Map<String, String> params)
			throws ZestAssertFailException, ZestActionFailException,
			IOException, ZestInvalidCommonTestException,
			ZestAssignFailException {
		return run((ZestScript) ZestJSON.fromString(script), params);
	}

	private ZestResponse send(HttpClient httpclient, ZestRequest req)
			throws IOException {
		HttpMethod method;
		URI uri = new URI(req.getUrl().toString(), false);

		switch (req.getMethod()) {
		case "GET":
			method = new GetMethod(uri.toString());
			// Can only redirect on GETs
			method.setFollowRedirects(req.isFollowRedirects());
			break;
		case "POST":
			method = new PostMethod(uri.toString());
			break;
		case "OPTIONS":
			method = new OptionsMethod(uri.toString());
			break;
		case "HEAD":
			method = new HeadMethod(uri.toString());
			break;
		case "PUT":
			method = new PutMethod(uri.toString());
			break;
		case "DELETE":
			method = new DeleteMethod(uri.toString());
			break;
		case "TRACE":
			method = new TraceMethod(uri.toString());
			break;
		default:
			throw new IllegalArgumentException("Method not supported: "
					+ req.getMethod());
		}
		setHeaders(method, req.getHeaders());

		if (req.getMethod().equals("POST")) {
			// Do this after setting the headers so the length is corrected
			RequestEntity requestEntity = new StringRequestEntity(
					req.getData(), null, null);
			((PostMethod) method).setRequestEntity(requestEntity);
		}

		int code = 0;
		String responseHeader = null;
		String responseBody = null;
		Date start = new Date();
		try {
			this.debug(req.getMethod() + " : " + req.getUrl());
			code = httpclient.executeMethod(method);

			responseHeader = method.getStatusLine().toString() + "\n"
					+ arrayToStr(method.getResponseHeaders());
			responseBody = method.getResponseBodyAsString();

		} finally {
			method.releaseConnection();
		}
		// Update the headers with the ones actually sent
		req.setHeaders(arrayToStr(method.getRequestHeaders()));

		return new ZestResponse(req.getUrl(), responseHeader, responseBody,
				code, new Date().getTime() - start.getTime());
	}

	private void setHeaders(HttpMethod method, String headers) {
		if (headers == null) {
			return;
		}
		String[] headerArray = headers.split("\r\n");
		String header;
		String value;
		for (String line : headerArray) {
			int colonIndex = line.indexOf(":");
			if (colonIndex > 0) {
				header = line.substring(0, colonIndex);
				value = line.substring(colonIndex + 1).trim();
				if (!header.toLowerCase().startsWith("cookie")
						&& !header.toLowerCase().startsWith("content-length")) {
					method.addRequestHeader(new Header(header, value));
				}
			}
		}

	}

	private String arrayToStr(Header[] headers) {
		StringBuilder sb = new StringBuilder();
		for (Header header : headers) {
			sb.append(header.toString());
		}
		return sb.toString();
	}

	@Override
	public void setStopOnAssertFail(boolean stop) {
		stopOnAssertFail = stop;
	}

	@Override
	public void setStopOnTestFail(boolean stop) {
		stopOnTestFail = stop;
	}

	@Override
	public boolean getStopOnAssertFail() {
		return stopOnAssertFail;
	}

	@Override
	public boolean getStopOnTestFail() {
		return stopOnTestFail;
	}

	@Override
	public void setOutputWriter(Writer writer) {
		this.outputWriter = writer;
	}

	@Override
	public void setProxy(String host, int port) {
		// TODO support credentials
		httpclient.getHostConfiguration().setProxy(host, port);
	}

	@Override
	public String getVariable(String name) {
		return this.variables.getVariable(name);
	}

	@Override
	public void setVariable(String name, String value) {
		if (this.debug) {
			String val = value;
			if (val != null) {
				if ( val.length() > 80) {
					val = val.substring(0, 80) + "...";
				}
				// Put on one line and strip duplicate whitespace - this is just for basic debuging
				val = val.replace("\n", "\\n");
				val = val.replace("\r", "\\r");
				val = val.replaceAll("\\s", " ");
			}
			this.debug("Set " + name + " = " + val);
		}
		this.variables.setVariable(name, value);
	}

	
	/**
	 * Sets the standard variables for a request.
	 *
	 * @param request the new standard variables
	 */
	@Override
	public void setStandardVariables(ZestRequest request) {
		if (request != null) {
			if (request.getUrl()!= null) {
				this.setVariable(ZestVariables.REQUEST_URL, request.getUrl().toString());
			}
			this.setVariable(ZestVariables.REQUEST_HEADER, request.getHeaders());
			this.setVariable(ZestVariables.REQUEST_METHOD, request.getMethod());
			this.setVariable(ZestVariables.REQUEST_BODY, request.getData());
		}
	}

	/**
	 * Sets the standard variables for a response.
	 *
	 * @param response the new standard variables
	 */
	@Override
	public void setStandardVariables(ZestResponse response) {
		if (response != null) {
			if (response.getUrl() != null) {
				this.setVariable(ZestVariables.RESPONSE_URL, response.getUrl().toString());
			}
			this.setVariable(ZestVariables.RESPONSE_HEADER, response.getHeaders());
			this.setVariable(ZestVariables.RESPONSE_BODY, response.getBody());
		}
	}

	public void setHttpClient(HttpClient httpclient) {
		this.httpclient = httpclient;
	}

	@Override
	public ZestResponse getLastResponse() {
		return lastResponse;
	}

	@Override
	public ZestRequest getLastRequest() {
		return lastRequest;
	}

	@Override
	public String replaceVariablesInString(String str, boolean urlEncode) {
		return this.variables.replaceInString(str, urlEncode);
	}

	public void setScriptEngineFactory(ScriptEngineFactory factory) {
		this.scriptEngineFactory = factory;
	}

	@Override
	public ScriptEngineFactory getScriptEngineFactory() {
		if (this.scriptEngineFactory == null) {
			this.scriptEngineFactory = new ZestScriptEngineFactory();
		}
		return this.scriptEngineFactory;
	}

	@Override
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	@Override
	public boolean isDebug() {
		return debug;
	}

}
