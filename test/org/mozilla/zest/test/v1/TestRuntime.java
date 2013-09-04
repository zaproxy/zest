package org.mozilla.zest.test.v1;

import org.mozilla.zest.core.v1.ZestResponse;
import org.mozilla.zest.core.v1.ZestRuntime;
import org.mozilla.zest.core.v1.ZestVariables;

public class TestRuntime implements ZestRuntime{
	private ZestResponse response;
	private ZestVariables vars = new ZestVariables();
	
	public TestRuntime() {
	}

	public TestRuntime(ZestResponse resp) {
		this.response = resp;
		vars.setStandardVariables(resp);
	}

	@Override
	public String getVariable(String name) {
		return vars.getVariable(name);
	}

	@Override
	public void setVariable(String name, String value) {
		vars.setVariable(name, value);
	}

	@Override
	public ZestResponse getLastResponse() {
		return response;
	}

	@Override
	public String replaceVariablesInString(String str, boolean urlEncode) {
		return vars.replaceInString(str, urlEncode);
	}

	@Override
	public void output(String str) {
		System.out.println(str);
	}
}
