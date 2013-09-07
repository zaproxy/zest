/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;

import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * The Class ZestLoop.
 * 
 * @param <T>
 *            the generic type
 */
public abstract class ZestLoop<T> extends ZestStatement implements
		ZestContainer, Enumeration<ZestStatement> {

	/** contains all the statement inside the loop. */
	private List<ZestStatement> statements = new LinkedList<>();

	/** The variable name. */
	private String variableName = "";
	/** contains the snapshot of the current state of the loop. */
	private transient ZestLoopState<T> currentState;
	/**
	 * contains the index of the current statement considered.
	 */
	private transient int stmtIndex = 0;

	/**
	 * Instantiates a new zest loop.
	 */
	protected ZestLoop() {
		super();
	}
	
	protected ZestLoop(int index){
		super(index);
	}

	/**
	 * Inits the Loop.
	 * 
	 * @param name
	 *            the name
	 * @param set
	 *            the set
	 * @param statements
	 *            the statements
	 */
	protected void init(ZestLoopTokenSet<T> set, List<ZestStatement> statements) {
		this.statements = statements;
		this.currentState = set.getFirstState();
	}

	/**
	 * sets the current state to the new one (for subclasses).
	 * 
	 * @param newSet
	 *            the new sets the
	 */
	protected void setSet(ZestLoopTokenSet<T> newSet) {
		this.setCurrentState(newSet.getFirstState());
	}

	/**
	 * Sets the statements.
	 * 
	 * @param stmts
	 *            the stmts
	 * @return the list
	 */
	public List<ZestStatement> setStatements(List<ZestStatement> stmts) {
		List<ZestStatement> oldStatements = this.statements;
		this.statements = stmts;
		return oldStatements;
	}

	/**
	 * Gets the statements.
	 * 
	 * @return the statements
	 */
	public List<ZestStatement> getStatements() {
		return this.statements;
	}

	/**
	 * increase the current state (ignoring all the statements which are still
	 * to be computed for this loop: a new one starts).
	 * 
	 * @return the new state (of the following loop)
	 */
	protected boolean loop(ZestLoopTokenSet<T> set) {
		return this.currentState.increase(set);
	}

	/**
	 * ends the loops and set the state to the final value.
	 */
	protected void endLoop(ZestLoopTokenSet<T> set) {
		this.currentState.toLastState(set);
	}

	/**
	 * adds a new statement inside the loop.
	 * 
	 * @param stmt
	 *            the new statement to add
	 */
	public void addStatement(ZestStatement stmt) {
		statements.add(stmt);
	}

	/**
	 * Adds the statement in the specified index in the script.
	 *
	 *@param index the index at which the statement will be added
	 * @param stmt the statement to add
	 */
	public void add(int index, ZestStatement stmt) {
		ZestStatement prev = this;
		if (index == this.statements.size()) {
			// Add at the end
			this.statements.add(stmt);
			
		} else {
			this.statements.add(index, stmt);
		}
		if (index > 0) {
			prev = this.statements.get(index-1);
		}
		// This will wire everything up
		stmt.insertAfter(prev);
	}

	/**
	 * returns the current state of the loop.
	 * 
	 * @return the current state
	 */
	public ZestLoopState<T> getCurrentState() {
		return this.currentState;
	}

	/**
	 * Sets the current state.
	 * 
	 * @param newState
	 *            the new current state
	 */
	protected void setCurrentState(ZestLoopState<T> newState) {
		this.currentState = newState;
	}

	/**
	 * return the current token considered inside the loop.
	 * 
	 * @return the current token considered inside the loop
	 */
	public T getCurrentToken() {
		return this.currentState.getCurrentToken();
	}
	
	public int getCurrentIndex(){
		return this.currentState.getCurrentIndex();
	}
	
	public int getCurrentStatementIndex(){
		return this.stmtIndex;
	}

	/**
	 * returns the set of the tokens in this loop.
	 * 
	 * @return the set of the tokens in this loop
	 */
	public abstract ZestLoopTokenSet<T> getSet();

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
	public Set<String> getVariableNames() {
		Set<String> tokens = new HashSet<String>();
		tokens.add(this.getVariableName());
		for (ZestStatement stmt : this.statements) {
			if (stmt instanceof ZestContainer) {
				tokens.addAll(((ZestContainer) stmt).getVariableNames());

			} else if (stmt instanceof ZestAssignment) {
				tokens.add(((ZestAssignment) stmt).getVariableName());
			}
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
	public abstract ZestLoop<T> deepCopy();

	@Override
	public boolean hasMoreElements() {
		boolean isLastLoop = isLastState();
		if (isLastLoop) {
			return false;
		}
		boolean isLastStmt = this.stmtIndex == statements.size();
		if (isLastStmt) {
			return false;
		}
		if (this.statements.get(stmtIndex) instanceof ZestControlLoopBreak) {
			return false;
		}
		return true;
	}
	
	public abstract boolean isLastState();
	
	protected abstract void increase();
	
	public abstract void toLastState();

	@Override
	public ZestStatement nextElement() {
		int currentStmt = stmtIndex;
		++stmtIndex;
		if (stmtIndex == statements.size()) {
			this.increase();
			stmtIndex = 0;
		}
		ZestStatement newStatement = statements.get(currentStmt);
		if (newStatement instanceof ZestControlLoopBreak) {
			toLastState();
			this.stmtIndex = statements.size();
			return null;
		} else if (newStatement instanceof ZestControlLoopNext) {
			increase();
			this.stmtIndex = 0;
			return statements.get(stmtIndex);
		}
		return statements.get(currentStmt);
	}

	/**
	 * Copy statements.
	 * 
	 * @return the list
	 */
	public List<ZestStatement> copyStatements() {
		if (this.getStatements() != null) {
			List<ZestStatement> statements = new LinkedList<>();
			for (ZestStatement stmt : this.getStatements()) {
				statements.add(stmt.deepCopy());
			}
		}
		return statements;
	}

	/**
	 * Returns the variable name.
	 * 
	 * @return the variable name
	 */
	public String getVariableName() {
		return variableName;
	}

	/**
	 * Sets the variable name.
	 * 
	 * @param name
	 *            the new variable name
	 */
	public void setVariableName(String name) {
		this.variableName = name;
	}

	@Override
	public boolean isPassive() {
		return true;
	}

}
