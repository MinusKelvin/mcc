package minusk.interp.parse.tree.statements;

import minusk.interp.parse.tree.partial.Expression;

/**
 * Created by MinusKelvin on 12/5/15.
 */
public class Branch {
	public Expression condition;
	public Statement ifTrue;
	public Statement ifFalse;
	public Branch branch;
	
	@Override
	public String toString() {
		return "branch:{condition:"+condition+",ifTrue:"+ifTrue+",ifFalse:"+ifFalse+"}";
	}
}
