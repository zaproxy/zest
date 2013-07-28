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
	protected ZestLoopString(){
		super(new ZestLoopStateString());
	}
	public ZestLoopString(String[] values){
		super(new ZestLoopStateString(values));
	}
	public ZestLoopString(String[] values, List<ZestStatement> statements){
		super(new ZestLoopStateString(values), statements);
	}
	protected ZestLoopString(int index){
		super(index, new ZestLoopStateString());
	}
	public ZestLoopString(int index, String[] values, List<ZestStatement> statements){
		super(index, new ZestLoopStateString(values), statements);
	}
	public ZestLoopString(int index, String[] values){
		this(index, values,new LinkedList<ZestStatement>());
	}
}
