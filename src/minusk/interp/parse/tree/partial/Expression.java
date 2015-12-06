package minusk.interp.parse.tree.partial;

import minusk.interp.parse.token.Token;
import minusk.interp.parse.tree.Identifier;
import minusk.interp.parse.tree.Literal;
import minusk.interp.parse.tree.Serializable;

import java.util.ArrayList;

/**
 * Created by MinusKelvin on 12/5/15.
 */
public class Expression extends Serializable {
	public Literal literal;
	public Identifier identifier;
	public Expression lhs;
	public Expression rhs;
	public Token operator;
	public ArrayList<Expression> array;
	public Expression arrayInit;
	public Expression arrayInitCount;
	public ArrayList<Expression> callArgs;
	public Expression index;
	public FuncBody func;
	public StructInitializer struct;
	
	@Override
	public String toString() {
		if (literal != null)
			return "expr:"+literal;
		if (identifier != null)
			return "expr:"+identifier;
		if (array != null)
			return "expr:array:" + array;
		if (arrayInit != null)
			return "expr:array:{init:"+arrayInit+",len:"+arrayInitCount+"}";
		if (callArgs != null)
			return "expr:{lhs:"+lhs+",callArgs:"+callArgs+"}";
		if (index != null)
			return "expr:{lhs:"+lhs+",index:"+index+"}";
		if (func != null)
			return "expr:"+func;
		
		return "expr:{lhs:"+lhs+",operator:"+operator+",rhs:"+rhs+"}";
	}
	
	@Override
	public String serialize(String indent) {
		if (literal != null)
			return "expr:"+literal.serialize(indent);
		if (identifier != null)
			return "expr:"+identifier.serialize(indent);
		if (array != null)
			return "expr:array:" + serial(array,indent);
		if (arrayInit != null)
			return "expr:array:{\n" +
					indent+"\tinit:"+arrayInit.serialize(indent+"\t")+",\n" +
					indent+"\tlen:"+arrayInitCount.serialize(indent+"\t")+"\n" +
					indent+"}";
		if (callArgs != null)
			return "expr:{\n" +
					indent+"\tlhs:"+lhs.serialize(indent+"\t")+",\n" +
					indent+"\tcallArgs:"+serial(callArgs,indent+"\t")+"\n" +
					indent+"}";
		if (index != null)
			return "expr:{\n" +
					indent+"\tlhs:"+lhs.serialize(indent+"\t")+",\n" +
					indent+"\tindex:"+index.serialize(indent+"\t")+"\n" +
					indent+"}";
		if (func != null)
			return "expr:"+func.serialize(indent);
		if (struct != null)
			return "expr:"+struct.serialize(indent);
		
		return "expr:{\n" +
				indent+"\tlhs:"+serial(lhs,indent+"\t")+",\n" +
				indent+"\toperator:"+operator+",\n" +
				indent+"\trhs:"+serial(rhs, indent+"\t")+"\n" +
				indent+"}";
	}
}
