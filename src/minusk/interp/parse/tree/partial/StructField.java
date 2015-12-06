package minusk.interp.parse.tree.partial;

import minusk.interp.parse.tree.Identifier;
import minusk.interp.parse.tree.Serializable;

/**
 * Created by MinusKelvin on 12/6/15.
 */
public class StructField extends Serializable {
	public Identifier name;
	public Expression value;
	
	@Override
	public String serialize(String indent) {
		return "structfield:{\n"+
				indent+"\tname:"+name.serialize(indent+"\t")+",\n" +
				indent+"\tvalue:"+value.serialize(indent+"\t")+",\n"+
				indent+"}";
	}
}
