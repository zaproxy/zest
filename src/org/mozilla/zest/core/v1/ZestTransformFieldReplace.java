/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;


public class ZestTransformFieldReplace extends ZestTransformation implements ZestRequestRef {

	private String requestString;
	private ZestFieldDefinition fieldDefinition; 
	
	public ZestTransformFieldReplace() {
	}

	public ZestTransformFieldReplace(String requestString) {
		super();
		this.requestString = requestString;
	}

	public ZestTransformFieldReplace(String requestString, ZestFieldDefinition fieldDefinition) {
		super();
		this.requestString = requestString;
		this.fieldDefinition = fieldDefinition;
	}

	@Override
	public void transform (ZestRunner runner, ZestRequest request) throws ZestTransformFailException {
		String replaceValue = runner.getReplacementValue(this.fieldDefinition);
		if (replaceValue == null) {
			throw new ZestTransformFailException(this, "No replace value supplied");
		}
		request.setData(request.getData().replace(this.requestString, replaceValue));
	}

	@Override
	public ZestTransformFieldReplace deepCopy() {
		return new ZestTransformFieldReplace(this.requestString, this.fieldDefinition.deepCopy());
	}

	public String getRequestString() {
		return requestString;
	}

	public void setRequestString(String requestString) {
		this.requestString = requestString;
	}

	public ZestFieldDefinition getFieldDefinition() {
		return fieldDefinition;
	}

	public void setFieldDefinition(ZestFieldDefinition fieldDefinition) {
		this.fieldDefinition = fieldDefinition;
	}

	@Override
	public ZestRequest getRequest() {
		if (fieldDefinition != null) {
			return fieldDefinition.getRequest();
		}
		return null;
	}

	@Override
	public int getRequestId() {
		if (fieldDefinition != null) {
			return fieldDefinition.getRequestId();
		}
		return 0;
	}

	@Override
	public void setRequest(ZestRequest request) {
		if (fieldDefinition != null) {
			fieldDefinition.setRequest(request);
		}
	}
	
}
