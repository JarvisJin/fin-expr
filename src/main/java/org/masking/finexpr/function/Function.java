package org.masking.finexpr.function;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

public abstract class Function {
	
	private final String name;
	
	/**the num of args,  -1 means variable */
	private final int argNum;
	
	public Function(String name, int argNum){
		if(argNum < -1){
			throw new IllegalArgumentException("The number of function arguments is incorrect. -1(variable) or not less than 0");
		}
		this.name = name;
		this.argNum = argNum;
	}
	
	public abstract BigDecimal apply(List<BigDecimal> args, MathContext mc);

	public String getName() {
		return name;
	}

	public int getArgNum() {
		return argNum;
	}
}
