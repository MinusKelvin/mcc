package minusk.interp.parse.tree.partial;

import minusk.interp.parse.tree.Serializable;
import minusk.interp.parse.tree.statements.TypeDef;

/**
 * Created by MinusKelvin on 12/5/15.
 */
public class SimpleDeclaration extends Serializable {
	public String name;
	public TypeDef type;
	
	@Override
	public java.lang.String toString() {
		return "simpdecl:{type:"+type+",name:"+name+"}";
	}
	
	public java.lang.String serialize(java.lang.String indent) {
		return "simpdecl:{\n" +
				indent+"\ttype:"+type.serialize(indent+"\t")+",\n" +
				indent+"\tname:"+name+"\n" +
				indent+"}";
	}
}
