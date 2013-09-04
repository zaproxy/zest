/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.mozilla.zest.core.v1.NoSuchExpressionException;
import org.mozilla.zest.core.v1.ZestExpression;
import org.mozilla.zest.core.v1.ZestExpressionEquals;
import org.mozilla.zest.core.v1.ZestExpressionLength;
import org.mozilla.zest.core.v1.ZestExpressionRegex;
import org.mozilla.zest.core.v1.ZestExpressionResponseTime;
import org.mozilla.zest.core.v1.ZestExpressionStatusCode;
import org.mozilla.zest.core.v1.ZestExpressionURL;
import org.mozilla.zest.core.v1.ZestResponse;

public class ZestUtils {
	public final static Pattern VARIABLE_NAME_PATTERN = Pattern
			.compile("\\sin\\.*$");
	public final static Pattern CASE_EXACT_PATTERN = Pattern
			.compile("(NOT\\s)?CASE\\sEXACT.*");
	public final static String START_VARIABLE = "{{";
	public final static String START_VARIABLE_REGEX = "\\{\\{";
	public final static String END_VARIABLE = "}}";
	public final static String END_VARIABLE_REGEX = "\\}\\}";

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

	private static List<String> getVariables(final String expression) {
		String literal = expression;
		List<String> variables = new LinkedList<>();
		while (!literal.isEmpty() && literal.contains(START_VARIABLE)
				&& literal.contains(END_VARIABLE)) {
			int start = literal.indexOf(START_VARIABLE);
			int end = literal.indexOf(END_VARIABLE);
			variables.add(literal.substring(start + 2, end));
			literal = literal.substring(end + END_VARIABLE.length());
		}
		return variables;
	}

	private static List<String>[] getURLVariables(final String expression) {
		if (!ZestExpressionURL.isLiteralInstance(expression)) {
			throw new IllegalArgumentException(expression
					+ " is not an instance of "
					+ ZestExpressionURL.class.getSimpleName());
		}
		@SuppressWarnings("unchecked")
		List<String>[] variables = new LinkedList[2];
		variables[0] = new LinkedList<>();
		variables[1] = new LinkedList<>();
		String literal = expression;
		StringTokenizer st = new StringTokenizer(literal);
		Pattern include = Pattern.compile("ACCEPT:");
		Pattern exclude = Pattern.compile("EXCLUDE:");
		String variable = "";
		while (st.hasMoreTokens() && !include.matcher(variable).matches()) {
			variable = st.nextToken();
		}
		// variable contains exactly the pattern "INCLUDE"
		while (st.hasMoreTokens() && !exclude.matcher(variable).matches()) {
			int length = variable.length();
			variable = st.nextToken();
			if (variable.endsWith(",")) {
				variable=variable.substring(0, variable.length()-1);
			}
			if (variable.startsWith(START_VARIABLE)) {
				variable=variable.substring(START_VARIABLE.length());
			}
			if(variable.endsWith(END_VARIABLE)){
				variable=variable.substring(0,variable.length()-END_VARIABLE.length());
			}
			variables[0].add(variable);
		}
		variables[0].remove(variables[0].size()-1);
		System.out.println("£££");
		// variable contains exactly the pattern "EXCLUDE"
		while (st.hasMoreTokens()) {
			variable = st.nextToken();
			if (variable.endsWith(",")) {
				variable=variable.substring(0, variable.length()-1);
			}
			if (variable.startsWith(START_VARIABLE)) {
				variable=variable.substring(START_VARIABLE.length());
			}
			if(variable.endsWith(END_VARIABLE)){
				variable=variable.substring(0, variable.length()-END_VARIABLE.length());
			}
			variables[1].add(variable);
		}
		return variables;
	}

	// private static String clearFromSpaces(String s){
	// String clearedString=s;
	// while(clearedString.charAt(0)==' '){
	// clearedString=clearedString.substring(1);
	// }
	// while(clearedString.charAt(clearedString.length()-1)==' '){
	// clearedString=clearedString.substring(0, clearedString.length()-1);
	// }
	// return clearedString;
	// }
	public static ZestExpression parseSimpleExpression(
			final String literalExpression) throws NoSuchExpressionException {
		ZestExpression toReturn = null;
		List<String> variables = getVariables(literalExpression);
		if (ZestExpressionEquals.isLiteralInstance(literalExpression)) {
			String regex = variables.get(0);
			String variableName = variables.get(1);
			Matcher case_exact_matcher = CASE_EXACT_PATTERN
					.matcher(literalExpression);
			toReturn = new ZestExpressionEquals(variableName, regex,
					case_exact_matcher.matches(), false);
		} else if (ZestExpressionLength.isLiteralInstance(literalExpression)) {
			String lengthS = variables.get(0);
			String approxS = variables.get(1);
			int length = Integer.parseInt(lengthS);
			int approx = Integer.parseInt(approxS);
			approx = approx * 100 / length;
			String variableName = variables.get(2);
			toReturn = new ZestExpressionLength(variableName, length, approx);
		} else if (ZestExpressionRegex.isLiteralInstance(literalExpression)) {
			String regex = variables.get(0);
			String variableName = variables.get(1);
			Matcher case_exact_matcher = CASE_EXACT_PATTERN
					.matcher(literalExpression);
			toReturn = new ZestExpressionRegex(variableName, regex,
					case_exact_matcher.matches(), false);
		} else if (ZestExpressionResponseTime
				.isLiteralInstance(literalExpression)) {
			long timeInMs;
			if (literalExpression.contains("< ")) {
				timeInMs = Long.parseLong(variables.get(0));
				toReturn = new ZestExpressionResponseTime(timeInMs);
				((ZestExpressionResponseTime) toReturn).setGreaterThan(false);
			} else if (literalExpression.contains("> ")) {
				timeInMs = Long.parseLong(variables.get(0));
				toReturn = new ZestExpressionResponseTime(timeInMs);
				((ZestExpressionResponseTime) toReturn).setGreaterThan(true);
			}
		} else if (ZestExpressionStatusCode
				.isLiteralInstance(literalExpression)) {
			int code = Integer.parseInt(variables.get(0));
			toReturn = new ZestExpressionStatusCode(code);
		} else if (ZestExpressionURL.isLiteralInstance(literalExpression)) {
			List<String>[] regexes = getURLVariables(literalExpression);
			List<String> includeRegexes = regexes[0];
			List<String> excludeRegexes = regexes[1];
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
}
