package minusk.interp.parse.tree;

import minusk.interp.parse.token.Token;

/**
 * Created by MinusKelvin on 12/5/15.
 */
public class Expression {
	public Literal literal;
	public Identifier identifier;
	public Expression lhs;
	public Expression rhs;
	public Token operator;
	
	@Override
	public String toString() {
		if (literal != null)
			return "expr:"+literal;
		if (identifier != null)
			return "expr:"+identifier;
		return "expr:{lhs:"+lhs+",operator:"+operator+",rhs:"+rhs+"}";
	}
}
