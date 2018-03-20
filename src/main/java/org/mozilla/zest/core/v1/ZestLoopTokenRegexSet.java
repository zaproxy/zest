/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Class ZestLoopTokenRegexSet.<br>
 * This class does not override ZestLoopTokenStringSet because<br>
 * the tokens inside the converted set must not be transient
 */
public class ZestLoopTokenRegexSet extends ZestElement implements ZestLoopTokenSet<String> {

    private String inputVariableName = null;
    private String regex = null;
    private int groupIndex = 0;
    private boolean caseExact;

    /** The converted set. */
    private transient ZestLoopTokenStringSet convertedSet = null;

    private transient ZestLoopRegex loop;

    public ZestLoopTokenRegexSet() {
        super();
    }

    /**
     * Instantiates a new {@code ZestLoopTokenRegexSet}.
     *
     * @param loop the loop.
     * @param inputVariableName the name of the variable.
     * @param regex the regular expression.
     * @param group the group to get from the regular expression.
     * @param caseExact {@code true} if the match is case sensitive, {@code false} otherwise.
     */
    public ZestLoopTokenRegexSet(
            ZestLoopRegex loop,
            String inputVariableName,
            String regex,
            int group,
            boolean caseExact) {
        super();
        this.loop = loop;
        this.inputVariableName = inputVariableName;
        this.regex = regex;
        this.groupIndex = group;
        this.caseExact = caseExact;
    }

    /**
     * private method for initialization of the loop (TokenSet & first state).
     *
     * @return the zest loop token string set
     * @throws ZestClientFailException
     */
    protected ZestLoopTokenStringSet getConvertedSet() throws ZestClientFailException {
        if (this.convertedSet == null) {
            if (loop == null) {
                // Not yet initialized
                return null;
            }
            ZestLoopTokenStringSet set = new ZestLoopTokenStringSet();
            Pattern pattern;

            if (caseExact) {
                pattern = Pattern.compile(regex);
            } else {
                pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            }

            String str = this.loop.getRuntime().getVariable(inputVariableName);
            if (str != null) {
                Matcher matcher = pattern.matcher(str);

                while (matcher.find()) {
                    if (matcher.groupCount() >= groupIndex) {
                        String match = matcher.group(groupIndex);
                        if (match != null) {
                            set.addToken(match);
                        }
                    }
                }
            }

            this.convertedSet = set;
        }
        return convertedSet;
    }

    @Override
    public String getToken(int index) {
        try {
            return this.getConvertedSet().getToken(index);
        } catch (ZestClientFailException e) {
            return null;
        }
    }

    public List<String> getTokens() {
        try {
            return Collections.unmodifiableList(getConvertedSet().getTokens());
        } catch (ZestClientFailException e) {
            return null;
        }
    }

    @Override
    public int indexOf(String token) {
        try {
            return getConvertedSet().indexOf(token);
        } catch (ZestClientFailException e) {
            return -1;
        }
    }

    @Override
    public String getLastToken() {
        try {
            return getConvertedSet().getLastToken();
        } catch (ZestClientFailException e) {
            return null;
        }
    }

    @Override
    public int size() {
        try {
            return this.getConvertedSet().size();
        } catch (ZestClientFailException e) {
            return -1;
        }
    }

    @Override
    public ZestLoopTokenRegexSet deepCopy() {
        return new ZestLoopTokenRegexSet(loop, inputVariableName, regex, groupIndex, caseExact);
    }

    public String getInputVariableName() {
        return inputVariableName;
    }

    public String getRegex() {
        return regex;
    }

    public boolean isCaseExact() {
        return caseExact;
    }

    public ZestLoopRegex getLoop() {
        return loop;
    }

    public void setInputVariableName(String inputVariableName) {
        this.inputVariableName = inputVariableName;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public int getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(int groupIndex) {
        this.groupIndex = groupIndex;
    }

    public void setCaseExact(boolean caseExact) {
        this.caseExact = caseExact;
    }

    public void setLoop(ZestLoopRegex loop) {
        this.loop = loop;
    }

    public void setConvertedSet(ZestLoopTokenStringSet convertedSet) {
        this.convertedSet = convertedSet;
    }

    @Override
    public ZestLoopStateRegex getFirstState() {
        try {
            return new ZestLoopStateRegex(this);
        } catch (ZestClientFailException e) {
            return null;
        }
    }
}
