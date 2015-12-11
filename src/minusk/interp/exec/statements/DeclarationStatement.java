package minusk.interp.exec.statements;

import minusk.interp.exec.Scope;
import minusk.interp.exec.Type;
import minusk.interp.exec.Variable;
import minusk.interp.exec.expressions.ExpressionBit;
import minusk.interp.exec.expressions.FunctionExpression;
import minusk.interp.exec.expressions.values.Value;
import minusk.interp.parse.tree.statements.Declaration;

/**
 * Created by MinusKelvin on 12/9/15.
 */
public class DeclarationStatement extends BaseStatement {
	private final String name;
	private final ExpressionBit init;
	
	public DeclarationStatement(Declaration declaration, Scope scope) {
		name = declaration.name;
		Type type = Type.makeFrom(declaration.type, scope);
		if (scope.isVariableDeclaredLocally(name))
			throw new IllegalStateException("cannot redeclare variable "+name);
		if (declaration.func != null) {
			scope.declareVariable(name, new Variable(type, declaration.isConst));
			init = new FunctionExpression(declaration.func, scope);
		} else if (declaration.init != null) {
			if (declaration.init.func != null)
				scope.declareVariable(name, new Variable(type, declaration.isConst));
			init = ExpressionBit.generate(declaration.init, scope);
			if (declaration.init.func == null)
				scope.declareVariable(name, new Variable(type, declaration.isConst));
		} else {
			scope.declareVariable(name, new Variable(type, declaration.isConst));
			init = type.getDefaultValue();
		}
	}
	
	@Override
	public Value execute(Scope.Context scope) {
		scope.declareVariable(name, init.evaluate(scope));
		return null;
	}
}
