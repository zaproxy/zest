/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

/** The Class ZestLoopTokenIntegerSet. */
public class ZestLoopTokenIntegerSet extends ZestElement implements ZestLoopTokenSet<Integer> {

    /** The start. */
    private int start = 0;

    /** The end. */
    private int end = 0;

    /** the step */
    private int step = 1;

    /**
     * Instantiates a new zest loop token integer set with default values:<br>
     * start=end=0. Note that this is a final state!
     */
    public ZestLoopTokenIntegerSet() {
        super();
        this.start = 0;
        this.end = 0;
    }
    /**
     * Instantiates a new zest loop token integer set.
     *
     * @param indexStart the index start
     * @param indexEnd the index end
     */
    public ZestLoopTokenIntegerSet(int indexStart, int indexEnd) {
        super();
        this.start = indexStart;
        this.end = indexEnd;
    }

    /**
     * Gets the start.
     *
     * @return the start
     */
    public int getStart() {
        return start;
    }

    /**
     * Gets the end.
     *
     * @return the end
     */
    public int getEnd() {
        return end;
    }

    @Override
    public ZestLoopTokenIntegerSet deepCopy() {
        return new ZestLoopTokenIntegerSet(this.start, this.end);
    }

    @Override
    public Integer getToken(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("the index must be non negative.");
        }
        if (start + index < end) { // restrictive
            return (start + index);
        }
        throw new IllegalArgumentException("the index given is not inside this set.");
    }

    @Override
    public int indexOf(Integer token) {
        int tokenValue = token;
        int index = tokenValue - start;
        if (index < 0 || index >= end - start) {
            throw new IllegalArgumentException("token not contained in this set.");
        }
        return index;
    }

    @Override
    public Integer getLastToken() {
        return end - 1; // restrictive
    }

    @Override
    public int size() {
        return end - start;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + end;
        result = prime * result + start;
        result = prime * result + step;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ZestLoopTokenIntegerSet other = (ZestLoopTokenIntegerSet) obj;
        if (end != other.end) {
            return false;
        }
        if (start != other.start) {
            return false;
        }
        if (step != other.step) {
            return false;
        }
        return true;
    }

    @Override
    public ZestLoopStateInteger getFirstState() {
        ZestLoopStateInteger fisrtState = new ZestLoopStateInteger(this);
        return fisrtState;
    }

    protected void setStep(int newStep) {
        this.step = newStep;
    }

    protected int getStep() {
        return this.step;
    }
}
