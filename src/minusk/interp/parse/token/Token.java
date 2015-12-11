package minusk.interp.parse.token;

/**
 * Created by MinusKelvin on 12/3/15.
 */
public class Token {
	public final TokenType type;
	public final int line, lowchar, highchar;
	
	public Token(TokenType type, int line, int lowchar, int highchar) {
		this.type = type;
		this.line = line;
		this.lowchar = lowchar;
		this.highchar = highchar;
	}
	
	@Override
	public String toString() {
		return type.name();
	}
	
	public String generateLineChar() {
		return " line " + line + " character " + lowchar + "-" + highchar + ".";
	}
	
	public enum TokenType {
		IDENTIFIER,
		OPEN_PARENTHESIS,
		CLOSE_PARENTHESIS,
		INTEGER,
		DECIMAL,
		STRING,
		KEYWORD,
		ADD,
		SUB,
		MUL,
		DIV,
		MOD,
		OPEN_BRACKET,
		CLOSE_BRACKET,
		LESS,
		GREATER,
		EQUALS,
		LEQUALS,
		GEQUALS,
		NEQUALS,
		DOT,
		OPEN_BRACE,
		CLOSE_BRACE,
		SEMICOLON,
		INCREMENT,
		DECREMENT,
		ASSIGN,
		ADD_ASSIGN,
		SUB_ASSIGN,
		MUL_ASSIGN,
		DIV_ASSIGN,
		MOD_ASSIGN,
		LEFT_SHIFT,
		RIGHT_SHIFT,
		LEFT_SHIFT_ASSIGN,
		RIGHT_SHIFT_ASSIGN,
		COMMA,
		LOGICAL_AND,
		BITWISE_AND,
		LOGICAL_AND_ASSIGN,
		BITWISE_AND_ASSIGN,
		LOGICAL_OR,
		BITWISE_OR,
		LOGICAL_OR_ASSIGN,
		BITWISE_OR_ASSIGN,
		LOGICAL_XOR,
		BITWISE_XOR,
		LOGICAL_XOR_ASSIGN,
		BITWISE_XOR_ASSIGN,
		LOGICAL_NOT,
		BITWISE_NOT,
		TRUE,
		FALSE,
		NULL,
	}
}
