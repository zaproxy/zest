/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;

public class NoSuchExpressionException extends Exception {
	private final String literal;
	public NoSuchExpressionException(String literal){
		super(literal+" : this expression cannot be resolved to an existing expression.");
		this.literal=literal;
	}
	public String getLiteralExpression(){
		return this.literal;
	}
}
