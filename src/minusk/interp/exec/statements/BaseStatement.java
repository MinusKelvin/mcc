package minusk.interp.exec.statements;

import minusk.interp.exec.Scope;
import minusk.interp.exec.Type;
import minusk.interp.exec.expressions.ExpressionBit;
import minusk.interp.exec.expressions.values.Value;
import minusk.interp.parse.tree.statements.Statement;

/**
 * Created by MinusKelvin on 12/6/15.
 */
public abstract class BaseStatement {
	public abstract Value execute(Scope.Context scope);
	
	public static BaseStatement generate(Statement stmt, Scope scope, Type returnType) {
		if (stmt.expression != null) {
			return new ExpressionStatement(ExpressionBit.generate(stmt.expression, scope));
//		} else if (stmt.block != null) {
//			return "stmt:"+block.serialize(indent);
//		} else if (stmt.branch != null) {
//			return "stmt:"+branch.serialize(indent);
//		} else if (stmt.loop != null) {
//			return "stmt:"+loop.serialize(indent);
		} else if (stmt.declaration != null)
			return new DeclarationStatement(stmt.declaration, scope);
//		else if (stmt.typeDec != null) {
//			return "stmt:"+typeDec.serialize(indent);
		/*}*/ else if (stmt.nullStmt) {
			return new BaseStatement() {
				@Override
				public Value execute(Scope.Context scope) {
					return null;
				}
			};
		}
		return new ReturnStatement(stmt.returns, scope, returnType);
	}
}
