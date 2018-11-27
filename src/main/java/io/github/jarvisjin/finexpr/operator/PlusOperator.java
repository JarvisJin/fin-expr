package io.github.jarvisjin.finexpr.operator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import io.github.jarvisjin.finexpr.expr.ExprException;

public class PlusOperator extends Operator {

	public static final String SYMBOL = "+";

	private static PlusOperator instance = new PlusOperator();

	private PlusOperator() {
		super(SYMBOL, 2, true, OperatorPrecedenceCode.PLUS_MINUS);
	}

	@Override
	public BigDecimal apply(List<BigDecimal> args, MathContext mc) {
		if (mc == null)
			throw new ExprException("the MathContext cannot be null!");
		return args.get(0).add(args.get(1), mc);
	}

	public static PlusOperator getInstance() {
		return instance;
	}

}
