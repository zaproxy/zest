/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZestRequest extends ZestElement {

	public static final String JSON_REQUEST_URL = "url";
	public static final String JSON_REQUEST_DATA = "data";
	public static final String JSON_REQUEST_METHOD = "method";
	public static final String JSON_REQUEST_ASSERTIONS = "assertions";

	private int index;
	private URL url;
	private String data;	// TODO this dependant on protocol, eg http / ws / ??
	private String method;
	private String headers;
	private ZestResponse response;
	private List<ZestTransformation> transformations = new ArrayList<ZestTransformation>();
	private List<ZestAssertion> assertions = new ArrayList<ZestAssertion>();
	private List<ZestTest> tests = new ArrayList<ZestTest>();
	
	private transient List<ZestRequestRef> referers = new ArrayList<ZestRequestRef>();
	
	public ZestRequest() {
		super();
	}
	
	@Override
	public ZestRequest deepCopy () {
		ZestRequest zr = new ZestRequest();
		zr.setIndex(this.index);
		zr.setUrl(this.url);
		zr.setData(this.data);
		zr.setMethod(this.method);
		zr.setHeaders(this.headers);
		
		if (this.getResponse()!= null) {
			zr.setResponse(this.getResponse().deepCopy());
		}
		for (ZestTransformation zt : this.getTransformations()) {
			zr.addTransformation((ZestTransformation)zt.deepCopy());
		}
		for (ZestAssertion zt : this.getAssertions()) {
			zr.addAssertion((ZestAssertion)zt.deepCopy());
		}
		for (ZestTest zt : this.getTests()) {
			zr.addTest((ZestTest)zt.deepCopy());
		}
		return zr;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
		// Correct the indexes of any referers (have to copy the array)
		for (Object ref : this.referers.toArray()) {
			((ZestRequestRef)ref).setRequest(this);
		}
	}

	public URL getUrl() {
		return url;
	}
	public void setUrl(URL url) {
		this.url = url;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getHeaders() {
		return headers;
	}
	public void setHeaders(String headers) {
		this.headers = headers;
	}
	public String getMethod() {
		return method;
	}
	/*
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	public void addHeader(String header, String value) {
		this.headers.put(header, value);
	}
	*/
	public void setMethod(String method) {
		this.method = method;
	}
	public void addTransformation(ZestTransformation transformation) {
		this.transformations.add(transformation);
	}
	public void removeTransformation (ZestTransformation transformation) {
		this.transformations.remove(transformation);
	}
	public List<ZestTransformation> getTransformations() {
		return transformations;
	}
	public void addAssertion (ZestAssertion assertion) {
		this.assertions.add(assertion);
	}
	public void removeAssertion (ZestAssertion assertion) {
		this.assertions.remove(assertion);
	}
	public List<ZestAssertion> getAssertions() {
		return assertions;
	}
	public void addTest(ZestTest test) {
		this.tests.add(test);
	}
	public void removeTest (ZestTest test) {
		this.tests.remove(test);
	}
	public List<ZestTest> getTests() {
		return tests;
	}

	public ZestResponse getResponse() {
		return response;
	}

	public void setResponse(ZestResponse response) {
		this.response = response;
	}
	
	protected void addReferer(ZestRequestRef ref) {
		this.referers.add(ref);
	}
	protected void removeReferer(ZestRequestRef ref) {
		this.referers.remove(ref);
	}

	public void setUpRefs(ZestScript script) {
		for (ZestTransformation zt : getTransformations()) {
			if (zt instanceof ZestRequestRef) {
				ZestRequestRef zrr = (ZestRequestRef)zt;
				ZestRequest req = script.getRequest(zrr.getRequestId());
				if (req != null) {
					zrr.setRequest(req);
				} else {
					throw new InvalidParameterException("Failed to find request id: " + zrr.getRequestId());
				}
			}
		}
	}
	
	public void moveUp (ZestElement ze) {
		int i;
		if (ze instanceof ZestAssertion) {
			ZestAssertion za = (ZestAssertion) ze;
			i = this.assertions.indexOf(za);
			if (i > 0) {
				this.assertions.remove(za);
				this.assertions.add(i-1, za);
			}
		} else if (ze instanceof ZestTransformation) {
			ZestTransformation zt = (ZestTransformation) ze;
			i = this.transformations.indexOf(zt);
			if (i > 0) {
				this.transformations.remove(zt);
				this.transformations.add(i-1, zt);
			}
		} else if (ze instanceof ZestTest) {
			ZestTest zt = (ZestTest) ze;
			i = this.tests.indexOf(zt);
			if (i > 0) {
				this.tests.remove(zt);
				this.tests.add(i-1, zt);
			}
		} else {
			throw new IllegalArgumentException("Unrecognised element class: " + ze.getClass().getCanonicalName());
		}
	}

	public void moveDown (ZestElement ze) {
		int i;
		if (ze instanceof ZestAssertion) {
			ZestAssertion za = (ZestAssertion) ze;
			i = this.assertions.indexOf(za);
			if (i >= 0 && i < this.assertions.size()) {
				this.assertions.remove(za);
				this.assertions.add(i+1, za);
			}
		} else if (ze instanceof ZestTransformation) {
			ZestTransformation zt = (ZestTransformation) ze;
			i = this.transformations.indexOf(zt);
			if (i >= 0 && i < this.transformations.size()) {
				this.transformations.remove(zt);
				this.transformations.add(i+1, zt);
			}
		} else if (ze instanceof ZestTest) {
			ZestTest zt = (ZestTest) ze;
			i = this.tests.indexOf(zt);
			if (i >= 0 && i < this.tests.size()) {
				this.tests.remove(zt);
				this.tests.add(i+1, zt);
			}
		} else {
			throw new IllegalArgumentException("Unrecognised element class: " + ze.getClass().getCanonicalName());
		}
	}
	
	public List<String> getTokens(String tokenStart, String tokenEnd) {
		List<String> tokens = new ArrayList<String>();
		// Note the .*? matches as little as possible
		Pattern p = Pattern.compile(Pattern.quote(tokenStart) + ".*?" + Pattern.quote(tokenEnd));
		Matcher matcher = p.matcher(this.getHeaders());
		String token;
		while (matcher.find()) {
			token = matcher.group();
			tokens.add(token.substring(tokenStart.length(), token.length() - tokenEnd.length()));
		}
		matcher = p.matcher(this.getData());
		while (matcher.find()) {
			token = matcher.group();
			tokens.add(token.substring(tokenStart.length(), token.length() - tokenEnd.length()));
		}
		return tokens;
	}

	public void replaceTokens(ZestTokens tokens) {
		// Note the .*? matches as little as possible
		Pattern p = Pattern.compile(Pattern.quote(tokens.getTokenStart()) + ".*?" + Pattern.quote(tokens.getTokenEnd()));
		String token;
		String work = this.getHeaders();
		Matcher matcher = p.matcher(work);
		while (matcher.find()) {
			token = matcher.group();
			// Strip off start and end chrs
			token = token.substring(tokens.getTokenStart().length(), token.length() - tokens.getTokenEnd().length());
			work = work.substring(0, matcher.start()) + tokens.getToken(token) + work.substring(matcher.end());
			matcher = p.matcher(work);
		}
		this.setHeaders(work);
		
		work = this.getData();
		matcher = p.matcher(work);
		while (matcher.find()) {
			token = matcher.group();
			// Strip off start and end chrs
			token = token.substring(tokens.getTokenStart().length(), token.length() - tokens.getTokenEnd().length());
			work = work.substring(0, matcher.start()) + tokens.getToken(token) + work.substring(matcher.end());
			matcher = p.matcher(work);
		}
		this.setData(work);
	}
	
	public static void main(String[] args) throws Exception {
		ZestRequest req = new ZestRequest();
		req.setHeaders("Testing tokens {{token1}} and {{token2}}");
		req.setData("Testing tokens {{token3}} and {{token4}}");
		
		
		ZestTokens tokens = new ZestTokens();

		List<String> tkns = req.getTokens(tokens.getTokenStart(), tokens.getTokenEnd());
		for (String tkn : tkns) {
			System.out.println("Main Token: " + tkn);
		}
		tokens.setToken("token1", "A");
		tokens.setToken("token2", "BBB");
		tokens.setToken("token3", "CCCCCCC");
		tokens.setToken("token4", "DD");
		tokens.setToken("token5", "EEEEEEEEEE");
		
		req.replaceTokens(tokens );
		System.out.println("Main header: " + req.getHeaders());
		System.out.println("Main data  : " + req.getData());
		
		

	}
}
