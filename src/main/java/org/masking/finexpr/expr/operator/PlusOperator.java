package org.masking.finexpr.expr.operator;

import java.math.BigDecimal;
import java.math.MathContext;

import org.masking.finexpr.expr.ExprException;

public class PlusOperator extends Operator {

	public static final String SYMBOL = "+";

	private static PlusOperator instance = new PlusOperator();

	private PlusOperator() {
		super(SYMBOL, 2, true, OperatorPrecedenceCode.PLUS_MINUS);
	}

	@Override
	public BigDecimal apply(MathContext mc, BigDecimal... args) {
		if (mc == null)
			throw new ExprException("the MathContext cannot be null!");
		return args[0].add(args[1], mc);
	}

	public static PlusOperator getInstance() {
		return instance;
	}

}
