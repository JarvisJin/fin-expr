package io.github.jarvisjin.finexpr.operator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import io.github.jarvisjin.finexpr.expr.ExprException;

public class UnaryMinusOperator extends Operator {

	public static final String SYMBOL = "-u";

	private static UnaryMinusOperator instance = new UnaryMinusOperator();

	private UnaryMinusOperator() {
		super(SYMBOL, 1, false, OperatorPrecedenceCode.UNARY_PLUS_MINUS);
	}

	@Override
	public BigDecimal apply(List<BigDecimal> args, MathContext mc) {
		if (mc == null)
			throw new ExprException("the MathContext cannot be null!");
		return args.get(0).negate(mc);
	}

	public static UnaryMinusOperator getInstance() {
		return instance;
	}
}
