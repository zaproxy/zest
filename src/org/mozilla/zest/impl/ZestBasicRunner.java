/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.mozilla.zest.core.v1.ZestAction;
import org.mozilla.zest.core.v1.ZestActionFailException;
import org.mozilla.zest.core.v1.ZestActionSetToken;
import org.mozilla.zest.core.v1.ZestAssertFailException;
import org.mozilla.zest.core.v1.ZestAssertion;
import org.mozilla.zest.core.v1.ZestAuthentication;
import org.mozilla.zest.core.v1.ZestConditional;
import org.mozilla.zest.core.v1.ZestFieldDefinition;
import org.mozilla.zest.core.v1.ZestHttpAuthentication;
import org.mozilla.zest.core.v1.ZestJSON;
import org.mozilla.zest.core.v1.ZestRequest;
import org.mozilla.zest.core.v1.ZestResponse;
import org.mozilla.zest.core.v1.ZestRunner;
import org.mozilla.zest.core.v1.ZestScript;
import org.mozilla.zest.core.v1.ZestStatement;
import org.mozilla.zest.core.v1.ZestTransformFailException;
import org.mozilla.zest.core.v1.ZestTransformFieldReplace;
import org.mozilla.zest.core.v1.ZestTransformation;

public class ZestBasicRunner implements ZestRunner {

	private HttpClient httpclient = new HttpClient();
	private boolean stopOnAssertFail = true;
	private boolean stopOnTestFail = true;
	private PrintStream outputStream = null;
	
	private List<ZestTransformation> transformList;
	private Map<String, String> replacementValues;

	@Override
	public void run(ZestScript script) 
			throws ZestAssertFailException, ZestActionFailException, ZestTransformFailException, IOException {

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
		
		// Cache all of the transformations so we dont have to cache all the responses
		transformList = new ArrayList<ZestTransformation>();
		replacementValues = new HashMap<String, String>(); 
		
		transformList.addAll(script.getTransformations());
		
		ZestResponse lastResponse = null;
		for (ZestStatement stmt : script.getStatements()) {
			lastResponse = this.runStatement(script, stmt, lastResponse);
		}
	}
	
	@Override
	public ZestResponse runStatement(ZestScript script, ZestStatement stmt, ZestResponse lastResponse) 
			throws ZestAssertFailException, ZestActionFailException, ZestTransformFailException, IOException {
		if (stmt instanceof ZestRequest) {
			ZestRequest req2 = ((ZestRequest)stmt).deepCopy();
			req2.replaceTokens(script.getTokens());
			ZestResponse resp = send(req2);
			handleResponse (req2, resp);
			handleTransforms (script, req2, resp);
			return resp;
			
		} else if (stmt instanceof ZestConditional) {
			ZestConditional zc = (ZestConditional) stmt;
			
			if (zc.isTrue(lastResponse)) {
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
			String result = handleAction(script, (ZestAction) stmt, lastResponse);
			if (stmt instanceof ZestActionSetToken) {
				ZestActionSetToken zast = (ZestActionSetToken) stmt;
				script.getTokens().setToken(zast.getTokenName(), result);
			}
		}
		return lastResponse;
	}
	
	@Override
	public String handleAction(ZestScript script, ZestAction action, ZestResponse lastResponse) throws ZestActionFailException {
		this.output("Action invoke: " + action.getClass().getName());
		String result = action.invoke(lastResponse);
		if (result != null) {
			this.output("Action result: " +result);
		}
		return result;
	}

	@Override
	public void handleTransforms(ZestScript script, ZestRequest request, ZestResponse response) throws ZestTransformFailException {
		for (ZestTransformation za : this.transformList) {
			if (za instanceof ZestTransformFieldReplace) {
				ZestTransformFieldReplace zfrt = (ZestTransformFieldReplace) za;
				ZestFieldDefinition defn = zfrt.getFieldDefinition();
				int index = zfrt.getFieldDefinition().getRequestId();
				if (request.getIndex() == index) {
					boolean found = false;
					String fieldStr = defn.getFieldName();
					int formId = defn.getFormIndex();
					
					Source src = new Source(response.getHeaders() + response.getBody());
					List<Element> formElements = src.getAllElements(HTMLElementName.FORM);

					if (formElements != null && formId < formElements.size()) {
						Element form = formElements.get(formId);
						
						List<Element> inputElements = form.getAllElements(HTMLElementName.INPUT);
						for (Element inputElement : inputElements) {
							if (fieldStr.equals(inputElement.getAttributeValue("ID")) ||
									fieldStr.equals(inputElement.getAttributeValue("NAME"))) {
								String replaceValue = inputElement.getAttributeValue("VALUE");
								this.output("Got replacement value " + defn.getKey() + " : " + replaceValue);
								replacementValues.put(defn.getKey(), replaceValue);
								found = true;
								break;
							}
						}
					}
					if (!found) {
						throw new ZestTransformFailException(zfrt);
					}
				}
			}
		}
	}

	private void output(String str) {
		if (this.outputStream != null) {
			this.outputStream.println(str);
		}
	}
	
	public String getReplacementValue(ZestFieldDefinition defn) {
		return this.replacementValues.get(defn.getKey());
	}

	@Override
	public ZestResponse send(ZestRequest request) throws ZestTransformFailException, IOException {
		List<ZestTransformation> ztList = request.getTransformations();
		for (ZestTransformation zt : ztList) {
			this.handleTransform(request, zt);
		}
		return send(httpclient, request);
	}
	
	@Override
	public void handleTransform (ZestRequest request, ZestTransformation transform) 
			throws ZestTransformFailException {
		transform.transform(this, request);
	}

	@Override
	public void handleResponse(ZestRequest request, ZestResponse response) throws ZestAssertFailException, ZestActionFailException {
		for (ZestAssertion za : request.getAssertions()) {
			if (za.isValid(response)) {
				this.responsePassed(request, response, za);
			} else {
				this.responseFailed(request, response, za);
			}
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
	public void runScript(File script) throws ZestTransformFailException, ZestAssertFailException, ZestActionFailException, 
			IOException {
	    BufferedReader fr = new BufferedReader(new FileReader(script));
	    StringBuilder sb = new StringBuilder();
        String line;
        while ((line = fr.readLine()) != null) {
            sb.append(line);
        }
        fr.close();
	    run ((ZestScript) ZestJSON.fromString(sb.toString()));
	}

	private ZestResponse send(HttpClient httpclient, ZestRequest req) throws IOException {
		HttpMethod method;
		
		switch (req.getMethod()) {
		case "GET":		method = new GetMethod(req.getUrl().toString()); 
						break;
		case "POST": 	method = new PostMethod(req.getUrl().toString());
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
		try {
			this.output(req.getMethod() + " : " + req.getUrl());
		    code = httpclient.executeMethod(method);
		    
		    responseHeader = method.getStatusLine().toString() + "\n" + arrayToStr(method.getResponseHeaders());
		    responseBody = method.getResponseBodyAsString();
		} catch (Exception e) { 
		    System.err.println(e); 
		} finally { 
		    method.releaseConnection(); 
		}
		// Update the headers with the ones actually sent
		req.setHeaders(arrayToStr(method.getRequestHeaders()));
		
		return new ZestResponse(responseHeader, responseBody, code);
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

	public void setOutputStream(PrintStream output) {
		this.outputStream = output;
	}

	@Override
	public void setProxy(String host, int port) {
		// TODO support credentials
		httpclient.getHostConfiguration().setProxy(host, port);		
	}

}
