/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

/** A client element statement that scrolls to the element, if it's not already in view. */
public class ZestClientElementScrollTo extends ZestClientElement {

    public ZestClientElementScrollTo(String sessionIdName, String type, String element) {
        super(sessionIdName, type, element);
    }

    public ZestClientElementScrollTo() {
        super();
    }

    @Override
    public String invoke(ZestRuntime runtime) throws ZestClientFailException {

        JavascriptExecutor js = (JavascriptExecutor) runtime.getWebDriver(getWindowHandle());

        if (!isElementInView(js, getWebElement(runtime))) {
            String script =
                    """
                    arguments[0].scrollIntoView({ block: "nearest" });
                    """;
            js.executeScript(script, getWebElement(runtime));
        }

        return null;
    }

    private boolean isElementInView(JavascriptExecutor js, WebElement element) {
        // Sourced from:
        // https://gist.github.com/davidtheclark/5515733#gistcomment-2113205
        return (boolean)
                js.executeScript(
                        """
                        const rect = arguments[0].getBoundingClientRect()

                        const windowHeight = (window.innerHeight || document.documentElement.clientHeight)
                        const windowWidth = (window.innerWidth || document.documentElement.clientWidth)

                        const vertInView = (rect.top <= windowHeight) && ((rect.top + rect.height) > 0)
                        const horInView = (rect.left <= windowWidth) && ((rect.left + rect.width) > 0)

                        return (vertInView && horInView)
                        """,
                        element);
    }

    @Override
    public ZestClientElementScrollTo deepCopy() {
        return this.deepCopy(ZestClientElementScrollTo::new);
    }

    @Override
    public boolean isPassive() {
        return false;
    }
}
