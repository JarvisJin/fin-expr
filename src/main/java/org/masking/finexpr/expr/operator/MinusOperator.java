package org.masking.finexpr.expr.operator;

import java.math.BigDecimal;
import java.math.MathContext;

import org.masking.finexpr.expr.ExprException;

public class MinusOperator extends Operator {

	public static final String SYMBOL = "-";

	private static MinusOperator instance = new MinusOperator();

	private MinusOperator() {
		super(SYMBOL, 2, true, OperatorPrecedenceCode.PLUS_MINUS);
	}

	@Override
	public BigDecimal apply(MathContext mc, BigDecimal... args) {
		if (mc == null)
			throw new ExprException("the MathContext cannot be null!");
		return args[0].subtract(args[1], mc);
	}

	public static MinusOperator getInstance() {
		return instance;
	}
}
