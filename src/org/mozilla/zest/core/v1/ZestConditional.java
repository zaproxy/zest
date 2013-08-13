/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * The Class ZestConditional.
 */
public class ZestConditional extends ZestStatement implements ZestContainer{

	/** The root expression. */
	private ZestExpressionElement rootExpression=null;
	
	/** The if statements. */
	private List<ZestStatement> ifStatements = new ArrayList<ZestStatement>();
	
	/** The else statements. */
	private List<ZestStatement> elseStatements = new ArrayList<ZestStatement>();

	/**
	 * Instantiates a new zest conditional.
	 */
	public ZestConditional() {
		super();
	}
	
	/**
	 * Instantiates a new zest conditional.
	 *
	 * @param rootExp the root exp
	 */
	public ZestConditional(ZestExpressionElement rootExp) {
		super();
		this.rootExpression=rootExp;
	}

	/**
	 * Instantiates a new zest conditional.
	 *
	 * @param index the index
	 */
	public ZestConditional(int index) {
		super(index);
	}
	
	/**
	 * Instantiates a new zest conditional.
	 *
	 * @param index the index
	 * @param rootExp the root exp
	 */
	public ZestConditional(int index, ZestExpressionElement rootExp) {
		super(index);
		this.rootExpression=rootExp;
	}
	
	/**
	 * Adds the if.
	 *
	 * @param req the req
	 */
	public void addIf(ZestStatement req) {
		this.addIf(this.ifStatements.size(), req);
	}
	
	/**
	 * Adds the if.
	 *
	 * @param index the index
	 * @param req the req
	 */
	public void addIf(int index, ZestStatement req) {
		ZestStatement prev = this;
		if (index > 0) {
			prev = this.ifStatements.get(index-1);
			if (prev instanceof ZestContainer) {
				prev = ((ZestContainer)prev).getLast();
			}
		}
		// Will rewire everything...
		req.insertAfter(prev);

		if (index == this.ifStatements.size()) {
			// Add at the end
			this.ifStatements.add(req);
		} else {
			this.ifStatements.add(index, req);
		}
	}
	
	/**
	 * Move if.
	 *
	 * @param index the index
	 * @param req the req
	 */
	public void moveIf(int index, ZestStatement req) {
		this.removeIf(req);
		this.addIf(index, req);
	}
	
	/**
	 * Removes the if.
	 *
	 * @param req the req
	 */
	public void removeIf(ZestStatement req) {
		this.ifStatements.remove(req);
		req.remove();
	}
	
	/**
	 * Removes the if statement.
	 *
	 * @param index the index
	 */
	public void removeIfStatement(int index) {
		this.removeIf(this.ifStatements.get(index));
	}
	
	/**
	 * Gets the if statement.
	 *
	 * @param index the index
	 * @return the if statement
	 * @throws IndexOutOfBoundsException the index out of bounds exception
	 */
	public ZestStatement getIfStatement (int index) throws IndexOutOfBoundsException {
		return this.ifStatements.get(index);
	}
	
	/**
	 * Gets the if statements.
	 *
	 * @return the if statements
	 */
	public List<ZestStatement> getIfStatements() {
		return ifStatements;
	}

	/**
	 * Adds the else.
	 *
	 * @param req the req
	 */
	public void addElse(ZestStatement req) {
		this.addElse(this.elseStatements.size(), req);
	}
	
	/**
	 * Adds the else.
	 *
	 * @param index the index
	 * @param req the req
	 */
	public void addElse(int index, ZestStatement req) {
		ZestStatement prev = this;
		if (this.ifStatements.size() > 0) {
			prev = this.ifStatements.get(this.ifStatements.size()-1);
			if (prev instanceof ZestContainer) {
				prev = ((ZestContainer)prev).getLast();
			}

		}
		if (index > 0) {
			prev = this.elseStatements.get(index-1);
			if (prev instanceof ZestContainer) {
				prev = ((ZestContainer)prev).getLast();
			}
		}
		// Will rewire everything...
		req.insertAfter(prev);
		if (index == this.elseStatements.size()) {
			// Add at the end
			this.elseStatements.add(req);
		} else {
			this.elseStatements.add(index, req);
		}
	}

	/**
	 * Move else.
	 *
	 * @param index the index
	 * @param req the req
	 */
	public void moveElse(int index, ZestStatement req) {
		this.removeElse(req);
		this.addElse(index, req);
	}
	
	/**
	 * Removes the else.
	 *
	 * @param req the req
	 */
	public void removeElse(ZestStatement req) {
		this.elseStatements.remove(req);
		req.remove();
	}
	
	/**
	 * Removes the else statement.
	 *
	 * @param index the index
	 */
	public void removeElseStatement(int index) {
		this.removeElse(this.elseStatements.get(index));
	}
	
	/**
	 * Gets the else statement.
	 *
	 * @param index the index
	 * @return the else statement
	 * @throws IndexOutOfBoundsException the index out of bounds exception
	 */
	public ZestStatement getElseStatement (int index) throws IndexOutOfBoundsException {
		return this.elseStatements.get(index);
	}
	
