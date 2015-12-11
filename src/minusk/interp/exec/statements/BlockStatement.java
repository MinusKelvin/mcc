package minusk.interp.exec.statements;

import minusk.interp.exec.Scope;
import minusk.interp.exec.Type;
import minusk.interp.exec.expressions.values.Value;
import minusk.interp.parse.tree.statements.Block;

/**
 * Created by MinusKelvin on 12/9/15.
 */
public class BlockStatement extends BaseStatement {
	private final BaseStatement[] statements;
	
	public BlockStatement(Block block, Scope scope, boolean newScope, Type returnType) {
		if (newScope)
			scope = new Scope(scope);
		statements = new BaseStatement[block.statements.size()];
		for (int i = 0; i < statements.length; i++)
			statements[i] = BaseStatement.generate(block.statements.get(i), scope, returnType);
	}
	
	@Override
	public Value execute(Scope.Context outerScope) {
		Scope.Context scope = new Scope.Context(outerScope);
		return funcExecute(scope);
	}
	
	public Value funcExecute(Scope.Context scope) {
		for (BaseStatement stmt : statements) {
			Value v = stmt.execute(scope);
			if (v != null)
				return v;
		}
		return null;
	}
}
