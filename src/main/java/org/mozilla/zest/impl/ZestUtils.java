/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.impl;

import java.util.ArrayList;
import java.util.List;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import org.mozilla.zest.core.v1.ZestResponse;

public class ZestUtils {

    public static List<String> getForms(ZestResponse response) {
        List<String> list = new ArrayList<String>();
        Source src = new Source(response.getHeaders() + response.getBody());
        List<Element> formElements = src.getAllElements(HTMLElementName.FORM);
        int formId = 0;
        while (formElements != null && formId < formElements.size()) {
            // TODO support form names
            // Element form = formElements.get(formId);
            list.add(Integer.toString(formId));

            formId++;
        }
        return list;
    }

    public static List<String> getFields(ZestResponse response, int formId) {
        List<String> list = new ArrayList<String>();

        Source src = new Source(response.getHeaders() + response.getBody());
        List<Element> formElements = src.getAllElements(HTMLElementName.FORM);
        if (formElements != null && formId < formElements.size()) {
            Element form = formElements.get(formId);
            List<Element> inputElements = form.getAllElements(HTMLElementName.INPUT);
            String field;

            for (Element inputElement : inputElements) {
                field = inputElement.getAttributeValue("ID");
                if (field == null || field.length() == 0) {
                    field = inputElement.getAttributeValue("NAME");
                }
                if (field != null && field.length() > 0) {
                    list.add(field);
                }
            }
        }
        return list;
    }
}
