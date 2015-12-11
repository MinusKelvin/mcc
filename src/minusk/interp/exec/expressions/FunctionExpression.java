package minusk.interp.exec.expressions;

import minusk.interp.exec.Scope;
import minusk.interp.exec.Type;
import minusk.interp.exec.Variable;
import minusk.interp.exec.expressions.values.FunctionValue;
import minusk.interp.exec.expressions.values.Value;
import minusk.interp.exec.statements.BlockStatement;
import minusk.interp.parse.tree.partial.FuncBody;
import minusk.interp.parse.tree.partial.SimpleDeclaration;

/**
 * Created by MinusKelvin on 12/9/15.
 */
public class FunctionExpression extends ExpressionBit {
	private final BlockStatement body;
	private final String[] paramNames;
	
	public FunctionExpression(FuncBody func, Scope outerScope) {
		Scope innerScope = new Scope(outerScope);
		paramNames = new String[func.params.size()];
		for (int i = 0; i < paramNames.length; i++) {
			SimpleDeclaration dec = func.params.get(i);
			innerScope.declareVariable(dec.name, new Variable(Type.makeFrom(dec.type, outerScope), false));
			paramNames[i] = dec.name;
		}
		type = Type.makeFrom(func.type, outerScope);
		body = new BlockStatement(func.body, innerScope, true, type.returnType);
	}
	
	@Override
	public Value evaluate(Scope.Context scope) {
		return new FunctionValue(scope, body, paramNames, type);
	}
}
