/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/** The Class ZestExpressionURL. */
public class ZestExpressionURL extends ZestExpression {

    /** The include regexes. */
    private List<String> includeRegexes = new ArrayList<String>();

    /** The exclude regexes. */
    private List<String> excludeRegexes = new ArrayList<String>();

    /** The include patterns. */
    private transient List<Pattern> includePatterns = null;

    /** The exclude patterns. */
    private transient List<Pattern> excludePatterns = null;

    /** Instantiates a new zest expression url. */
    public ZestExpressionURL() {
        super();
    }

    /**
     * Instantiates a new zest expression url.
     *
     * @param includeRegexes the include regexes
     * @param excludeRegexes the exclude regexes
     */
    public ZestExpressionURL(List<String> includeRegexes, List<String> excludeRegexes) {
        this.setIncludeRegexes(includeRegexes);
        this.setExcludeRegexes(excludeRegexes);
    }

    @Override
    public boolean isTrue(ZestRuntime runtime) {
        ZestRequest req = runtime.getLastRequest();
        if (req == null) {
            return false;
        }
        String url = req.getUrl().toString();
        boolean inc = false;

        if (this.includePatterns == null) {
            this.initPatterns();
        }

        for (Pattern pattern : includePatterns) {
            if (pattern.matcher(url).find()) {
                inc = true;
                break;
            }
        }
        if (!inc) {
            // Not explicitly included
            return false;
        }
        for (Pattern pattern : excludePatterns) {
            if (pattern.matcher(url).find()) {
                // explicitly excluded
                return false;
            }
        }
        // Included and not excluded
        return true;
    }

    /**
     * Gets the include regexes.
     *
     * @return the include regexes
     */
    public List<String> getIncludeRegexes() {
        return includeRegexes;
    }

    /**
     * Gets the exclude regexes.
     *
     * @return the exclude regexes
     */
    public List<String> getExcludeRegexes() {
        return excludeRegexes;
    }

    private void initPatterns() {
        includePatterns = new ArrayList<Pattern>();
        excludePatterns = new ArrayList<Pattern>();

        if (includeRegexes != null) {
            for (String regex : includeRegexes) {
                this.includePatterns.add(Pattern.compile(regex));
            }
        }
        if (excludeRegexes != null) {
            for (String regex : excludeRegexes) {
                this.excludePatterns.add(Pattern.compile(regex));
            }
        }
    }

    /**
     * Sets the include regexes.
     *
     * @param includeRegexes the new include regexes
     */
    public void setIncludeRegexes(List<String> includeRegexes) {
        this.includeRegexes = includeRegexes;
        // Force the patterns to be regenerated
        this.includePatterns = null;
    }

    /**
     * Sets the exclude regexes.
     *
     * @param excludeRegexes the new exclude regexes
     */
    public void setExcludeRegexes(List<String> excludeRegexes) {
        this.excludeRegexes = excludeRegexes;
        // Force the patterns to be regenerated
        this.excludePatterns = null;
    }

    @Override
    public ZestExpressionURL deepCopy() {
        ZestExpressionURL copy = new ZestExpressionURL();
        List<String> copyIncludeRegex = new ArrayList<>(includeRegexes);
        List<String> copyExcludeRegex = new ArrayList<>(excludeRegexes);
        copy.setIncludeRegexes(copyIncludeRegex);
        copy.setExcludeRegexes(copyExcludeRegex);
        copy.setInverse(isInverse());
        return copy;
    }

    @Override
    public String toString() {
        StringBuilder expression = new StringBuilder(150);
        if (isInverse()) {
            expression.append("NOT ");
        }
        expression.append("URL: ACCEPT:");
        for (String s : includeRegexes) {
            expression.append(' ').append(s);
        }
        expression.append(", EXCLUDE:");
        for (String s : excludeRegexes) {
            expression.append(' ').append(s);
        }
        return expression.toString();
    }
}
