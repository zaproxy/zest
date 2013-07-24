/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package tries;

import org.mozilla.zest.core.v1.ZestJSON;
import org.mozilla.zest.core.v1.ZestLoopInteger;
import org.mozilla.zest.core.v1.ZestLoopState;
import org.mozilla.zest.core.v1.ZestLoopToken;
import org.mozilla.zest.core.v1.ZestLoopTokenSet;

public class TryJSONLoops {

	int[] valuesI={1,2,3,4,5,6,7,8,9,10,9,7,5,6,4,3,3};
	String[] valuesS=new String[valuesI.length];
	{
		for(int i=0; i<valuesI.length; i++){
			valuesS[i]=""+valuesI[i];
		}
	}
	public static void main(String[] args) {
		ZestLoopInteger loopInteger;
	}
}
