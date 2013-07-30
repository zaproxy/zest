/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;


// TODO: Auto-generated Javadoc
/**
 * The Class ZestTransformFieldReplace.
 */
public class ZestTransformFieldReplace extends ZestTransformation implements ZestRequestRef {

	/** The request string. */
	private String requestString;
	
	/** The field definition. */
	private ZestFieldDefinition fieldDefinition; 
	
	/**
	 * Instantiates a new zest transform field replace.
	 */
	public ZestTransformFieldReplace() {
	}

	/**
	 * Instantiates a new zest transform field replace.
	 *
	 * @param requestString the request string
	 */
	public ZestTransformFieldReplace(String requestString) {
		super();
		this.requestString = requestString;
	}

	/**
	 * Instantiates a new zest transform field replace.
	 *
	 * @param requestString the request string
	 * @param fieldDefinition the field definition
	 */
	public ZestTransformFieldReplace(String requestString, ZestFieldDefinition fieldDefinition) {
		super();
		this.requestString = requestString;
		this.fieldDefinition = fieldDefinition;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestTransformation#transform(org.mozilla.zest.core.v1.ZestRunner, org.mozilla.zest.core.v1.ZestRequest)
	 */
	@Override
	public void transform (ZestRunner runner, ZestRequest request) throws ZestTransformFailException {
		String replaceValue = runner.getReplacementValue(this.fieldDefinition);
		if (replaceValue == null) {
			throw new ZestTransformFailException(this, "No replace value supplied");
		}
		request.setData(request.getData().replace(this.requestString, replaceValue));
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestElement#deepCopy()
	 */
	@Override
	public ZestTransformFieldReplace deepCopy() {
		return new ZestTransformFieldReplace(this.requestString, this.fieldDefinition.deepCopy());
	}

	/**
	 * Gets the request string.
	 *
	 * @return the request string
	 */
	public String getRequestString() {
		return requestString;
	}

	/**
	 * Sets the request string.
	 *
	 * @param requestString the new request string
	 */
	public void setRequestString(String requestString) {
		this.requestString = requestString;
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

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestRequestRef#getRequest()
	 */
	@Override
	public ZestRequest getRequest() {
		if (fieldDefinition != null) {
			return fieldDefinition.getRequest();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestRequestRef#getRequestId()
	 */
	@Override
	public int getRequestId() {
		if (fieldDefinition != null) {
			return fieldDefinition.getRequestId();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestRequestRef#setRequest(org.mozilla.zest.core.v1.ZestRequest)
	 */
	@Override
	public void setRequest(ZestRequest request) {
		if (fieldDefinition != null) {
			fieldDefinition.setRequest(request);
		}
	}
	
}
