package minusk.interp.parse.tree.statements;

import minusk.interp.parse.tree.Identifier;
import minusk.interp.parse.tree.partial.Expression;
import minusk.interp.parse.tree.partial.FuncBody;

/**
 * Created by MinusKelvin on 12/5/15.
 */
public class Declaration {
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
}
