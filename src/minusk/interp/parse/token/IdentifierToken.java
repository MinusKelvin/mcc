package minusk.interp.parse.token;

/**
 * Created by MinusKelvin on 12/3/15.
 */
public class IdentifierToken extends Token {
	public final String value;
	
	public IdentifierToken(String value, int line, int lowchar, int highchar) {
		super(TokenType.IDENTIFIER, line, lowchar, highchar);
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "identifier:" + value;
	}
}
