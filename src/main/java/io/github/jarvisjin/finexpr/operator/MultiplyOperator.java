package io.github.jarvisjin.finexpr.operator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import io.github.jarvisjin.finexpr.expr.ExprException;

public class MultiplyOperator extends Operator {

	public static final String SYMBOL = "*";

	private static MultiplyOperator instance = new MultiplyOperator();

	private MultiplyOperator() {
		super(SYMBOL, 2, true, OperatorPrecedenceCode.MULTIPLY_DIVIDE);
	}

	@Override
	public BigDecimal apply(List<BigDecimal> args, MathContext mc) {
		if (mc == null)
			throw new ExprException("the MathContext cannot be null!");
		return args.get(0).multiply(args.get(1), mc);
	}

	public static MultiplyOperator getInstance() {
		return instance;
	}
}
