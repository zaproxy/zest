/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package mozilla.zest.corre.v1.testWithMain;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.mozilla.zest.core.v1.ZestJSON;
import org.mozilla.zest.core.v1.ZestScript;

public class DeserializeZestScript {
	public static void main(String[] args) throws FileNotFoundException{
		Scanner in=new Scanner(new File("/home/alessandro/Scrivania/parse.zst"));
		String toParse="";
		while(in.hasNextLine()){
			toParse+=in.nextLine()+"\n";
		}
		ZestScript script=(ZestScript)ZestJSON.fromString(toParse);
		System.out.println("OK");
//		ZestExpressionStatusCode code=new ZestExpressionStatusCode(200);
//		ZestExpressionLength length=new ZestExpressionLength(2096, 1);
//		ZestAssertion ass1=new ZestAssertion(length);
//		ZestAssertion ass2=new ZestAssertion(code);
//		String ass1String=ZestJSON.toString(ass1);
//		String ass2String=ZestJSON.toString(ass2);
//		String codeString=ZestJSON.toString(code);
//		ZestStructuredExpression and=new ZestExpressionAnd();
//		and.addChildCondition(length);
//		and.addChildCondition(code);
//		String andString=ZestJSON.toString(and);
//		ZestStructuredExpression andCOPY=(ZestStructuredExpression) ZestJSON.fromString(andString);
//		ZestConditional cond=new ZestConditional();
//		String condString=ZestJSON.toString(cond);
//		ZestConditional condCOPY=(ZestConditional)ZestJSON.fromString(condString);
//		ZestElement codeCOPY= ZestJSON.fromString(codeString);
//		ZestElement ass1COPY= ZestJSON.fromString(ass1String);
//		ZestElement ass2COPY=(ZestElement) ZestJSON.fromString(ass2String);
//		String ass1StringCOPY=ZestJSON.toString(ass1COPY);
//		String ass2StringCOPY=ZestJSON.toString(ass2COPY);
//		System.out.println("ASS2: "+ass2String.equals(ass2StringCOPY));
//		System.out.println("ASS1: "+ass1String.equals(ass1StringCOPY));
	}
}
