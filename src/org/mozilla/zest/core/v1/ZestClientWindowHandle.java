/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import org.openqa.selenium.WebDriver;

/**
 * A class which allows you to get a session associated with a popup window
 * @author simon
 *
 */
public class ZestClientWindowHandle extends ZestClient {

	private String windowHandle = null;
	private String url = null;
	
	public ZestClientWindowHandle() {
		super();
	}

	public ZestClientWindowHandle(String windowHandle, String url) {
		super();
		this.windowHandle = windowHandle;
		this.url = url;
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
		return new ZestClientWindowHandle(this.getWindowHandle(), this.getUrl());
	}

	@Override
	public boolean isPassive() {
		return false;
	}

	public String invoke(ZestRuntime runtime) throws ZestClientFailException {
		WebDriver window;
		for (WebDriver wd : runtime.getWebDrivers()) {
			for (String wh : wd.getWindowHandles()) {
				window = wd.switchTo().window(wh);
				if (window.getCurrentUrl().equals(url)) {
					runtime.addWebDriver(this.windowHandle, wd);
					break;
				}
			}
			
		}
		
		return this.windowHandle;
	}

}
