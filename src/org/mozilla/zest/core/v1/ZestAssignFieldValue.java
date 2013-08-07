/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

/**
 * The Class ZestTransformFieldReplace.
 */
public class ZestAssignFieldValue extends ZestAssignment {

	/** The field definition. */
	private ZestFieldDefinition fieldDefinition; 
	
	/**
	 * Instantiates a new zest transform field replace.
	 */
	public ZestAssignFieldValue() {
	}

	/**
	 * Instantiates a new zest transform field replace.
	 *
	 * @param requestString the variableName
	 */
	public ZestAssignFieldValue(String variableName) {
		super(variableName);
	}

	/**
	 * Instantiates a new zest transform field replace.
	 *
	 * @param requestString the request string
	 * @param fieldDefinition the field definition
	 */
	public ZestAssignFieldValue(String variableName, ZestFieldDefinition fieldDefinition) {
		super(variableName);
		this.fieldDefinition = fieldDefinition;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestElement#deepCopy()
	 */
	@Override
	public ZestAssignFieldValue deepCopy() {
		return new ZestAssignFieldValue(this.getVariableName(), this.fieldDefinition.deepCopy());
	}

	/**
	 * Gets the field definition.
	 *
	 * @return the field definition
	 */
	public ZestFieldDefinition getFieldDefinition() {
		return fieldDefinition;
	}

	/**
	 * Sets the field definition.
	 *
	 * @param fieldDefinition the new field definition
	 */
	public void setFieldDefinition(ZestFieldDefinition fieldDefinition) {
		this.fieldDefinition = fieldDefinition;
	}

	@Override
	public String assign(ZestResponse response) throws ZestAssignFailException {
		Source src = new Source(response.getHeaders() + response.getBody());
		List<Element> formElements = src.getAllElements(HTMLElementName.FORM);

		if (formElements != null && fieldDefinition.getFormIndex() < formElements.size()) {
			Element form = formElements.get(fieldDefinition.getFormIndex());
			
			List<Element> inputElements = form.getAllElements(HTMLElementName.INPUT);
			for (Element inputElement : inputElements) {
				if (fieldDefinition.getFieldName().equals(inputElement.getAttributeValue("ID")) ||
						fieldDefinition.getFieldName().equals(inputElement.getAttributeValue("NAME"))) {
					// Got it
					return inputElement.getAttributeValue("VALUE");
				}
			}
		}
		
		return null;
	}
	
}
