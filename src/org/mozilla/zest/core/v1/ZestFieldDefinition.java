/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

public class ZestFieldDefinition extends ZestElement implements ZestRequestRef {

	private int requestId;
	private int formIndex;
	private String formName;
	private String fieldName;
	
	private transient ZestRequest request = null;

	public ZestFieldDefinition () {
		super();
	}
	
	public ZestFieldDefinition(ZestRequest request, int formIndex, String fieldName) {
		super();
		this.setRequest(request);
		this.formIndex = formIndex;
		this.fieldName = fieldName;
	}
	
	public ZestFieldDefinition(ZestRequest request, String formName, String fieldName) {
		super();
		this.setRequest(request);
		this.formName = formName;
		this.fieldName = fieldName;
	}
	
	private ZestFieldDefinition(int requestId, String formName, String fieldName) {
		super();
		this.requestId = requestId;
		this.formName = formName;
		this.fieldName = fieldName;
	}
	
	public ZestFieldDefinition deepCopy() {
		ZestFieldDefinition ze = new ZestFieldDefinition(this.requestId, this.formName, this.fieldName);
		ze.setFormIndex(this.getFormIndex());
		return ze;
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public int getFormIndex() {
		return formIndex;
	}

	public void setFormIndex(int formIndex) {
		this.formIndex = formIndex;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public String getKey() {
		if (this.formName != null) {
			return this.getRequestId() + ":" + this.getFormName() + ":" + this.getFieldName();
		} else {
			return this.getRequestId() + ":" + this.getFormIndex() + ":" + this.getFieldName();
		}
	}

	@Override
	public ZestRequest getRequest() {
		return this.request;
	}

	@Override
	public void setRequest(ZestRequest request) {
		this.request = request;
		if (request != null) {
			this.request.addReferer(this);
			this.requestId = request.getIndex();
		}
	}

}
