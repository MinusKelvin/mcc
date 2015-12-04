package minusk.interp.parse;

import minusk.interp.parse.ast.contructs.Block;
import minusk.interp.parse.token.Token;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MinusKelvin on 12/2/15.
 */
public class SyntaxTree {
	private Block root;
	
	public void generate(String code) {
		Token token;
		Tokenizer tokenizer = new Tokenizer(code);
		ArrayList<Token> block = new ArrayList<>();
		while ((token = tokenizer.next()) != null)
			block.add(token);
		root = new Block(block);
	}
	
	@Override
	public String toString() {
		return "syntaxTree:{root:"+root+"}";
	}
}
