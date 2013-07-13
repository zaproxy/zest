/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package mozilla.zest.core.v1;

import org.mozilla.zest.core.v1.ZestExpressionAnd;
import org.mozilla.zest.core.v1.ZestExpressionLength;
import org.mozilla.zest.core.v1.ZestExpressionOr;
import org.mozilla.zest.core.v1.ZestExpressionResponseTime;
import org.mozilla.zest.core.v1.ZestExpressionStatusCode;
import org.mozilla.zest.impl.ZestPrinter;

public class TestPrint {
	public static void main(String[] args){
		ZestExpressionOr or=new ZestExpressionOr();
		or.addChildCondition(new ZestExpressionLength(10, 20));
		or.addChildCondition(new ZestExpressionStatusCode(200));
		ZestExpressionAnd and=new ZestExpressionAnd();
		and.addChildCondition(new ZestExpressionLength(100,200));
		and.addChildCondition(new ZestExpressionResponseTime(1000));
		and.addChildCondition(or);
		and.addChildCondition(and.deepCopy());
		ZestPrinter.printExpression(and, -1);
//		ZestPrinter.printExpression(or.deepCopy());
	}
}
