/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.test.v1;

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
	ZestActionInvokeUnitTest.class,
	ZestActionSleepUnitTest.class,
	ZestAssertBodyRegexUnitTest.class,
	ZestAssignRegexDelimitersUnitTest.class,
	ZestAssignReplaceUnitTest.class,
	ZestAssignStringDelimitersUnitTest.class,
	ZestAssignStringUnitTest.class,
	ZestConditionalRegexComplexUnitTest.class,
	ZestConditionalRegexExprUnitTest.class,
	ZestConditionalRegexUnitTest.class,
	ZestExpressionAndUnitTest.class,
	ZestExpressionLengthUnitTest.class,
	ZestExpressionRegexUnitTest.class,
	ZestExpressionResponseTimeUnitTest.class,
	ZestExpressionURLUnitTest.class,
	ZestLoopFileUnitTest.class,
	ZestLoopIntegerUnitTest.class,
	ZestLoopSerializationUnitTest.class,
	ZestLoopStateUnitTest.class,
	ZestLoopStringUnitTest.class,
	ZestLoopTokenSetUnitTest.class,
	ZestRequestUnitTest.class,
	ZestScriptUnitTest.class,
	ZestStructuredExpressionUnitTest.class
})

public final class ZestAllTestSuite {

}