package minusk.interp.parse.tree.statements;

import minusk.interp.parse.tree.Serializable;
import minusk.interp.parse.tree.partial.Expression;

/**
 * Created by MinusKelvin on 12/5/15.
 */
public class Loop extends Serializable {
	public Expression initialize;
	public Expression condition;
	public Expression increment;
	public Statement body;
	public boolean conditionLast;
	
	@Override
	public String toString() {
		return "loop:{init:"+initialize+",conditionLast:"+conditionLast+",condition:"+condition+",increment:"+increment+",body:"+body+"}";
	}
	
	@Override
	public String serialize(String indent) {
		return "loop:{\n" +
				indent+"\t"+serial(initialize,indent+"\t")+",\n"+
				indent+"\t"+conditionLast+",\n"+
				indent+"\t"+serial(condition,indent+"\t")+",\n"+
				indent+"\t"+serial(increment,indent+"\t")+",\n"+
				indent+"\t"+body.serialize(indent+"\t")+",\n"+
				indent+"}";
	}
}
