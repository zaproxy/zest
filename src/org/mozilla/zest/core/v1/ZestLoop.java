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

// TODO: Auto-generated Javadoc
/**
 * The Class ZestLoop.
 *
 * @param <T> the generic type
 */
public abstract class ZestLoop<T> extends ZestStatement implements ZestContainer, Enumeration<ZestStatement> {
	
	/** contains all the statement inside the loop. */
	private List<ZestStatement> statements=new LinkedList<>();
	
	/** The set. */
	private ZestLoopTokenSet<T> set=null;
	
	/** The variable name. */
	private String variableName="";
	/** contains the snapshot of the current state of the loop. */
	private transient ZestLoopState<T> currentState;
	/**
	 * contains the index of the current statement considered.
	 */
	private int stmtIndex=0;
	
	/** The step. */
	private int step=1;
	
	/** The counter. */
	private transient static int counter=0;
	
	/**
	 * Instantiates a new zest loop.
	 */
	protected ZestLoop(){
		super();
		init(getName(), null, new LinkedList<ZestStatement>());
	}
	
	/**
 * Instantiates a new zest loop.
 *
 * @param name the name
 * @param set the set
 * @param stmts the stmts
 */
	protected ZestLoop(String name, ZestLoopTokenSet<T> set, List<ZestStatement> stmts){
		super();
		init(name, set, stmts);
	}

/**
 * Construptor with initialization state and the list of statement inside the loop.
 *
 * @param index the index
 * @param name the name
 * @param set the set
 * @param statements all the statements inside the loop
 */
	protected ZestLoop(int index,String name, ZestLoopTokenSet<T> set,
			List<ZestStatement> statements) {
		super(index);
		init(name, set, statements);
	}


		/**
 * Inits the.
 *
 * @param name the name
 * @param set the set
 * @param statements the statements
 */
private void init(String name, ZestLoopTokenSet<T> set, List<ZestStatement> statements){
			this.set=set;
			this.statements=statements;
			this.variableName=name;
			this.currentState=set.getFirstState();
		}
		
		/**
		 * Gets the name.
		 *
		 * @return the name
		 */
		private String getName(){
			return "ZestLoop"+counter++;
		}

/**
 * sets the current state to the new one (for subclasses).
 *
 * @param newSet the new sets the
 */
	protected void setSet(ZestLoopTokenSet<T> newSet) {
		this.set = newSet;
	}
	
	/**
	 * Sets the step.
	 *
	 * @param step the new step
	 */
	protected void setStep(int step){
		this.step=step;
	}
	
	/**
	 * Sets the statements.
	 *
	 * @param stmts the stmts
	 * @return the list
	 */
	protected List<ZestStatement> setStatements(List<ZestStatement> stmts){
		List<ZestStatement> oldStatements=this.statements;
		this.statements=stmts;
		return oldStatements;
	}
	
