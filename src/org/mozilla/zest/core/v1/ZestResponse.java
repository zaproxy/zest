/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

public class ZestResponse extends ZestElement {
	
	private String headers;
	private String body;
	private int statusCode;
	
	public ZestResponse (String headers, String body, int statusCode) {
		this.headers = headers;
		this.body = body;
		this.statusCode = statusCode;
	}

	@Override
	public ZestResponse deepCopy() {
		ZestResponse zr = new ZestResponse(this.headers, this.body, this.statusCode);
		return zr;
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

}
