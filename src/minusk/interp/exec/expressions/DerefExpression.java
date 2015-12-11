package minusk.interp.exec.expressions;

import minusk.interp.exec.Scope;
import minusk.interp.exec.expressions.values.Value;

/**
 * Created by MinusKelvin on 12/7/15.
 */
public class DerefExpression extends ExpressionBit {
	private final String name;
	
	public DerefExpression(String name, Scope scope) {
		this.name = name;
		if (!scope.isVariableDeclaredGlobally(name))
			throw new IllegalStateException("var doesn't exist");
		type = scope.getVariable(name).type;
	}
	
	@Override
	public Value evaluate(Scope.Context scope) {
		return scope.getVariable(name);
	}
	
	public void setVariable(Scope.Context scope, Value value) {
		scope.setVariable(name, value);
	}
}
