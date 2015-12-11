package minusk.interp.exec.expressions.values;

import minusk.interp.exec.Type;

/**
 * Created by MinusKelvin on 12/7/15.
 */
public class DoubleValue extends Value {
	public final double value;
	public DoubleValue(double value) {
		this.value = value;
		type = Type.Double;
	}
	
	@Override
	public String toString() {
		return "DoubleValue:"+value;
	}
}
