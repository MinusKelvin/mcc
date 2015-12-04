package minusk.interp.parse;

/**
 * Created by MinusKelvin on 12/3/15.
 */
public enum Keyword {
	STRUCT("struct"),
	STATIC("static"),
	CONST("const"),
	WHILE("while"),
	ELSE("else"),
	FUNC("func"),
	FOR("for"),
	IF("if"),
	;
	
	public final String name;
	Keyword(String name) {
		this.name = name;
	}
}
