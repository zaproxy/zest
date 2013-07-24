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
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ZestLoop<T> extends ZestStatement implements ZestContainer, Enumeration<ZestStatement> {
	/**
	 * contains all the statement inside the loop
	 */
	private List<ZestStatement> statements;
	/**
	 * contains the snapshot of the current state of the loop
	 */
	private ZestLoopState<T> currentState;
	/**
	 * contains the index of the current statement considered.
	 */
	private int stmtIndex=0;
/**
 * Main construptor with the initialization state
 * @param initializationState the initialization state (first value and the set of values)
 */
	protected ZestLoop(ZestLoopState<T> initializationState) {
		this.statements = new LinkedList<>();
		this.currentState = initializationState;
	}
/**
 * Construptor with initialization state and the list of statement inside the loop
 * @param initializationState the initialization state (first value and the set of values)
 * @param statements all the statements inside the loop
 */
	protected ZestLoop(ZestLoopState<T> initializationState,
			List<ZestStatement> statements) {
		this.currentState = initializationState;
		this.statements = statements;
	}
/**
 * protected empty method for subclasses
 */
	protected ZestLoop() {
	}
/**
 * sets the current state to the new one (for subclasses)
 * @param newState the new state
 */
	protected void setState(ZestLoopState<T> newState) {
		this.currentState = newState;
	}
	protected List<ZestStatement> setStatement(List<ZestStatement> stmts){
		List<ZestStatement> oldStatements=this.statements;
		this.statements=stmts;
		return oldStatements;
	}
/**
 * increase the current state (all the statements are compiuted for this loop, lets start a new one)
 * @return the new state (of the following loop)
 */
	public boolean loop() {
		return this.currentState.increase();
	}
/**
 * ends the loops and set the state to the final value
 */
	public void endLoop() {
		this.currentState.endState();
	}
/**
 * adds a new statement inside the loop
 * @param stmt the new statement to add
 */
	public void addStatement(ZestStatement stmt) {
		statements.add(stmt);
	}
/**
 * returns the current state of the loop
 * @return
 */
	public ZestLoopState<T> getCurrentState() {
		return this.currentState;
	}
/**
 * return the current token considered inside the loop
 * @return the current token considered inside the loop
 */
	public ZestLoopToken<T> getCurrentToken() {
		return this.currentState.getCurrentToken();
	}
/**
 * return the current value of the token considered inside the loop
 * @return the current value of the token considered inside the loop
 */
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
	@Override
	public boolean hasMoreElements() {
		boolean isLastLoop=this.getCurrentState().isLastState();
		boolean isLastStmt=this.stmtIndex==statements.size()-1;
		boolean isBreakStatement=this.statements.get(stmtIndex).getClass().equals(ZestLoopBreak.class);
		return !isLastLoop && !isLastStmt && !isBreakStatement;
	}
	@Override
	public ZestStatement nextElement() {
		++stmtIndex;
		if(stmtIndex==statements.size()){
			this.currentState.increase();
			stmtIndex=0;
		}
		ZestStatement newStatement=statements.get(stmtIndex);
		if(newStatement instanceof ZestLoopBreak){
			this.currentState.endState();
			this.stmtIndex=statements.size();
			return null;
		}
		else if(newStatement instanceof ZestLoopNext){
			this.currentState.increase();
			this.stmtIndex=0;
		}
		return statements.get(stmtIndex);
	}
}
