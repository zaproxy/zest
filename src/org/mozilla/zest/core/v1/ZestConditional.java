/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ZestConditional extends ZestStatement implements ZestContainer{

	private ZestExpressionElement rootExpression;
	private List<ZestStatement> ifStatements = new ArrayList<ZestStatement>();
	private List<ZestStatement> elseStatements = new ArrayList<ZestStatement>();

	public ZestConditional() {
		super();
	}
	public ZestConditional(ZestExpressionElement rootExp) {
		super();
		this.rootExpression=rootExp;
	}

	public ZestConditional(int index) {
		super(index);
	}
	public ZestConditional(int index, ZestExpressionElement rootExp) {
		super(index);
		this.rootExpression=rootExp;
	}
	public void addIf(ZestStatement req) {
		this.addIf(this.ifStatements.size(), req);
	}
	
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
	
	public void moveIf(int index, ZestStatement req) {
		this.removeIf(req);
		this.addIf(index, req);
	}
	
	public void removeIf(ZestStatement req) {
		this.ifStatements.remove(req);
		req.remove();
	}
	
	public void removeIfStatement(int index) {
		this.removeIf(this.ifStatements.get(index));
	}
	
	public ZestStatement getIfStatement (int index) throws IndexOutOfBoundsException {
		return this.ifStatements.get(index);
	}
	
	public List<ZestStatement> getIfStatements() {
		return ifStatements;
	}

	public void addElse(ZestStatement req) {
		this.addElse(this.elseStatements.size(), req);
	}
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

	public void moveElse(int index, ZestStatement req) {
		this.removeElse(req);
		this.addElse(index, req);
	}
	
	public void removeElse(ZestStatement req) {
		this.elseStatements.remove(req);
		req.remove();
	}
	
	public void removeElseStatement(int index) {
		this.removeElse(this.elseStatements.get(index));
	}
	
	public ZestStatement getElseStatement (int index) throws IndexOutOfBoundsException {
		return this.elseStatements.get(index);
	}
	
	public List<ZestStatement> getElseStatements() {
		return elseStatements;
	}
	
	@Override
	public int getIndex (ZestStatement child) {
		if (this.ifStatements.contains(child)) {
			return this.ifStatements.indexOf(child);
		}
		return this.elseStatements.indexOf(child);
	}
	
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

	@Override
	public boolean isSameSubclass(ZestElement ze) {
		return ze instanceof ZestConditional;
	}
	
	@Override
	public void setPrefix(String oldPrefix, String newPrefix) throws MalformedURLException {
		for (ZestStatement stmt : this.ifStatements) {
			stmt.setPrefix(oldPrefix, newPrefix);
		}
		for (ZestStatement stmt : this.elseStatements) {
			stmt.setPrefix(oldPrefix, newPrefix);
		}
	}

	@Override
	public Set<String> getTokens(String tokenStart, String tokenEnd) {
		Set<String> tokens = new HashSet<String>();
		for (ZestStatement stmt : this.ifStatements) {
			tokens.addAll(stmt.getTokens(tokenStart, tokenEnd));
		}
		for (ZestStatement stmt : this.elseStatements) {
			tokens.addAll(stmt.getTokens(tokenStart, tokenEnd));
		}
		return tokens;
	}

	@Override
	void setUpRefs(ZestScript script) {
		for (ZestStatement stmt : this.ifStatements) {
			stmt.setUpRefs(script);
		}
		for (ZestStatement stmt : this.elseStatements) {
			stmt.setUpRefs(script);
		}
	}

	@Override
	public List<ZestTransformation> getTransformations() {
		List<ZestTransformation> xforms = new ArrayList<ZestTransformation>();
		for (ZestStatement stmt : this.ifStatements) {
			xforms.addAll(stmt.getTransformations());
		}
		for (ZestStatement stmt : this.elseStatements) {
			xforms.addAll(stmt.getTransformations());
		}
		
		return xforms;
	}
	
	public ZestStatement getLast() {
		if (this.elseStatements.size() > 0) {
			return this.elseStatements.get(this.elseStatements.size()-1);
		}
		if (this.ifStatements.size() > 0) {
			return this.ifStatements.get(this.ifStatements.size()-1);
		}
		return this;
	}

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
	public boolean isTrue(ZestResponse response){
		return getRootExpression().evaluate(response);
	}
	public ZestExpressionElement getRootExpression(){
		return this.rootExpression;
	}
	public ZestExpressionElement setRootExpression(ZestExpressionElement new_root){
		ZestExpressionElement old_root=this.getRootExpression();
		this.rootExpression=new_root;
		return old_root;
	}

	@Override
	public ZestStatement deepCopy() {
		ZestConditional copy=new ZestConditional(getIndex());
		copy.rootExpression=(ZestExpressionElement)rootExpression.deepCopy();
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
}
