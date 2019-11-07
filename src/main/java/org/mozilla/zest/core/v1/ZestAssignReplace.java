/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.util.regex.Pattern;

/**
 * The Class ZestAssignString assigns a string (which can include other variables) to the specified
 * variable.
 */
public class ZestAssignReplace extends ZestAssignment {

    private String replace = null;
    private String replacement = null;
    private boolean regex = false;
    private boolean caseExact = false;

    /** Instantiates a new {@code ZestAssignReplace}. */
    public ZestAssignReplace() {}

    /**
     * Instantiates a new {@code ZestAssignReplace}.
     *
     * @param variableName the variable name
     */
    public ZestAssignReplace(String variableName) {
        super(variableName);
    }

    /**
     * Instantiates a new {@code ZestAssignReplace}.
     *
     * @param variableName the name of the variable where to do the replacement.
     * @param replace what to replace.
     * @param replacement the replacement.
     * @param regex {@code true} if {@code replace} is a regular expression, {@code false}
     *     otherwise.
     * @param caseExact {@code true} if the replace match is case sensitive, {@code false}
     *     otherwise.
     */
    public ZestAssignReplace(
            String variableName,
            String replace,
            String replacement,
            boolean regex,
            boolean caseExact) {
        super(variableName);
        this.replace = replace;
        this.replacement = replacement;
        this.regex = regex;
        this.caseExact = caseExact;
    }

    @Override
    public String assign(ZestResponse response, ZestRuntime runtime)
            throws ZestAssignFailException {
        String var = runtime.getVariable(getVariableName());
        if (var == null) {
            return null;
        }
        String orig = runtime.replaceVariablesInString(var, false);
        try {
            return createPattern().matcher(orig).replaceAll(replacement);
        } catch (Exception e) {
            throw new ZestAssignFailException(this, e.getMessage());
        }
    }

    private Pattern createPattern() {
        return Pattern.compile(
                regex ? replace : Pattern.quote(replace), caseExact ? 0 : Pattern.CASE_INSENSITIVE);
    }

    @Override
    public ZestAssignReplace deepCopy() {
        ZestAssignReplace copy =
                new ZestAssignReplace(
                        this.getVariableName(),
                        this.replace,
                        this.replacement,
                        this.regex,
                        this.caseExact);
        copy.setEnabled(this.isEnabled());
        return copy;
    }

    public String getReplace() {
        return replace;
    }

    public void setReplace(String replace) {
        this.replace = replace;
    }

    public String getReplacement() {
        return replacement;
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    public boolean isRegex() {
        return regex;
    }

    public void setRegex(boolean regex) {
        this.regex = regex;
    }

    public boolean isCaseExact() {
        return caseExact;
    }

    public void setCaseExact(boolean caseExact) {
        this.caseExact = caseExact;
    }
}
