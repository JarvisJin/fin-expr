package org.masking.finexpr;

public class Tokenizer {
	
	/**  the origin infix expression */
	private final String expr;
	
	/**  current position */
	private int pos=0;
	
	/** the previous token */
	private Token previousToken;
	
	public Tokenizer(String expression){
		this.expr=expression.trim();
	}
	
	public boolean hasNext(){
		return pos < expr.length();
	}
	
	public String getExpr() {
		return expr;
	}

	public int getPos() {
		return pos;
	}
}
