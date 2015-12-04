package minusk.interp.parse.ast.contructs;

import minusk.interp.parse.SyntaxError;
import minusk.interp.parse.token.Token;

import java.util.ArrayList;

/**
 * Created by MinusKelvin on 12/3/15.
 */
public class Block {
	private final ArrayList<Statement> statements = new ArrayList<>();
	public final Expression returnValue;
	
	public Block(ArrayList<Token> tokens) {
		ArrayList<Token> statement = new ArrayList<>();
		ArrayList<Token> groupTracker = new ArrayList<>();
		for (Token token : tokens) {
			if (token.type == Token.TokenType.SEMICOLON && groupTracker.isEmpty()) {
				statements.add(new Statement(statement));
				statement.clear();
			} else {
				switch (token.type) {
					case OPEN_BRACE:
					case OPEN_BRACKET:
					case OPEN_PARENTHESIS:
						groupTracker.add(token);
						break;
					case CLOSE_BRACE:
						Token opener = groupTracker.remove(groupTracker.size() - 1);
						if (opener.type == Token.TokenType.OPEN_PARENTHESIS || opener.type == Token.TokenType.OPEN_BRACKET)
							throw new SyntaxError("Unexpected }:" + token.generateLineChar());
						else
							break;
					case CLOSE_BRACKET:
						opener = groupTracker.remove(groupTracker.size() - 1);
						if (opener.type == Token.TokenType.OPEN_PARENTHESIS || opener.type == Token.TokenType.OPEN_BRACE)
							throw new SyntaxError("Unexpected ]:" + token.generateLineChar());
						else
							break;
					case CLOSE_PARENTHESIS:
						opener = groupTracker.remove(groupTracker.size() - 1);
						if (opener.type == Token.TokenType.OPEN_BRACE || opener.type == Token.TokenType.OPEN_BRACKET)
							throw new SyntaxError("Unexpected ):" + token.generateLineChar());
						else
							break;
				}
				statement.add(token);
			}
		}
		if (statement.size() == 0)
			returnValue = null;
		else
			returnValue = new Expression(statement);
	}
	
	@Override
	public String toString() {
		return "block:{statements:"+statements+",return:"+returnValue+"}";
	}
}
