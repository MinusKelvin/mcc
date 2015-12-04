package minusk.interp.parse.token;

/**
 * Created by MinusKelvin on 12/3/15.
 */
public class DecimalToken extends Token {
	public final double value;
	
	public DecimalToken(double value, int line, int lowchar, int highchar) {
		super(TokenType.DECIMAL, line, lowchar, highchar);
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "decimal:" + value;
	}
}
