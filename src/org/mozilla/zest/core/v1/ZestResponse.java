/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.net.URL;

public class ZestResponse extends ZestElement {
	
	private URL url;
	private String headers;
	private String body;
	private int statusCode;
	private long responseTimeInMs;
	
	public ZestResponse (URL url, String headers, String body, int statusCode, long responseTimeInMs) {
		this.url = url;
		this.headers = headers;
		this.body = body;
		this.statusCode = statusCode;
		this.responseTimeInMs = responseTimeInMs;
	}

	@Override
	public ZestResponse deepCopy() {
		ZestResponse zr = new ZestResponse(this.url, this.headers, this.body, this.statusCode, this.responseTimeInMs);
		return zr;
	}
	
	public URL getUrl() {
		return url;
	}

	public String getHeaders() {
		return headers;
	}

	public String getBody() {
		return body;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public long getResponseTimeInMs() {
		return responseTimeInMs;
	}

}
