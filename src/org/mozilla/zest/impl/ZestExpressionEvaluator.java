/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.impl;

import java.util.Hashtable;
import java.util.Iterator;

import net.astesana.javaluator.AbstractEvaluator;
import net.astesana.javaluator.BracketPair;
import net.astesana.javaluator.Operator;
import net.astesana.javaluator.Parameters;

import org.mozilla.zest.core.v1.NoSuchExpressionException;
import org.mozilla.zest.core.v1.ZestExpression;
import org.mozilla.zest.core.v1.ZestExpressionAnd;
import org.mozilla.zest.core.v1.ZestExpressionElement;
import org.mozilla.zest.core.v1.ZestExpressionOr;
import org.mozilla.zest.core.v1.ZestStructuredExpression;

public class ZestExpressionEvaluator extends AbstractEvaluator<ZestExpression> {
	private static final Parameters PARAMETERS = new Parameters();
	private final static Operator AND;
	private static final Operator OR;
	private static final Operator NOT;
	private final Hashtable<ZestExpression, ZestStructuredExpression> table = new Hashtable<>();
	static {
		AND = new Operator("AND", 2, Operator.Associativity.LEFT, 2);
		OR = new Operator("OR", 2, Operator.Associativity.LEFT, 1);
		NOT = new Operator("NOT", 1, Operator.Associativity.RIGHT, 3);
		// PARAMETERS.add(new Operator("and", 2, Operator.Associativity.LEFT,
		// 2));
		// PARAMETERS.add(new Operator("&&", 2, Operator.Associativity.LEFT,
		// 2));
		// PARAMETERS.add(new Operator("&", 2, Operator.Associativity.LEFT, 2));
		// PARAMETERS.add(new Operator("or", 2, Operator.Associativity.LEFT,
		// 1));
		// PARAMETERS.add(new Operator("||", 2, Operator.Associativity.LEFT,
		// 1));
		// PARAMETERS.add(new Operator("|", 2, Operator.Associativity.LEFT, 1));
		// PARAMETERS.add(new Operator("not", 1, Operator.Associativity.RIGHT,
		// 3));
		// PARAMETERS.add(new Operator("!", 1, Operator.Associativity.RIGHT,
		// 3));
		PARAMETERS.add(AND);
		PARAMETERS.add(OR);
		PARAMETERS.add(NOT);
		PARAMETERS.addExpressionBracket(BracketPair.PARENTHESES);// ()
		PARAMETERS.addExpressionBracket(BracketPair.BRACKETS);// []
		PARAMETERS.addExpressionBracket(BracketPair.BRACES);// {}
	}

	public ZestExpressionEvaluator() {
		super(PARAMETERS);
	}

	@Override
	protected ZestExpression toValue(String literal, Object arg1) {
		try {
			return ZestUtils.parseSimpleExpression(literal);
		} catch (NoSuchExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected ZestExpression evaluate(Operator operator,
			Iterator<ZestExpression> operands, Object evaluationContext) {
		ZestExpression toReturn=null;
		ZestExpression exp1=operands.next();
		if(operator==NOT){
			exp1.setInverse(!exp1.isInverse());
			toReturn=exp1;
		} else{
			ZestExpression exp2=operands.next();
			if(operator==AND){
				if(exp1 instanceof ZestExpressionAnd && exp2 instanceof ZestExpressionAnd){
					for(ZestExpressionElement exp:((ZestExpressionAnd)exp2).getChildrenCondition()){
						((ZestExpressionAnd)exp1).addChildCondition(exp);
					}
					toReturn = exp1;
				} else if(exp1 instanceof ZestExpressionAnd && !(exp2 instanceof ZestExpressionAnd)){
					((ZestExpressionAnd)exp1).addChildCondition(exp2);
					toReturn= exp1;
				} else if(exp2 instanceof ZestExpressionAnd && !(exp1 instanceof ZestExpressionAnd)){
					((ZestExpressionAnd) exp2).addChildCondition(exp1);
					toReturn= exp2;
				} else {
					ZestExpressionAnd and=new ZestExpressionAnd();
					and.addChildCondition(exp1);
					and.addChildCondition(exp2);
					toReturn= and;
				}
			}
			else if(operator==OR){
				if(exp1 instanceof ZestExpressionOr && exp2 instanceof ZestExpressionOr){
					for(ZestExpressionElement expr:((ZestExpressionOr)exp2).getChildrenCondition()){
						((ZestExpressionOr)exp1).addChildCondition(expr);
					}
					toReturn= exp1;
				} else if(exp1 instanceof ZestExpressionOr && !(exp2 instanceof ZestExpressionOr)){
					((ZestExpressionOr)exp1).addChildCondition(exp2);
					toReturn= exp1;
				} else if(exp2 instanceof ZestExpressionOr){
					((ZestExpressionOr)exp2).addChildCondition(exp1);
					toReturn= exp2;
				} else{
					ZestExpressionOr or=new ZestExpressionOr();
					or.addChildCondition(exp1);
					or.addChildCondition(exp2);
					toReturn= or;
				}
			}
		}
		return toReturn;
	}

}
