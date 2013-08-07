/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.test.v1;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mozilla.zest.core.v1.ZestConditional;
import org.mozilla.zest.core.v1.ZestJSON;
import org.mozilla.zest.core.v1.ZestLoopFile;
import org.mozilla.zest.core.v1.ZestLoopInteger;
import org.mozilla.zest.core.v1.ZestLoopString;
import org.mozilla.zest.core.v1.ZestStatement;

@RunWith(MockitoJUnitRunner.class)
public class ZestLoopSerializationUnitTest {
	String[] values={"a","b","c"};
	List<ZestStatement> statements=new LinkedList<>();
	{
		statements.add(new ZestConditional());
		statements.add(new ZestLoopInteger(0, 2));
	}
	@Test
	public void testSerializationLoopString() {
		ZestLoopString loop=new ZestLoopString(values, statements);
		String loopString=ZestJSON.toString(loop);
		ZestLoopString copy=(ZestLoopString) ZestJSON.fromString(loopString);
		String copyString=ZestJSON.toString(copy);
		assertTrue(copyString.equals(loopString));
	}
	@Test
	public void testSerializationLoopInteger(){
		ZestLoopInteger loop=new ZestLoopInteger(0, 1235, statements);
		String loopString=ZestJSON.toString(loop);
		ZestLoopInteger copy=(ZestLoopInteger) ZestJSON.fromString(loopString);
		String copyString=ZestJSON.toString(copy);
		assertTrue(copyString.equals(loopString));
	}
	@Test
	public void testSerializationLoopFile() throws FileNotFoundException{
		ZestLoopFile loop=new ZestLoopFile(ZestLoopFileUnitTest.file, statements);
		String loopString=ZestJSON.toString(loop);
		ZestLoopFile copy=(ZestLoopFile) ZestJSON.fromString(loopString);
		String copyString=ZestJSON.toString(copy);
		assertTrue(loopString.equals(copyString));
	}

}
