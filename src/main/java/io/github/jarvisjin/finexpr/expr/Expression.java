/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.jarvisjin.finexpr.expr;

import java.util.Map;
import java.util.Set;
import java.util.Stack;

import io.github.jarvisjin.finexpr.function.Function;
import io.github.jarvisjin.finexpr.operator.DivideOperator;
import io.github.jarvisjin.finexpr.operator.MinusOperator;
import io.github.jarvisjin.finexpr.operator.MultiplyOperator;
import io.github.jarvisjin.finexpr.operator.Operator;
import io.github.jarvisjin.finexpr.operator.PlusOperator;
import io.github.jarvisjin.finexpr.operator.PowOperator;
import io.github.jarvisjin.finexpr.operator.UnaryMinusOperator;
import io.github.jarvisjin.finexpr.operator.UnaryPlusOperator;
import io.github.jarvisjin.finexpr.token.NumberToken;
import io.github.jarvisjin.finexpr.token.ShuntingYard;
import io.github.jarvisjin.finexpr.token.Token;
import io.github.jarvisjin.finexpr.token.TokenType;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * new the instance of Expression to calculate.
 * 
 * @author JarvisJin
 *
 */
public class Expression {

	/** the origin infix expression */
	private final String expr;
	private List<Token> rpn;
	private MathContext mc;

	private Map<String, Operator> opMap = new HashMap<String, Operator>();
	private Map<String, BigDecimal> varMap = new HashMap<String, BigDecimal>();
	private Map<String, Function> funcMap = new HashMap<String, Function>();

	public Expression(String expr) {
		this(expr, MathContext.DECIMAL64);
	}

	public Expression(String expr, MathContext mc) {
		assert mc != null;

		this.expr = expr.trim();
		this.mc = mc;
		initDefaultOperator();
	}

	public void compile() {
		if (rpn == null) {
			rpn = ShuntingYard.generateRPN(expr, opMap);
		}
	}
	
	public Set<String> getUsedFunction(){
		if(rpn == null) {
			throw new RuntimeException("The Expression need to be compiled befero getUsedFunction!");
		}
		Set<String> usedFunc = new HashSet<>();
		for (Token t : rpn) {
			if(TokenType.FUNCTION.equals(t.getType())) {
				usedFunc.add(t.getContent());
			}
		}
		return usedFunc;
	}
	
	public BigDecimal calculate() {
		
		Stack<Token> numStack = new Stack<Token>();
		compile();
		
		for (Token t : rpn) {
			switch (t.getType()) {

			case OPEN_PAREN:
			case NUMBER:
			case VARIABLE:
				numStack.push(t);
				break;

			case FUNCTION:
				Function func = funcMap.get(t.getContent());
				if (func != null) {
					List<BigDecimal> params = new ArrayList<BigDecimal>();
					while (!numStack.isEmpty() && numStack.peek().getType() != TokenType.OPEN_PAREN) {
						params.add(toNumber(numStack.pop()));
					}
					if (!numStack.isEmpty()) {
						numStack.pop();
						if (func.getArgNum() != -1 && func.getArgNum() != params.size()) {
							throw new ExprException(
									"Error, the number of params for function " + t.getContent() + " is incorrect.");
						}
						numStack.push(new NumberToken(func.apply(reverse(params), mc)));
					} else {
						throw new ExprException("Error, function " + t.getContent() + " without (.");
					}
				} else {
					throw new ExprException(
							"Unkown function '" + t.getContent() + "' at Expression:" + (t.getPos() + 1));
				}
				break;

			case OPERATOR:
				Operator op = opMap.get(t.getContent());
				if (op != null) {
					List<BigDecimal> params = new ArrayList<BigDecimal>();
					while (!numStack.isEmpty() && params.size() < op.getOperandNum()) {
						params.add(toNumber(numStack.pop()));
					}
					if (op.getOperandNum() != params.size()) {
						throw new ExprException("Error, operator " + t.getContent() + " donot have enough operands!");
					}
					numStack.push(new NumberToken(op.apply(reverse(params), mc)));

				} else {
					throw new ExprException("Unkown operator '" + t.getContent() + "' at " + (t.getPos() + 1));
				}
				break;

			default:
				throw new ExprException("Invalid token in rpn position:" + (t.getPos() + 1));
			}
		}

		if (numStack.isEmpty() || numStack.peek().getType() != TokenType.NUMBER) {
			throw new ExprException("the expression is invalid!");
		}

		BigDecimal result = toNumber(numStack.pop());
		if (!numStack.isEmpty()) {
			throw new ExprException("the expression is invalid!");
		}
		return result;
	}

