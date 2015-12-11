package minusk.interp.exec;

import minusk.interp.exec.statements.BaseStatement;
import minusk.interp.parse.tree.ParseTree;
import minusk.interp.parse.tree.statements.Statement;

import java.util.ArrayList;

/**
 * Created by MinusKelvin on 12/6/15.
 */
public class Executable {
	private ArrayList<BaseStatement> statements = new ArrayList<>();
	
	public Executable(ParseTree buildFrom) {
		Scope scope = new Scope(null);
		for (Statement stmt : buildFrom.statements) {
			statements.add(BaseStatement.generate(stmt, scope, Type.Null));
		}
	}
	
	public void execute() {
		Scope.Context scope = new Scope.Context(null);
		for (BaseStatement statement : statements)
			statement.execute(scope);
	}
}
