/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class ZestRequest.
 */
public class ZestRequest extends ZestStatement {

	/** The url. */
	private URL url;
	
	/** The url token. */
	private String urlToken;	// String version of the URL including tokens - as these wont be a valid urls
	
	/** The data. */
	private String data;	// TODO this dependant on protocol, eg http / ws / ??
	
	/** The method. */
	private String method;
	
	/** The headers. */
	private String headers;
	
	/** The response. */
	private ZestResponse response;
	
	/** The assertions. */
	private List<ZestAssertion> assertions = new ArrayList<ZestAssertion>();
	
	/** The referers. */
	private transient List<ZestRequestRef> referers = new ArrayList<ZestRequestRef>();
	
	/**
	 * Instantiates a new zest request.
	 *
	 * @param index the index
	 */
	public ZestRequest(int index) {
		super(index);
	}
	
	/**
	 * Instantiates a new zest request.
	 */
	public ZestRequest() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#deepCopy()
	 */
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
		for (ZestAssertion zt : this.getAssertions()) {
			zr.addAssertion((ZestAssertion)zt.deepCopy());
		}
		return zr;
	}
	
	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
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
	
	/**
	 * Sets the url.
	 *
	 * @param url the new url
	 */
	public void setUrl(URL url) {
		this.url = url;
	}
	
	/**
	 * Gets the url token.
	 *
	 * @return the url token
	 */
	public String getUrlToken() {
		return urlToken;
	}
	
	/**
	 * Sets the url token.
	 *
	 * @param urlToken the new url token
	 */
	public void setUrlToken(String urlToken) {
		this.urlToken = urlToken;
	}
	
	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public String getData() {
		return data;
	}
	
	/**
	 * Sets the data.
	 *
	 * @param data the new data
	 */
	public void setData(String data) {
		this.data = data;
	}
	
	/**
	 * Gets the headers.
	 *
	 * @return the headers
	 */
	public String getHeaders() {
		return headers;
	}
	
	/**
	 * Sets the headers.
	 *
	 * @param headers the new headers
	 */
	public void setHeaders(String headers) {
		this.headers = headers;
	}
	
	/**
	 * Gets the method.
	 *
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}
	
	/**
	 * Sets the method.
	 *
	 * @param method the new method
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	
	/**
	 * Adds the assertion.
	 *
	 * @param assertion the assertion
	 */
	public void addAssertion (ZestAssertion assertion) {
		this.assertions.add(assertion);
	}
	
	/**
	 * Removes the assertion.
	 *
	 * @param assertion the assertion
	 */
	public void removeAssertion (ZestAssertion assertion) {
		this.assertions.remove(assertion);
	}
	
	/**
	 * Gets the assertions.
	 *
	 * @return the assertions
	 */
	public List<ZestAssertion> getAssertions() {
		return assertions;
	}

	/**
	 * Gets the response.
	 *
	 * @return the response
	 */
	public ZestResponse getResponse() {
		return response;
	}

	/**
	 * Sets the response.
	 *
	 * @param response the new response
	 */
	public void setResponse(ZestResponse response) {
		this.response = response;
	}
	
	/**
	 * Adds the referer.
	 *
	 * @param ref the ref
	 */
	protected void addReferer(ZestRequestRef ref) {
		this.referers.add(ref);
	}
	
	/**
	 * Removes the referer.
	 *
	 * @param ref the ref
	 */
	protected void removeReferer(ZestRequestRef ref) {
		this.referers.remove(ref);
	}

	/**
	 * Move up.
	 *
	 * @param ze the ze
	 */
	public void moveUp (ZestElement ze) {
		int i;
		if (ze instanceof ZestAssertion) {
			ZestAssertion za = (ZestAssertion) ze;
			i = this.assertions.indexOf(za);
			if (i > 0) {
				this.assertions.remove(za);
				this.assertions.add(i-1, za);
			}
		} else {
			throw new IllegalArgumentException("Unrecognised element class: " + ze.getClass().getCanonicalName());
		}
	}

	/**
	 * Move down.
	 *
	 * @param ze the ze
	 */
	public void moveDown (ZestElement ze) {
		int i;
		if (ze instanceof ZestAssertion) {
			ZestAssertion za = (ZestAssertion) ze;
			i = this.assertions.indexOf(za);
			if (i >= 0 && i < this.assertions.size()) {
				this.assertions.remove(za);
				this.assertions.add(i+1, za);
			}
		} else {
			throw new IllegalArgumentException("Unrecognised element class: " + ze.getClass().getCanonicalName());
		}
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#getTokens(java.lang.String, java.lang.String)
	 */
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
	
	/**
	 * Replace in string.
	 *
	 * @param tokens the tokens
	 * @param str the str
	 * @return the string
	 */
	private String replaceInString (ZestVariables tokens, String str, boolean urlEncode) {
		if (str == null) {
			return null;
		}
		for (String [] nvPair : tokens.getVariable()) {
			String tokenStr = tokens.getTokenStart() + nvPair[0] + tokens.getTokenEnd();
			if (urlEncode) {
				try {
					tokenStr = URLEncoder.encode(tokenStr, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// Ignore
				}
			}
			if (str.contains(tokenStr)) {
				str = str.replace(tokenStr, nvPair[1]);
			}
		}
		return str;
	}

	/**
	 * Replace tokens.
	 *
	 * @param tokens the tokens
	 */
	public void replaceTokens(ZestVariables tokens) {
		if (this.url != null) {
			try {
				this.setUrl(new URL(replaceInString(tokens, this.url.toString(), false)));	// TODO Work in progress
			} catch (MalformedURLException e) {
				// Ignore
			}
		} else if (this.urlToken != null) {
			this.setUrlToken(this.replaceInString(tokens, this.urlToken, false));	// TODO Work in progress
			try {
				this.setUrl(new URL(this.getUrlToken()));
			} catch (MalformedURLException e) {
				// Ignore
			}
		}
		this.setMethod(this.replaceInString(tokens, this.getMethod(), false));
		this.setHeaders(this.replaceInString(tokens, this.getHeaders(), false));
		this.setData(this.replaceInString(tokens, this.getData(), false));
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#isSameSubclass(org.mozilla.zest.core.v1.ZestElement)
	 */
	@Override
	public boolean isSameSubclass(ZestElement ze) {
		return ze instanceof ZestRequest;
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#setPrefix(java.lang.String, java.lang.String)
	 */
	@Override
	public void setPrefix(String oldPrefix, String newPrefix) throws MalformedURLException {
		if (this.getUrl() != null && this.getUrl().toString().startsWith(oldPrefix)) {
			String urlStr = newPrefix + getUrl().toString().substring(oldPrefix.length());
			this.setUrl(new URL(urlStr));
		} else {
			throw new IllegalArgumentException("Request url " + getUrl() + " does not start with " + oldPrefix);
		}
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {
		ZestRequest req = new ZestRequest();
		req.setHeaders("Testing tokens {{token1}} and {{token2}}");
		req.setData("Testing tokens {{token3}} and {{token4}}");
		
		
		ZestVariables tokens = new ZestVariables();

		Set<String> tkns = req.getTokens(tokens.getTokenStart(), tokens.getTokenEnd());
		for (String tkn : tkns) {
			System.out.println("Main Token: " + tkn);
		}
		tokens.setVariable("token1", "A");
		tokens.setVariable("token2", "BBB");
		tokens.setVariable("token3", "CCCCCCC");
		tokens.setVariable("token4", "DD");
		tokens.setVariable("token5", "EEEEEEEEEE");
		
		req.replaceTokens(tokens );
		System.out.println("Main header: " + req.getHeaders());
		System.out.println("Main data  : " + req.getData());
	}

	@Override
	public boolean isPassive() {
		return false;
	}

}
