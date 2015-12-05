package minusk.interp.parse.tree;

/**
 * Created by MinusKelvin on 12/5/15.
 */
public class Literal {
	public boolean isInt;
	public long intValue;
	
	@Override
	public String toString() {
		if (isInt)
			return "int:"+intValue;
		return "literal";
	}
}
