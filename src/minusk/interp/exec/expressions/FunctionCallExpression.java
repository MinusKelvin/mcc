package minusk.interp.exec.expressions;

import minusk.interp.exec.Scope;
import minusk.interp.exec.expressions.values.FunctionValue;
import minusk.interp.exec.expressions.values.Value;
import minusk.interp.parse.tree.partial.Expression;

import java.util.ArrayList;

/**
 * Created by MinusKelvin on 12/9/15.
 */
public class FunctionCallExpression extends ExpressionBit {
	private final ExpressionBit func;
	private final ExpressionBit[] args;
	
	public FunctionCallExpression(Expression lhs, ArrayList<Expression> arglist, Scope scope) {
		func = ExpressionBit.generate(lhs, scope);
		if (func.type.returnType == null)
			throw new IllegalStateException("type error");
		this.args = new ExpressionBit[arglist.size()];
		type = func.type.returnType;
		for (int i = 0; i < args.length; i++) {
			args[i] = ExpressionBit.generate(arglist.get(i), scope);
			if (!args[i].type.compatibleWith(func.type.typeList[i]))
				throw new IllegalStateException("type error");
		}
	}
	
	@Override
	public Value evaluate(Scope.Context scope) {
		FunctionValue function = (FunctionValue) func.evaluate(scope);
		Scope.Context callContext = new Scope.Context(function.scope);
		for (int i = 0; i < args.length; i++)
			callContext.declareVariable(function.params[i], args[i].evaluate(scope));
		return function.body.funcExecute(callContext);
	}
}
