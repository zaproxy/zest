package org.mozilla.zest.core.v1;

import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ZestBooleanAnd extends ZestStatement implements ZestConditionalElement, ZestContainer {
	
	private List<ZestConditionalElement> andList;
	private ZestConditionalElement parent;
	public ZestBooleanAnd(ZestConditionalElement parent){
		super();
		this.parent=parent;
		andList=new LinkedList<>();
	}
	public ZestBooleanAnd(ZestConditionalElement parent, List<ZestConditionalElement> andList){
		super();
		this.andList=andList;
		this.parent=parent;
	}
	public ZestBooleanAnd(int index, ZestConditionalElement parent){
		super(index);
	}

	@Override
	public ZestStatement getLast() {
		if(!andList.isEmpty())
			return (ZestStatement) andList.get(andList.size()-1);
		return (ZestStatement) parent;
	}
	@Override
	public ZestStatement getStatement(int index) {
		if(parent.getIndex()==index)
			//if(parent instanceof ZestStatement)
				return (ZestStatement) parent;
		for(int i=0; i<andList.size(); i++){
			if(andList.get(i).getIndex()==index)
				return (ZestStatement) andList.get(i);
		}
		return null;
	}
	@Override
	public int getIndex(ZestStatement child) {
		if(parent.equals(child))
			return parent.getIndex();
		else if(andList.contains(child)){
			return andList.indexOf(child);
		}
		return -1;
	}
	@Override
	public void move(int index, ZestStatement stmt) {
		// TODO Auto-generated method stub
	}
	@Override
	public ZestStatement getChildBefore(ZestStatement child) {
		//TODO
		return null;
	}
	@Override
	void setPrefix(String oldPrefix, String newPrefix)
			throws MalformedURLException {
	}
	@Override
	void setUpRefs(ZestScript script) {
	}
	@Override
	public List<ZestTransformation> getTransformations() {
		return null;
	}
	@Override
	public List<ZestConditionalElement> getChildrenCondition() {
		return andList;
	}
	@Override
	public ZestStatement deepCopy() {
		ZestBooleanAnd copy=new ZestBooleanAnd(getIndex(), this.parent);
		copy.andList=new LinkedList<>();
		for(ZestConditionalElement elem:andList){
			copy.andList.add(elem);
		}
		return copy;
	}
	@Override
	public Set<String> getTokens(String tokenStart, String tokenEnd) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isLeaf() {
		return false;
	}
	@Override
	public boolean isRoot() {
		return parent==null;
	}
	@Override
	public boolean evaluate() {
		boolean toReturn=true;
		for(ZestConditionalElement con:andList){
			toReturn=toReturn && con.evaluate();//compute AND for each child
		}
		return toReturn;
	}

}
