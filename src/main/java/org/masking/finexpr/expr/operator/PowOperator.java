package org.masking.finexpr.expr.operator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.masking.finexpr.expr.ExprException;

public class PowOperator extends Operator {

	public static final String SYMBOL = "^";

	private static PowOperator instance = new PowOperator();

	private PowOperator() {
		super(SYMBOL, 2, false, OperatorPrecedenceCode.POW);
	}

	@Override
	public BigDecimal apply(MathContext mc, BigDecimal... args) {
		if (mc == null)
			throw new ExprException("the MathContext cannot be null!");

		/**
		 * thanks evalex, the implement is from
		 * https://github.com/uklimaschewski/EvalEx/blob/master/src/com/udojava/
		 * evalex/Expression.java
		 */
		int signOf2 = args[1].signum();
		double dn1 = args[0].doubleValue();
		args[1] = args[1].multiply(new BigDecimal(signOf2)); // n2 is now
																// positive
		BigDecimal remainderOf2 = args[1].remainder(BigDecimal.ONE);
		BigDecimal n2IntPart = args[1].subtract(remainderOf2);
		BigDecimal intPow = args[0].pow(n2IntPart.intValueExact(), mc);
		BigDecimal doublePow = new BigDecimal(Math.pow(dn1, remainderOf2.doubleValue()));

		BigDecimal result = intPow.multiply(doublePow, mc);
		if (signOf2 == -1) {
			result = BigDecimal.ONE.divide(result, mc.getPrecision(), RoundingMode.HALF_UP);
		}
		return result;
	}

	public static PowOperator getInstance() {
		return instance;
	}
}
