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
	 * @param message the message
	 */
	public ZestActionInvoke(String script, String variableName, List<String[]> parameters) {
		super();
		this.script = script;
		this.parameters = parameters;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestAction#isSameSubclass(org.mozilla.zest.core.v1.ZestElement)
	 */
	@Override
	public boolean isSameSubclass(ZestElement ze) {
		return ze instanceof ZestActionInvoke;
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestAction#invoke(org.mozilla.zest.core.v1.ZestResponse)
	 */
	public String invoke(ZestResponse response, ZestRuntime runtime) throws ZestActionFailException {
		File f = new File(this.script);
		if (! f.exists()) {
			throw new ZestActionFailException(this, "No such file: " + f.getAbsolutePath());
		}
		if (f.isDirectory()) {
			throw new ZestActionFailException(this, "Is a directory: " + f.getAbsolutePath());
		}
		
		if (f.canExecute()) {
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
						cmdarray[i] = kvPair[0] + "=" + kvPair[1];
					}
				}
				Process p = Runtime.getRuntime().exec(cmdarray);
				p.waitFor();

				// Capture anything written to strout
	            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
	            String line = null;
	            while ((line = br.readLine()) != null) {
	            	sb.append(line);
	            	sb.append("\n");
				}
				// Capture anything written to sterr
	            br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
	            line = null;
	            while ( (line = br.readLine()) != null) {
	            	sb.append(line);
	            	sb.append("\n");
				}
	            runtime.setVariable(variableName, sb.toString());
				return sb.toString();
				
			} catch (Exception e) {
				throw new ZestActionFailException(this, e);
			}
		}
		// Its not executable - try to find a suitable Java scripting engine
		String ext = null;
		if (this.script.indexOf(".") > 0) {
			ext = this.script.substring(this.script.lastIndexOf(".") + 1);
		}
		if (ext == null) {
			throw new ZestActionFailException(this, "Script doesnt not have an extension: " + f.getAbsolutePath());
		}
		
		ScriptEngine engine = new ScriptEngineManager().getEngineByName(ext);
		if (engine == null && (ext.equalsIgnoreCase("zest") || ext.equalsIgnoreCase("zst"))) {
			// Its a Zest script, handle as a special case if the engine if not registered
			engine = runtime.getScriptEngineFactory().getScriptEngine();
		}
		if (engine == null) {
			throw new ZestActionFailException(this, "Unknown script engine for extension: " + ext);
		}
		
		// Set the same writer so that output not lost
		engine.getContext().setWriter(runtime.getScriptEngineFactory().getScriptEngine().getContext().getWriter());
		
		try {
			Bindings bindings = engine.createBindings();
			if (this.parameters != null) {
				for (String[] kvPair : this.parameters) {
					bindings.put(kvPair[0], kvPair[1]);
				}
			}

			Object result =  engine.eval(new FileReader(f), bindings);
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

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#deepCopy()
	 */
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

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#isPassive()
	 */
	@Override
	public boolean isPassive() {
		return false;
	}
}
