package mozilla.zest.test.v1;

import org.mozilla.zest.core.v1.ZestResponse;
import org.mozilla.zest.core.v1.ZestRuntime;
import org.mozilla.zest.core.v1.ZestVariables;

public class TestRuntime implements ZestRuntime{
	private ZestResponse response;
	private ZestVariables vars = new ZestVariables();
	
	TestRuntime(ZestResponse resp) {
		this.response = resp;
		vars.setStandardVariables(resp);
	}

	@Override
	public String getVariable(String name) {
		String val = vars.getVariable(name);
		return val;
	}

	@Override
	public ZestResponse getLastResponse() {
		return response;
	}
}
