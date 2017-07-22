package org.masking.finexpr.expr;

import java.math.BigDecimal;

public abstract class Operator {

	private final String symbol;
	private final int operandNum;
	private final boolean leftAssociative;
	private final int precedence;

	public Operator(String symbol, int operandNum, boolean leftAssociative, int precedence) {
		this.symbol=symbol;
		this.operandNum=operandNum;
		this.leftAssociative=leftAssociative;
		this.precedence=precedence;
	};

	public abstract BigDecimal apply(BigDecimal... args);

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
