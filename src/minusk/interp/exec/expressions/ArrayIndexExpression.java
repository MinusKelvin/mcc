package minusk.interp.exec.expressions;

import minusk.interp.exec.Scope;
import minusk.interp.exec.Type;
import minusk.interp.exec.expressions.values.ArrayValue;
import minusk.interp.exec.expressions.values.Value;
import minusk.interp.parse.tree.partial.Expression;

/**
 * Created by MinusKelvin on 12/10/15.
 */
public class ArrayIndexExpression extends ExpressionBit {
	private final ExpressionBit array;
	private final ExpressionBit index;
	
	public ArrayIndexExpression(Expression ary, Expression idx, Scope scope) {
		array = ExpressionBit.generate(ary, scope);
		if (array.type.arrayof == null)
			throw new IllegalStateException("type error cannot index nonarray");
		index = ExpressionBit.generate(idx, scope);
		if (!index.type.compatibleWith(Type.Int))
			throw new IllegalStateException("type error cannot index with nonint");
	}
	
	@Override
	public Value evaluate(Scope.Context scope) {
		return ((ArrayValue) array.evaluate(scope)).values[index.evaluate(scope).asInt().value];
	}
}
