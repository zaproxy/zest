/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.core.v1;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * A {@code By} that finds elements through a chain of shadow hosts, each CSS selector being
 * relative to the (open or closed) shadow root of the previous one.
 */
final class ShadowBy extends By {

    private final List<String> path;

    /** The CSS selectors, the last one is the target element. */
    ShadowBy(List<String> path) {
        this.path = path;
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        SearchContext root = context;
        List<WebElement> found = List.of();
        for (int i = 0; i < path.size(); i++) {
            if (root == null) {
                return List.of();
            }
            found = root.findElements(By.cssSelector(path.get(i)));
            if (found.isEmpty()) {
                return List.of();
            }
            if (i < path.size() - 1) {
                root = found.get(0).getShadowRoot();
            }
        }
        return found;
    }

    @Override
    public String toString() {
        return "ShadowBy: " + path;
    }
}
