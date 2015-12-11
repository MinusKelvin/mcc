package minusk.interp.exec.expressions.values;

import minusk.interp.exec.Scope;
import minusk.interp.exec.expressions.ExpressionBit;

/**
 * Created by MinusKelvin on 12/6/15.
 */
public abstract class Value extends ExpressionBit {
	
	@Override
	public Value evaluate(Scope.Context context) {
		return this;
	}
	
	public IntValue asInt() {
		throw new IllegalStateException("invalid cast");
	}
	
	public LongValue asLong() {
		throw new IllegalStateException("invalid cast");
	}
}
