package org.masking.finexpr.token;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.masking.finexpr.operator.Operator;

/**
 * Shunting-yard algorithm https://en.wikipedia.org/wiki/Shunting-yard_algorithm
 * https://zh.wikipedia.org/wiki/%E8%B0%83%E5%BA%A6%E5%9C%BA%E7%AE%97%E6%B3%95
 * 
 * @author xuanlu
 *
 */
public class ShuntingYard {

	public static List<Token> getRPN(String expression, Map<String, Operator> opMap) {

		List<Token> rpn = new LinkedList<Token>();
		Tokenizer tokenizer = new Tokenizer(expression, opMap.keySet());
		Stack<Token> opStack = new Stack<Token>();
		
		while (tokenizer.hasNext()) {
			Token t = tokenizer.next();
			switch (t.getType()) {
			
				case NUMBER:
					
				case VARIABLE:
					rpn.add(t);
					break;
					
				case OPERATOR:
					Operator tOp = opMap.get(t.getContent());
					while(!opStack.isEmpty()){
						Token ot = opStack.peek();
						if(ot.getType()==TokenType.OPERATOR){
							Operator otOp = opMap.get(ot.getContent());
							if(otOp.getPrecedence() > tOp.getPrecedence() 
									|| otOp.getPrecedence() == tOp.getPrecedence()){
								opStack.pop();
								rpn.add(ot);
							}else{
								break;
							}
						}else{
							break;
						}
					}
					opStack.push(t);
					break;
				
				case FUNCTION:
				
				case OPEN_PAREN:
					opStack.push(t);
					break;
					
				case SEPARATOR:
					break;
					
				case CLOSE_PAREN:
					break;
			}
		}

		return rpn;
	}
}
