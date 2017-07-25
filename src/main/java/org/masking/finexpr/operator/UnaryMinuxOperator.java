package org.masking.finexpr.operator;

import java.math.BigDecimal;
import java.math.MathContext;

import org.masking.finexpr.expr.ExprException;

public class UnaryMinuxOperator extends Operator {

	public static final String SYMBOL = "-u";

	private static UnaryMinuxOperator instance = new UnaryMinuxOperator();

	private UnaryMinuxOperator() {
		super(SYMBOL, 1, false, OperatorPrecedenceCode.UNARY_PLUS_MINUS);
	}

	@Override
	public BigDecimal apply(MathContext mc, BigDecimal... args) {
		if (mc == null)
			throw new ExprException("the MathContext cannot be null!");
		return args[0].negate(mc);
	}

	public static UnaryMinuxOperator getInstance() {
		return instance;
	}
}
