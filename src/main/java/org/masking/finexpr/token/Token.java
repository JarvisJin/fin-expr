package org.masking.finexpr.token;

public class Token {

	private StringBuilder content;
	private TokenType type;
	
	/** position in origin expression */
	private int pos;
	
	public Token(){
		content = new StringBuilder(6);
	}
	
	public String getContent() {
		return content.toString();
	}
	
	public void append(char ch){
		content.append(ch);
	}
	
	public char charAt(int index){
		return content.charAt(index);
	}
	
	public int length(){
		return content.length();
	}
	
	public TokenType getType() {
		return type;
	}
	public void setType(TokenType type) {
		this.type = type;
	}
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
}
