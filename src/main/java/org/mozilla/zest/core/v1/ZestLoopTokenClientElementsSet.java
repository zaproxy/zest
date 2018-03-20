/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.util.Collections;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * The Class ZestLoopTokenFileSet.<br>
 * This class does not override ZestLoopTokenStringSet because<br>
 * the tokens inside the converted set must not be transient
 */
public class ZestLoopTokenClientElementsSet extends ZestElement
        implements ZestLoopTokenSet<String> {

    private String windowHandle = null;
    private String type = null;
    private String element = null;
    private String attribute = null;

    /** The converted set. */
    private transient ZestLoopTokenStringSet convertedSet = null;

    private transient ZestLoopClientElements loop;

    /**
     * Instantiates a new zest loop token file set.
     *
     * @param loop the loop.
     * @param windowHandle the window handle.
     * @param type the type to select the {@code element}.
     * @param element the name of the element(s).
     * @param attribute the attribute of the element, might be {@code null}.
     */
    public ZestLoopTokenClientElementsSet(
            ZestLoopClientElements loop,
            String windowHandle,
            String type,
            String element,
            String attribute) {
        super();
        this.loop = loop;
        this.windowHandle = windowHandle;
        this.type = type;
        this.element = element;
        this.attribute = attribute;
    }

    /**
     * private method for initialization of the loop (TokenSet & first state).
     *
     * @return the zest loop token string set
     * @throws ZestClientFailException
     */
    protected ZestLoopTokenStringSet getConvertedSet() throws ZestClientFailException {
        if (this.convertedSet == null) {
            if (loop == null) {
                // Not yet initialized
                return null;
            }
            ZestLoopTokenStringSet set = new ZestLoopTokenStringSet();

            WebDriver wd = this.loop.getRuntime().getWebDriver(getWindowHandle());

            List<WebElement> weList;

            if ("className".equalsIgnoreCase(type)) {
                weList = wd.findElements(By.className(this.getElement()));
            } else if ("cssSelector".equalsIgnoreCase(type)) {
                weList = wd.findElements(By.cssSelector(this.getElement()));
            } else if ("id".equalsIgnoreCase(type)) {
                weList = wd.findElements(By.id(this.getElement()));
            } else if ("linkText".equalsIgnoreCase(type)) {
                weList = wd.findElements(By.linkText(this.getElement()));
            } else if ("name".equalsIgnoreCase(type)) {
                weList = wd.findElements(By.name(this.getElement()));
            } else if ("partialLinkText".equalsIgnoreCase(type)) {
                weList = wd.findElements(By.partialLinkText(this.getElement()));
            } else if ("tagName".equalsIgnoreCase(type)) {
                weList = wd.findElements(By.tagName(this.getElement()));
            } else if ("xpath".equalsIgnoreCase(type)) {
                weList = wd.findElements(By.xpath(this.getElement()));
            } else {
                throw new ZestClientFailException(this, "Unsupported type: " + type);
            }
            for (WebElement we : weList) {
                String val;

                if (this.attribute == null || this.attribute.length() == 0) {
                    val = we.getText();
                } else {
                    val = we.getAttribute(attribute);
                }

                if (val != null && val.length() > 0) {
                    set.addToken(val);
                }
            }

            this.convertedSet = set;
        }
        return convertedSet;
    }

    @Override
    public String getToken(int index) {
        try {
            return this.getConvertedSet().getToken(index);
        } catch (ZestClientFailException e) {
            return null;
        }
    }

    public List<String> getTokens() {
        try {
            return Collections.unmodifiableList(getConvertedSet().getTokens());
        } catch (ZestClientFailException e) {
            return null;
        }
    }

    @Override
    public int indexOf(String token) {
        try {
            return getConvertedSet().indexOf(token);
        } catch (ZestClientFailException e) {
            return -1;
        }
    }

    @Override
    public String getLastToken() {
        try {
            return getConvertedSet().getLastToken();
        } catch (ZestClientFailException e) {
            return null;
        }
    }

    @Override
    public int size() {
        try {
            return this.getConvertedSet().size();
        } catch (ZestClientFailException e) {
            return -1;
        }
    }

    @Override
    public ZestLoopTokenClientElementsSet deepCopy() {
        return new ZestLoopTokenClientElementsSet(loop, windowHandle, type, element, attribute);
    }

    public String getWindowHandle() {
        return windowHandle;
    }

    public String getElement() {
        return element;
    }

    public void setWindowHandle(String windowHandle) {
        this.windowHandle = windowHandle;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getType() {
        return type;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void setConvertedSet(ZestLoopTokenStringSet convertedSet) {
        this.convertedSet = convertedSet;
    }

    @Override
    public ZestLoopStateClientElements getFirstState() {
        try {
            return new ZestLoopStateClientElements(this);
        } catch (ZestClientFailException e) {
            return null;
        }
    }

    public void setLoop(ZestLoopClientElements loop) {
        this.loop = loop;
    }
}
