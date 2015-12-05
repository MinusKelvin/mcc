package minusk.interp.parse.tree;

/**
 * Created by MinusKelvin on 12/5/15.
 */
public class Identifier {
	public String name;
	
	@Override
	public String toString() {
		return "ident:"+name;
	}
}
