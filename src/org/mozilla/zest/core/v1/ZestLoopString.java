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
// TODO: Auto-generated Javadoc

/**
 * this class represents a Loop through String values.
 */
public class ZestLoopString extends ZestLoop<String> {
	
	/**
	 * Instantiates a new zest loop string.
	 */
	protected ZestLoopString(){
		super();
	}
	
	/**
	 * Instantiates a new zest loop string.
	 *
	 * @param values the values
	 */
	public ZestLoopString(String[] values){
		super(new ZestLoopStateString(values));
	}
	
	/**
	 * Instantiates a new zest loop string.
	 *
	 * @param values the values
	 * @param statements the statements
	 */
	public ZestLoopString(String[] values, List<ZestStatement> statements){
		super(new ZestLoopStateString(values), statements);
	}
	
	/**
	 * Instantiates a new zest loop string.
	 *
	 * @param index the index
	 */
	protected ZestLoopString(int index){
		super(index, new ZestLoopStateString());
	}
	
	/**
	 * Instantiates a new zest loop string.
	 *
	 * @param index the index
	 * @param values the values
	 * @param statements the statements
	 */
	public ZestLoopString(int index, String[] values, List<ZestStatement> statements){
		super(index, new ZestLoopStateString(values), statements);
	}
	
	/**
	 * Instantiates a new zest loop string.
	 *
	 * @param index the index
	 * @param values the values
	 */
	public ZestLoopString(int index, String[] values){
		this(index, values,new LinkedList<ZestStatement>());
	}
	public String[] getValues(){
		ZestLoopTokenStringSet set=(ZestLoopTokenStringSet) this.getCurrentState().getSet();
		String[] array=new String[set.size()];
		for(int i=0; i<array.length; i++){
			array[i]=set.getToken(i);
		}
		return  array;
	}
	@Override
	public ZestLoopString deepCopy(){
		ZestLoopString copy=new ZestLoopString(this.getIndex());
		copy.setState(this.getCurrentState().deepCopy());
		copy.setStatements(this.copyStatements());
		return copy;
	}
}
