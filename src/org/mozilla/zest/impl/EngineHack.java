package org.mozilla.zest.impl;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;
import java.util.List;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.SimpleScriptContext;

import org.mozilla.zest.core.v1.ZestJSON;
import org.mozilla.zest.core.v1.ZestRequest;
import org.mozilla.zest.core.v1.ZestScript;

public class EngineHack {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ZestScriptEngineFactory zsef = new ZestScriptEngineFactory(); 

		ScriptEngineManager factory = new ScriptEngineManager();


		factory.registerEngineName(zsef.getEngineName(), zsef);
		
		ScriptEngine engine = factory.getEngineByName("Mozilla Zest");

		List<ScriptEngineFactory> factories = factory.getEngineFactories();
		
		for (ScriptEngineFactory f : factories) {
			System.out.println("Factory: " + f.getEngineName() + " lang: " + f.getLanguageName());
			System.out.println(" call  : " + f.getMethodCallSyntax("obj", "m", "args"));
			List<String> exts = f.getExtensions();
			if (exts != null) {
				for (String ext : exts) {
					
					System.out.println(" ext   : " + ext);
				}
			}
		}

				// ZestScriptingEngine
		
		System.out.println("Engine " + engine);
		
		ZestScript script = new ZestScript();
		ZestRequest req = new ZestRequest ();
		script.setType(ZestScript.Type.StandAlone);
		
		req.setUrl(new URL("http://localhost:8080/bodgeit/"));
		req.setMethod("GET");
		
		script.add(req);
		
		ScriptContext context = new SimpleScriptContext();;
		engine.setContext(context );
		
		engine.getContext().setWriter(new PrintWriter(System.out));
		engine.getContext().setErrorWriter(new PrintWriter(System.out));
		
		engine.eval(ZestJSON.toString(script));
		
		// System.out.println("Result: " + req.getResponse().getBody());
		System.out.println("Script");
		System.out.println(ZestJSON.toString(script));
		
		ZestBasicRunner zbr = new ZestBasicRunner();
		Reader reader = new FileReader(new File("/home/simon/dev/zest/parse.zst"));
		zbr.runScript(reader, null);
	}

}
