package minusk.interp.parse.tree.statements;

import minusk.interp.parse.tree.Identifier;
import minusk.interp.parse.tree.partial.SimpleDeclaration;

import java.util.ArrayList;

/**
 * Created by MinusKelvin on 12/5/15.
 */
public class TypeDef {
	public TypeDef returnType;
	public ArrayList<TypeDef> paramTypes;
	public ArrayList<SimpleDeclaration> fields;
	public Identifier typeName;
	
	@Override
	public String toString() {
		if (returnType != null)
			return "typedef:func:{returnType:"+returnType+",paramTypes:"+paramTypes+"}";
		if (fields != null)
			return "typedef:struct:fields:"+fields;
		return "typedef:"+typeName;
	}
}
