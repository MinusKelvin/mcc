package minusk.interp.parse.token;

import minusk.interp.parse.Keyword;

/**
 * Created by MinusKelvin on 12/3/15.
 */
public class KeywordToken extends Token {
	public final Keyword value;
	
	public KeywordToken(Keyword value, int line, int lowchar, int highchar) {
		super(TokenType.KEYWORD, line, lowchar, highchar);
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "keyword:" + value.name;
	}
}
