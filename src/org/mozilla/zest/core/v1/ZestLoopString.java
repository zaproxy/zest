/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;

import java.util.LinkedList;
import java.util.List;
/**
 * this class represents a Loop through String values
 */
public class ZestLoopString extends ZestLoop<String> {
	public ZestLoopString(){
		super();
	}
	public ZestLoopString(String[] values, int startIndex, List<ZestStatement> statements){
		super();
		ZestLoopTokenStringSet set=new ZestLoopTokenStringSet(values);
		ZestLoopStateString state=new ZestLoopStateString(set);
		this.setState(state);
	}
	public ZestLoopString(String[] values, List<ZestStatement> statements){
		this(values,0,statements);
	}
	public ZestLoopString(String[] values){
		this(values,0,new LinkedList<ZestStatement>());
	}
}
