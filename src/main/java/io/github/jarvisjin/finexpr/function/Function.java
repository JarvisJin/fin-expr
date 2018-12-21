package io.github.jarvisjin.finexpr.function;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

public abstract class Function {
	
	private final String name;
	
	/**
	 * the num of args,  -1 means variable 
	 * 该函数接受的参数个数:
	 * 若 argNum>0 或 argNum==0 代表固定参数个数，Expression计算时会验证个数是否匹配
	 * 若 argNum == -1 代表该函数支持可变参数个数，不校验个数是否匹配。
	 * */
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
