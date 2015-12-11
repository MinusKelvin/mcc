package minusk.interp.exec.statements;

import minusk.interp.exec.Scope;
import minusk.interp.exec.expressions.ExpressionBit;
import minusk.interp.exec.expressions.values.Value;

/**
 * Created by MinusKelvin on 12/7/15.
 */
public class ExpressionStatement extends BaseStatement {
	private final ExpressionBit evaluates;
	
	public ExpressionStatement(ExpressionBit expr) {
		evaluates = expr;
	}
	
	@Override
	public Value execute(Scope.Context scope) {
		Value value = evaluates.evaluate(scope);
		System.out.println(value);
		return null;
	}
}
