package minusk.interp.exec.expressions.values;

import minusk.interp.exec.Type;

/**
 * Created by MinusKelvin on 12/7/15.
 */
public class StringValue extends Value {
	public final String value;
	public StringValue(String value) {
		this.value = value;
		type = Type.String;
	}
	
	@Override
	public String toString() {
		return "StringValue:"+value;
	}
}
