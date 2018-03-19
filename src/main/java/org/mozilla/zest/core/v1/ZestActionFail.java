/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

// TODO: Auto-generated Javadoc
/** The Class ZestActionFail. */
public class ZestActionFail extends ZestAction {

    /** The Enum Priority. */
    public enum Priority {
        INFO,
        LOW,
        MEDIUM,
        HIGH
    };

    /** The message. */
    private String message;

    /** The priority. */
    private String priority;

    /** Instantiates a new zest action fail. */
    public ZestActionFail() {
        super();
    }

    /**
     * Instantiates a new zest action fail.
     *
     * @param index the index
     */
    public ZestActionFail(int index) {
        super(index);
    }

    /**
     * Instantiates a new zest action fail.
     *
     * @param message the message
     */
    public ZestActionFail(String message) {
        super();
        this.message = message;
    }

    /**
     * Instantiates a new zest action fail.
     *
     * @param message the message
     * @param priority the priority
     */
    public ZestActionFail(String message, String priority) {
        super();
        this.message = message;
        this.setPriority(priority);
    }

    /**
     * Instantiates a new zest action fail.
     *
     * @param message the message
     * @param priority the priority
     */
    public ZestActionFail(String message, Priority priority) {
        super();
        this.message = message;
        this.setPriority(priority);
    }

    @Override
    public boolean isSameSubclass(ZestElement ze) {
        return ze instanceof ZestActionFail;
    }

    @Override
    public String invoke(ZestResponse response, ZestRuntime runtime)
            throws ZestActionFailException {
        throw new ZestActionFailException(
                this, runtime.replaceVariablesInString(this.message, false));
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message.
     *
     * @param message the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the priority.
     *
     * @return the priority
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Sets the priority.
     *
     * @param priority the new priority
     */
    public void setPriority(Priority priority) {
        this.priority = priority.name();
    }

    /**
     * Sets the priority.
     *
     * @param priority the new priority
     */
    public void setPriority(String priority) {
        try {
            Priority.valueOf(priority);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unsupported priority: " + priority);
        }
        this.priority = priority;
    }

    @Override
    public ZestActionFail deepCopy() {
        ZestActionFail copy = new ZestActionFail(this.getIndex());
        copy.message = message;
        copy.priority = priority;
        copy.setEnabled(this.isEnabled());
        return copy;
    }

    @Override
    public boolean isPassive() {
        return true;
    }
}
