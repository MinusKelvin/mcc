package minusk.interp.exec.expressions.values;

import minusk.interp.exec.Type;

/**
 * Created by MinusKelvin on 12/7/15.
 */
public class ShortValue extends Value {
	public final short value;
	public ShortValue(short value) {
		this.value = value;
		type = Type.Short;
	}
	
	@Override
	public IntValue asInt() {
		return new IntValue(value);
	}
	
	@Override
	public LongValue asLong() {
		return new LongValue(value);
	}
	
	@Override
	public String toString() {
		return "ShortValue:"+value;
	}
}
