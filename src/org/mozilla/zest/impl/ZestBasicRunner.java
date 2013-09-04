/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
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
import org.mozilla.zest.core.v1.ZestConditional;
import org.mozilla.zest.core.v1.ZestHttpAuthentication;
import org.mozilla.zest.core.v1.ZestInvalidCommonTestException;
import org.mozilla.zest.core.v1.ZestJSON;
import org.mozilla.zest.core.v1.ZestLoop;
import org.mozilla.zest.core.v1.ZestRequest;
import org.mozilla.zest.core.v1.ZestResponse;
import org.mozilla.zest.core.v1.ZestRunner;
import org.mozilla.zest.core.v1.ZestRuntime;
import org.mozilla.zest.core.v1.ZestScript;
import org.mozilla.zest.core.v1.ZestStatement;
import org.mozilla.zest.core.v1.ZestVariables;

public class ZestBasicRunner implements ZestRunner, ZestRuntime {

	private HttpClient httpclient = new HttpClient();
	private boolean stopOnAssertFail = true;
	private boolean stopOnTestFail = true;
	private Writer outputWriter = null;
	
	private ZestVariables variables;
	private ZestRequest lastRequest = null;
	private ZestResponse lastResponse = null;

	@Override
	public void run(ZestScript script) throws ZestAssertFailException, ZestActionFailException, 
			IOException, ZestInvalidCommonTestException, ZestAssignFailException {
		
		this.run(script, null, new HashMap<String, String>());
	}

	@Override
	public void run (ZestScript script, ZestRequest target) 
			throws ZestAssertFailException, ZestActionFailException, IOException,
			ZestInvalidCommonTestException, ZestAssignFailException {
		
		if (target == null) {
			throw new InvalidParameterException("Null target supplied");
		}
		HashMap<String, String> initialValues = new HashMap<String, String>();
		initialValues.put(ZestVariables.REQUEST_METHOD, target.getMethod());
		initialValues.put(ZestVariables.REQUEST_URL, target.getUrl().toString());
		initialValues.put(ZestVariables.REQUEST_HEADER, target.getHeaders());
		initialValues.put(ZestVariables.REQUEST_BODY, target.getData());
		
		if (target.getResponse() != null) {
			initialValues.put(ZestVariables.RESPONSE_URL, target.getResponse().getUrl().toString());
			initialValues.put(ZestVariables.RESPONSE_HEADER, target.getResponse().getHeaders());
			initialValues.put(ZestVariables.RESPONSE_BODY, target.getResponse().getBody());
			
		}
		
		this.run(script, target, initialValues);
	}

	private void run (ZestScript script, ZestRequest target, HashMap<String, String> tokens) throws ZestAssertFailException,
			ZestActionFailException, ZestInvalidCommonTestException, IOException, ZestAssignFailException {
		List<ZestAuthentication> auth = script.getAuthentication();
		if (auth != null) {
			for (ZestAuthentication za : auth) {
				if (za instanceof ZestHttpAuthentication) {
					ZestHttpAuthentication zha = (ZestHttpAuthentication) za;
					
					Credentials defaultcreds = new UsernamePasswordCredentials(zha.getUsername(), zha.getPassword());
					httpclient.getState().setCredentials(new AuthScope(zha.getSite(), 80, AuthScope.ANY_REALM), defaultcreds);
				}
			}
		}
		
		variables = script.getParameters().deepCopy();
		
		if (tokens != null) {
			for (Map.Entry<String, String> token : tokens.entrySet()) {
				this.setVariable(token.getKey(), token.getValue());
			}
		}
		
		lastRequest = target;
		
		if (target != null) {
			// used in passive trests
			lastResponse = target.getResponse();
		} else {
			lastResponse = null;
		}
		
		for (ZestStatement stmt : script.getStatements()) {
			lastResponse = this.runStatement(script, stmt, lastResponse);
		}

	}
	

