package org.masking.finexpr;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import org.masking.finexpr.expr.Expression;
import org.masking.finexpr.function.Function;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class ExpressionTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public ExpressionTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(ExpressionTest.class);
	}

	/**
	 * Test :-)
	 */
	public void testExpr() {
		Expression e = new Expression("fx(9, 7.3, x)+2^3");
		e.addFunction(new Function("fx", 3){
			@Override
			public BigDecimal apply(List<BigDecimal> args, MathContext mc) {
				return args.get(0).add(args.get(1),mc).subtract(args.get(2),mc);
			}
		});
		e.addVariable("x", new BigDecimal("8.5"));	
		System.out.println(e.calculate());
		assertTrue(true);
	}
}
