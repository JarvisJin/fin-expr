package org.masking.finexpr.token;

import java.math.BigDecimal;

import org.masking.finexpr.expr.ExprException;

public class NumberToken extends Token{
	
	private final BigDecimal value;
	
	public NumberToken(BigDecimal value){
		super(TokenType.NUMBER);
		assert value!=null;
		this.value = value;
	}
	
	@Override
	public void setType(TokenType type) {
		throw new ExprException("NumberToken's type is NUMBER, cannot be changed.");
	}

	public BigDecimal getValue() {
		return value;
	}
}
