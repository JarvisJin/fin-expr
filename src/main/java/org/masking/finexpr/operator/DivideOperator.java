package org.masking.finexpr.operator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import org.masking.finexpr.expr.ExprException;

public class DivideOperator extends Operator {

	public static final String SYMBOL = "/";

	private static DivideOperator instance = new DivideOperator();

	private DivideOperator() {
		super(SYMBOL, 2, true, OperatorPrecedenceCode.MULTIPLY_DIVIDE);
	}

	@Override
	public BigDecimal apply(MathContext mc, List<BigDecimal> args) {
		if (mc == null)
			throw new ExprException("the MathContext cannot be null!");
		return args.get(0).divide(args.get(1), mc);
	}

	public static DivideOperator getInstance() {
		return instance;
	}
}
