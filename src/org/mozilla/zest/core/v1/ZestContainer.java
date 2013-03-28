/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.util.Set;

public interface ZestContainer {

	public ZestStatement getLast();
	
	public ZestStatement getStatement (int index);

	public ZestStatement getChildBefore(ZestStatement child);
	
	public Set<String> getTokens(String tokenStart, String tokenEnd);

}
