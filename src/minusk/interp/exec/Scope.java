package minusk.interp.exec;

import minusk.interp.exec.expressions.values.Value;

import java.util.HashMap;

/**
 * Created by MinusKelvin on 12/6/15.
 */
public class Scope {
	private final HashMap<String, Type> types = new HashMap<>();
	private final HashMap<String, Variable> variables = new HashMap<>();
	public final Scope parent;
	
	public Scope(Scope parent) {
		this.parent = parent;
		
		types.put("long", Type.Long);
		types.put("int", Type.Int);
		types.put("short", Type.Short);
		types.put("byte", Type.Byte);
		types.put("char", Type.Char);
		types.put("float", Type.Float);
		types.put("double", Type.Double);
		types.put("boolean", Type.Boolean);
		types.put("string", Type.String);
	}
	
	public boolean isTypeDeclaredLocally(String name) {
		return types.containsKey(name);
	}
	
	public boolean isTypeDeclaredGlobally(String name) {
		return types.containsKey(name) || parent != null && parent.isTypeDeclaredGlobally(name);
	}
	
	public boolean isVariableDeclaredLocally(String name) {
		return variables.containsKey(name);
	}
	
	public boolean isVariableDeclaredGlobally(String name) {
		return variables.containsKey(name) || parent != null && parent.isVariableDeclaredGlobally(name);
	}
	
	public Type getType(String name) {
		if (isTypeDeclaredGlobally(name))
			return isTypeDeclaredLocally(name) ? types.get(name) : parent.getType(name);
		return null;
	}
	
	public Variable getVariable(String name) {
		if (isVariableDeclaredGlobally(name))
			return isVariableDeclaredLocally(name) ? variables.get(name) : parent.getVariable(name);
		return null;
	}
	
	public void declareVariable(String name, Variable var) {
		variables.put(name, var);
	}
	
	/**
	 * Created by MinusKelvin on 12/7/15.
	 */
	public static class Context {
		private final HashMap<String, Value> variables = new HashMap<>();
		private final Context parent;
		
		public Context(Context parent) {
			this.parent = parent;
		}
		
		public Value getVariable(String name) {
			if (variables.containsKey(name))
				return variables.get(name);
			return parent.getVariable(name);
		}
		
		public void setVariable(String name, Value var) {
			if (variables.containsKey(name))
				variables.put(name, var);
			else
				parent.setVariable(name, var);
		}
		
		public void declareVariable(String name, Value var) {
			variables.put(name, var);
		}
	}
}
