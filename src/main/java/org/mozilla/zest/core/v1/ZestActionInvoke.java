/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/** The Class ZestActionPrint. */
public class ZestActionInvoke extends ZestAction {

    /** The variable name that will be assigned to the script result. */
    private String variableName;

    /** The path to the script - can be relative or absolute. */
    private String script;

    private List<String[]> parameters = new ArrayList<String[]>();

    /**
     * The name of the charset used for read operations (for example, read the script or the output
     * of the invoked program).
     *
     * <p>Might be {@code null}, in which case it is used the default charset of the JVM.
     *
     * @see #getCharsetImpl()
     */
    private String charset;

    /** Instantiates a new zest action invoke. */
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
        this(script, variableName, parameters, null);
    }

    /**
     * Constructs a {@code ZestActionInvoke} with the given data.
     *
     * @param script the name of the script or process to invoke.
     * @param variableName the name of the variable to assign the result of the invocation.
     * @param parameters the parameters for the script or the process.
     * @param charset the name of the charset used for read operations (for example, read the script
     *     or the output of the invoked program).
     * @since 0.14
     */
    public ZestActionInvoke(
            String script, String variableName, List<String[]> parameters, String charset) {
        super();
        this.script = script;
        this.variableName = variableName;
        this.parameters = parameters;
        this.charset = charset;
    }

    @Override
    public boolean isSameSubclass(ZestElement ze) {
        return ze instanceof ZestActionInvoke;
    }

    @Override
    public String invoke(ZestResponse response, ZestRuntime runtime)
            throws ZestActionFailException {
        File f = new File(this.script);
        if (!f.exists()) {
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
                    throw new ZestActionFailException(
                            this, "Unknown script engine for extension: " + ext);
                }
                throw new ZestActionFailException(
                        this,
                        "Script is not executable and does not have an extension: "
                                + f.getAbsolutePath());
            }

            // Its executable - just run directly
            try {
                StringBuilder sb = new StringBuilder();
                // Set up the parameters
                String[] cmdarray;
                if (this.parameters == null) {
                    cmdarray = new String[1];
                    cmdarray[0] = f.getAbsolutePath();
                } else {
                    cmdarray = new String[this.parameters.size() + 1];
                    cmdarray[0] = f.getAbsolutePath();
                    int i = 1;
                    for (String[] kvPair : this.parameters) {
                        cmdarray[i] =
                                kvPair[0]
                                        + "="
                                        + runtime.replaceVariablesInString(kvPair[1], false);
                    }
                }
                Process p = new ProcessBuilder(cmdarray).redirectErrorStream(true).start();
                p.waitFor();

                // Capture anything written to stdout/err
                BufferedReader br =
                        new BufferedReader(
                                new InputStreamReader(p.getInputStream(), getCharsetImpl()));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
                String result = sb.toString();
                runtime.setVariable(variableName, result);
                return result;

            } catch (Exception e) {
                throw new ZestActionFailException(this, e);
            }
        }

        // Set the same writer so that output not lost
        engine.getContext()
                .setWriter(
                        runtime.getScriptEngineFactory()
                                .getScriptEngine()
                                .getContext()
                                .getWriter());

        try (Reader reader = Files.newBufferedReader(f.toPath(), getCharsetImpl())) {
            Bindings bindings = engine.createBindings();
            if (this.parameters != null) {
                for (String[] kvPair : this.parameters) {
                    bindings.put(kvPair[0], runtime.replaceVariablesInString(kvPair[1], false));
                }
            }

            Object result = engine.eval(reader, bindings);
            if (result == null) {
                return null;
            }

            String resultStr = result.toString();
            runtime.setVariable(variableName, resultStr);
            return resultStr;
        } catch (Exception e) {
            throw new ZestActionFailException(this, e);
        }
    }

    private Charset getCharsetImpl() {
        if (charset == null || charset.isEmpty()) {
            return Charset.defaultCharset();
        }
        return Charset.forName(charset);
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

    /**
     * Gets the name of the charset used for read operations (for example, read the script from the
     * file or the output of the invoked program).
     *
     * @return the name of the charset.
     * @since 0.14
     */
    public String getCharset() {
        return charset;
    }

    /**
     * Sets the name of the charset used for read operations (for example, read the script from the
     * file or the output of the invoked program).
     *
     * @param charset the name of the charset.
     * @since 0.14
     */
    public void setCharset(String charset) {
        this.charset = charset;
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
        copy.setCharset(getCharset());
        return copy;
    }

    @Override
    public boolean isPassive() {
        return false;
    }
}
