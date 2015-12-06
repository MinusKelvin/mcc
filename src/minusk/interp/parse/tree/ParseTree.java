package minusk.interp.parse.tree;

import minusk.interp.parse.Keyword;
import minusk.interp.parse.SyntaxError;
import minusk.interp.parse.Tokenizer;
import minusk.interp.parse.token.*;
import minusk.interp.parse.tree.partial.*;
import minusk.interp.parse.tree.statements.*;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.stream.Collectors;

/**
 * Created by MinusKelvin on 12/5/15.
 */
public class ParseTree {
	public final ArrayList<Statement> statements = new ArrayList<>();
	
	public ParseTree(java.lang.String code) {
		Tokenizer in = new Tokenizer(code);
		Statement stmt;
		do {
			stmt = parseStmt(in);
			if (stmt != null)
				statements.add(stmt);
			System.out.println(stmt != null ? stmt.serialize("") : null);
		} while (stmt != null);
	}
	
	private Statement parseStmt(Tokenizer in) {
		Statement stmt = new Statement();
		Token first = in.next();
		if (first == null)
			return null;
		base:
		switch (first.type) {
			case OPEN_BRACE:
				stmt.block = parseBlock(in);
				return stmt;
			case IDENTIFIER:
				Token second = in.next();
				if (second.type == Token.TokenType.OPEN_BRACKET) {
					Token third = in.next();
					if (third.type == Token.TokenType.CLOSE_BRACKET) {
						in.pushTokens(third);
						in.pushTokens(second);
						in.pushTokens(first);
						stmt.declaration = parseDeclaration(in);
						break;
					}
					in.pushTokens(third);
				} else if (second.type == Token.TokenType.IDENTIFIER) {
					in.pushTokens(second);
					in.pushTokens(first);
					stmt.declaration = parseDeclaration(in);
					break;
				}
				in.pushTokens(second);
			case KEYWORD:
				if (first.type == Token.TokenType.KEYWORD) {
					Keyword keyword = ((KeywordToken) first).value;
					switch (keyword) {
						case RETURN:
							stmt.returns = parseExpr(in, OperatorLevel.lowest);
							break base;
						case IF:
							stmt.branch = parseIf(in);
							return stmt;
						case FOR:
							stmt.loop = parseFor(in);
							return stmt;
						case WHILE:
							stmt.loop = parseWhile(in);
							return stmt;
						case DO:
							stmt.loop = parseDo(in);
							break base;
						case FUNC:case STATIC:case CONST:case STRUCT:
							in.pushTokens(first);
							stmt.declaration = parseDeclaration(in);
							break base;
						case NEW:
							break;
						case TYPEDEF:
							stmt.typeDef = parseTypeDef(in);
							break base;
						default:
							throw new SyntaxError("Unexpected token: '" + first + "'" + first.generateLineChar());
					}
				}
			case INTEGER:case OPEN_PARENTHESIS:case SUB:case BITWISE_NOT:case LOGICAL_NOT:case INCREMENT:case DECREMENT:
				in.pushTokens(first);
				stmt.expression = parseExpr(in,OperatorLevel.lowest);
				break;
			case SEMICOLON:
				return stmt;
			default:
				throw new SyntaxError("Unexpected token: '" + first + "'" + first.generateLineChar());
		}
		Token last = in.next();
		if (last.type == Token.TokenType.SEMICOLON)
			return stmt;
		throw new SyntaxError("Expected ; got: '" + last + "'" + last.generateLineChar());
	}
	
	private Expression parseAtom(Tokenizer in) {
		Token expectAtom = in.next();
		Expression atom = new Expression();
		switch (expectAtom.type) {
			case OPEN_PARENTHESIS:
				return parseParen(in);
			case INTEGER:
				Literal literal = new Literal();
				literal.intValue = ((IntToken) expectAtom).value;
				literal.isInt = true;
				atom.literal = literal;
				break;
			case DECIMAL:
				literal = new Literal();
				literal.decimalValue = ((DecimalToken) expectAtom).value;
				literal.isDecimal = true;
				atom.literal = literal;
				break;
			case IDENTIFIER:
				atom.identifier = ((IdentifierToken) expectAtom).value;
				break;
			case KEYWORD:
				if (((KeywordToken) expectAtom).value == Keyword.NEW) {
					Token next = in.next();
					if (next.type == Token.TokenType.KEYWORD) {
						Keyword keyword = ((KeywordToken) next).value;
						if (keyword == Keyword.FUNC)
							atom.func = parseLambda(in);
						else if (keyword == Keyword.STRUCT) {
							in.pushTokens(next);
							atom.struct = parseNewStruct(parseType(in),in);
						} else
							throw new SyntaxError("Expected struct identifier or func got: '" + next + "'" + next.generateLineChar());
					} else if (next.type == Token.TokenType.IDENTIFIER) {
						in.pushTokens(next);
						TypeDef type = parseType(in);
						if (type.arrayOf != null)
							atom = parseArray(type, in);
						else
							atom.struct = parseNewStruct(type, in);
					} else
						throw new SyntaxError("Expected struct for func got: '" + next + "'" + next.generateLineChar());
					break;
				}
			default:
				throw new SyntaxError("Expected an atom, got: '" + expectAtom + "'" + expectAtom.generateLineChar());
		}
		return atom;
	}
	
