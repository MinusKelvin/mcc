package minusk.interp.parse.token;

/**
 * Created by MinusKelvin on 12/3/15.
 */
public class StringToken extends Token {
	public final String value;
	
	public StringToken(String value, int line, int lowchar, int highchar) {
		super(TokenType.STRING, line, lowchar, highchar);
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "string:" + value;
	}
}
