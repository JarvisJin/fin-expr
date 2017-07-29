package org.masking.finexpr.token;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.masking.finexpr.expr.ExprException;
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

		List<Token> output = new LinkedList<Token>();
		Tokenizer tokenizer = new Tokenizer(expression, opMap.keySet());
		Stack<Token> opStack = new Stack<Token>();
		
		Token lastToken = null;
		
		while (tokenizer.hasNext()) {
			Token t = tokenizer.next();
			switch (t.getType()) {
			
				case NUMBER:
				case VARIABLE:
					if(lastToken!=null && 
						( lastToken.getType()==TokenType.NUMBER || lastToken.getType()==TokenType.VARIABLE)
					){
						throw new ExprException("Parse error! missing operator or separator ',' at "+(t.getPos()+1));
					}
					output.add(t);
					break;
					
				case OPERATOR:
					Operator thisOp = opMap.get(t.getContent());
					
					while(!opStack.isEmpty()){
						Token peekToken = opStack.peek();
						if(peekToken.getType()==TokenType.OPERATOR){
							Operator lastOp = opMap.get(peekToken.getContent());
							if(lastOp.getPrecedence() > thisOp.getPrecedence()){
								opStack.pop();
								output.add(peekToken);
							}else if(lastOp.getPrecedence() == thisOp.getPrecedence() && lastOp.isLeftAssociative()){
								opStack.pop();
								output.add(peekToken);
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
					opStack.push(t);
					break;
				case OPEN_PAREN:
					opStack.push(t);
					if(lastToken!=null && lastToken.getType()==TokenType.FUNCTION){
						output.add(t); // to determine the number of parameters of the functions with variable params
					}
					break;
					
				case SEPARATOR:
					while(!opStack.isEmpty() && opStack.peek().getType()!=TokenType.OPEN_PAREN){
						output.add(opStack.pop());
					}
					if(opStack.isEmpty()){
						throw new ExprException("Parse error! for ',' at "+(t.getPos()+1));
					}
					break;
					
				case CLOSE_PAREN:
					while(!opStack.isEmpty() && opStack.peek().getType()!=TokenType.OPEN_PAREN){
						output.add(opStack.pop());
					}
					if(opStack.isEmpty()){
						throw new ExprException("Parse error! PAREN not match! at "+(t.getPos()+1));
					}
					opStack.pop();
					if(!opStack.isEmpty() && opStack.peek().getType()==TokenType.FUNCTION){
						output.add(opStack.pop());
					}
					break;
			}
			
			lastToken = t;
		}
		
		while(!opStack.isEmpty()){
			Token leftT = opStack.pop();
			if(leftT.getType()==TokenType.OPEN_PAREN || leftT.getType()==TokenType.CLOSE_PAREN){
				throw new ExprException("Parse error! PAREN not match! at "+(leftT.getPos()+1));
			}
			output.add(leftT);
		}
		return output;
	}
}
