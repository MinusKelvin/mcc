package minusk.interp.parse.tree.statements;

import minusk.interp.parse.tree.Serializable;
import minusk.interp.parse.tree.partial.Expression;

/**
 * Created by MinusKelvin on 12/5/15.
 */
public class Statement extends Serializable {
	public Expression expression;
	public Block block;
	public Expression returns;
	public Branch branch;
	public Loop loop;
	public Declaration declaration;
	public TypeDeclaration typeDec;
	public boolean nullStmt;
	
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
	
	public String serialize(String indent) {
		if (expression != null)
			return "stmt:"+expression.serialize(indent);
		if (block != null)
			return "stmt:"+block.serialize(indent);
		if (branch != null)
			return "stmt:"+branch.serialize(indent);
		if (loop != null)
			return "stmt:"+loop.serialize(indent);
		if (declaration != null)
			return "stmt:"+declaration.serialize(indent);
		if (typeDec != null)
			return "stmt:"+typeDec.serialize(indent);
		if (nullStmt)
			return "stmt:null";
		return "stmt:returns:"+serial(returns,indent);
	}
}
