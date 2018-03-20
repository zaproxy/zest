/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.impl;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import org.mozilla.zest.core.v1.ZestRunner;

public class ZestScriptEngine extends AbstractScriptEngine {

    private ZestScriptEngineFactory factory;
    private ZestRunner runner;

    public ZestScriptEngine(ZestScriptEngineFactory factory, ZestRunner runner) {
        this.factory = factory;
        this.runner = runner;
    }

    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
        Map<String, String> params = new HashMap<String, String>();

        if (context != null) {
            this.runner.setOutputWriter(context.getWriter());

            Bindings bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);

            if (bindings != null) {
                for (Entry<String, Object> entry : bindings.entrySet()) {
                    params.put(entry.getKey(), entry.getValue().toString());
                }
            }
        }

        try {
            return runner.runScript(script, params);
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
        Map<String, String> params = new HashMap<String, String>();

        if (context != null) {
            this.runner.setOutputWriter(context.getWriter());
            Bindings bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);

            if (bindings != null) {
                for (Entry<String, Object> entry : bindings.entrySet()) {
                    params.put(entry.getKey(), entry.getValue().toString());
                }
            }
        }

        try {
            return runner.runScript(reader, params);
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

    @Override
    public Bindings createBindings() {
        return new SimpleBindings();
    }

    @Override
    public ScriptEngineFactory getFactory() {
        return factory;
    }
}