	private BigDecimal toNumber(Token t) {
		if (t instanceof NumberToken) {
			return ((NumberToken) t).getValue();
		}
		switch (t.getType()) {
		case NUMBER:
			try {
				return new BigDecimal(t.getContent(), mc);
			} catch (NumberFormatException e) {
				throw new ExprException("Invalid number at " + (t.getPos() + 1) + " \n " + e.getMessage());
			}
		case VARIABLE:
			BigDecimal var = varMap.get(t.getContent());
			if (var != null) {
				return var;
			} else {
				throw new ExprException("Unkown variable '" + t.getContent() + "' at " + (t.getPos() + 1));
			}
		default:
			throw new ExprException("expected token should be NUMBER or VARIABLE");
		}
	}

	private List<BigDecimal> reverse(List<BigDecimal> origin) {
		List<BigDecimal> result = new ArrayList<BigDecimal>(origin.size());
		for (int i = origin.size() - 1; i > -1; i--) {
			result.add(origin.get(i));
		}
		return result;
	}

	/**
	 * add custom operator
	 * 
	 * @param op
	 *            the {@code Operator} to be added
	 * @param replaceOnDuplicate
	 *            when {@code replaceOnDuplicate==true}, if there is already an
	 *            operator {@code oldOp} with the same symbol, {@code oldOp} will be
	 *            replaced by {@code op}.
	 */
	public Expression addOperator(Operator op, boolean replaceOnDuplicate) {
		assert op != null;
		if (opMap.containsKey(op.getSymbol()) && (!replaceOnDuplicate)) {
			throw new ExprException("the operator '" + op.getSymbol() + "' has already exsit!");
		}
		opMap.put(op.getSymbol(), op);
		return this;
	}

	public Expression addOperator(Operator op) {
		return addOperator(op, false);
	}

	/**
	 * add custom function
	 * 
	 * @param func
	 *            the {@code Function} to be added
	 * @param replaceOnDuplicate
	 *            when {@code replaceOnDuplicate==true}, if there is already an
	 *            function {@code oldFunc} with the same symbol, {@code oldFunc}
	 *            will be replaced by {@code func}.
	 * @return
	 */
	public Expression addFunction(Function func, boolean replaceOnDuplicate) {
		assert func != null;
		if (funcMap.containsKey(func.getName()) && (!replaceOnDuplicate)) {
			throw new ExprException("the function '" + func.getName() + "' has already exsit!");
		}
		funcMap.put(func.getName(), func);
		return this;
	}

	public Expression addFunction(Function func) {
		return addFunction(func, false);
	}

	/**
	 * add custom variable
	 */
	public Expression addVariable(String name, BigDecimal value, boolean replaceOnDuplicate) {
		assert name != null && value != null;
		if (varMap.containsKey(name) && (!replaceOnDuplicate)) {
			throw new ExprException("the operator '" + name + "' has already exsit!");
		}
		varMap.put(name, value.round(mc));
		return this;
	}

	public Expression addVariable(String name, BigDecimal value) {
		return addVariable(name, value, false);
	}

	/**
	 * Removes all of the variables added to the Expression The variables in the
	 * Expression will be empty after this call returns, and can be reset
	 *
	 */
	public void clearVariables() {
		varMap.clear();
	}

	/**
	 * init defaul operator +, -, *, /, ^, -(unary), +(unary)
	 */
	private void initDefaultOperator() {
		opMap.put(PlusOperator.SYMBOL, PlusOperator.getInstance());
		opMap.put(MinusOperator.SYMBOL, MinusOperator.getInstance());
		opMap.put(MultiplyOperator.SYMBOL, MultiplyOperator.getInstance());
		opMap.put(DivideOperator.SYMBOL, DivideOperator.getInstance());
		opMap.put(PowOperator.SYMBOL, PowOperator.getInstance());
		opMap.put(UnaryMinusOperator.SYMBOL, UnaryMinusOperator.getInstance());
		opMap.put(UnaryPlusOperator.SYMBOL, UnaryPlusOperator.getInstance());
	}
}
