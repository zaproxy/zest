package org.mozilla.zest.core.v1;

import java.util.List;

public interface ZestConditionalElement extends ZestContainer {
	public int getIndex();
	public List<ZestConditionalElement> getChildrenCondition();
	public boolean isLeaf();
	public boolean isRoot();
	public boolean evaluate();
}
