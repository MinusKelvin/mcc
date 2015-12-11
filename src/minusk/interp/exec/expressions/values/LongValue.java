package minusk.interp.exec.expressions.values;

import minusk.interp.exec.Type;

/**
 * Created by MinusKelvin on 12/7/15.
 */
public class LongValue extends Value {
	public final long value;
	public LongValue(long value) {
		this.value = value;
		type = Type.Long;
	}
	
	@Override
	public LongValue asLong() {
		return this;
	}
	
	@Override
	public String toString() {
		return "LongValue:"+value;
	}
}
