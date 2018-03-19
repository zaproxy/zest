/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mozilla.zest.core.v1.ZestAuthentication;
import org.mozilla.zest.core.v1.ZestHttpAuthentication;
import org.mozilla.zest.core.v1.ZestJSON;
import org.mozilla.zest.core.v1.ZestScript;

public class CmdLine {

    private static final String USAGE =
            "Usage: -script <file> [-summary | -list] [-debug] [-prefix <http://prefix>] [-token <name>=<value>]...\n"
                    + "    [-http-auth-site <site> -http-auth-realm <realm> -http-auth-user <user> -http-auth-password <password>] \n"
                    + "    For more information about Zest visit "
                    + ZestScript.ZEST_URL;

    private enum Mode {
        run,
        summary,
        list
    }
    /** @param args */
    public static void main(String[] args) {
        /*
         * -script file
         * -summary
         * -list
         * -prefix = ppp
         * -token xxx=yyy
         */
        File script = null;
        Mode mode = null;
        String prefix = null;
        Map<String, String> tokens = new HashMap<String, String>();
        String httpAuthSite = null;
        String httpAuthRealm = null;
        String httpAuthUser = null;
        String httpAuthPassword = null;
        boolean debug = false;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-summary")) {
                if (mode != null) {
                    error(USAGE);
                    return;
                }
                mode = Mode.summary;
            } else if (args[i].equals("-list")) {
                if (mode != null) {
                    error(USAGE);
                    return;
                }
                mode = Mode.list;
            } else if (args[i].equals("-debug")) {
                debug = true;
            } else if (args[i].equals("-prefix")) {
                if (i >= args.length - 1) {
                    error(USAGE);
                    return;
                }
                i++;
                prefix = args[i];
            } else if (args[i].equals("-script")) {
                if (script != null || i >= args.length - 1) {
                    error(USAGE);
                    return;
                }
                i++;
                script = new File(args[i]);
            } else if (args[i].equals("-token")) {
                if (i >= args.length - 1 || args[i + 1].indexOf("=") < 0) {
                    error(USAGE);
                    return;
                }
                i++;
                int eqInd = args[i].indexOf("=");
                tokens.put(args[i].substring(0, eqInd), args[i].substring(eqInd + 1));
            } else if (args[i].equals("-http-auth-site")) {
                if (i >= args.length - 1) {
                    error(USAGE);
                    return;
                }
                i++;
                httpAuthSite = args[i];
            } else if (args[i].equals("-http-auth-realm")) {
                if (i >= args.length - 1) {
                    error(USAGE);
                    return;
                }
                i++;
                httpAuthRealm = args[i];
            } else if (args[i].equals("-http-auth-user")) {
                if (i >= args.length - 1) {
                    error(USAGE);
                    return;
                }
                i++;
                httpAuthUser = args[i];
            } else if (args[i].equals("-http-auth-password")) {
                if (i >= args.length - 1) {
                    error(USAGE);
                    return;
                }
                i++;
                httpAuthPassword = args[i];
            } else {
                error("Parameter not recognised: " + args[i]);
                error(USAGE);
                return;
            }
        }

        if (script == null) {
            error(USAGE);
            return;
        } else if (!script.exists()) {
            error("Script " + script.getAbsolutePath() + " does not exist");
            return;
        }

        StringBuilder sb;
        try (BufferedReader fr = new BufferedReader(new FileReader(script))) {
            sb = new StringBuilder();
            String line;
            while ((line = fr.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            error("Error reading file " + script.getAbsolutePath() + ": " + e);
            return;
        }

        ZestScript zs;
        try {
            zs = (ZestScript) ZestJSON.fromString(sb.toString());

            if (!ZestScript.VERSION.equals(zs.getZestVersion())) {
                error("Error Zest version " + zs.getZestVersion() + " not supported");
                return;
            }

        } catch (Exception e) {
            error("Error loading script " + script.getAbsolutePath() + ": " + e);
            return;
        }

        if (mode == null) {
            mode = Mode.run;
        }

        switch (mode) {
            case summary:
                ZestPrinter.summary(zs);
                break;
            case list:
                ZestPrinter.list(zs);
                break;
            default:
                if (prefix != null) {
                    try {
                        zs.setPrefix(zs.getPrefix(), prefix);
                    } catch (MalformedURLException e) {
                        error("Invalid prefix: " + e);
                        return;
                    }
                }

                if (httpAuthSite != null) {
                    List<ZestAuthentication> authList = new ArrayList<ZestAuthentication>();
                    authList.add(
                            new ZestHttpAuthentication(
                                    httpAuthSite, httpAuthRealm, httpAuthUser, httpAuthPassword));
                    zs.setAuthentication(authList);
                }

                run(zs, tokens, debug);
                break;
        }
    }

    private static void error(String str) {
        System.err.println(str);
    }

    private static void run(ZestScript zs, Map<String, String> parameters, boolean debug) {
        ZestBasicRunner zbr = new ZestBasicRunner();
        zbr.setOutputWriter(new OutputStreamWriter(System.out));
        zbr.setStopOnAssertFail(false);
        zbr.setDebug(debug);
        try {
            zbr.run(zs, parameters);
        } catch (Exception e) {
            System.out.println("Error running script: " + e);
            e.printStackTrace();
        }
    }
}
