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
 */
@RunWith(Suite.class)
@SuiteClasses({ 
	ZestActionSetTokenUnitTest.class, 
	ZestAssertBodyRegexUnitTest.class,
	ZestConditionalRegexExprUnitTest.class,
	ZestRequestUnitTest.class,
	ZestScriptUnitTest.class,
	ZestStructuredExpressionUnitTest.class,
	ZestExpressionLengthUnitTest.class,
	ZestExpressionRegexUnitTest.class
	})

public final class ZestAllTestSuite {

}