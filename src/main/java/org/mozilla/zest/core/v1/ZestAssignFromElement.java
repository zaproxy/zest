/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

public class ZestAssignFromElement extends ZestAssignment {

    private boolean filterElementName;
    private String elementNameFilter;

    private boolean filterAttributeValue;
    private String attributeNameFilter;
    private String attributeValueFilter;
    private int attributeValueFilterFlags;
    private transient Pattern attributeValueFilterPattern;

    private boolean reverseFilteredElements;
    private int elementIndex;

    private boolean returnElement;

    private boolean returnAttribute;
    private String returnedAttributeName;

    public ZestAssignFromElement() {}

    public ZestAssignFromElement(String variableName) {
        super(variableName);
    }

    @Override
    public String assign(ZestResponse response, ZestRuntime runtime)
            throws ZestAssignFailException {
        if (response == null) {
            throw new ZestAssignFailException(this, "Null response");
        }

        Source src = new Source(response.getBody());
        List<Element> elementsFilteredByElementName = filterByElementNameIfConfigured(src);
        List<Element> elementsFilteredByAttributeValue =
                filterByAttributeValueIfConfigured(elementsFilteredByElementName);
        List<Element> elements = reverseIfConfigured(elementsFilteredByAttributeValue);
        Element element = findElementAtIndex(elements);
        if (element == null) {
            return null;
        }
        return getReturnValue(element);
    }

    private String getReturnValue(Element element) throws ZestAssignFailException {
        if (returnElement) {
            return element.getContent().toString();
        } else if (returnAttribute) {
            return element.getAttributeValue(returnedAttributeName);
        } else {
            throw new ZestAssignFailException(this, "A selection method must be configured");
        }
    }

    private Element findElementAtIndex(List<Element> elements) {
        if (indexIsOutOfRange(elements)) {
            return null;
        }
        return elements.get(elementIndex);
    }

    private boolean indexIsOutOfRange(List<Element> elements) {
        return elements.isEmpty() || elementIndex >= elements.size();
    }

    private List<Element> reverseIfConfigured(List<Element> elements) {
        if (reverseFilteredElements) {
            Collections.reverse(elements);
            return elements;
        }
        return elements;
    }

    private List<Element> filterByElementNameIfConfigured(Source source) {
        if (filterElementName) {
            return source.getAllElements(elementNameFilter);
        }
        return source.getAllElements();
    }

    private List<Element> filterByAttributeValueIfConfigured(List<Element> elements)
            throws ZestAssignFailException {
        if (filterAttributeValue) {
            List<Element> matchingElements = new ArrayList<>();
            for (Element element : elements) {
                if (isMatchingAttributeFilter(element)) {
                    matchingElements.add(element);
                }
            }
            return matchingElements;
        }
        return elements;
    }

    private boolean isMatchingAttributeFilter(Element element) throws ZestAssignFailException {
        if (attributeValueFilterPattern == null) {
            try {
                attributeValueFilterPattern =
                        Pattern.compile(attributeValueFilter, attributeValueFilterFlags);
            } catch (IllegalArgumentException ex) {
                throw new ZestAssignFailException(
                        this,
                        ex.getMessage()
                                + "\r\nRegEx:'"
                                + attributeValueFilter
                                + "' with Flags:'"
                                + attributeValueFilterFlags
                                + "' can not be compiled to pattern.");
            }
        }

        String attributeValue = element.getAttributeValue(attributeNameFilter);
        if (attributeValue == null) {
            return false;
        }
        Matcher matcher = attributeValueFilterPattern.matcher(attributeValue);
        return matcher.matches();
    }

    public ZestAssignFromElement whereElementIs(String elementName) {
        filterElementName = true;
        elementNameFilter = elementName;
        return this;
    }

    public ZestAssignFromElement whereAttributeIs(
            String attributeName, String attributeValueRegExp) {
        return whereAttributeIs(attributeName, attributeValueRegExp, 0);
    }

    public ZestAssignFromElement whereAttributeIs(
            String attributeName, String attributeValueRegExp, int flags) {
        filterAttributeValue = true;
        attributeNameFilter = attributeName;
        attributeValueFilter = attributeValueRegExp;
        attributeValueFilterFlags = flags;
        attributeValueFilterPattern = null;
        return this;
    }

    public ZestAssignFromElement first() {
        return atIndex(0);
    }

    public ZestAssignFromElement last() {
        return atIndex(0, true);
    }

    public ZestAssignFromElement atIndex(int index) {
        return atIndex(index, false);
    }

    public ZestAssignFromElement atIndex(int index, boolean reverse) {
        reverseFilteredElements = reverse;
        elementIndex = index;
        return this;
    }

    private void clearSelectMethod() {
        returnElement = false;
        returnAttribute = false;
    }

    public ZestAssignFromElement selectContent() {
        clearSelectMethod();
        returnElement = true;
        return this;
    }

    public ZestAssignFromElement selectAttributeValue(String attributeName) {
        clearSelectMethod();
        returnAttribute = true;
        returnedAttributeName = attributeName;
        return this;
    }

    public boolean isFilteredByElementName() {
        return filterElementName;
    }

    public String getElementNameFilter() {
        return elementNameFilter;
    }

    public boolean isFilteredByAttribute() {
        return filterAttributeValue;
    }

    public String getAttributeNameFilter() {
        return attributeNameFilter;
    }

    public String getAttributeValueFilter() {
        return attributeValueFilter;
    }

    public int getAttributeValueFilterFlags() {
        return attributeValueFilterFlags;
    }

    public boolean areFilteredElementsReversed() {
        return reverseFilteredElements;
    }

    public int getElementIndex() {
        return elementIndex;
    }

    public boolean isReturningElement() {
        return returnElement;
    }

    public boolean isReturningAttribute() {
        return returnAttribute;
    }

    public String getReturnedAttributeName() {
        return returnedAttributeName;
    }

    public void removeFilter() {
        filterElementName = false;
        elementNameFilter = null;

        filterAttributeValue = false;
        attributeNameFilter = null;
        attributeValueFilter = null;
        attributeValueFilterFlags = 0;
        attributeValueFilterPattern = null;
    }

    @Override
    public ZestStatement deepCopy() {
        ZestAssignFromElement zestAssignFromElement =
                new ZestAssignFromElement(this.getVariableName());
        zestAssignFromElement.filterElementName = this.filterElementName;
        zestAssignFromElement.elementNameFilter = this.elementNameFilter;

        zestAssignFromElement.filterAttributeValue = this.filterAttributeValue;
        zestAssignFromElement.attributeNameFilter = this.attributeNameFilter;
        zestAssignFromElement.attributeValueFilter = this.attributeValueFilter;
        zestAssignFromElement.attributeValueFilterFlags = this.attributeValueFilterFlags;
        zestAssignFromElement.attributeValueFilterPattern = this.attributeValueFilterPattern;

        zestAssignFromElement.reverseFilteredElements = this.reverseFilteredElements;
        zestAssignFromElement.elementIndex = this.elementIndex;

        zestAssignFromElement.returnElement = this.returnElement;

        zestAssignFromElement.returnAttribute = this.returnAttribute;
        zestAssignFromElement.returnedAttributeName = this.returnedAttributeName;
        return zestAssignFromElement;
    }
}