	private Expression parseTopExpr(Tokenizer in) {
		Expression expr = new Expression();
		expr.lhs = parseAtom(in);
		while (true) {
			Token next = in.next();
			switch (next.type) {
				case OPEN_PARENTHESIS:
					expr.callArgs = parseArgList(in);
					Expression ex = new Expression();
					ex.lhs = expr;
					expr = ex;
					break;
				case OPEN_BRACKET:
					expr.index = parseExpr(in, OperatorLevel.lowest);
					next = in.next();
					if (next.type != Token.TokenType.CLOSE_BRACKET)
						throw new SyntaxError("Expected ] got: '" + next + "'" + next.generateLineChar());
					ex = new Expression();
					ex.lhs = expr;
					expr = ex;
					break;
				case DOT:
					expr.operator = next;
					expr.rhs = parseAtom(in);
					ex = new Expression();
					ex.lhs = expr;
					expr = ex;
					break;
				case INCREMENT:case DECREMENT:
					expr.operator = next;
					return expr;
				default:
					in.pushTokens(next);
					return expr.lhs;
			}
		}
	}
	
	private static class OperatorLevel {
		static final OperatorLevel lowest;
		static {
			OperatorLevel l = new OperatorLevel(null, EnumSet.of(
					Token.TokenType.SUB, Token.TokenType.BITWISE_NOT, Token.TokenType.LOGICAL_NOT,
					Token.TokenType.INCREMENT, Token.TokenType.DECREMENT
			), false, true, false);
			l = new OperatorLevel(l, EnumSet.of(
					Token.TokenType.MUL, Token.TokenType.DIV, Token.TokenType.MOD
			), true, false, false);
			l = new OperatorLevel(l, EnumSet.of(
					Token.TokenType.ADD, Token.TokenType.SUB
			), true, false, false);
			l = new OperatorLevel(l, EnumSet.of(
					Token.TokenType.LEFT_SHIFT, Token.TokenType.RIGHT_SHIFT
			), true, false, false);
			l = new OperatorLevel(l, EnumSet.of(
					Token.TokenType.BITWISE_AND, Token.TokenType.BITWISE_OR, Token.TokenType.BITWISE_XOR
			), true, false, false);
			l = new OperatorLevel(l, EnumSet.of(
					Token.TokenType.LESS, Token.TokenType.LEQUALS, Token.TokenType.GREATER, Token.TokenType.GEQUALS
			), true, false, false);
			l = new OperatorLevel(l, EnumSet.of(
					Token.TokenType.EQUALS, Token.TokenType.NEQUALS
			), true, false, false);
			l = new OperatorLevel(l, EnumSet.of(
					Token.TokenType.LOGICAL_AND, Token.TokenType.LOGICAL_OR, Token.TokenType.LOGICAL_XOR
			), true, false, false);
			l = new OperatorLevel(l, EnumSet.of(
					Token.TokenType.ASSIGN, Token.TokenType.ADD_ASSIGN, Token.TokenType.BITWISE_AND_ASSIGN,
					Token.TokenType.BITWISE_OR_ASSIGN, Token.TokenType.BITWISE_XOR_ASSIGN, Token.TokenType.MOD_ASSIGN,
					Token.TokenType.LOGICAL_OR_ASSIGN, Token.TokenType.LOGICAL_AND_ASSIGN, Token.TokenType.LEFT_SHIFT_ASSIGN,
					Token.TokenType.DIV_ASSIGN, Token.TokenType.LOGICAL_XOR_ASSIGN, Token.TokenType.SUB_ASSIGN,
					Token.TokenType.RIGHT_SHIFT_ASSIGN, Token.TokenType.MUL_ASSIGN
			), false, false, false);
			lowest = l;
		}
		
