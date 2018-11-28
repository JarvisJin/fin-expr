package io.github.jarvisjin.finexpr;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import io.github.jarvisjin.finexpr.expr.Expression;
import io.github.jarvisjin.finexpr.function.Function;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for Expression.
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
		Expression e = new Expression("add(x,y) + a^b");
		
		// define function "add"
		e.addFunction(new Function("add", 2){
			@Override
			public BigDecimal apply(List<BigDecimal> args, MathContext mc) {
				return args.get(0).add(args.get(1),mc);
			}
		});
		
		/*
		 *  set variables, 
		 *  in this case:
		 *  the expression 
		 *  = add(8.5,5.77) + 5^3 
		 *  = 8.5+5.77 + 5^3 
		 *  = 14.27 + 125 
		 *  = 139.27
		 */
		e.addVariable("x", new BigDecimal("8.5"));	
		e.addVariable("y", new BigDecimal("5.77"));	
		e.addVariable("a", new BigDecimal("5"));	
		e.addVariable("b", new BigDecimal("3"));	
		
		BigDecimal result = e.calculate();
		System.out.println(result);
		assertTrue(result.equals(new BigDecimal("139.27")));
		
		/*
		 * set replaceOnDuplicate==true, to replace the value of x and b, then caculate again.
		 * the expression
		 * = -9+5.77 + 5^5
		 * = -3.23 + 3125 
		 * = 3121.77
		 * 
		 * if you don't want to use replaceOnDuplicate, you can use Expression.clearVariables() instead.
		 * that function will clean all variables, and you need to reset all of the variables;
		 */
		e.addVariable("x", new BigDecimal("-9"), true);
		e.addVariable("b", new BigDecimal("5"), true);
		result = e.calculate();
		System.out.println(result);
		assertTrue(result.equals(new BigDecimal("3121.77")));
	}
}