	/**
	 * Gets the statements.
	 *
	 * @return the statements
	 */
	public List<ZestStatement> getStatements(){
		return this.statements;
	}

/**
 * increase the current state (ignoring all the statements which are still to be computed for this loop: a new one starts).
 *
 * @return the new state (of the following loop)
 */
	public boolean loop() {
		return this.currentState.increase(this.step, this.set);
	}

/**
 * ends the loops and set the state to the final value.
 */
	public void endLoop() {
		this.currentState.toLastState(this.set);
	}

/**
 * adds a new statement inside the loop.
 *
 * @param stmt the new statement to add
 */
	public void addStatement(ZestStatement stmt) {
		statements.add(stmt);
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
	 * @param newState the new current state
	 */
	public void setCurrentState(ZestLoopState<T> newState){
		this.currentState=newState;
	}

/**
 * return the current token considered inside the loop.
 *
 * @return the current token considered inside the loop
 */
	public T getCurrentToken() {
		return this.currentState.getCurrentToken();
	}
	
	/**
	 * returns the set of the tokens in this loop.
	 *
	 * @return the set of the tokens in this loop
	 */
	public ZestLoopTokenSet<T> getSet(){
		return this.set;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestContainer#getLast()
	 */
	@Override
	public ZestStatement getLast() {
		if (statements == null || statements.isEmpty()) {
			return this;
		}
		return statements.get(statements.size() - 1);
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestContainer#getStatement(int)
	 */
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

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestContainer#getIndex(org.mozilla.zest.core.v1.ZestStatement)
	 */
	@Override
	public int getIndex(ZestStatement child) {
		if (statements.contains(child)) {
			return statements.indexOf(child);
		} else {
			return -1;
		}
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestContainer#move(int, org.mozilla.zest.core.v1.ZestStatement)
	 */
	@Override
	public void move(int index, ZestStatement stmt) {
		if (this.statements.contains(stmt)) {
			this.statements.remove(stmt);
			this.statements.add(index, stmt);
		} else {
			throw new IllegalArgumentException("Not a direct child: " + stmt);
		}
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#isSameSubclass(org.mozilla.zest.core.v1.ZestElement)
	 */
	@Override
	public boolean isSameSubclass(ZestElement ze) {
		return ze instanceof ZestLoop<?>;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestContainer#getChildBefore(org.mozilla.zest.core.v1.ZestStatement)
	 */
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

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#getTokens(java.lang.String, java.lang.String)
	 */
	@Override
	public Set<String> getVariableNames() {
		Set<String> tokens = new HashSet<String>();
		
		for (ZestStatement stmt : this.statements) {
			if (stmt instanceof ZestContainer) {
				tokens.addAll(((ZestContainer)stmt).getVariableNames());
				
			} else if (stmt instanceof ZestAssignment) {
				tokens.add(((ZestAssignment)stmt).getVariableName());
			}
		}
		return tokens;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#setPrefix(java.lang.String, java.lang.String)
	 */
	@Override
	public void setPrefix(String oldPrefix, String newPrefix)
			throws MalformedURLException {
		for (ZestStatement stmt : this.statements) {
			stmt.setPrefix(oldPrefix, newPrefix);
		}
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#deepCopy()
	 */
	@Override
	public abstract ZestLoop<T> deepCopy();
	
	/* (non-Javadoc)
	 * @see java.util.Enumeration#hasMoreElements()
	 */
	@Override
	public boolean hasMoreElements() {
		boolean isLastLoop=this.getCurrentState().isLastState(this.set);
		if(isLastLoop){
			return false;
		}
		boolean isLastStmt=this.stmtIndex==statements.size();
		if(isLastStmt){
			return false;
		}
		if(this.statements.get(stmtIndex) instanceof ZestLoopBreak){
			return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Enumeration#nextElement()
	 */
	@Override
	public ZestStatement nextElement() {
		int currentStmt=stmtIndex;
		++stmtIndex;
		if(stmtIndex==statements.size()){
			this.currentState.increase(step, set);
			stmtIndex=0;
		}
		ZestStatement newStatement=statements.get(currentStmt);
		if(newStatement instanceof ZestLoopBreak){
			this.currentState.toLastState(this.set);
			this.stmtIndex=statements.size();
			return null;
		}
		else if(newStatement instanceof ZestLoopNext){
			this.currentState.increase(step, set);
			this.stmtIndex=0;
			return statements.get(stmtIndex);
		}
		return statements.get(currentStmt);
	}
	
	/**
	 * Copy statements.
	 *
	 * @return the list
	 */
	public List<ZestStatement> copyStatements(){
		List<ZestStatement> statements=new LinkedList<>();
		for(ZestStatement stmt:this.getStatements()){
			statements.add(stmt.deepCopy());
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
	 * @param name the new variable name
	 */
	public void setVariableName(String name) {
		this.variableName = name;
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#isPassive()
	 */
	@Override
	public boolean isPassive() {
		return true;
	}

}
