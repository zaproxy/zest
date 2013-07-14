/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZestRequest extends ZestStatement {

	private URL url;
	private String urlToken;	// String version of the URL including tokens - as these wont be a valid urls
	private String data;	// TODO this dependant on protocol, eg http / ws / ??
	private String method;
	private String headers;
	private ZestResponse response;
	private List<ZestTransformation> transformations = new ArrayList<ZestTransformation>();
	private List<ZestAssertion> assertions = new ArrayList<ZestAssertion>();
	
	private transient List<ZestRequestRef> referers = new ArrayList<ZestRequestRef>();
	
	public ZestRequest(int index) {
		super(index);
	}
	
	public ZestRequest() {
		super();
	}
	
	@Override
	public ZestRequest deepCopy () {
		ZestRequest zr = new ZestRequest(this.getIndex());
		zr.setUrl(this.url);
		zr.setUrlToken(this.urlToken);
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
		return zr;
	}
	
	public URL getUrl() {
		if (url == null && urlToken != null) {
			try {
				// We're assuming that the token has been replaced by this time ;)
				return new URL(this.urlToken);
			} catch (MalformedURLException e) {
				// Ignore - assume it includes an unexpanded token
			}
		}
		return url;
	}
	public void setUrl(URL url) {
		this.url = url;
	}
	public String getUrlToken() {
		return urlToken;
	}
	public void setUrlToken(String urlToken) {
		this.urlToken = urlToken;
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

	@Override
	public void setUpRefs(ZestScript script) {
		for (ZestTransformation zt : getTransformations()) {
			if (zt instanceof ZestRequestRef) {
				ZestRequestRef zrr = (ZestRequestRef)zt;
				ZestStatement stmt = script.getStatement(zrr.getRequestId());
				if (stmt != null && stmt instanceof ZestRequest) {
					zrr.setRequest((ZestRequest)stmt);
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
		} else {
			throw new IllegalArgumentException("Unrecognised element class: " + ze.getClass().getCanonicalName());
		}
	}

	@Override
	public Set<String> getTokens(String tokenStart, String tokenEnd) {
		Set<String> tokens = new TreeSet<String>();
		// Note the .*? matches as little as possible
		Pattern p = Pattern.compile(Pattern.quote(tokenStart) + ".*?" + Pattern.quote(tokenEnd));
		String token;
		if (this.getHeaders() != null) {
			Matcher matcher = p.matcher(this.getHeaders());
			while (matcher.find()) {
				token = matcher.group();
				tokens.add(token.substring(tokenStart.length(), token.length() - tokenEnd.length()));
			}
		}
		if (this.getData() != null) {
			Matcher matcher = p.matcher(this.getData());
			while (matcher.find()) {
				token = matcher.group();
				tokens.add(token.substring(tokenStart.length(), token.length() - tokenEnd.length()));
			}
		}
		return tokens;
	}
	
	private String replaceInString (ZestTokens tokens, String str) {
		if (str == null) {
			return null;
		}
		for (String [] nvPair : tokens.getTokens()) {
			String tokenStr = tokens.getTokenStart() + nvPair[0] + tokens.getTokenEnd();
			if (str.contains(tokenStr)) {
				str = str.replace(tokenStr, nvPair[1]);
			}
		}
		return str;
	}

	public void replaceTokens(ZestTokens tokens) {
		if (this.url != null) {
			try {
				this.setUrl(new URL(replaceInString(tokens, this.url.toString())));
			} catch (MalformedURLException e) {
				// Ignore
			}
		} else if (this.urlToken != null) {
			this.setUrlToken(this.replaceInString(tokens, this.urlToken));
		}
		this.setMethod(this.replaceInString(tokens, this.getMethod()));
		this.setHeaders(this.replaceInString(tokens, this.getHeaders()));
		this.setData(this.replaceInString(tokens, this.getData()));
	}
	
	@Override
	public boolean isSameSubclass(ZestElement ze) {
		return ze instanceof ZestRequest;
	}
	
	@Override
	public void setPrefix(String oldPrefix, String newPrefix) throws MalformedURLException {
		if (this.getUrl() != null && this.getUrl().toString().startsWith(oldPrefix)) {
			String urlStr = newPrefix + getUrl().toString().substring(oldPrefix.length());
			this.setUrl(new URL(urlStr));
			// TODO handle urlToken in the same way?
		} else {
			throw new IllegalArgumentException("Request url " + getUrl() + " does not start with " + oldPrefix);
		}
	}

	public static void main(String[] args) throws Exception {
		ZestRequest req = new ZestRequest();
		req.setHeaders("Testing tokens {{token1}} and {{token2}}");
		req.setData("Testing tokens {{token3}} and {{token4}}");
		
		
		ZestTokens tokens = new ZestTokens();

		Set<String> tkns = req.getTokens(tokens.getTokenStart(), tokens.getTokenEnd());
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
