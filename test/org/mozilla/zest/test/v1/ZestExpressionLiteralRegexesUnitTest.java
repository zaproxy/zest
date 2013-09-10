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
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mozilla.zest.core.v1.NoSuchExpressionException;
import org.mozilla.zest.core.v1.ZestExpression;
import org.mozilla.zest.core.v1.ZestExpressionAnd;
import org.mozilla.zest.core.v1.ZestExpressionEquals;
import org.mozilla.zest.core.v1.ZestExpressionLength;
import org.mozilla.zest.core.v1.ZestExpressionOr;
import org.mozilla.zest.core.v1.ZestExpressionRegex;
import org.mozilla.zest.core.v1.ZestExpressionResponseTime;
import org.mozilla.zest.core.v1.ZestExpressionStatusCode;
import org.mozilla.zest.core.v1.ZestExpressionURL;
import org.mozilla.zest.impl.ZestExpressionEvaluator;
import org.mozilla.zest.impl.ZestUtils;
@RunWith(MockitoJUnitRunner.class)
public class ZestExpressionLiteralRegexesUnitTest {

	@Test
	public void testZestExpressionStatusCodeLiteralRegex() {
		ZestExpressionStatusCode code=new ZestExpressionStatusCode(100);
		assertTrue(ZestExpressionStatusCode.isLiteralInstance(code.toString()));
		code.setInverse(true);
		assertTrue(ZestExpressionStatusCode.isLiteralInstance(code.toString()));
	}
	@Test
	public void testZestExpressionEqualsLiteralRegex() {
		ZestExpressionEquals equals=new ZestExpressionEquals("location", "Sample string");
		assertTrue(ZestExpressionEquals.isLiteralInstance(equals.toString()));
		equals.setInverse(true);
		assertTrue(ZestExpressionEquals.isLiteralInstance(equals.toString()));
	}
	@Test
	public void testZestExpressionLengthLiteralRegex() {
		ZestExpressionLength length=new ZestExpressionLength("location", 10,20);
		assertTrue(ZestExpressionLength.isLiteralInstance(length.toString()));
		length.setInverse(true);
		assertTrue(ZestExpressionLength.isLiteralInstance(length.toString()));
	}
	@Test
	public void testZestExpressionRegexLiteralRegex(){
		ZestExpressionRegex regex=new ZestExpressionRegex("location", "brugboil");
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
		ZestExpressionStatusCode codeCopy;
		try {
			codeCopy = (ZestExpressionStatusCode)ZestUtils.parseSimpleExpression(codeString);
			assertTrue("same code:",code.getCode()==codeCopy.getCode());
			assertTrue("same isInverse", code.isInverse()==codeCopy.isInverse());
		} catch (NoSuchExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testZestExpressionEqualsParsing(){
		ZestExpressionEquals equals=new ZestExpressionEquals("location", "Sample string\\");
		String equalsString=equals.toString();
		ZestExpressionEquals equalsCopy;
		try {
			equalsCopy = (ZestExpressionEquals)ZestUtils.parseSimpleExpression(equalsString);
			assertTrue("same value:",equals.getValue().equals(equalsCopy.getValue()));
			assertTrue("same isInverse", equals.isInverse()==equalsCopy.isInverse());
			assertTrue("same case exact:", equals.isCaseExact()==equalsCopy.isCaseExact());
		} catch (NoSuchExpressionException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void testZestExpressionLengthParsing(){
		ZestExpressionLength length=new ZestExpressionLength("location", 10,20);
		String lengthString=length.toString();
		ZestExpressionLength lengthCopy;
		try {
			lengthCopy = (ZestExpressionLength)ZestUtils.parseSimpleExpression(lengthString);
			assertTrue("Same Approx", length.getApprox()==lengthCopy.getApprox());
			assertTrue("same length", length.getLength()==lengthCopy.getLength());
		} catch (NoSuchExpressionException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Test
	public void testZestExpressionRegexParsing(){
		ZestExpressionRegex regex=new ZestExpressionRegex("location", "brugboil");
		String regexString=regex.toString();
		ZestExpressionRegex regexCopy;
		try {
			regexCopy = (ZestExpressionRegex)ZestUtils.parseSimpleExpression(regexString);
			assertTrue("same regex", regex.getRegex().equals(regexCopy.getRegex()));
		} catch (NoSuchExpressionException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void testZestExpressionResposeTimeParsing(){
		ZestExpressionResponseTime time=new ZestExpressionResponseTime(1235486);
		String timeString=time.toString();
		ZestExpressionResponseTime timeCopy;
		try {
			timeCopy = (ZestExpressionResponseTime) ZestUtils.parseSimpleExpression(timeString);
			assertTrue("same time", time.getTimeInMs()==timeCopy.getTimeInMs());
		} catch (NoSuchExpressionException e) {
			fail(e.getMessage());
		}
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
		ZestExpressionURL urlCopy;
		try {
			urlCopy = (ZestExpressionURL)ZestUtils.parseSimpleExpression(urlString);
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
		} catch (NoSuchExpressionException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void testZestStructuredExpressionParsing(){
		ZestExpressionAnd and=new ZestExpressionAnd();
		ZestExpressionOr or=new ZestExpressionOr();
		ZestExpression[] expressions=new ZestExpression[6];
		int i=0;
		expressions[i++]=new ZestExpressionStatusCode(100);
		expressions[i++]=new ZestExpressionEquals("location", "Sample string\\");
		expressions[i++]=new ZestExpressionLength("location.body", 10,20);
		expressions[i++]=new ZestExpressionRegex("location.header", "brugboil");
		ZestExpressionResponseTime time=new ZestExpressionResponseTime(1357835);
		ZestExpressionURL url=new ZestExpressionURL();
		for(int j=0; j<10; j++){
			url.getIncludeRegexes().add("includetoken"+j);
		}
		for(int j=10; j<20; j++){
			url.getExcludeRegexes().add("excludetoken"+j);
		}
		time.setInverse(true);
		expressions[i++]=time;
		expressions[i++]=url;
		for(int j=0; j<expressions.length; j++){
			and.addChildCondition(expressions[j]);
			or.addChildCondition(expressions[j]);
		}
		and.addChildCondition(or);
		String andString=and.toString();
		System.out.println("-------------");
		System.out.println(andString);
//		System.exit(0);
		assertTrue("AND:",ZestExpressionAnd.isLiteralInstance(andString));
		String orString=or.toString();
		assertTrue("OR:", ZestExpressionOr.isLiteralInstance(orString));
		ZestExpressionEvaluator evaluator=new ZestExpressionEvaluator();
		ZestExpressionAnd andCopy=(ZestExpressionAnd)evaluator.evaluate(and.toString());
		assertTrue(andCopy.toString().equals(andString));
	}
	@Test
	public void testZestStructuredExpressionManuallyBuiltParsing(){
		ZestExpression[] expressions=new ZestExpression[6];
		int i=0;
		expressions[i++]=new ZestExpressionStatusCode(100);
		expressions[i++]=new ZestExpressionEquals("location", "Sample string\\");
		expressions[i++]=new ZestExpressionLength("any.location", 10,20);
		expressions[i++]=new ZestExpressionRegex("some.location", "brugboil");
		ZestExpressionResponseTime time=new ZestExpressionResponseTime(1357835);
		ZestExpressionURL url=new ZestExpressionURL();
		for(int j=0; j<10; j++){
			url.getIncludeRegexes().add("includetoken"+j);
		}
		for(int j=10; j<20; j++){
			url.getExcludeRegexes().add("excludetoken"+j);
		}
		time.setInverse(true);
		expressions[i++]=time;
		expressions[i++]=url;
		String complexExpression=expressions[0]+"AND"+expressions[1]+"OR( "+expressions[2]+")   AND"+expressions[3];
		ZestExpressionEvaluator evaluator=new ZestExpressionEvaluator();
		ZestExpression builtExpr=evaluator.evaluate(complexExpression);
		ZestExpressionAnd and1=new ZestExpressionAnd();
		and1.addChildCondition(expressions[0]);
		and1.addChildCondition(expressions[1]);
		ZestExpressionOr or=new ZestExpressionOr();
		ZestExpressionAnd and2=new ZestExpressionAnd();
		and2.addChildCondition(expressions[2]);
		and2.addChildCondition(expressions[3]);
		or.addChildCondition(and1);
		or.addChildCondition(and2);
		assertTrue(or.toString().equals(builtExpr.toString()));
	}
	@Test
	public void testZestExpressionInverseParsing(){
		ZestExpressionEquals equals=new ZestExpressionEquals("location", "gnruweomc");
		equals.setInverse(true);
		ZestExpressionEquals copy;
		try {
			copy = (ZestExpressionEquals)ZestUtils.parseSimpleExpression(equals.toString());
			assertTrue(copy.isInverse());
		} catch (NoSuchExpressionException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void testZestExpressionRegexCaseExactParsing(){
		ZestExpressionRegex regex=new ZestExpressionRegex();
		regex.setCaseExact(true);
		regex.setRegex("newoiu4pgn4p");
		regex.setVariableName("$£$£ n3owiue in nin   q03wh");
		String regexString=regex.toString();
		try{
		ZestExpression parsedExpression=ZestUtils.parseSimpleExpression(regexString);
		assertTrue(parsedExpression.toString().equals(regexString));
		} catch(NoSuchExpressionException e){
			fail(e.getMessage());
		}
	}
}
