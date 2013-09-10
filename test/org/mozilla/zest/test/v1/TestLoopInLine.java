/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.test.v1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.mozilla.zest.core.v1.ZestActionFailException;
import org.mozilla.zest.core.v1.ZestAssertFailException;
import org.mozilla.zest.core.v1.ZestAssignFailException;
import org.mozilla.zest.core.v1.ZestInvalidCommonTestException;
import org.mozilla.zest.core.v1.ZestJSON;
import org.mozilla.zest.core.v1.ZestScript;
import org.mozilla.zest.impl.ZestBasicRunner;

public class TestLoopInLine {
	public static void main(String[] args) throws ZestAssertFailException,
			ZestActionFailException, IOException,
			ZestInvalidCommonTestException, ZestAssignFailException, InterruptedException {
		Scanner in=new Scanner(new File("/home/alessandro/Scrivania/toParse.txt"));
		String toParse="";
		while(in.hasNextLine()){
			toParse+=in.nextLine()+"\n";
		}
		in.close();
		ZestScript script=(ZestScript) ZestJSON.fromString(toParse);
		ZestBasicRunner runner=new ZestBasicRunner();
		FileWriter out=new FileWriter(new File("/home/alessandro/Scrivania/output.txt"));
		runner.setOutputWriter(out);
		runner.run(script);
		out.close();
		for(String variable:script.getVariableNames()){
			System.out.println(variable);
		}
	}
}
