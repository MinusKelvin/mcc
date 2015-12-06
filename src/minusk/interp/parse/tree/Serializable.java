package minusk.interp.parse.tree;

import java.util.ArrayList;

/**
 * Created by MinusKelvin on 12/6/15.
 */
public abstract class Serializable {
	public abstract String serialize(String indent);
	
	protected static String serial(ArrayList<? extends Serializable> list, String indent) {
		if (list == null)
			return null;
		StringBuilder sb = new StringBuilder();
		sb.append("[\n");
		for (Serializable s : list)
			sb.append(indent).append("\t").append(s.serialize(indent + "\t")).append(",\n");
		return sb.append(indent).append("]").toString();
	}
	
	protected static String serial(Serializable serializable, String indent) {
		if (serializable == null)
			return null;
		return serializable.serialize(indent);
	}
}
