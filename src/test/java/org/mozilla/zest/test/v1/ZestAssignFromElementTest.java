/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.test.v1;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import org.junit.Test;
import org.mozilla.zest.core.v1.ZestAssignFailException;
import org.mozilla.zest.core.v1.ZestAssignFromElement;
import org.mozilla.zest.core.v1.ZestJSON;
import org.mozilla.zest.core.v1.ZestResponse;

/** Unit test for {@link ZestAssignFromElement}. */
public class ZestAssignFromElementTest {

    private final String htmlScaffold =
            "<html><head><title>Test Document</title></head><body>"
                    + "<div>"
                    + "%s"
                    + "</div>"
                    + "</body></html>";

    private final String testPageA =
            "<ul class=\"customA\">\n"
                    + "   <li id=\"1\" class=\"abc12\">Lorem ipsum dolor sit amet, consectetuer adipiscing elit.</li>\n"
                    + "   <li id=\"2\" class=\"abc13\">Aliquam tincidunt mauris eu risus.</li>\n"
                    + "   <li id=\"3\" class=\"abc14\">Vestibulum auctor dapibus neque.</li>\n"
                    + "</ul>\n"
                    + "<ul class=\"customB\">\n"
                    + "</ul>";

    private final String testPageB =
            "<nav>\n"
                    + "  <ul>\n"
                    + "    <li><a href=\"#nowhere\" title=\"Lorum ipsum dolor sit amet\">Lorem</a></li>\n"
                    + "    <li><a href=\"#nowhere\" title=\"Aliquam tincidunt mauris eu risus\">Aliquam</a></li>\n"
                    + "    <li><a href=\"#nowhere\" title=\"Morbi in sem quis dui placerat ornare\">Morbi</a></li>\n"
                    + "    <li><a href=\"#nowhere\" title=\"Praesent dapibus, neque id cursus faucibus\">Praesent</a></li>\n"
                    + "    <li><a href=\"#nowhere\" title=\"Pellentesque fermentum dolor\">Pellentesque</a></li>\n"
                    + "  </ul>\n"
                    + "</nav>"
                    + "<form id=\"0815\" action=\"bar.html#name=123\" method=\"post\">\n"
                    + "    <div>\n"
                    + "         <label for=\"name\">Text Input:</label>\n"
                    + "         <input type=\"text\" name=\"name\" id=\"name\" value=\"\" tabindex=\"1\" />\n"
                    + "    </div>\n"
                    + "    <div>\n"
                    + "         <h4>Radio Button Choice</h4>\n"
                    + "         <label for=\"radio-choice-1\">Choice 1</label>\n"
                    + "         <input type=\"radio\" name=\"radio-choice-1\" id=\"radio-choice-1\" tabindex=\"2\" value=\"choice-1\" />\n"
                    + "     <label for=\"radio-choice-2\">Choice 2</label>\n"
                    + "         <input type=\"radio\" name=\"radio-choice-2\" id=\"radio-choice-2\" tabindex=\"3\" value=\"choice-2\" />\n"
                    + "    </div>\n"
                    + "  <div>\n"
                    + "    <label for=\"select-choice\">Select Dropdown Choice:</label>\n"
                    + "    <select name=\"select-choice\" id=\"select-choice\">\n"
                    + "      <option value=\"Choice 1\">Choice 1</option>\n"
                    + "      <option value=\"Choice 2\">Choice 2</option>\n"
                    + "      <option value=\"Choice 3\">Choice 3</option>\n"
                    + "    </select>\n"
                    + "  </div>\n"
                    + "  <div>\n"
                    + "    <label for=\"textarea\">Textarea:</label>\n"
                    + "    <textarea cols=\"40\" rows=\"8\" name=\"textarea\" id=\"textarea\"></textarea>\n"
                    + "  </div>\n"
                    + "  <div><input type=\"submit\" value=\"Submit\" /></div>\n"
                    + "  <div>\n"
                    + "      <label for=\"checkbox\">Checkbox:</label>\n"
                    + "    <input type=\"checkbox\" name=\"checkbox\" id=\"checkbox\" />\n"
                    + "    </div>\n"
                    + "</form>\n"
                    + "<form id=\"99\" action=\"foo.html#site=456\" method=\"post\"><input type=\"submit\" value=\"Submit\" /></form>\n";

    @Test
    public void selectClassAttributeOfLastElement() throws Exception {
        // Given
        ZestAssignFromElement fromElement =
                new ZestAssignFromElement("Var").last().selectAttributeValue("class");

        // When
        String result = assign(fromElement, testPageA);

        // Then
        assertEquals("customB", result);
    }

    @Test
    public void noElementFoundReturnsNull() throws Exception {
        // Given
        ZestAssignFromElement fromElement =
                new ZestAssignFromElement("Var").whereElementIs("xyz").first().selectContent();

        // When
        String result = assign(fromElement, testPageA);

        // Then
        assertEquals(null, result);
    }

