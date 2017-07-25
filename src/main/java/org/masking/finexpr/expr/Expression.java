package org.masking.finexpr.expr;

import java.util.Map;

import org.masking.finexpr.operator.DivideOperator;
import org.masking.finexpr.operator.MinusOperator;
import org.masking.finexpr.operator.MultiplyOperator;
import org.masking.finexpr.operator.Operator;
import org.masking.finexpr.operator.PlusOperator;
import org.masking.finexpr.operator.PowOperator;
import org.masking.finexpr.operator.UnaryMinuxOperator;
import org.masking.finexpr.operator.UnaryPlusOperator;

import java.util.HashMap;

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