		final OperatorLevel nextUp;
		final EnumSet<Token.TokenType> ops;
		final boolean isLeftAssocitive;
		final boolean prefix;
		final boolean postfix;
		
		OperatorLevel(OperatorLevel nextUp, EnumSet<Token.TokenType> ops, boolean isLeftAssocitive, boolean prefix, boolean postfix) {
			this.nextUp = nextUp;
			this.ops = ops;
			this.isLeftAssocitive = isLeftAssocitive;
			this.prefix = prefix;
			this.postfix = postfix;
		}
	}
	
	private Expression parseExpr(Tokenizer in, OperatorLevel opLevel) {
		if (opLevel == null)
			return parseTopExpr(in);
		Expression expression = new Expression();
		if (!opLevel.prefix)
			expression.lhs = parseExpr(in, opLevel.nextUp);
		Token operator = in.next();
		if (!opLevel.ops.contains(operator.type)) {
			in.pushTokens(operator);
			if (opLevel.prefix)
				return parseExpr(in, opLevel.nextUp);
			return expression.lhs;
		}
		expression.operator = operator;
		if (opLevel.postfix)
			return expression;
		if (opLevel.isLeftAssocitive) {
			expression.rhs = parseExpr(in, opLevel.nextUp);
			while (!opLevel.prefix) {
				Token next = in.next();
				if (!opLevel.ops.contains(next.type)) {
					in.pushTokens(next);
					return expression;
				}
				Expression expr = new Expression();
				expr.lhs = expression;
				expression = expr;
				expression.operator = next;
				expression.rhs = parseExpr(in, opLevel.nextUp);
			}
		} else
			expression.rhs = parseExpr(in, opLevel);
		return expression;
	}
	
	private Expression parseParen(Tokenizer in) {
		// Open Parenthesis should have already been parsed
		Expression expr = parseExpr(in,OperatorLevel.lowest);
		Token expectParen = in.next();
		if (expectParen.type != Token.TokenType.CLOSE_PARENTHESIS)
			throw new SyntaxError("Expected ) got: '" + expectParen + "'" + expectParen.generateLineChar());
		return expr;
	}
	
	private Block parseBlock(Tokenizer in) {
		// Open Brace should have already been parsed
		Block block = new Block();
		Token next = in.next();
		while (next.type != Token.TokenType.CLOSE_BRACE) {
			in.pushTokens(next);
			block.statements.add(parseStmt(in));
			next = in.next();
		}
		return block;
	}
	
	private Expression parseArray(TypeDef type, Tokenizer in) {
		Expression array = new Expression();
		array.arrayType = type;
		boolean allowNesting = type.arrayOf.arrayOf != null;
		
		Token next = in.next();
		if (next.type != Token.TokenType.OPEN_BRACE)
			throw new SyntaxError("Expected { got: '"+next+"'"+next.generateLineChar());
		
		Expression initial;
		
		next = in.next();
		if (next.type == Token.TokenType.CLOSE_BRACE) {
			array.array = new ArrayList<>();
			return array;
		} else {
			in.pushTokens(next);
			if (allowNesting && next.type == Token.TokenType.OPEN_BRACE) {
				initial = parseArray(type.arrayOf, in);
			} else
				initial = parseExpr(in, OperatorLevel.lowest);
		}
		
		next = in.next();
		if (next.type == Token.TokenType.CLOSE_BRACE) {
			array.array = new ArrayList<>();
			array.array.add(initial);
			return array;
		} else if (next.type == Token.TokenType.SEMICOLON) {
			array.arrayInit = initial;
			array.arrayInitCount = parseExpr(in, OperatorLevel.lowest);
			next = in.next();
			if (next.type != Token.TokenType.CLOSE_BRACE)
				throw new SyntaxError("Expected } got: '"+next+"'"+next.generateLineChar());
			return array;
		}
		
		array.array = new ArrayList<>();
		array.array.add(initial);
		while (true) {
			if (next.type == Token.TokenType.CLOSE_BRACE)
				return array;
			if (next.type != Token.TokenType.COMMA)
				throw new SyntaxError("Expected } or , got: '"+next+"'"+next.generateLineChar());
			next = in.next();
			if (allowNesting && next.type == Token.TokenType.OPEN_BRACE) {
				in.pushTokens(next);
				array.array.add(parseArray(type.arrayOf, in));
				next = in.next();
				continue;
			}
			in.pushTokens(next);
			array.array.add(parseExpr(in,OperatorLevel.lowest));
			next = in.next();
		}
	}
	
