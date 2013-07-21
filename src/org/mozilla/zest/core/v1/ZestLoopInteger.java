/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;

import java.util.List;

public class ZestLoopInteger extends ZestLoop<Integer> {
	public ZestLoopInteger(){
		super();
	}
	public ZestLoopInteger(ZestLoopState<Integer> initializationState){
		super(initializationState);
	}
	public ZestLoopInteger(ZestLoopState<Integer> initializationState, List<ZestStatement> statements){
		super(initializationState, statements);
	}
}