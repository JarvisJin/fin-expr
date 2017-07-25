package org.masking.finexpr.expr.operator;

import java.math.BigDecimal;
import java.math.MathContext;

import org.masking.finexpr.expr.ExprException;

public class MultiplyOperator extends Operator {

	public static final String SYMBOL = "*";

	private static MultiplyOperator instance = new MultiplyOperator();

	private MultiplyOperator() {
		super(SYMBOL, 2, true, OperatorPrecedenceCode.MULTIPLY_DIVIDE);
	}

	@Override
	public BigDecimal apply(MathContext mc, BigDecimal... args) {
		if (mc == null)
			throw new ExprException("the MathContext cannot be null!");
		return args[0].multiply(args[1], mc);
	}

	public static MultiplyOperator getInstance() {
		return instance;
	}
}
