package minusk.interp.parse.ast.contructs;

import minusk.interp.parse.Keyword;
import minusk.interp.parse.token.KeywordToken;
import minusk.interp.parse.token.Token;

import java.util.ArrayList;

/**
 * Created by MinusKelvin on 12/3/15.
 */
public class Statement {
	public final Declaration declaration;
	public final Expression expression;
	
	public Statement(ArrayList<Token> statement) {
		Token first = statement.get(0);
		switch (first.type) {
			case IDENTIFIER:
				Token second = statement.get(1);
				switch (second.type) {
					case OPEN_BRACKET:case IDENTIFIER: // Declaration
						declaration = new Declaration(statement);
						expression = null;
						return;
					default:
						expression = new Expression(statement);
						declaration = null;
						break;
				}
				break;
			case KEYWORD:
				Keyword keyword = ((KeywordToken) first).value;
				if (keyword == Keyword.STRUCT || keyword == Keyword.FUNC) { // Declaration
					declaration = new Declaration(statement);
					expression = null;
					return;
				}
				// FALLTHROUGH
			default:
				expression = new Expression(statement);
				declaration = null;
				break;
		}
	}
	
	@Override
	public String toString() {
		if (declaration != null)
			return "stmt:{"+declaration+"}";
		return "stmt:{"+expression+"}";
	}
}
