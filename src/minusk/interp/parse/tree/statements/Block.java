package minusk.interp.parse.tree.statements;

import minusk.interp.parse.tree.Serializable;

import java.util.ArrayList;

/**
 * Created by MinusKelvin on 12/5/15.
 */
public class Block extends Serializable {
	public final ArrayList<Statement> statements = new ArrayList<>();
	
	@Override
	public String toString() {
		return "block:stmts:"+statements;
	}
	
	@Override
	public String serialize(String indent) {
		return "block:stmts:"+serial(statements, indent);
	}
}
