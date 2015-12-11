package minusk.interp.exec.expressions.values;

import minusk.interp.exec.Type;

/**
 * Created by MinusKelvin on 12/7/15.
 */
public class ByteValue extends Value {
	public final byte value;
	public ByteValue(byte value) {
		this.value = value;
		type = Type.Byte;
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
		return "ByteValue:"+value;
	}
}