	private Branch parseIf(Tokenizer in) {
		// If keyword should have already been parsed
		Branch branch = new Branch();
		Token expectParen = in.next();
		if (expectParen.type != Token.TokenType.OPEN_PARENTHESIS)
			throw new SyntaxError("Expected ( got: '"+expectParen+"'"+expectParen.generateLineChar());
		branch.condition = parseParen(in);
		branch.ifTrue = parseStmt(in);
		Token next = in.next();
		if (next != null && next.type == Token.TokenType.KEYWORD && ((KeywordToken) next).value == Keyword.ELSE)
			branch.ifFalse = parseStmt(in);
		else
			in.pushTokens(next);
		return branch;
	}
	
	private Loop parseWhile(Tokenizer in) {
		// While keyword should have already been parsed
		Loop loop = new Loop();
		Token expectParen = in.next();
		if (expectParen.type != Token.TokenType.OPEN_PARENTHESIS)
			throw new SyntaxError("Expected ( got: '"+expectParen+"'"+expectParen.generateLineChar());
		loop.condition = parseParen(in);
		loop.body = parseStmt(in);
		return loop;
	}
	
	private Loop parseFor(Tokenizer in) {
		// For keyword should have already been parsed
		Loop loop = new Loop();
		Token expectParen = in.next();
		if (expectParen.type != Token.TokenType.OPEN_PARENTHESIS)
			throw new SyntaxError("Expected ( got: '"+expectParen+"'"+expectParen.generateLineChar());
		loop.initialize = parseExpr(in,OperatorLevel.lowest);
		Token expectSemi = in.next();
		if (expectSemi.type != Token.TokenType.SEMICOLON)
			throw new SyntaxError("Expected ; got: '"+expectSemi+"'"+expectSemi.generateLineChar());
		loop.condition = parseExpr(in,OperatorLevel.lowest);
		expectSemi = in.next();
		if (expectSemi.type != Token.TokenType.SEMICOLON)
			throw new SyntaxError("Expected ; got: '"+expectSemi+"'"+expectSemi.generateLineChar());
		loop.increment = parseParen(in);
		loop.body = parseStmt(in);
		return loop;
	}
	
	private Loop parseDo(Tokenizer in) {
		// Do keyword should have already been parsed
		Loop loop = new Loop();
		loop.body = parseStmt(in);
		Token expectWhile = in.next();
		if (expectWhile.type != Token.TokenType.KEYWORD || ((KeywordToken) expectWhile).value != Keyword.WHILE)
			throw new SyntaxError("Expected while got: '"+expectWhile+"'"+expectWhile.generateLineChar());
		Token expectParen = in.next();
		if (expectParen.type != Token.TokenType.OPEN_PARENTHESIS)
			throw new SyntaxError("Expected ( got: '"+expectParen+"'"+expectParen.generateLineChar());
		
		loop.condition = parseParen(in);
		return loop;
	}
	
	private ArrayList<Expression> parseArgList(Tokenizer in) {
		ArrayList<Expression> args = new ArrayList<>();
		Token next = in.next();
		if (next.type == Token.TokenType.CLOSE_PARENTHESIS)
			return args;
		in.pushTokens(next);
		args.add(parseExpr(in,OperatorLevel.lowest));
		next = in.next();
		while (true) {
			if (next.type == Token.TokenType.CLOSE_PARENTHESIS)
				return args;
			if (next.type != Token.TokenType.COMMA )
				throw new SyntaxError("Expected , or ) got: '"+next+"'"+next.generateLineChar());
			args.add(parseExpr(in,OperatorLevel.lowest));
			next = in.next();
		}
	}
	
