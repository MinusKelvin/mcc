package minusk.interp.exec.expressions.values;

import minusk.interp.exec.Type;

/**
 * Created by MinusKelvin on 12/7/15.
 */
public class IntValue extends Value {
	public final int value;
	public IntValue(int value) {
		this.value = value;
		type = Type.Int;
	}
	
	@Override
	public IntValue asInt() {
		return this;
	}
	
	@Override
	public LongValue asLong() {
		return new LongValue(value);
	}
	
	@Override
	public String toString() {
		return "IntValue:"+value;
	}
}