	@Override
	public ZestResponse runStatement(ZestScript script, ZestStatement stmt, ZestResponse lastRes) 
			throws ZestAssertFailException, ZestActionFailException,  
			ZestInvalidCommonTestException, IOException, ZestAssignFailException {
		
		if (ZestScript.Type.Passive.equals(script.getType()) && !stmt.isPassive()) {
			throw new IllegalArgumentException(stmt.getElementType() + " not allowed in passive scripts");
		}
		
		if (stmt instanceof ZestRequest) {
			this.lastRequest = ((ZestRequest)stmt).deepCopy();
			this.lastRequest.replaceTokens(this.variables);
			this.lastResponse = send(this.lastRequest);
			
			this.variables.setStandardVariables(this.lastRequest);
			this.variables.setStandardVariables(this.lastResponse);

			handleResponse (this.lastRequest, this.lastResponse);
			return this.lastResponse;
			
		} else if (stmt instanceof ZestConditional) {
			ZestConditional zc = (ZestConditional) stmt;
			if (zc.isTrue(this)) {
				this.output("Conditional TRUE: " + zc.getClass().getName());
				for (ZestStatement ifStmt : zc.getIfStatements()) {
					lastResponse = this.runStatement(script, ifStmt, lastResponse);
				}
			} else {
				this.output("Conditional FALSE: " + zc.getClass().getName());
				for (ZestStatement elseStmt : zc.getElseStatements()) {
					lastResponse = this.runStatement(script, elseStmt, lastResponse);
				}
			}
		} else if (stmt instanceof ZestAction) {
			handleAction(script, (ZestAction) stmt, lastResponse);
		} else if (stmt instanceof ZestAssignment) {
			handleAssignment(script, (ZestAssignment) stmt, lastResponse);
		}
		else if (stmt instanceof ZestLoop){
			lastResponse=handleLoop(script, (ZestLoop<?>) stmt, lastResponse);
		}
		return lastResponse;
	}
	
	@Override
	public String handleAction(ZestScript script, ZestAction action, ZestResponse lastResponse) throws ZestActionFailException {
		this.output("Action invoke: " + action.getClass().getName());
		String result = action.invoke(lastResponse, this);
		if (result != null) {
			this.output("Action result: " +result);
		}
		return result;
	}
	
