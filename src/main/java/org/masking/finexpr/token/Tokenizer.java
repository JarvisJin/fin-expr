package org.masking.finexpr.token;

import java.util.Set;

import org.masking.finexpr.expr.ExprException;

public class Tokenizer {

	/** the origin infix expression */
	private final String expr;

	/** current position */
	private int pos = 0;

	/** the previous token */
	private Token previousToken;
	
	private final char UNDER_LINE = '_';
	private final char SEPARATOR = ',';
	private final char DECIMAL_POINT = '.';
	private final char OPEN_PAREN = '(';
	private final char CLOSE_PAREN = ')';
	private final char UNARY_SUFFIX = 'u'; // unary operator suffix : '-' => '-u';  '+' => '+u'
	
	private final Set<String> operators;
	
	public Tokenizer(String expression, Set<String> operators) {
		this.expr = expression.trim();
		this.operators = operators;
	}

	public boolean hasNext() {
		return pos < expr.length();
	}

	public Token next() {
		if (!hasNext())
			return previousToken = null;

		char ch = expr.charAt(pos);
		while (Character.isWhitespace(ch) && ++pos < expr.length()) {
			ch = expr.charAt(pos);
		}

		if (Character.isWhitespace(ch))
			return previousToken = null;

		Token token = new Token();
		token.setPos(pos);

		if (Character.isDigit(ch)) { // Number token
			do {
				token.append(ch);
				ch = ++pos < expr.length() ? expr.charAt(pos) : 0;
			} while (isCotinueNumber(ch, token));
			token.setType(TokenType.NUMBER);
			
		}else if(Character.isLetter(ch) || UNDER_LINE==ch){ // variable or function
			do {
				token.append(ch);
				ch = ++pos < expr.length() ? expr.charAt(pos) : 0;
			} while (isCotinueVarOrFuncName(ch));
			token.setType(ch=='(' ? TokenType.FUNCTION : TokenType.VARIABLE);
			
		}else if(SEPARATOR == ch){
			token.append(ch);
			token.setType(TokenType.SEPARATOR);
			++pos;
			
		}else if(OPEN_PAREN == ch){
			token.append(ch);
			token.setType(TokenType.OPEN_PAREN);
			++pos;
			
		}else if(CLOSE_PAREN == ch){
			token.append(ch);
			token.setType(TokenType.CLOSE_PAREN);
			++pos;
			
		}else if(operators!=null && operators.contains(String.valueOf(ch))){
			token.append(ch);
			token.setType(TokenType.OPERATOR);
			if('-'==ch || '+'==ch){
				if(previousToken==null
						|| previousToken.getType()==TokenType.SEPARATOR
						|| previousToken.getType()==TokenType.OPEN_PAREN
						|| previousToken.getType()==TokenType.OPERATOR){
					token.append(UNARY_SUFFIX);
				}
			}
			++pos;
			
		}else{
			throw new ExprException("Invalid char in expression, position: "+pos);
		}
		
		return previousToken = token;
	}

	private boolean isCotinueNumber(char ch, Token token) {
		if (Character.isDigit(ch) || 'e' == ch || 'E' == ch || DECIMAL_POINT==ch) {
			return true;
		}

		if ('-' == ch || '+' == ch) { // scientific notation
			if (token.length() > 0
					&& (token.charAt(token.length() - 1) == 'e' || token.charAt(token.length() - 1) == 'E')) {
				return true;
			}
		}

		return false;
	}
	
	private boolean isCotinueVarOrFuncName(char ch) {
		return ( Character.isDigit(ch) || Character.isLetter(ch) || UNDER_LINE==ch );
	}

	public Token previous() {
		return previousToken;
	}

	public String getExpr() {
		return expr;
	}

	public int getPos() {
		return pos;
	}
}
