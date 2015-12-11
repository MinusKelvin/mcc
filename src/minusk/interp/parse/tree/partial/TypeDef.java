package minusk.interp.parse.tree.partial;

import minusk.interp.parse.tree.Serializable;

import java.util.ArrayList;

/**
 * Created by MinusKelvin on 12/5/15.
 */
public class TypeDef extends Serializable {
	public TypeDef returnType;
	public ArrayList<TypeDef> paramTypes;
	public String typeName;
	public TypeDef arrayOf;
	
	@Override
	public String toString() {
		if (returnType != null)
			return "typedef:func:{returnType:"+returnType+",paramTypes:"+paramTypes+"}";
		return "typedef:"+typeName;
	}
	
	@Override
	public String serialize(String indent) {
		if (paramTypes != null)
			return "typedef:func:{\n" +
					indent+"\treturnType:"+returnType.serialize(indent+"\t")+",\n" +
					indent+"\tparamTypes:"+serial(paramTypes,indent+"\t")+"\n" +
					indent+"}";
		if (arrayOf != null)
			return "typedef:array:"+arrayOf.serialize(indent);
		return "typedef:"+typeName;
	}
}
