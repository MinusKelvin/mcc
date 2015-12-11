package minusk.interp.parse.tree;

/**
 * Created by MinusKelvin on 12/5/15.
 */
public class Literal extends Serializable {
	public boolean isInt;
	public long intValue;
	public boolean isDecimal;
	public double decimalValue;
	public boolean isString;
	public String stringValue;
	public boolean booleanValue;
	public boolean isBoolean;
	
	@Override
	public String toString() {
		if (isInt)
			return "int:"+intValue;
		if (isDecimal)
			return "decimal:"+decimalValue;
		if (isString)
			return "string:"+stringValue;
		if (isBoolean)
			return "boolean:"+booleanValue;
		return "null";
	}
	
	@Override
	public String serialize(String indent) {
		return toString();
	}
}