	private Declaration parseDeclaration(Tokenizer in) {
		Declaration decl = new Declaration();
		boolean isFunc = false;
		Token next = in.next();
		while (next.type == Token.TokenType.KEYWORD) {
			Keyword keyword = ((KeywordToken) next).value;
			if (keyword == Keyword.STATIC) {
				if (decl.isStatic)
					throw new SyntaxError("Duplicate keyword: 'static'"+next.generateLineChar());
				else
					decl.isStatic = true;
			} else if (keyword == Keyword.CONST) {
				if (decl.isConst)
					throw new SyntaxError("Duplicate keyword: 'const'"+next.generateLineChar());
				else
					decl.isConst = true;
			} else if (keyword == Keyword.FUNC) {
				isFunc = true;
				next = in.next();
				break;
			} else if (keyword == Keyword.STRUCT) {
				break;
			}
			next = in.next();
		}
		in.pushTokens(next);
		decl.type = parseType(in);
		next = in.next();
		boolean funcTypesigDone = false;
		if (next.type != Token.TokenType.IDENTIFIER) {
			if (isFunc) {
				TypeDef type = new TypeDef();
				type.returnType = decl.type;
				type.paramTypes = parseParamTypeList(in);
				decl.type = type;
				funcTypesigDone = true;
				next = in.next();
				if (next.type != Token.TokenType.IDENTIFIER)
					throw new SyntaxError("Expected identifier got: '"+next+"'"+next.generateLineChar());
			} else
				throw new SyntaxError("Expected identifier got: '" + next + "'" + next.generateLineChar());
		}
		decl.name = ((IdentifierToken) next).value;
		
		if (!funcTypesigDone && isFunc) {
			next = in.next();
			if (next.type != Token.TokenType.OPEN_PARENTHESIS)
				throw new SyntaxError("Expected ( got: '"+next+"'"+next.generateLineChar());
			decl.func = new FuncBody();
			decl.func.params = parseParamList(in);
			decl.func.type = new TypeDef();
			decl.func.type.returnType = decl.type;
			decl.type = decl.func.type;
			decl.type.paramTypes = new ArrayList<>();
			decl.type.paramTypes.addAll(decl.func.params.stream().map(d -> d.type).collect(Collectors.toList()));
			
			next = in.next();
			if (next.type != Token.TokenType.OPEN_BRACE)
				throw new SyntaxError("Expected { got: '"+next+"'"+next.generateLineChar());
			decl.func.body = parseBlock(in);
			in.pushTokens(new Token(Token.TokenType.SEMICOLON,-1,-1,-1));
			return decl;
		}
		
		next = in.next();
		if (next.type == Token.TokenType.ASSIGN) {
			decl.init = parseExpr(in, OperatorLevel.lowest);
		} else
			in.pushTokens(next);
		return decl;
	}
	
	private TypeDef parseType(Tokenizer in) {
		TypeDef typeDef = new TypeDef();
		Token next = in.next();
		if (next.type == Token.TokenType.KEYWORD) {
			switch (((KeywordToken) next).value) {
				case FUNC:
					typeDef.returnType = parseType(in);
					next = in.next();
					if (next.type != Token.TokenType.OPEN_PARENTHESIS)
						throw new SyntaxError("Expected ( got: '"+next+"'"+next.generateLineChar());
					typeDef.paramTypes = parseParamTypeList(in);
					break;
				case STRUCT:
					next = in.next();
					if (next.type != Token.TokenType.OPEN_BRACE)
						throw new SyntaxError("Expected { got: '"+next+"'"+next.generateLineChar());
					typeDef.fields = parseStructFields(in);
					break;
				default:
					throw new SyntaxError("Expected func struct or identifier got: '"+next+"'"+next.generateLineChar());
			}
		} else if (next.type == Token.TokenType.IDENTIFIER) {
			typeDef.typeName = ((IdentifierToken) next).value;
		} else
			throw new SyntaxError("Expected func struct or identifier got: '"+next+"'"+next.generateLineChar());
		next = in.next();
		while (next.type == Token.TokenType.OPEN_BRACKET) {
			next = in.next();
			if (next.type != Token.TokenType.CLOSE_BRACKET)
				throw new SyntaxError("Expected ] got: '"+next+"'"+next.generateLineChar());
			next = in.next();
			TypeDef type = new TypeDef();
			type.arrayOf = typeDef;
			typeDef = type;
		}
		in.pushTokens(next);
		return typeDef;
	}
	
	private ArrayList<TypeDef> parseParamTypeList(Tokenizer in) {
		// ( should have already been parsed
		ArrayList<TypeDef> types = new ArrayList<>();
		Token next = in.next();
		if (next.type == Token.TokenType.CLOSE_PARENTHESIS)
			return types;
		in.pushTokens(next);
		types.add(parseType(in));
		next = in.next();
		while (true) {
			if (next.type == Token.TokenType.CLOSE_PARENTHESIS)
				return types;
			if (next.type != Token.TokenType.COMMA)
				throw new SyntaxError("Expected ) or , got: '"+next+"'"+next.generateLineChar());
			types.add(parseType(in));
			next = in.next();
		}
	}
	
