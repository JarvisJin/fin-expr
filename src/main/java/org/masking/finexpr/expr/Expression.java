package org.masking.finexpr.expr;

import java.util.Map;
import java.util.HashMap;

import org.masking.finexpr.expr.operator.DivideOperator;
import org.masking.finexpr.expr.operator.MinusOperator;
import org.masking.finexpr.expr.operator.MultiplyOperator;
import org.masking.finexpr.expr.operator.Operator;
import org.masking.finexpr.expr.operator.PlusOperator;
import org.masking.finexpr.expr.operator.PowOperator;
import org.masking.finexpr.expr.operator.UnaryMinuxOperator;
import org.masking.finexpr.expr.operator.UnaryPlusOperator;

public class Expression {
	
	/** the origin infix expression */
	private final String expr;
	
	private Map<String, Operator> opMap = new HashMap<String, Operator>();
	
	public Expression(String expr){
		this.expr = expr;
		initDefaultOperator();
	}
	
	public String getExpr() {
		return expr;
	}
	
	private void initDefaultOperator(){
		opMap.put(PlusOperator.SYMBOL, PlusOperator.getInstance());
		opMap.put(MinusOperator.SYMBOL, MinusOperator.getInstance());
		opMap.put(MultiplyOperator.SYMBOL, MultiplyOperator.getInstance());
		opMap.put(DivideOperator.SYMBOL, DivideOperator.getInstance());
		opMap.put(PowOperator.SYMBOL, PowOperator.getInstance());
		opMap.put(UnaryMinuxOperator.SYMBOL, UnaryMinuxOperator.getInstance());
		opMap.put(UnaryPlusOperator.SYMBOL, UnaryPlusOperator.getInstance());
	}
}
