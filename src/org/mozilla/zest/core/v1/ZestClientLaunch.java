/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.lang.reflect.Constructor;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;

import com.opera.core.systems.OperaDriver;

/**
 * Launch a new client (browser)
 * @author simon
 *
 */
public class ZestClientLaunch extends ZestClient {

	private String windowHandle = null;
	private String browserType = null;
	private String url = null;
	
	public ZestClientLaunch(String windowHandle, String browserType, String url) {
		super();
		this.windowHandle = windowHandle;
		this.browserType = browserType;
		this.url = url;
	}

	public ZestClientLaunch() {
		super();
	}

	public String getWindowHandle() {
		return windowHandle;
	}

	public void setWindowHandle(String windowHandle) {
		this.windowHandle = windowHandle;
	}

	public String getBrowserType() {
		return browserType;
	}

	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public ZestStatement deepCopy() {
		return new ZestClientLaunch(this.getWindowHandle(), this.getBrowserType(), this.getUrl());
	}

	@Override
	public boolean isPassive() {
		return false;
	}

	public String invoke(ZestRuntime runtime) throws ZestClientFailException {

		WebDriver driver = null;
		DesiredCapabilities cap = new DesiredCapabilities();
		// TODO add option to set other capabilities?
		String httpProxy = runtime.getProxy();
		if (httpProxy.length() > 0) {
			Proxy proxy = new Proxy();
			proxy.setHttpProxy(httpProxy);
			cap.setCapability(CapabilityType.PROXY, proxy);
		}

		if ("Firefox".equalsIgnoreCase(this.browserType)) {
			driver = new FirefoxDriver(cap);
		} else if ("Chrome".equalsIgnoreCase(this.browserType)) {
			driver = new ChromeDriver(cap); 
		} else if ("HtmlUnit".equalsIgnoreCase(this.browserType)) {
			driver = new HtmlUnitDriver(cap); 
		} else if ("InternetExplorer".equalsIgnoreCase(this.browserType)) {
			driver = new InternetExplorerDriver(cap); 
		} else if ("Opera".equalsIgnoreCase(this.browserType)) {
			driver = new OperaDriver(cap);
		} else if ("Safari".equalsIgnoreCase(this.browserType)) {
			driver = new SafariDriver(cap);
		} else {
			// See if its a class name
			try {
				Class<?> browserClass = this.getClass().getClassLoader().loadClass(this.browserType);
				Constructor<?> cons = browserClass.getConstructor(Capabilities.class);
				if (cons != null) {
					driver = (WebDriver) cons.newInstance(cap);
				} else {
					throw new ZestClientFailException(this, "Unsupported browser type: " + this.getBrowserType());
				}
			} catch (ClassNotFoundException e) {
				throw new ZestClientFailException(this, "Unsupported browser type: " + this.getBrowserType());
			} catch (Exception e) {
				throw new ZestClientFailException(this, e);
			}
		}
		
		runtime.addWebDriver(getWindowHandle(), driver);
		
		if (this.url != null) {
			driver.get(runtime.replaceVariablesInString(this.url, true));
		}
		
		return getWindowHandle();
	}

}
