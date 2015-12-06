package minusk.interp.parse.tree;

/**
 * Created by MinusKelvin on 12/5/15.
 */
public class Identifier extends Serializable {
	public String name;
	
	@Override
	public String toString() {
		return "ident:"+name;
	}
	
	public String serialize(String indent) {
		return toString();
	}
}
