package minusk.interp.parse.tree.partial;

import minusk.interp.parse.tree.Serializable;

/**
 * Created by MinusKelvin on 12/5/15.
 */
public class SimpleDeclaration extends Serializable {
	public String name;
	public TypeDef type;
	
	@Override
	public String toString() {
		return "simpdecl:{type:"+type+",name:"+name+"}";
	}
	
	public String serialize(String indent) {
		return "simpdecl:{\n" +
				indent+"\ttype:"+type.serialize(indent+"\t")+",\n" +
				indent+"\tname:"+name+"\n" +
				indent+"}";
	}
}
