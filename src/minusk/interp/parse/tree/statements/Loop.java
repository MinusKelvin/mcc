package minusk.interp.parse.tree.statements;

import minusk.interp.parse.tree.partial.Expression;

/**
 * Created by MinusKelvin on 12/5/15.
 */
public class Loop {
	public Expression initialize;
	public Expression condition;
	public Expression increment;
	public Statement body;
	public boolean conditionLast;
	
	@Override
	public String toString() {
		return "loop:{init:"+initialize+",conditionLast:"+conditionLast+",condition:"+condition+",increment:"+increment+",body:"+body+"}";
	}
}
