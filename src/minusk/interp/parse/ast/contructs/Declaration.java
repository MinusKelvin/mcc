package minusk.interp.parse.ast.contructs;

import minusk.interp.parse.token.Token;

import java.util.ArrayList;

/**
 * Created by MinusKelvin on 12/3/15.
 */
public class Declaration {
//	public final boolean isStatic;
//	public final boolean isConst;
//	public final Identifier name;
//	public final Expression initialValue;
	
	public Declaration(ArrayList<Token> tokens) {
		
	}
	
	@Override
	public String toString() {
		return "declaration:{" +
//				"isStatic:"+isStatic+"," +
//				"isConst:"+isConst+"," +
//				"name:"+name+"," +
//				"initialValue:"+initialValue+"," +
				"}";
	}
}
