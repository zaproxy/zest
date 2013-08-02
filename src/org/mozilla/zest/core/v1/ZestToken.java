/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;

public interface ZestToken extends Comparable<ZestToken> {
	public boolean equals(ZestToken token);
	public String getToken();
	public String setToken(String newToken);
	public ZestToken deepCopy();
}
