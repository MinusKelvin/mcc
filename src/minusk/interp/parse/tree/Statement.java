package minusk.interp.parse.tree;

/**
 * Created by MinusKelvin on 12/5/15.
 */
public class Statement {
	public Expression expression;
	
	@Override
	public String toString() {
		return "stmt:"+expression;
	}
}
