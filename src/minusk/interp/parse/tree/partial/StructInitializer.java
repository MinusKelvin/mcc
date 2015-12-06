package minusk.interp.parse.tree.partial;

import minusk.interp.parse.tree.Serializable;
import minusk.interp.parse.tree.statements.TypeDef;

import java.util.ArrayList;

/**
 * Created by MinusKelvin on 12/6/15.
 */
public class StructInitializer extends Serializable {
	public ArrayList<StructField> fieldValues = new ArrayList<>();
	public TypeDef type;
	
	@Override
	public String serialize(String indent) {
		return "struct:{\n" +
				indent+"\ttype:"+type.serialize(indent+"\t")+",\n" +
				indent+"\tfields:"+serial(fieldValues,indent+"\t")+"\n" +
				indent+"}";
	}
}
