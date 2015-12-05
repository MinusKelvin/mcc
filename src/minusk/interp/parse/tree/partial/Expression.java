package minusk.interp.parse.tree.partial;

import minusk.interp.parse.token.Token;
import minusk.interp.parse.tree.Identifier;
import minusk.interp.parse.tree.Literal;

import java.util.ArrayList;

/**
 * Created by MinusKelvin on 12/5/15.
 */
public class Expression {
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
}
