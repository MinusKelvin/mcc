package minusk.interp.parse.token;

/**
 * Created by MinusKelvin on 12/3/15.
 */
public class IntToken extends Token {
	public final long value;
	
	public IntToken(long value, int line, int lowchar, int highchar) {
		super(TokenType.INTEGER, line, lowchar, highchar);
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "integer:"+value;
	}
}
