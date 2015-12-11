package minusk.interp.exec.statements;

import minusk.interp.exec.Scope;
import minusk.interp.exec.Type;
import minusk.interp.exec.expressions.ExpressionBit;
import minusk.interp.exec.expressions.values.Value;
import minusk.interp.parse.tree.partial.Expression;

/**
 * Created by MinusKelvin on 12/10/15.
 */
public class ReturnStatement extends BaseStatement {
	private final ExpressionBit returnValue;
	
	public ReturnStatement(Expression expr, Scope scope, Type returnType) {
		returnValue = ExpressionBit.generate(expr, scope);
		if (!returnValue.type.compatibleWith(returnType))
			throw new IllegalStateException("type error "+returnType+" "+returnValue.type);
	}
	
	@Override
	public Value execute(Scope.Context scope) {
		return returnValue.evaluate(scope);
	}
}
