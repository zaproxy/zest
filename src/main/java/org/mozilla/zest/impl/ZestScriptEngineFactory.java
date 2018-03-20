/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import org.mozilla.zest.core.v1.ZestRunner;
import org.mozilla.zest.core.v1.ZestScript;

public class ZestScriptEngineFactory implements ScriptEngineFactory {

    public static final String NAME = "Mozilla Zest";

    private ZestRunner runner = new ZestBasicRunner(this);

    @Override
    public String getEngineName() {
        return NAME;
    }

    @Override
    public String getEngineVersion() {
        return "1.0";
    }

    @Override
    public String getLanguageName() {
        return "Zest";
    }

    @Override
    public String getLanguageVersion() {
        return ZestScript.VERSION;
    }

    @Override
    public List<String> getExtensions() {
        List<String> list = new ArrayList<String>();
        list.add("zest");
        list.add("zst");
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<String> getMimeTypes() {
        // dont support any yet
        return null;
    }

    @Override
    public List<String> getNames() {
        List<String> list = new ArrayList<String>();
        list.add("Zest");
        return Collections.unmodifiableList(list);
    }

    @Override
    public Object getParameter(String key) {
        if (key.equals(ScriptEngine.ENGINE)) {
            return getEngineName();
        } else if (key.equals(ScriptEngine.ENGINE_VERSION)) {
            return getEngineVersion();
        } else if (key.equals(ScriptEngine.NAME)) {
            return getNames().get(0);
        } else if (key.equals(ScriptEngine.LANGUAGE)) {
            return getLanguageName();
        } else if (key.equals(ScriptEngine.LANGUAGE_VERSION)) {
            return getLanguageVersion();
        } else if (key.equals("THREADING")) {
            return null; // Until thoroughly tested.
        } else {
            return null;
        }
    }

    @Override
    public String getMethodCallSyntax(String obj, String m, String... args) {
        // Not supported yet
        return null;
    }

    @Override
    public String getOutputStatement(String toDisplay) {
        // Not supported yet
        return null;
    }

    @Override
    public String getProgram(String... statements) {
        // Not supported yet
        return null;
    }

    @Override
    public ScriptEngine getScriptEngine() {
        return new ZestScriptEngine(this, runner);
    }

    public void setRunner(ZestRunner runner) {
        this.runner = runner;
        this.runner.setScriptEngineFactory(this);
    }
}
