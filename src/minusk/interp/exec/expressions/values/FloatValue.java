package minusk.interp.exec.expressions.values;

import minusk.interp.exec.Type;

/**
 * Created by MinusKelvin on 12/7/15.
 */
public class FloatValue extends Value {
	public final float value;
	public FloatValue(float value) {
		this.value = value;
		type = Type.Float;
	}
	
	@Override
	public String toString() {
		return "FloatValue:"+value;
	}
}
