/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;


/**
 * The Class ZestActionPrint.
 */
public class ZestActionInvoke extends ZestAction {
	
	/** The variable name that will be assigned to the script result. */
	private String variableName;

	/** The path to the script - can be relative or absolute. */
	private String script;
	
	private List<String[]> parameters = new ArrayList<String[]>();

	/**
	 * Instantiates a new zest action invoke.
	 */
	public ZestActionInvoke() {
		super();
	}

	/**
	 * Instantiates a new zest action invoke.
	 *
	 * @param index the index
	 */
	public ZestActionInvoke(int index) {
		super(index);
	}

	/**
	 * Instantiates a new zest action invoke.
	 *
	 * @param script the name of the script or process to invoke.
	 * @param variableName the name of the variable to assign the result of the invocation.
	 * @param parameters the parameters for the script or the process.
	 */
	public ZestActionInvoke(String script, String variableName, List<String[]> parameters) {
		super();
		this.script = script;
		this.variableName = variableName;
		this.parameters = parameters;
	}

	@Override
	public boolean isSameSubclass(ZestElement ze) {
		return ze instanceof ZestActionInvoke;
	}
	
	@Override
	public String invoke(ZestResponse response, ZestRuntime runtime) throws ZestActionFailException {
		File f = new File(this.script);
		if (! f.exists()) {
			throw new ZestActionFailException(this, "No such file: " + f.getAbsolutePath());
		}
		if (f.isDirectory()) {
			throw new ZestActionFailException(this, "Is a directory: " + f.getAbsolutePath());
		}
		
		// Check for scripts first - on Windows standard files often have execute perms
		String ext = null;
		if (this.script.indexOf(".") > 0) {
			ext = this.script.substring(this.script.lastIndexOf(".") + 1);
		}
		
		ScriptEngine engine = null;
		if (ext != null) {
			if (ext.equalsIgnoreCase("zest") || ext.equalsIgnoreCase("zst")) {
				// Its a Zest script
				engine = runtime.getScriptEngineFactory().getScriptEngine();
			} else {
				engine = new ScriptEngineManager().getEngineByName(ext);
			}
		}

		if (engine == null) {
			if (!f.canExecute()) {
				if (ext != null) {
					throw new ZestActionFailException(this, "Unknown script engine for extension: " + ext);
				}
				throw new ZestActionFailException(
						this,
						"Script is not executable and does not have an extension: " + f.getAbsolutePath());
			}

			// Its executable - just run directly
			try {
				StringBuilder sb = new StringBuilder();
				// Set up the parameters
				String [] cmdarray;
				if (this.parameters == null) {
					cmdarray = new String[1];
					cmdarray[0] = f.getAbsolutePath();
				} else {
					cmdarray = new String[this.parameters.size() + 1];
					cmdarray[0] = f.getAbsolutePath();
					int i=1;
					for (String[] kvPair : this.parameters) {
						cmdarray[i] = kvPair[0] + "=" + runtime.replaceVariablesInString(kvPair[1], false);
					}
				}
				Process p = new ProcessBuilder(cmdarray).redirectErrorStream(true).start();
				p.waitFor();

				// Capture anything written to stdout/err
	            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
	            String line = null;
	            while ((line = br.readLine()) != null) {
	            	sb.append(line);
	            	sb.append("\n");
				}
	            runtime.setVariable(variableName, sb.toString());
				return sb.toString();
				
			} catch (Exception e) {
				throw new ZestActionFailException(this, e);
			}
		}
		
		// Set the same writer so that output not lost
		engine.getContext().setWriter(runtime.getScriptEngineFactory().getScriptEngine().getContext().getWriter());
		
		try (FileReader fr = new FileReader(f)) {
			Bindings bindings = engine.createBindings();
			if (this.parameters != null) {
				for (String[] kvPair : this.parameters) {
					bindings.put(kvPair[0], runtime.replaceVariablesInString(kvPair[1], false));
				}
			}

			Object result =  engine.eval(fr, bindings);
			if (result == null) {
				return null;
			}

			runtime.setVariable(variableName, result.toString());
			return result.toString();
		} catch (Exception e) {
			throw new ZestActionFailException(this, e);
		}
	}
	
	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public List<String[]> getParameters() {
		return parameters;
	}

	public void setParameters(List<String[]> parameters) {
		this.parameters = parameters;
	}

	@Override
	public ZestActionInvoke deepCopy() {
		ZestActionInvoke copy = new ZestActionInvoke(this.getIndex());
		copy.script = script;
		copy.variableName = variableName;
		copy.parameters = new ArrayList<String[]>();
		for (String[] kvPair : this.parameters) {
			copy.parameters.add(new String[] {kvPair[0], kvPair[1]});
		}
		copy.setEnabled(this.isEnabled());
		return copy;
	}

	@Override
	public boolean isPassive() {
		return false;
	}
}
