package minusk.interp.parse.tree;

/**
 * Created by MinusKelvin on 12/5/15.
 */
public class Literal {
	public boolean isInt;
	public long intValue;
	public boolean isDecimal;
	public double decimalValue;
	public boolean isString;
	public String stringValue;
	
	@Override
	public String toString() {
		if (isInt)
			return "int:"+intValue;
		if (isDecimal)
			return "decimal:"+decimalValue;
		if (isString)
			return "string:"+stringValue;
		throw new IllegalStateException("untyped literal");
	}
}
