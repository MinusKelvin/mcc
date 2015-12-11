package minusk.interp.exec.expressions.values;

import minusk.interp.exec.Type;

/**
 * Created by MinusKelvin on 12/7/15.
 */
public class BooleanValue extends Value {
	public final boolean value;
	public BooleanValue(boolean value) {
		this.value = value;
		type = Type.Boolean;
	}
	
	@Override
	public String toString() {
		return "BooleanValue:"+value;
	}
}
