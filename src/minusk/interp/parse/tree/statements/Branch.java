package minusk.interp.parse.tree.statements;

import minusk.interp.parse.tree.Serializable;
import minusk.interp.parse.tree.partial.Expression;

/**
 * Created by MinusKelvin on 12/5/15.
 */
public class Branch extends Serializable {
	public Expression condition;
	public Statement ifTrue;
	public Statement ifFalse;
	public Branch branch;
	
	@Override
	public String toString() {
		return "branch:{condition:"+condition+",ifTrue:"+ifTrue+",ifFalse:"+ifFalse+"}";
	}
	
	@Override
	public String serialize(String indent) {
		return "branch:{\n" +
				indent+"\tcondition:"+condition.serialize(indent+"\t")+",\n" +
				indent+"\tifTrue:"+ifTrue.serialize(indent+"\t")+",\n" +
				indent+"\tifFalse:"+serial(ifFalse,indent+"\t")+"\n" +
				indent+"}";
	}
}
