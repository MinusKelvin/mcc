package minusk.interp.exec.expressions.values;

import minusk.interp.exec.Type;

/**
 * Created by MinusKelvin on 12/7/15.
 */
public class NullValue extends Value {
	public NullValue() {
		type = Type.Null;
	}
	
	@Override
	public String toString() {
		return "NullValue";
	}
}
