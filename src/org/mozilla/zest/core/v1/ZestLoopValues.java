/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;

import java.util.List;

public class ZestLoopValues extends ZestLoop<String> {
	public ZestLoopValues(){
		super();
	}
	public ZestLoopValues(ZestLoopState<String> initializationState){
		super(initializationState);
	}
	public ZestLoopValues(ZestLoopState<String> initializationState, List<ZestStatement> statements){
		super(initializationState, statements);
	}
}
