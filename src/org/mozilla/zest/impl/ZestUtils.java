/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

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
import org.mozilla.zest.core.v1.ZestResponse;

public class ZestUtils {
	private static Pattern genericExpressionPattern = null;

	/**
	 * @param args
	 */
	public static List<String> getForms(ZestResponse response) {
		List<String> list = new ArrayList<String>();
		Source src = new Source(response.getHeaders() + response.getBody());
		List<Element> formElements = src.getAllElements(HTMLElementName.FORM);
		int formId = 0;
		while (formElements != null && formId < formElements.size()) {
			// TODO support form names
			// Element form = formElements.get(formId);
			list.add(Integer.toString(formId));

			formId++;
		}
		return list;
	}

	public static List<String> getFields(ZestResponse response, int formId) {
		List<String> list = new ArrayList<String>();

		Source src = new Source(response.getHeaders() + response.getBody());
		List<Element> formElements = src.getAllElements(HTMLElementName.FORM);
		if (formElements != null && formId < formElements.size()) {
			Element form = formElements.get(formId);
			List<Element> inputElements = form
					.getAllElements(HTMLElementName.INPUT);
			String field;

			for (Element inputElement : inputElements) {
				field = inputElement.getAttributeValue("ID");
				if (field == null || field.length() == 0) {
					field = inputElement.getAttributeValue("NAME");
				}
				if (field != null && field.length() > 0) {
					list.add(field);
				}
			}
		}
		return list;
	}
	private static String clearFromSpaces(String s){
		String clearedString=s;
		while(clearedString.charAt(0)==' '){
			clearedString=clearedString.substring(1);
		}
		while(clearedString.charAt(clearedString.length()-1)==' '){
			clearedString=clearedString.substring(0, clearedString.length()-1);
		}
		return clearedString;
	}
	private static Pattern getGenericExpressionPattern(){
		if(genericExpressionPattern==null){
			String pattern="";
			pattern+="("+ZestExpressionAnd.getPattern()+")|";
			pattern+="("+ZestExpressionEquals.getPattern()+")|";
			pattern+="("+ZestExpressionLength.getPattern()+")|";
			pattern+="("+ZestExpressionOr.getPattern()+")|";
			pattern+="("+ZestExpressionRegex.getPattern()+")|";
			pattern+="("+ZestExpressionResponseTime.getPattern()+")|";
			pattern+="("+ZestExpressionURL.getPattern()+")|";
			pattern+="("+ZestExpressionStatusCode.getPattern()+")";
			pattern+="";
			genericExpressionPattern=Pattern.compile(pattern);
		}
		return genericExpressionPattern;		
	}
	public static ZestExpression parseSimpleExpression(final String literalExpression) throws NoSuchExpressionException {
		ZestExpression toReturn = null;
		if (ZestExpressionEquals.isLiteralInstance(literalExpression)) {
			String regex = literalExpression.substring(literalExpression
					.indexOf(": ") + 2);
			regex=clearFromSpaces(regex);
			final String caseExactPattern = "(NOT\\s)?CASE\\sEXACT";
			toReturn = new ZestExpressionEquals("", regex,
					literalExpression.matches(caseExactPattern), false);
		} else if (ZestExpressionLength.isLiteralInstance(literalExpression)) {
			String lengthS = literalExpression.substring(
					literalExpression.indexOf(": ") + 2,
					literalExpression.indexOf("+/-") - 1);
			lengthS=clearFromSpaces(lengthS);
			String approxS = literalExpression.substring(literalExpression
					.indexOf("+/- ") + 4);
			int length = Integer.parseInt(lengthS);
			int approx = Integer.parseInt(approxS);
			approx = approx * 100 / length;
			toReturn = new ZestExpressionLength("", length, approx);
		} else if (ZestExpressionRegex.isLiteralInstance(literalExpression)) {
			String regex = literalExpression.substring(literalExpression
					.indexOf(": ") + 2);
			regex=clearFromSpaces(regex);
			toReturn = new ZestExpressionRegex("", regex);
		} else if (ZestExpressionResponseTime
				.isLiteralInstance(literalExpression)) {
			long timeInMs;
			if (literalExpression.contains("< ")) {
				timeInMs = Long.parseLong(clearFromSpaces(literalExpression
						.substring(literalExpression.indexOf("< ") + 2)));
				toReturn = new ZestExpressionResponseTime(timeInMs);
				((ZestExpressionResponseTime) toReturn).setGreaterThan(false);
			} else if (literalExpression.contains("> ")) {
				timeInMs = Long.parseLong(clearFromSpaces(literalExpression
						.substring(literalExpression.indexOf("> ") + 2)));
				toReturn = new ZestExpressionResponseTime(timeInMs);
				((ZestExpressionResponseTime) toReturn).setGreaterThan(true);
			}
		} else if (ZestExpressionStatusCode
				.isLiteralInstance(literalExpression)) {
			int code = Integer.parseInt(clearFromSpaces(literalExpression
					.substring(literalExpression.indexOf(": ") + 2)));
			toReturn = new ZestExpressionStatusCode(code);
		} else if (ZestExpressionURL.isLiteralInstance(literalExpression)) {
			List<String> includeRegexes = new LinkedList<>();
			List<String> excludeRegexes = new LinkedList<>();
			StringTokenizer st = new StringTokenizer(
					literalExpression.substring(literalExpression
							.indexOf("ACCEPT:") + 7));
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				if ("EXCLUDE:".equals(token)) {
					if (!includeRegexes.isEmpty()) {
						String lastIncluded = includeRegexes
								.remove(includeRegexes.size() - 1);
						lastIncluded = lastIncluded.substring(0,
								lastIncluded.length() - 1);// removes the comma
						includeRegexes.add(lastIncluded);
					}
					while (st.hasMoreTokens()) {
						token = st.nextToken();
						excludeRegexes.add(token);
					}
				} else {
					includeRegexes.add(token);
				}
			}
			toReturn = new ZestExpressionURL(includeRegexes, excludeRegexes);
		} else {
			throw new NoSuchExpressionException(literalExpression);
		}
		if (literalExpression.startsWith("NOT")) {
			toReturn.setInverse(true);
		} else {
			toReturn.setInverse(false);
		}
		return toReturn;
	}
	private static boolean isKnownExpression(String literal) {
		return ZestExpressionAnd.isLiteralInstance(literal)
				|| ZestExpressionOr.isLiteralInstance(literal)
				|| ZestExpressionURL.isLiteralInstance(literal)
				|| ZestExpressionEquals.isLiteralInstance(literal)
				|| ZestExpressionLength.isLiteralInstance(literal)
				|| ZestExpressionRegex.isLiteralInstance(literal)
				|| ZestExpressionResponseTime.isLiteralInstance(literal)
				|| ZestExpressionStatusCode.isLiteralInstance(literal);
	}

}
