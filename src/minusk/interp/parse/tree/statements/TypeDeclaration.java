package minusk.interp.parse.tree.statements;

import minusk.interp.parse.tree.Serializable;
import minusk.interp.parse.tree.partial.SimpleDeclaration;
import minusk.interp.parse.tree.partial.TypeDef;

import java.util.ArrayList;

/**
 * Created by MinusKelvin on 12/7/15.
 */
public class TypeDeclaration extends Serializable {
	public TypeDef type;
	public ArrayList<SimpleDeclaration> fields;
	public String name;
	
	@Override
	public String serialize(String indent) {
		if (type != null)
			return "typeDecl:{\n" +
					indent+"\tname:"+name+",\n" +
					indent+"\ttype:"+type.serialize(indent+"\t")+"\n" +
					indent+"}";
		if (fields != null)
			return "typeDecl:{\n" +
					indent+"\tname:"+name+",\n" +
					indent+"\tstruct:"+serial(fields, indent+"\t")+"\n" +
					indent+"}";
		return "typeDecl:name:"+name;
	}
}
