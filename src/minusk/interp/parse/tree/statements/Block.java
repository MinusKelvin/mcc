package minusk.interp.parse.tree.statements;

import minusk.interp.parse.tree.statements.Statement;

import java.util.ArrayList;

/**
 * Created by MinusKelvin on 12/5/15.
 */
public class Block {
	public final ArrayList<Statement> statements = new ArrayList<>();
	
	@Override
	public String toString() {
		return "block:stmts:"+statements;
	}
}
