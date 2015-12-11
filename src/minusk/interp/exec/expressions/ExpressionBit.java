package minusk.interp.exec.expressions;

import minusk.interp.exec.Scope;
import minusk.interp.exec.Type;
import minusk.interp.exec.expressions.values.*;
import minusk.interp.parse.tree.partial.Expression;

/**
 * Created by MinusKelvin on 12/6/15.
 */
public abstract class ExpressionBit {
	public Type type;
	
	public abstract Value evaluate(Scope.Context scope);
	
	public static ExpressionBit generate(Expression expr, Scope scope) {
		if (expr.literal != null) {
			if (expr.literal.isBoolean)
				return new BooleanValue(expr.literal.booleanValue);
			if (expr.literal.isInt)
				return expr.literal.intValue > Integer.MAX_VALUE ?
						new LongValue(expr.literal.intValue) :
						new IntValue((int) expr.literal.intValue);
			if (expr.literal.isDecimal)
				return new DoubleValue(expr.literal.decimalValue);
			if (expr.literal.isString)
				return new StringValue(expr.literal.stringValue);
			return new NullValue();
		} else if (expr.identifier != null)
			return new DerefExpression(expr.identifier, scope);
		else if (expr.array != null)
			return new ArrayExpression(Type.makeFrom(expr.arrayType,scope), expr.array, scope);
		else if (expr.arrayInit != null)
			return new ArrayExpression(Type.makeFrom(expr.arrayType,scope), expr.arrayInitCount, expr.arrayInit, scope);
		else if (expr.callArgs != null)
			return new FunctionCallExpression(expr.lhs, expr.callArgs, scope);
		else if (expr.index != null)
			return new ArrayIndexExpression(expr.lhs, expr.index, scope);
		else if (expr.func != null)
			return new FunctionExpression(expr.func, scope);
//		if (struct != null)
//			return "expr:"+struct.serialize(indent);
//		
//		return "expr:{\n" +
//				indent+"\tlhs:"+serial(lhs,indent+"\t")+",\n" +
//				indent+"\toperator:"+operator+",\n" +
//				indent+"\trhs:"+serial(rhs, indent+"\t")+"\n" +
//				indent+"}";
		return new ExpressionBit() {
			{
				type = Type.Null;
			}
			@Override
			public Value evaluate(Scope.Context scope) {
				return new NullValue();
			}
		};
	}
}
