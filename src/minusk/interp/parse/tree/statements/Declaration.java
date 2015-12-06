package minusk.interp.parse.tree.statements;

import minusk.interp.parse.tree.Identifier;
import minusk.interp.parse.tree.Serializable;
import minusk.interp.parse.tree.partial.Expression;
import minusk.interp.parse.tree.partial.FuncBody;

/**
 * Created by MinusKelvin on 12/5/15.
 */
public class Declaration extends Serializable {
	public Identifier name;
	public Expression init;
	public TypeDef type;
	public FuncBody func;
	public boolean isStatic;
	public boolean isConst;
	
	@Override
	public String toString() {
		if (func != null)
			return "decl:{static:"+isStatic+",const:"+isConst+",type:"+type+",name:"+name+",funcValue:"+func+"}";
		return "decl:{static:"+isStatic+",const:"+isConst+",type:"+type+",name:"+name+",init:"+init+"}";
	}
	
	public String serialize(String indent) {
		if (func != null)
			return "decl:{\n"+
					indent+"\tstatic:"+isStatic+",\n" +
					indent+"\tconst:"+isConst+",\n" +
					indent+"\ttype:"+type.serialize(indent+"\t")+",\n" +
					indent+"\tname:"+name.serialize(indent+"\t")+",\n" +
					indent+"\tfuncValue:"+func.serialize(indent+"\t")+"\n"+
					indent+"}";
		return "decl:{\n"+
				indent+"\tstatic:"+isStatic+",\n" +
				indent+"\tconst:"+isConst+",\n" +
				indent+"\ttype:"+type.serialize(indent+"\t")+",\n" +
				indent+"\tname:"+name.serialize(indent+"\t")+",\n" +
				indent+"\tinit:"+serial(init,indent+"\t")+"\n"+
				indent+"}";
	}
}
