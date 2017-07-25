package org.masking.finexpr.operator;

import java.math.BigDecimal;
import java.math.MathContext;

import org.masking.finexpr.expr.ExprException;

public class DivideOperator extends Operator {

	public static final String SYMBOL = "/";

	private static DivideOperator instance = new DivideOperator();

	private DivideOperator() {
		super(SYMBOL, 2, true, OperatorPrecedenceCode.MULTIPLY_DIVIDE);
	}

	@Override
	public BigDecimal apply(MathContext mc, BigDecimal... args) {
		if (mc == null)
			throw new ExprException("the MathContext cannot be null!");
		return args[0].divide(args[1], mc);
	}

	public static DivideOperator getInstance() {
		return instance;
	}
}
