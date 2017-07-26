package org.masking.finexpr.operator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

public abstract class Operator {

	private final String symbol;
	private final int operandNum;
	private final boolean leftAssociative;
	private final int precedence;

	public Operator(String symbol, int operandNum, boolean leftAssociative, int precedence) {
		this.symbol = symbol;
		this.operandNum = operandNum;
		this.leftAssociative = leftAssociative;
		this.precedence = precedence;
	};

	public abstract BigDecimal apply(MathContext mc, List<BigDecimal> args);

	public String getSymbol() {
		return symbol;
	}

	public int getOperandNum() {
		return operandNum;
	}

	public boolean isLeftAssociative() {
		return leftAssociative;
	}

	public int getPrecedence() {
		return precedence;
	}
}
