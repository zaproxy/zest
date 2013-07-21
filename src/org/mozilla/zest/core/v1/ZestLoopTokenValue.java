/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;

public class ZestLoopTokenValue extends ZestLoopToken<String> {
	private static int counter=0;//don't like this
	public ZestLoopTokenValue(String value) {
		super(value, ++counter);
	}

//	@Override
//	public int compareTo(ZestLoopToken<String> otherToken) {
//		return 0;//TODO really needed?
//	}

	@Override
	public ZestLoopTokenValue deepCopy() {
		return new ZestLoopTokenValue(this.getValue());
	}

}
