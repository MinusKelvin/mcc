package minusk.interp.parse.tree.statements;

import minusk.interp.parse.tree.Serializable;
import minusk.interp.parse.tree.partial.SimpleDeclaration;

import java.util.ArrayList;

/**
 * Created by MinusKelvin on 12/5/15.
 */
public class TypeDef extends Serializable {
	public TypeDef returnType;
	public ArrayList<TypeDef> paramTypes;
	public ArrayList<SimpleDeclaration> fields;
	public String typeName;
	public TypeDef arrayOf;
	
	@Override
	public java.lang.String toString() {
		if (returnType != null)
			return "typedef:func:{returnType:"+returnType+",paramTypes:"+paramTypes+"}";
		if (fields != null)
			return "typedef:struct:fields:"+fields;
		return "typedef:"+typeName;
	}
	
	@Override
	public java.lang.String serialize(java.lang.String indent) {
		if (paramTypes != null)
			return "typedef:func:{\n" +
					indent+"\treturnType:"+returnType.serialize(indent+"\t")+",\n" +
					indent+"\tparamTypes:"+serial(paramTypes,indent+"\t")+"\n" +
					indent+"}";
		if (fields != null)
			return "typedef:struct:fields:"+serial(fields,indent);
		if (returnType != null)
			return "typedef:{\n" +
					indent+"\tname:"+typeName+",\n" +
					indent+"\ttype:"+returnType.serialize(indent+"\t")+"\n" +
					indent+"}";
		if (arrayOf != null)
			return "typedef:array:"+arrayOf.serialize(indent);
		return "typedef:"+typeName;
	}
}
