/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.zest.core.v1;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Suite for all Zest v1 unit tests.
 *
 * @author psiinon
 * @version $Revision: 1.0 $
 */
@RunWith(Suite.class)
@SuiteClasses({ 
	ZestAssignRegexDelimitersUnitTest.class, 
	ZestAssertBodyRegexUnitTest.class,
	ZestConditionalRegexUnitTest.class,
	ZestConditionalRegexExprUnitTest.class,
	ZestRequestUnitTest.class,
	ZestScriptUnitTest.class,
	ZestStructuredExpressionUnitTest.class,
	ZestExpressionLengthUnitTest.class,
	ZestExpressionRegexUnitTest.class,
	ZestExpressionResponseTimeUnitTest.class,
	ZestExpressionURLUnitTest.class,
	ZestLoopTokenSetUnitTest.class,
	ZestLoopStateUnitTest.class,
	ZestLoopStringUnitTest.class,
	ZestLoopIntegerUnitTest.class,
	ZestLoopSerializationUnitTest.class
})

public final class ZestAllTestSuite {

}