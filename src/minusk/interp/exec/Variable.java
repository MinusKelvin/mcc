package minusk.interp.exec;

import minusk.interp.exec.expressions.values.Value;

/**
 * Created by MinusKelvin on 12/7/15.
 */
public class Variable {
	public final boolean isConst;
	public final Type type;
	public Value value;
	
	public Variable(Type type, boolean c0nst) {
		isConst = c0nst;
		this.type = type;
	}
	
	public Variable copy() {
		return new Variable(type, isConst);
	}
}