	private ArrayList<SimpleDeclaration> parseParamList(Tokenizer in) {
		ArrayList<SimpleDeclaration> params = new ArrayList<>();
		Token next = in.next();
		if (next.type == Token.TokenType.CLOSE_PARENTHESIS)
			return params;
		in.pushTokens(next);
		params.add(parseSimpleDeclaration(in));
		next = in.next();
		while (true) {
			if (next.type == Token.TokenType.CLOSE_PARENTHESIS)
				return params;
			if (next.type != Token.TokenType.COMMA)
				throw new SyntaxError("Expected ) or , got: '"+next+"'"+next.generateLineChar());
			params.add(parseSimpleDeclaration(in));
			next = in.next();
		}
	}
	
	private SimpleDeclaration parseSimpleDeclaration(Tokenizer in) {
		SimpleDeclaration dec = new SimpleDeclaration();
		dec.type = parseType(in);
		Token ident = in.next();
		if (ident.type != Token.TokenType.IDENTIFIER)
			throw new SyntaxError("Expected identifier got: '"+ident+"'"+ident.generateLineChar());
		dec.name = ((IdentifierToken) ident).value;
		return dec;
	}
	
	private FuncBody parseLambda(Tokenizer in) {
		// Lambda keyword should have already been parsed
		FuncBody lambda = new FuncBody();
		lambda.type = new TypeDef();
		lambda.type.returnType = parseType(in);
		lambda.type.paramTypes = new ArrayList<>();
		Token next = in.next();
		if (next.type != Token.TokenType.OPEN_PARENTHESIS)
			throw new SyntaxError("Expected ( got: '"+next+"'"+next.generateLineChar());
		lambda.params = parseParamList(in);
		lambda.type.paramTypes.addAll(lambda.params.stream().map(d -> d.type).collect(Collectors.toList()));
		next = in.next();
		if (next.type != Token.TokenType.OPEN_BRACE)
			throw new SyntaxError("Expected { got: '"+next+"'"+next.generateLineChar());
		lambda.body = parseBlock(in);
		return lambda;
	}
	
	private ArrayList<SimpleDeclaration> parseStructFields(Tokenizer in) {
		// Open brace should have already been parsed
		ArrayList<SimpleDeclaration> fields = new ArrayList<>();
		Token next = in.next();
		if (next.type == Token.TokenType.CLOSE_BRACE)
			return fields;
		in.pushTokens(next);
		fields.add(parseSimpleDeclaration(in));
		next = in.next();
		while (true) {
			if (next.type == Token.TokenType.CLOSE_BRACE)
				return fields;
			if (next.type != Token.TokenType.COMMA)
				throw new SyntaxError("Expected } or , got: '"+next+"'"+next.generateLineChar());
			fields.add(parseSimpleDeclaration(in));
			next = in.next();
		}
	}
	
	private TypeDef parseTypeDef(Tokenizer in) {
		TypeDef typeDef = new TypeDef();
		Token next = in.next();
		if (next.type != Token.TokenType.IDENTIFIER)
			throw new SyntaxError("Expected identifier got: '"+next+"'"+next.generateLineChar());
		typeDef.typeName = ((IdentifierToken) next).value;
		next = in.next();
		if (next.type != Token.TokenType.ASSIGN)
			throw new SyntaxError("Expected = got: '"+next+"'"+next.generateLineChar());
		typeDef.returnType = parseType(in);
		return typeDef;
	}
	
	private StructInitializer parseNewStruct(TypeDef type, Tokenizer in) {
		StructInitializer struct = new StructInitializer();
		struct.fieldValues = new ArrayList<>();
		struct.type = type;
		Token next = in.next();
		if (next.type != Token.TokenType.OPEN_BRACE)
			throw new SyntaxError("Expected { got: '"+next+"'"+next.generateLineChar());
		while (true) {
			next = in.next();
			if (next.type != Token.TokenType.IDENTIFIER)
				throw new SyntaxError("Expected identifier got: '"+next+"'"+next.generateLineChar());
			StructField field = new StructField();
			field.name = ((IdentifierToken) next).value;
			next = in.next();
			if (next.type != Token.TokenType.ASSIGN)
				throw new SyntaxError("Expected = got: '"+next+"'"+next.generateLineChar());
			field.value = parseExpr(in, OperatorLevel.lowest);
			struct.fieldValues.add(field);
			next = in.next();
			if (next.type == Token.TokenType.CLOSE_BRACE)
				return struct;
			if (next.type != Token.TokenType.COMMA)
				throw new SyntaxError("Expected } or , got: '"+next+"'"+next.generateLineChar());
		}
	}
}
