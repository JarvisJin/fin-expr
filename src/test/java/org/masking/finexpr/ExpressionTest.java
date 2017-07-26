package org.masking.finexpr;

import org.masking.finexpr.expr.Expression;

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
	 * Rigourous Test :-)
	 */
	public void testExpr() {
		Expression e = new Expression("9*&9-9)");
		System.out.println(e.toRPN());
		assertTrue(true);
	}
}
