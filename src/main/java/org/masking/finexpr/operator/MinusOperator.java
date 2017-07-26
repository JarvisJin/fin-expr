package org.masking.finexpr.operator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import org.masking.finexpr.expr.ExprException;

public class MinusOperator extends Operator {

	public static final String SYMBOL = "-";

	private static MinusOperator instance = new MinusOperator();

	private MinusOperator() {
		super(SYMBOL, 2, true, OperatorPrecedenceCode.PLUS_MINUS);
	}

	@Override
	public BigDecimal apply(MathContext mc, List<BigDecimal> args) {
		if (mc == null)
			throw new ExprException("the MathContext cannot be null!");
		return args.get(0).subtract(args.get(1), mc);
	}

	public static MinusOperator getInstance() {
		return instance;
	}
}
