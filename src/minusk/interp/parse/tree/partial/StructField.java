package minusk.interp.parse.tree.partial;

import minusk.interp.parse.tree.Serializable;

/**
 * Created by MinusKelvin on 12/6/15.
 */
public class StructField extends Serializable {
	public String name;
	public Expression value;
	
	@Override
	public java.lang.String serialize(java.lang.String indent) {
		return "structfield:{\n"+
				indent+"\tname:"+name+",\n" +
				indent+"\tvalue:"+value.serialize(indent+"\t")+",\n"+
				indent+"}";
	}
}
