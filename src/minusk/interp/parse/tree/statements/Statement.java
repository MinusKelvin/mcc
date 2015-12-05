package minusk.interp.parse.tree.statements;

import minusk.interp.parse.tree.partial.Expression;

/**
 * Created by MinusKelvin on 12/5/15.
 */
public class Statement {
	public Expression expression;
	public Block block;
	public Expression returns;
	public Branch branch;
	public Loop loop;
	public Declaration declaration;
	
	@Override
	public String toString() {
		if (expression != null)
			return "stmt:"+expression;
		if (block != null)
			return "stmt:"+block;
		if (branch != null)
			return "stmt:"+branch;
		if (loop != null)
			return "stmt:"+loop;
		if (declaration != null)
			return "stmt:"+declaration;
		return "stmt:{returns:"+returns+"}";
	}
}