	@Override
	public String handleAssignment(ZestScript script, ZestAssignment assign, ZestResponse lastResponse) throws ZestAssignFailException {
		this.output("Assign: " + assign.getClass().getName());
		String result = assign.assign(lastResponse, this);
		this.setVariable(assign.getVariableName(), result);
		if (result != null) {
			this.output("Assignment result: " +result);
		}
		return result;
	}
	@Override
	public ZestResponse handleLoop(ZestScript script, ZestLoop<?> loop, ZestResponse lastResponse) throws ZestAssertFailException, ZestActionFailException, ZestInvalidCommonTestException, IOException, ZestAssignFailException{
		String token="";
		this.setVariable(loop.getVariableName(), loop.getCurrentToken().toString());
		while(loop.hasMoreElements()){
			String loopOutput="Loop "+ loop.getVariableName()+ " iteration: "+loop.getCurrentIndex();
			if(!token.equals(loop.getCurrentToken().toString())){
				token=loop.getCurrentToken().toString();
				loopOutput+=", Current Token: "+token;
				this.setVariable(loop.getVariableName(), token);
			}
			this.output(loopOutput);
			lastResponse = this.runStatement(script, loop.nextElement(), lastResponse);
		}
		return  lastResponse;
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
	
	@Override
	public ZestResponse send(ZestRequest request) throws IOException {
		return send(httpclient, request);
	}
	
	@Override
	public void handleResponse(ZestRequest request, ZestResponse response) throws ZestAssertFailException, ZestActionFailException {
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
	public void responsePassed(ZestRequest request, ZestResponse response, ZestAssertion assertion) {
		this.output("Assertion PASSED: " + assertion.getClass().getName());
	}

	@Override
	public void responseFailed(ZestRequest request, ZestResponse response, ZestAssertion assertion)
			throws ZestAssertFailException {
		this.output("Assertion FAILED: " + assertion.getClass().getName());
		if (this.getStopOnAssertFail()) {
			throw new ZestAssertFailException(assertion);
		}
	}

	@Override
	public void responsePassed(ZestRequest request, ZestResponse response) {
		//this.output("Response PASSED");
	}

	@Override
	public void responseFailed(ZestRequest request, ZestResponse response)
			throws ZestAssertFailException {
		this.output("Response FAILED");
	}

	@Override
	public void runScript(Reader reader) throws ZestAssertFailException, ZestActionFailException, 
			IOException, ZestInvalidCommonTestException, ZestAssignFailException {
	    BufferedReader fr = new BufferedReader(reader);
	    StringBuilder sb = new StringBuilder();
        String line;
        while ((line = fr.readLine()) != null) {
            sb.append(line);
        }
        fr.close();
	    run ((ZestScript) ZestJSON.fromString(sb.toString()));
	}

	@Override
	public void runScript(String  script) throws ZestAssertFailException, ZestActionFailException, 
			IOException, ZestInvalidCommonTestException, ZestAssignFailException {
	    run ((ZestScript) ZestJSON.fromString(script));
	}

	private ZestResponse send(HttpClient httpclient, ZestRequest req) throws IOException {
		HttpMethod method;
		
		switch (req.getMethod()) {
		case "GET":		method = new GetMethod(req.getUrl().toString()); 
						break;
		case "POST": 	method = new PostMethod(req.getUrl().toString());
						break;
		case "OPTIONS": method = new OptionsMethod(req.getUrl().toString());
						break;
		case "HEAD": 	method = new HeadMethod(req.getUrl().toString());
						break;
		case "PUT": 	method = new PutMethod(req.getUrl().toString());
						break;
		case "DELETE": 	method = new DeleteMethod(req.getUrl().toString());
						break;
		case "TRACE": 	method = new TraceMethod(req.getUrl().toString());
						break;
		default:		throw new IllegalArgumentException("Method not supported: " + req.getMethod());
		}
		method.setURI(new URI(req.getUrl().toString(), true));
		setHeaders(method, req.getHeaders());
		method.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
		
		if (req.getMethod().equals("POST")) {
			// Do this after setting the headers so the length is corrected
			RequestEntity requestEntity = new StringRequestEntity(req.getData(), null, null);
			((PostMethod)method).setRequestEntity(requestEntity);
		}

		int code = 0;
		String responseHeader = null;
		String responseBody = null;
		Date start = new Date();
		try {
			this.output(req.getMethod() + " : " + req.getUrl());
		    code = httpclient.executeMethod(method);
		    
		    responseHeader = method.getStatusLine().toString() + "\n" + arrayToStr(method.getResponseHeaders());
		    responseBody = method.getResponseBodyAsString();
		} catch (Exception e) { 
			// TODO
		    System.err.println(e);
		    e.printStackTrace();
		} finally { 
		    method.releaseConnection(); 
		}
		// Update the headers with the ones actually sent
		req.setHeaders(arrayToStr(method.getRequestHeaders()));
		
		return new ZestResponse(req.getUrl(), responseHeader, responseBody, code, new Date().getTime() - start.getTime());
	}

	private void setHeaders(HttpMethod method, String headers) {
	if (headers == null) {
			return;
		}
		String [] headerArray = headers.split("\r\n");
		String header;
		String value;
		for (String line : headerArray) {
			int colonIndex = line.indexOf(":");
			if (colonIndex > 0) {
				header = line.substring(0, colonIndex);
				value = line.substring(colonIndex + 1).trim();
				if (! header.toLowerCase().startsWith("cookie") && 
						! header.toLowerCase().startsWith("content-length")) {
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
		this.variables.setVariable(name, value);
	}

	@Override
	public void setVariables(Map<String, String> variables) {
		if (variables != null) {
			for (Map.Entry<String, String>var : variables.entrySet()) {
				this.setVariable(var.getKey(), var.getValue());
			}
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

}
