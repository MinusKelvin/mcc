package minusk.interp.exec;

import minusk.interp.exec.expressions.values.*;
import minusk.interp.parse.tree.partial.TypeDef;

import java.util.Arrays;

/**
 * Created by MinusKelvin on 12/7/15.
 */
public class Type {
	public static final Type Long = new Type(Primitive.LONG);
	public static final Type Int = new Type(Primitive.INT);
	public static final Type Short = new Type(Primitive.SHORT);
	public static final Type Byte = new Type(Primitive.BYTE);
	public static final Type Char = new Type(Primitive.CHAR);
	public static final Type String = new Type(Primitive.STRING);
	public static final Type Float = new Type(Primitive.FLOAT);
	public static final Type Double = new Type(Primitive.DOUBLE);
	public static final Type Boolean = new Type(Primitive.BOOLEAN);
	public static final Type Null = new Type(Primitive.NULL);
	
	private final Primitive primitive;
	private final String name;
	public final Type arrayof;
	public final Type[] typeList;
	public final Type returnType;
	public final String[] nameList;
	
	private Type(Primitive primitive) {
		this.primitive = primitive;
		arrayof = null;
		typeList = null;
		nameList = null;
		name = null;
		returnType = null;
	}
	
	public Type(Type arrayof) {
		primitive = Primitive.ARRAY;
		this.arrayof = arrayof;
		typeList = null;
		nameList = null;
		name = null;
		returnType = null;
	}
	
	public Type(Type[] paramTypes, Type returnType) {
		primitive = Primitive.FUNC;
		arrayof = null;
		typeList = paramTypes;
		nameList = null;
		name = null;
		this.returnType = returnType;
	}
	
	public Type(String name, Type[] paramTypes, String[] names) {
		primitive = Primitive.STRUCT;
		arrayof = null;
		typeList = paramTypes;
		nameList = names;
		this.name = name;
		returnType = null;
	}
	
	public Value getDefaultValue() {
		if (equals(Long))
			return new LongValue(0);
		if (equals(Int))
			return new IntValue(0);
		if (equals(Short))
			return new ShortValue((short)0);
		if (equals(Byte))
			return new ByteValue((byte)0);
		if (equals(Char))
			return new CharValue((byte)0);
		if (equals(Float))
			return new FloatValue(0);
		if (equals(Double))
			return new DoubleValue(0);
		if (equals(Boolean))
			return new BooleanValue(false);
		return new NullValue();
	}
	
	public static Type makeFrom(TypeDef def, Scope scope) {
		if (def.paramTypes != null) {
			Type[] paramTypes = new Type[def.paramTypes.size()];
			for (int i = 0; i < paramTypes.length; i++)
				paramTypes[i] = makeFrom(def.paramTypes.get(i), scope);
			return new Type(paramTypes, makeFrom(def.returnType, scope));
		} else if (def.arrayOf != null)
			return new Type(makeFrom(def.arrayOf, scope));
		
		return scope.getType(def.typeName);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Type))
			return false;
		Type other = (Type) obj;
		if (primitive != other.primitive)
			return false;
		switch (primitive) {
			case STRUCT:
				assert name != null;
				return name.equals(other.name);
			case FUNC:
				assert typeList != null;
				return Arrays.equals(typeList, other.typeList);
			case ARRAY:
				assert arrayof != null;
				return arrayof.equals(other.arrayof);
			default:
				return true;
		}
	}
	
	/** if this can be cast to or is equivalent to other */
	public boolean compatibleWith(Type other) {
		switch (primitive) {
			case INT:case SHORT:case BYTE:case CHAR:
				return other.primitive == Primitive.INT || other.primitive == Primitive.LONG;
			case FLOAT:
				return other.primitive == Primitive.DOUBLE;
			case ARRAY:
				if (primitive == other.primitive) {
					assert arrayof != null;
					return arrayof.equals(other.arrayof);
				}
				return false;
			case NULL:
				switch (other.primitive) {
					case STRING:case NULL:case FUNC:case STRUCT:case ARRAY:
						return true;
					default:
						return false;
				}
			default:
				return equals(other);
		}
	}
	
	@Override
	public String toString() {
		return primitive.name();
	}
	
	public enum Primitive {
		LONG,
		INT,
		SHORT,
		BYTE,
		CHAR,
		STRING,
		FLOAT,
		DOUBLE,
		BOOLEAN,
		FUNC,
		STRUCT,
		ARRAY,
		NULL
	}
}
