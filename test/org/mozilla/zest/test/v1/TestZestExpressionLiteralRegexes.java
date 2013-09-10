/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.test.v1;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mozilla.zest.core.v1.ZestExpressionEquals;
import org.mozilla.zest.core.v1.ZestExpressionLength;
import org.mozilla.zest.core.v1.ZestExpressionRegex;
import org.mozilla.zest.core.v1.ZestExpressionResponseTime;
import org.mozilla.zest.core.v1.ZestExpressionStatusCode;
import org.mozilla.zest.core.v1.ZestExpressionURL;
import org.mozilla.zest.impl.ZestUtils;

public class TestZestExpressionLiteralRegexes {

	@Test
	public void testZestExpressionStatusCodeLiteralRegex() {
		ZestExpressionStatusCode code=new ZestExpressionStatusCode(100);
		assertTrue(ZestExpressionStatusCode.isLiteralInstance(code.toString()));
		code.setInverse(true);
		assertTrue(ZestExpressionStatusCode.isLiteralInstance(code.toString()));
	}
	@Test
	public void testZestExpressionEqualsLiteralRegex() {
		ZestExpressionEquals equals=new ZestExpressionEquals("", "Sample string\\");
		assertTrue(ZestExpressionEquals.isLiteralInstance(equals.toString()));
		equals.setInverse(true);
		assertTrue(ZestExpressionEquals.isLiteralInstance(equals.toString()));
	}
	@Test
	public void testZestExpressionLengthLiteralRegex() {
		ZestExpressionLength length=new ZestExpressionLength("", 10,20);
		assertTrue(ZestExpressionLength.isLiteralInstance(length.toString()));
		length.setInverse(true);
		assertTrue(ZestExpressionLength.isLiteralInstance(length.toString()));
	}
	@Test
	public void testZestExpressionRegexLiteralRegex(){
		ZestExpressionRegex regex=new ZestExpressionRegex("", "brugboil");
		assertTrue(ZestExpressionRegex.isLiteralInstance(regex.toString()));
		regex.setInverse(true);
		assertTrue(ZestExpressionRegex.isLiteralInstance(regex.toString()));
	}
	@Test
	public void testZestExpressionResponseTimeLiteralRegex(){
		ZestExpressionResponseTime time=new ZestExpressionResponseTime(1357835);
		assertTrue(ZestExpressionResponseTime.isLiteralInstance(time.toString()));
		time.setInverse(true);
		assertTrue(ZestExpressionResponseTime.isLiteralInstance(time.toString()));
	}
	@Test
	public void testZestExpressionUrlLiteralRegex(){
		ZestExpressionURL url=new ZestExpressionURL();
		for(int i=0; i<10; i++){
			url.getIncludeRegexes().add("includetoken"+i);
		}
		for(int i=10; i<20; i++){
			url.getExcludeRegexes().add("excludetoken"+i);
		}
		assertTrue(ZestExpressionURL.isLiteralInstance(url.toString()));
		url.setInverse(true);
		assertTrue(ZestExpressionURL.isLiteralInstance(url.toString()));
	}
	@Test
	public void testZestExpressionStatusCodeParsing(){
		ZestExpressionStatusCode code=new ZestExpressionStatusCode(100);
		String codeString=code.toString();
		ZestExpressionStatusCode codeCopy=(ZestExpressionStatusCode)ZestUtils.parseExpression(codeString);
		assertTrue("same code:",code.getCode()==codeCopy.getCode());
		assertTrue("same isInverse", code.isInverse()==codeCopy.isInverse());
	}
	@Test
	public void testZestExpressionEqualsParsing(){
		ZestExpressionEquals equals=new ZestExpressionEquals("", "Sample string\\");
		String equalsString=equals.toString();
		ZestExpressionEquals equalsCopy=(ZestExpressionEquals)ZestUtils.parseExpression(equalsString);
		assertTrue("same value:",equals.getValue().equals(equalsCopy.getValue()));
		assertTrue("same isInverse", equals.isInverse()==equalsCopy.isInverse());
		assertTrue("same case exact:", equals.isCaseExact()==equalsCopy.isCaseExact());
	}
	@Test
	public void testZestExpressionLengthParsing(){
		ZestExpressionLength length=new ZestExpressionLength("", 10,20);
		String lengthString=length.toString();
		ZestExpressionLength lengthCopy=(ZestExpressionLength)ZestUtils.parseExpression(lengthString);
		assertTrue("Same Approx", length.getApprox()==lengthCopy.getApprox());
		assertTrue("same length", length.getLength()==lengthCopy.getLength());
	}
	@Test
	public void testZestExpressionRegexParsing(){
		ZestExpressionRegex regex=new ZestExpressionRegex("", "brugboil");
		String regexString=regex.toString();
		ZestExpressionRegex regexCopy=(ZestExpressionRegex)ZestUtils.parseExpression(regexString);
		assertTrue("same regex", regex.getRegex().equals(regexCopy.getRegex()));
	}
	@Test
	public void testZestExpressionResposeTimeParsing(){
		ZestExpressionResponseTime time=new ZestExpressionResponseTime(1235486);
		String timeString=time.toString();
		ZestExpressionResponseTime timeCopy=(ZestExpressionResponseTime) ZestUtils.parseExpression(timeString);
		assertTrue("same time", time.getTimeInMs()==timeCopy.getTimeInMs());
	}
	@Test
	public void testZestExpressionURLParsing(){
		ZestExpressionURL url=new ZestExpressionURL();
		for(int i=0; i<10; i++){
			url.getIncludeRegexes().add("includetoken"+i);
		}
		for(int i=10; i<20; i++){
			url.getExcludeRegexes().add("excludetoken"+i);
		}
		String urlString=url.toString();
		ZestExpressionURL urlCopy=(ZestExpressionURL)ZestUtils.parseExpression(urlString);
		assertTrue("Same size of include regexes",url.getIncludeRegexes().size()==urlCopy.getIncludeRegexes().size());
		assertTrue("Same size of excluded regexes", url.getExcludeRegexes().size()==urlCopy.getExcludeRegexes().size());
		int includeSize=url.getIncludeRegexes().size();
		int excludeSize=url.getExcludeRegexes().size();
		for(int i=0; i<includeSize; i++){
			String expected=url.getIncludeRegexes().get(i);
			String obtained=urlCopy.getIncludeRegexes().get(i);
			String message="INCLUDE ["+i+"] expected "+expected+", obtained "+obtained;
			assertTrue(message, expected.equals(obtained));
		}
		for(int i=0; i<excludeSize; i++){
			String expected=url.getExcludeRegexes().get(i);
			String obtained=urlCopy.getExcludeRegexes().get(i);
			String message="EXCLUDE ["+i+"] expected "+expected+", obtained "+obtained;
			assertTrue(message, expected.equals(obtained));
		}
	}
	@Test
	public void testZestExpressionInverseParsing(){
		ZestExpressionEquals equals=new ZestExpressionEquals("", "gnruweomc");
		equals.setInverse(true);
		ZestExpressionEquals copy=(ZestExpressionEquals)ZestUtils.parseExpression(equals.toString());
		assertTrue(copy.isInverse());
	}
}
