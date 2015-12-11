package minusk.interp.exec.expressions.values;

import minusk.interp.exec.Scope;
import minusk.interp.exec.Type;
import minusk.interp.exec.statements.BlockStatement;

/**
 * Created by MinusKelvin on 12/9/15.
 */
public class FunctionValue extends Value {
	public final BlockStatement body;
	public final String[] params;
	public final Scope.Context scope;
	
	public FunctionValue(Scope.Context scope, BlockStatement body, String[] params, Type type) {
		this.type = type;
		this.body = body;
		this.params = params;
		this.scope = scope;
	}
}
