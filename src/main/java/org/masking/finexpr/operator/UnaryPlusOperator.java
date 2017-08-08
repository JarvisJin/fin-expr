package org.masking.finexpr.operator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import org.masking.finexpr.expr.ExprException;

public class UnaryPlusOperator extends Operator {

	public static final String SYMBOL = "+u";

	private static UnaryPlusOperator instance = new UnaryPlusOperator();

	private UnaryPlusOperator() {
		super(SYMBOL, 1, false, OperatorPrecedenceCode.UNARY_PLUS_MINUS);
	}

	@Override
	public BigDecimal apply(List<BigDecimal> args, MathContext mc) {
		if (mc == null)
			throw new ExprException("the MathContext cannot be null!");
		return args.get(0).add(BigDecimal.ZERO, mc);
	}

	public static UnaryPlusOperator getInstance() {
		return instance;
	}
}
