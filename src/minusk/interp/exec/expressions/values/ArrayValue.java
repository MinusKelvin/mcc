package minusk.interp.exec.expressions.values;

import minusk.interp.exec.Type;

/**
 * Created by MinusKelvin on 12/8/15.
 */
public class ArrayValue extends Value {
	public final Value[] values;
	
	public ArrayValue(Type type, int length) {
		this.type = type;
		values = new Value[length];
	}
}
