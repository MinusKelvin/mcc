package minusk.interp.exec.expressions;

import minusk.interp.exec.Scope;
import minusk.interp.exec.Type;
import minusk.interp.exec.expressions.values.ArrayValue;
import minusk.interp.exec.expressions.values.Value;
import minusk.interp.parse.tree.partial.Expression;

import java.util.ArrayList;

/**
 * Created by MinusKelvin on 12/7/15.
 */
public class ArrayExpression extends ExpressionBit {
	private ExpressionBit[] init;
	private ExpressionBit length;
	private ExpressionBit initAll;
	
	public ArrayExpression(Type type, ArrayList<Expression> expr, Scope scope) {
		this.type = type;
		init = new ExpressionBit[expr.size()];
		for (int i = 0; i < init.length; i++) {
			ExpressionBit e = ExpressionBit.generate(expr.get(i), scope);
			if (!e.type.compatibleWith(type.arrayof))
				throw new IllegalStateException("type error");
			init[i] = e;
		}
	}
	
	public ArrayExpression(Type type, Expression length, Expression init, Scope scope) {
		this.type = type;
		this.length = ExpressionBit.generate(length, scope);
		initAll = ExpressionBit.generate(init, scope);
		if (!this.length.type.compatibleWith(Type.Int))
			throw new IllegalStateException("type error");
		if (!initAll.type.compatibleWith(type.arrayof))
			throw new IllegalStateException("type error");
	}
	
	@Override
	public Value evaluate(Scope.Context scope) {
		if (init != null) {
			ArrayValue array = new ArrayValue(type, init.length);
			for (int i = 0; i < init.length; i++)
				array.values[i] = init[i].evaluate(scope);
			return array;
		}
		ArrayValue array = new ArrayValue(type, length.evaluate(scope).asInt().value);
		Value value = initAll.evaluate(scope);
		for (int i = 0; i < array.values.length; i++)
			array.values[i] = value;
		return array;
	}
}
