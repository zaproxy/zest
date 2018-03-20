/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

// TODO: Auto-generated Javadoc
/** The Class ZestFieldDefinition. */
public class ZestFieldDefinition extends ZestElement {

    /** The form index. */
    private int formIndex;

    /** The form name. */
    private String formName;

    /** The field name. */
    private String fieldName;

    /** The request. */
    // private transient ZestRequest request = null;

    /** Instantiates a new zest field definition. */
    public ZestFieldDefinition() {
        super();
    }

    /**
     * Instantiates a new zest field definition.
     *
     * @param formIndex the form index
     * @param fieldName the field name
     */
    public ZestFieldDefinition(int formIndex, String fieldName) {
        super();
        this.formIndex = formIndex;
        this.fieldName = fieldName;
    }

    /**
     * Instantiates a new zest field definition.
     *
     * @param formName the form name
     * @param fieldName the field name
     */
    public ZestFieldDefinition(String formName, String fieldName) {
        super();
        this.formName = formName;
        this.fieldName = fieldName;
    }

    @Override
    public ZestFieldDefinition deepCopy() {
        ZestFieldDefinition ze = new ZestFieldDefinition(this.formName, this.fieldName);
        ze.setFormIndex(this.getFormIndex());
        return ze;
    }

    /**
     * Gets the form index.
     *
     * @return the form index
     */
    public int getFormIndex() {
        return formIndex;
    }

    /**
     * Sets the form index.
     *
     * @param formIndex the new form index
     */
    public void setFormIndex(int formIndex) {
        this.formIndex = formIndex;
    }

    /**
     * Gets the form name.
     *
     * @return the form name
     */
    public String getFormName() {
        return formName;
    }

    /**
     * Sets the form name.
     *
     * @param formName the new form name
     */
    public void setFormName(String formName) {
        this.formName = formName;
    }

    /**
     * Gets the field name.
     *
     * @return the field name
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Sets the field name.
     *
     * @param fieldName the new field name
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Gets the key.
     *
     * @return the key
     */
    public String getKey() {
        if (this.formName != null) {
            return this.getFormName() + ":" + this.getFieldName();
        } else {
            return this.getFormIndex() + ":" + this.getFieldName();
        }
    }
}
