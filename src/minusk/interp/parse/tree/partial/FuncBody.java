package minusk.interp.parse.tree.partial;

import minusk.interp.parse.tree.Serializable;
import minusk.interp.parse.tree.statements.Block;
import minusk.interp.parse.tree.statements.TypeDef;

import java.util.ArrayList;

/**
 * Created by MinusKelvin on 12/5/15.
 */
public class FuncBody extends Serializable {
	public Block body;
	public ArrayList<SimpleDeclaration> params;
	public TypeDef type;
	
	@Override
	public String toString() {
		return "func:{type:"+type+",params:"+params+",body:"+body+"}";
	}
	
	public String serialize(String indent) {
		return "func:{\n" +
				indent+"\ttype:"+type.serialize(indent+"\t")+",\n" +
				indent+"\tparams:"+serial(params,indent+"\t")+",\n" +
				indent+"\tbody:"+body.serialize(indent+"\t")+"\n" +
				indent+"}";
	}
}
