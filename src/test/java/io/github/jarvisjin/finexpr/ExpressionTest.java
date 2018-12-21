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
		Expression e = new Expression("min(x,y) + a^b");
		
		// define function "min"
		e.addFunction(new Function("min", 2){
			@Override
			public BigDecimal apply(List<BigDecimal> args, MathContext mc) {
				if(args.get(0).compareTo(args.get(1))<0) {
					return args.get(0);
				}else {
					return args.get(1);
				}
			}
		});
		
		/*
		 *  set variables, 
		 *  in this case:
		 *  the expression 
		 *  = min(8.5,5.77) + 5^3 
		 *  = 5.77 + 5^3 
		 *  = 5.77 + 125 
		 *  = 130.77
		 */
		e.addVariable("x", new BigDecimal("8.5"));	
		e.addVariable("y", new BigDecimal("5.77"));	
		e.addVariable("a", new BigDecimal("5"));	
		e.addVariable("b", new BigDecimal("3"));	
		
		BigDecimal result = e.calculate();
		System.out.println(result);
		assertTrue(result.equals(new BigDecimal("130.77")));
		
		/*
		 * set replaceOnDuplicate==true, to replace the value of x and b, then caculate again.
		 * the expression
		 * = -9 + 5^5
		 * = -9 + 3125 
		 * = 3116
		 * 
		 * if you don't want to use replaceOnDuplicate, you can use Expression.clearVariables() instead.
		 * that function will clean all variables, and you need to reset all of the variables;
		 */
		e.addVariable("x", new BigDecimal("-9"), true);
		e.addVariable("b", new BigDecimal("5"), true);
		result = e.calculate();
		System.out.println(result);
		assertTrue(result.equals(new BigDecimal("3116")));
	}
}
