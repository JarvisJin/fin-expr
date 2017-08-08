package org.masking.finexpr.operator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

import org.masking.finexpr.expr.ExprException;

public class PowOperator extends Operator {

	public static final String SYMBOL = "^";

	private static PowOperator instance = new PowOperator();

	private PowOperator() {
		super(SYMBOL, 2, false, OperatorPrecedenceCode.POW);
	}

	@Override
	public BigDecimal apply(List<BigDecimal> args, MathContext mc) {
		if (mc == null)
			throw new ExprException("the MathContext cannot be null!");

		/**
		 * thanks evalex, the implement is from
		 * https://github.com/uklimaschewski/EvalEx/blob/master/src/com/udojava/
		 * evalex/Expression.java
		 */
		BigDecimal v1 = args.get(0), v2 = args.get(1);
		
		int signOf2 = v2.signum();
		double dn1 = v1.doubleValue();
		v2 = v2.multiply(new BigDecimal(signOf2)); // n2 is now positive
		BigDecimal remainderOf2 = v2.remainder(BigDecimal.ONE);
		BigDecimal n2IntPart = v2.subtract(remainderOf2);
		BigDecimal intPow = v1.pow(n2IntPart.intValueExact(), mc);
		BigDecimal doublePow = new BigDecimal(Math.pow(dn1,
				remainderOf2.doubleValue()));

		BigDecimal result = intPow.multiply(doublePow, mc);
		if (signOf2 == -1) {
			result = BigDecimal.ONE.divide(result, mc.getPrecision(),
					RoundingMode.HALF_UP);
		}
		return result;
	}

	public static PowOperator getInstance() {
		return instance;
	}
}
