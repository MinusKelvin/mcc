package minusk.interp.parse.ast.leafs;

/**
 * Created by MinusKelvin on 12/3/15.
 */
public class Identifier {
	public final String name;
	
	public Identifier(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "identifier:\""+name+"\"";
	}
}
