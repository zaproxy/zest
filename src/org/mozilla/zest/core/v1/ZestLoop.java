/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ZestLoop<T> extends ZestStatement implements ZestContainer {
	private List<ZestStatement> statements;
	private ZestLoopState<T> currentState;

	public ZestLoop(ZestLoopState<T> initializationState) {
		this.statements = new LinkedList<>();
		this.currentState = initializationState;
	}

	public ZestLoop(ZestLoopState<T> initializationState,
			List<ZestStatement> statements) {
		this.currentState = initializationState;
		this.statements = statements;
	}

	protected ZestLoop() {
	}

	protected void setState(ZestLoopState<T> newState) {
		this.currentState = newState;
	}

	public boolean loop() {
		return this.currentState.increase();
	}

	public void endLoop() {
		this.currentState.endState();
	}

	public void addStatement(ZestStatement stmt) {
		statements.add(stmt);
	}

	public ZestLoopState<T> getCurrentState() {
		return this.currentState;
	}

	public ZestLoopToken<T> getCurrentToken() {
		return this.currentState.getCurrentToken();
	}

	public T getCurrentValue() {
		return this.currentState.getCurrentToken().getValue();
	}

	@Override
	public ZestStatement getLast() {
		if (statements == null || statements.isEmpty()) {
			return this;
		}
		return statements.get(statements.size() - 1);
	}

	@Override
	public ZestStatement getStatement(int index) {
		for (ZestStatement zr : this.statements) {
			if (zr.getIndex() == index) {
				return zr;
			}
			if (zr instanceof ZestContainer) {
				ZestStatement stmt = ((ZestContainer) zr).getStatement(index);
				if (stmt != null) {
					return stmt;
				}
			}
		}
		return null;
	}

	@Override
	public int getIndex(ZestStatement child) {
		if (statements.contains(child)) {
			return statements.indexOf(child);
		} else {
			return -1;
		}
	}

	@Override
	public void move(int index, ZestStatement stmt) {
		if (this.statements.contains(stmt)) {
			this.statements.remove(stmt);
			this.statements.add(index, stmt);
		} else {
			throw new IllegalArgumentException("Not a direct child: " + stmt);
		}
	}

	@Override
	public boolean isSameSubclass(ZestElement ze) {
		return ze instanceof ZestLoop<?>;
	}

	@Override
	public ZestStatement getChildBefore(ZestStatement child) {
		if (this.statements.contains(child)) {
			int childIndex = this.statements.indexOf(child);
			if (childIndex > 1) {
				return this.statements.get(childIndex - 1);
			}
		}
		return null;
	}

	@Override
	public Set<String> getTokens(String tokenStart, String tokenEnd) {
		Set<String> tokens = new HashSet<String>();
		for (ZestStatement stmt : this.statements) {
			tokens.addAll(stmt.getTokens(tokenStart, tokenEnd));
		}
		return tokens;
	}

	@Override
	public void setPrefix(String oldPrefix, String newPrefix)
			throws MalformedURLException {
		for (ZestStatement stmt : this.statements) {
			stmt.setPrefix(oldPrefix, newPrefix);
		}
	}

	@Override
	void setUpRefs(ZestScript script) {
		for (ZestStatement stmt : this.statements) {
			stmt.setUpRefs(script);
		}
	}

	@Override
	public List<ZestTransformation> getTransformations() {
		List<ZestTransformation> xforms = new ArrayList<ZestTransformation>();
		for (ZestStatement stmt : this.statements) {
			xforms.addAll(stmt.getTransformations());
		}
		return xforms;
	}

	@Override
	public ZestLoop<T> deepCopy() {
		ZestLoop<T> copy = new ZestLoop<>();
		copy.currentState = this.currentState.deepCopy();
		if (this.statements == null) {
			return copy;
		}
		copy.statements = new LinkedList<>();
		for (int i = 0; i < this.statements.size(); i++) {
			copy.statements.add(this.statements.get(i).deepCopy());
		}
		return copy;
	}
}