    @Test
    public void filterByElementAndAttributeThenSelectTheFirstContent() throws Exception {
        // Given
        ZestAssignFromElement fromElement =
                new ZestAssignFromElement("Var")
                        .whereElementIs("li")
                        .whereAttributeIs("id", "2")
                        .first()
                        .selectContent();

        // When
        String result = assign(fromElement, testPageA);

        // Then
        assertEquals("Aliquam tincidunt mauris eu risus.", result);
    }

    @Test
    public void filterByElementAndThenSelectTheLastClassAttribute() throws Exception {
        // Given
        ZestAssignFromElement fromElement =
                new ZestAssignFromElement("Var")
                        .whereElementIs("li")
                        .last()
                        .selectAttributeValue("class");

        // When
        String result = assign(fromElement, testPageA);

        // Then
        assertEquals("abc14", result);
    }

    @Test
    public void filterBeforeLastDivAndSelectContent() throws Exception {
        // Given
        ZestAssignFromElement fromElement =
                new ZestAssignFromElement("Var")
                        .whereElementIs("div")
                        .atIndex(1, true)
                        .selectContent();

        // When
        String result = assign(fromElement, testPageB);

        // Then
        assertEquals("<input type=\"submit\" value=\"Submit\" />", result);
    }

    @Test
    public void filterByValueIsStartingWithChoiceAndSelectContentOfSecondItem() throws Exception {
        // Given
        ZestAssignFromElement fromElement =
                new ZestAssignFromElement("Var")
                        .whereAttributeIs("value", "Choice.*")
                        .atIndex(1)
                        .selectContent();

        // When
        String result = assign(fromElement, testPageB);

        // Then
        assertEquals("Choice 2", result);
    }

    @Test
    public void selectActionAttributeOfSecondForm() throws Exception {
        // Given
        ZestAssignFromElement fromElement =
                new ZestAssignFromElement("Var")
                        .whereElementIs("form")
                        .atIndex(1)
                        .selectAttributeValue("action");

        // When
        String result = assign(fromElement, testPageB);

        // Then
        assertEquals("foo.html#site=456", result);
    }

    @Test
    public void testSerialization() {
        ZestAssignFromElement fromElement =
                new ZestAssignFromElement("Var")
                        .whereElementIs("form")
                        .atIndex(1)
                        .selectAttributeValue("action");

        String str = ZestJSON.toString(fromElement);
        ZestAssignFromElement fromElement2 = (ZestAssignFromElement) ZestJSON.fromString(str);

        assertEquals(fromElement.getElementType(), fromElement2.getElementType());
        assertEquals(fromElement.getVariableName(), fromElement2.getVariableName());
        assertEquals(fromElement.getAttributeNameFilter(), fromElement2.getAttributeNameFilter());
        assertEquals(fromElement.getAttributeValueFilter(), fromElement2.getAttributeValueFilter());
        assertEquals(fromElement.getElementNameFilter(), fromElement2.getElementNameFilter());
        assertEquals(
                fromElement.getReturnedAttributeName(), fromElement2.getReturnedAttributeName());
        assertEquals(
                fromElement.getAttributeValueFilterFlags(),
                fromElement2.getAttributeValueFilterFlags());
        assertEquals(fromElement.getElementIndex(), fromElement2.getElementIndex());
        assertEquals(
                fromElement.areFilteredElementsReversed(),
                fromElement2.areFilteredElementsReversed());
        assertEquals(fromElement.isReturningAttribute(), fromElement2.isReturningAttribute());
        assertEquals(fromElement.isReturningElement(), fromElement2.isReturningElement());
        assertEquals(fromElement.isFilteredByAttribute(), fromElement2.isFilteredByAttribute());
        assertEquals(fromElement.isFilteredByElementName(), fromElement2.isFilteredByElementName());
    }

    @Test(expected = ZestAssignFailException.class)
    public void invalidRegExShouldThrowAZestAssignFailException() throws Exception {
        // Given
        ZestAssignFromElement fromElement =
                new ZestAssignFromElement("Var")
                        .whereAttributeIs("title", "[")
                        .atIndex(0)
                        .selectContent();

        // When
        assign(fromElement, testPageB);

        // Then = ZestAssignFailException
    }

    private String assign(ZestAssignFromElement assignFromElement, String pageContent)
            throws ZestAssignFailException {
        ZestResponse response = makeZestResponse(pageContent);
        return assignFromElement.assign(response, new TestRuntime());
    }

    private ZestResponse makeZestResponse(String pageContent) {
        String html = String.format(htmlScaffold, pageContent);
        String notRelevantHeader = "Header not relevant";
        URL notRelevantUrl = null;
        return new ZestResponse(notRelevantUrl, notRelevantHeader, html, 200, 0);
    }
}