	/**
	 * Gets the else statements.
	 *
	 * @return the else statements
	 */
	public List<ZestStatement> getElseStatements() {
		return elseStatements;
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestContainer#getIndex(org.mozilla.zest.core.v1.ZestStatement)
	 */
	@Override
	public int getIndex (ZestStatement child) {
		if (this.ifStatements.contains(child)) {
			return this.ifStatements.indexOf(child);
		}
		return this.elseStatements.indexOf(child);
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestContainer#move(int, org.mozilla.zest.core.v1.ZestStatement)
	 */
	public void move(int index, ZestStatement stmt) {
		if (this.ifStatements.contains(stmt)) {
			this.removeIf(stmt);
			this.addIf(index, stmt);
		} else if (this.elseStatements.contains(stmt)) {
			this.removeElse(stmt);
			this.addElse(index, stmt);
		} else {
			throw new IllegalArgumentException("Not a direct child: " + stmt);
		}
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#isSameSubclass(org.mozilla.zest.core.v1.ZestElement)
	 */
	@Override
	public boolean isSameSubclass(ZestElement ze) {
		return ze instanceof ZestConditional;
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#setPrefix(java.lang.String, java.lang.String)
	 */
	@Override
	public void setPrefix(String oldPrefix, String newPrefix) throws MalformedURLException {
		for (ZestStatement stmt : this.ifStatements) {
			stmt.setPrefix(oldPrefix, newPrefix);
		}
		for (ZestStatement stmt : this.elseStatements) {
			stmt.setPrefix(oldPrefix, newPrefix);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#getTokens(java.lang.String, java.lang.String)
	 */
	@Override
	public Set<String> getVariableNames() {
		Set<String> tokens = new HashSet<String>();

		for (ZestStatement stmt : this.ifStatements) {
			if (stmt instanceof ZestContainer) {
				tokens.addAll(((ZestContainer)stmt).getVariableNames());
				
			} else if (stmt instanceof ZestAssignment) {
				tokens.add(((ZestAssignment)stmt).getVariableName());
			}
		}
		for (ZestStatement stmt : this.elseStatements) {
			if (stmt instanceof ZestContainer) {
				tokens.addAll(((ZestContainer)stmt).getVariableNames());
				
			} else if (stmt instanceof ZestAssignment) {
				tokens.add(((ZestAssignment)stmt).getVariableName());
			}
		}
		return tokens;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestContainer#getLast()
	 */
	public ZestStatement getLast() {
		if (this.elseStatements.size() > 0) {
			return this.elseStatements.get(this.elseStatements.size()-1);
		}
		if (this.ifStatements.size() > 0) {
			return this.ifStatements.get(this.ifStatements.size()-1);
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestContainer#getChildBefore(org.mozilla.zest.core.v1.ZestStatement)
	 */
	@Override
	public ZestStatement getChildBefore(ZestStatement child) {
		if (this.ifStatements.contains(child)) {
			int childIndex = this.ifStatements.indexOf(child);
			if (childIndex > 1) {
				return this.ifStatements.get(childIndex-1);
			}
		} else if (this.elseStatements.contains(child)) {
			int childIndex = this.elseStatements.indexOf(child);
			if (childIndex > 1) {
				return this.elseStatements.get(childIndex-1);
			} 
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestContainer#getStatement(int)
	 */
	@Override
	public ZestStatement getStatement (int index) {
		for (ZestStatement zr : this.getIfStatements()) {
			if (zr.getIndex() == index) {
				return zr;
			}
			if (zr instanceof ZestContainer) {
				ZestStatement stmt = ((ZestContainer)zr).getStatement(index);
				if (stmt != null) {
					return stmt;
				}
			}
		}
		for (ZestStatement zr : this.getElseStatements()) {
			if (zr.getIndex() == index) {
				return zr;
			}
			if (zr instanceof ZestContainer) {
				ZestStatement stmt = ((ZestContainer)zr).getStatement(index);
				if (stmt != null) {
					return stmt;
				}
			}
		}

		return null;
	}
	
	/**
	 * Checks if is true.
	 *
	 * @param response the response
	 * @return true, if is true
	 */
	public boolean isTrue(ZestRuntime runtime){
		return getRootExpression().evaluate(runtime);
	}
	
	/**
	 * Gets the root expression.
	 *
	 * @return the root expression
	 */
	public ZestExpressionElement getRootExpression(){
		return this.rootExpression;
	}
	
	/**
	 * Sets the root expression.
	 *
	 * @param new_root the new_root
	 * @return the zest expression element
	 */
	public ZestExpressionElement setRootExpression(ZestExpressionElement new_root){
		ZestExpressionElement old_root=this.getRootExpression();
		this.rootExpression=new_root;
		return old_root;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestStatement#deepCopy()
	 */
	@Override
	public ZestStatement deepCopy() {
		ZestConditional copy=new ZestConditional(getIndex());
		if(this.rootExpression!=null){
			copy.rootExpression=(ZestExpressionElement)rootExpression.deepCopy();
		}
		ArrayList<ZestStatement> ifList=new ArrayList<>();
		for(ZestStatement stmt:ifStatements){
			copy.ifStatements.add(stmt.deepCopy());
		}
		ArrayList<ZestStatement> elseList=new ArrayList<>();
		for(ZestStatement stmt:elseStatements){
			copy.elseStatements.add(stmt.deepCopy());
		}
		copy.elseStatements=elseList;
		copy.ifStatements=ifList;
		return copy;
	}
	
	@Override
	public boolean isPassive() {
		return true;
	}
}
