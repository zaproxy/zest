/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import org.openqa.selenium.WebDriver;

/**
 * Close the specified client 
 * @author simon
 *
 */
public class ZestClientWindowOpenUrl extends ZestClient {

	private String windowHandle = null;
	private String url = "";
	
	public ZestClientWindowOpenUrl() {
		super();
	}

	public ZestClientWindowOpenUrl(String windowHandle, String url) {
		super();
		this.windowHandle = windowHandle;
		this.url = url;;
	}

	public String getWindowHandle() {
		return windowHandle;
	}

	public void setWindowHandle(String windowHandle) {
		this.windowHandle = windowHandle;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public ZestStatement deepCopy() {
		return new ZestClientWindowOpenUrl(this.getWindowHandle(), this.getUrl());
	}

	@Override
	public boolean isPassive() {
		return false;
	}
	
	public String invoke(ZestRuntime runtime) throws ZestClientFailException {
		WebDriver wd = runtime.getWebDriver(this.getWindowHandle());
		
		if (wd == null) {
			// No point throwing an exception as we were going to close it anyway
			return null;
		}
		
		String targetUrl = runtime.replaceVariablesInString(url, true);
		wd.navigate().to(targetUrl);
		
		return targetUrl;
	}

}
