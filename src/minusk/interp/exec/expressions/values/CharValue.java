package minusk.interp.exec.expressions.values;

import minusk.interp.exec.Type;

/**
 * Created by MinusKelvin on 12/7/15.
 */
public class CharValue extends Value {
	public final byte value;
	public CharValue(byte value) {
		this.value = value;
		type = Type.Char;
	}
	
	@Override
	public IntValue asInt() {
		return new IntValue((int)value & 0xff);
	}
	
	@Override
	public LongValue asLong() {
		return new LongValue((int)value & 0xff);
	}
	
	@Override
	public String toString() {
		return "CharValue:"+((int)value & 0xff);
	}
}
