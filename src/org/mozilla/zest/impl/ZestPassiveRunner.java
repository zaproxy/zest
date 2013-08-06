package org.mozilla.zest.impl;

import java.io.IOException;

import org.mozilla.zest.core.v1.ZestAction;
import org.mozilla.zest.core.v1.ZestActionFail;
import org.mozilla.zest.core.v1.ZestActionFailException;
import org.mozilla.zest.core.v1.ZestRequest;
import org.mozilla.zest.core.v1.ZestResponse;
import org.mozilla.zest.core.v1.ZestScript;

public class ZestPassiveRunner extends ZestBasicRunner {

	@Override
	public ZestResponse send(ZestRequest request) throws IOException {
		throw new IllegalArgumentException("ZestRequest not allowed in passive tests");
	}
	
	@Override
	public String handleAction(ZestScript script, ZestAction action, ZestResponse lastResponse) throws ZestActionFailException {
		if (action instanceof ZestActionFail) {
			return super.handleAction(script, action, lastResponse);
		} else {
			throw new IllegalArgumentException(action.getElementType() + " not allowed in passive tests");
		}
	}
	
}
