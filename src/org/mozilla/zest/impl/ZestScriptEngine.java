/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.impl;

import java.io.Reader;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;

import org.mozilla.zest.core.v1.ZestRunner;

public class ZestScriptEngine extends AbstractScriptEngine {

	private ZestRunner runner;
	
	public ZestScriptEngine (ZestRunner runner) {
		this.runner = runner;
	}
	
	@Override
	public Object eval(String script, ScriptContext context)
			throws ScriptException {
		if (context != null) {
			this.runner.setOutputWriter(context.getWriter());
		}
		
		try {
			runner.runScript(script);
		} catch (Exception e) {
			throw new ScriptException(e);
		}
		// Dont support returning objects yet
		return null;
	}

	@Override
	public Object eval(Reader reader, ScriptContext context)
			throws ScriptException {
		
		if (context != null) {
			this.runner.setOutputWriter(context.getWriter());
		}
		
		try {
			runner.runScript(reader);
		} catch (Exception e) {
			throw new ScriptException(e);
		}
		// Dont support returning objects yet
		return null;
	}

	@Override
	public Bindings createBindings() {
		// Not supported yet
		return null;
	}

	@Override
	public ScriptEngineFactory getFactory() {
		return new ZestScriptEngineFactory();
	}
}
