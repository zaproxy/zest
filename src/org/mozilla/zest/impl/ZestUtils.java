/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.mozilla.zest.core.v1.ZestExpression;
import org.mozilla.zest.core.v1.ZestExpressionEquals;
import org.mozilla.zest.core.v1.ZestExpressionLength;
import org.mozilla.zest.core.v1.ZestExpressionRegex;
import org.mozilla.zest.core.v1.ZestExpressionResponseTime;
import org.mozilla.zest.core.v1.ZestExpressionStatusCode;
import org.mozilla.zest.core.v1.ZestExpressionURL;
import org.mozilla.zest.core.v1.ZestResponse;

public class ZestUtils {

	/**
	 * @param args
	 */
	public static List<String> getForms (ZestResponse response) {
		List<String> list = new ArrayList<String>();
		Source src = new Source(response.getHeaders() + response.getBody());
		List<Element> formElements = src.getAllElements(HTMLElementName.FORM);
		int formId = 0;
		while (formElements != null && formId < formElements.size()) {
			// TODO support form names
			//Element form = formElements.get(formId);
			list.add(Integer.toString(formId));
			
			formId++;
		}
		return list;
	}

	public static List<String> getFields (ZestResponse response, int formId) {
		List<String> list = new ArrayList<String>();
		
		Source src = new Source(response.getHeaders() + response.getBody());
		List<Element> formElements = src.getAllElements(HTMLElementName.FORM);
		if (formElements != null && formId < formElements.size()) {
			Element form = formElements.get(formId);
			List<Element> inputElements = form.getAllElements(HTMLElementName.INPUT);
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
	public static ZestExpression parseExpression(final String literalExpression){
		ZestExpression toReturn=null;
		if(ZestExpressionEquals.isLiteralInstance(literalExpression)){
			String regex=literalExpression.substring(literalExpression.indexOf(": ")+2);
			final String caseExactPattern="(NOT\\s)?CASE\\sEXACT";
			toReturn=new ZestExpressionEquals("", regex, literalExpression.matches(caseExactPattern), false);
		} else if (ZestExpressionLength.isLiteralInstance(literalExpression)){
			String lengthS=literalExpression.substring(literalExpression.indexOf(": ")+2, literalExpression.indexOf("+/-")-1);
			String approxS=literalExpression.substring(literalExpression.indexOf("+/- ")+4);
			int length=Integer.parseInt(lengthS);
			int approx=Integer.parseInt(approxS);
			approx=approx*100/length;
			toReturn=new ZestExpressionLength("", length, approx);
		} else if (ZestExpressionRegex.isLiteralInstance(literalExpression)){
			String regex=literalExpression.substring(literalExpression.indexOf(": ")+2);
			toReturn=new ZestExpressionRegex("", regex);
		} else if (ZestExpressionResponseTime.isLiteralInstance(literalExpression)){
			long timeInMs;
			if(literalExpression.contains("< ")){
				timeInMs=Long.parseLong(literalExpression.substring(literalExpression.indexOf("< ")+2));
				toReturn=new ZestExpressionResponseTime(timeInMs);
				((ZestExpressionResponseTime)toReturn).setGreaterThan(false);
			} else if(literalExpression.contains("> ")){
				timeInMs=Long.parseLong(literalExpression.substring(literalExpression.indexOf("> ")+2));
				toReturn=new ZestExpressionResponseTime(timeInMs);
				((ZestExpressionResponseTime)toReturn).setGreaterThan(true);
			}
		} else if (ZestExpressionStatusCode.isLiteralInstance(literalExpression)){
			int code=Integer.parseInt(literalExpression.substring(literalExpression.indexOf(": ")+2));
			toReturn=new ZestExpressionStatusCode(code);
		} else if(ZestExpressionURL.isLiteralInstance(literalExpression)){
			List<String> includeRegexes=new LinkedList<>();
			List<String> excludeRegexes=new LinkedList<>();
			StringTokenizer st=new StringTokenizer(literalExpression.substring(literalExpression.indexOf("ACCEPT:")+7));
			while(st.hasMoreTokens()){
				String token= st.nextToken();
				if("EXCLUDE:".equals(token)){
					if(!includeRegexes.isEmpty()){
						String lastIncluded=includeRegexes.remove(includeRegexes.size()-1);
						lastIncluded=lastIncluded.substring(0, lastIncluded.length()-1);//removes the comma
						includeRegexes.add(lastIncluded);
					}
					while(st.hasMoreTokens()){
						token=st.nextToken();
						excludeRegexes.add(token);
					}
				}
				else{
					includeRegexes.add(token);
				}
			}
			toReturn=new ZestExpressionURL(includeRegexes, excludeRegexes);
		} else{
			throw new IllegalArgumentException("The given literal expression had not been recognized. It can be bad formulated");
		}
		if(literalExpression.startsWith("NOT")){
			toReturn.setInverse(true);
		} else{
			toReturn.setInverse(false);
		}
		return toReturn;
	}

}
