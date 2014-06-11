/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * An abstract class representing an action on a client element.
 */

public abstract class ZestClientElement extends ZestClient {

	private String windowHandle = null;
	private String type = null;
	private String element = null;
	
	public ZestClientElement(String windowHandle, String type, String element) {
		super();
		this.windowHandle = windowHandle;
		this.type = type;
		this.element = element;
	}

	public ZestClientElement() {
		super();
	}
	
	public String getWindowHandle() {
		return windowHandle;
	}

	public void setWindowHandle(String windowHandle) {
		this.windowHandle = windowHandle;
	}

	public String getElement() {
		return element;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setElement(String element) {
		this.element = element;
	}

	protected WebElement getWebElement(ZestRuntime runtime) throws ZestClientFailException {
		
		WebDriver wd = runtime.getWebDriver(this.getWindowHandle());
		
		if (wd == null) {
			throw new ZestClientFailException(this, "No client: " + runtime.getVariable(getWindowHandle()));
		}

		if ("className".equalsIgnoreCase(type)) {
			return wd.findElement(By.className(this.getElement()));
		} else if ("cssSelector".equalsIgnoreCase(type)) {
			return wd.findElement(By.cssSelector(this.getElement()));
		} else if ("id".equalsIgnoreCase(type)) {
			return wd.findElement(By.id(this.getElement()));
		} else if ("linkText".equalsIgnoreCase(type)) {
			return wd.findElement(By.linkText(this.getElement()));
		} else if ("name".equalsIgnoreCase(type)) {
			return wd.findElement(By.name(this.getElement()));
		} else if ("partialLinkText".equalsIgnoreCase(type)) {
			return wd.findElement(By.partialLinkText(this.getElement()));
		} else if ("tagName".equalsIgnoreCase(type)) {
			return wd.findElement(By.tagName(this.getElement()));
		} else if ("xpath".equalsIgnoreCase(type)) {
			return wd.findElement(By.xpath(this.getElement()));
		}
		throw new ZestClientFailException(this, "Unsupported type: " + type);
	}

}
