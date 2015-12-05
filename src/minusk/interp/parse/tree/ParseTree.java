package minusk.interp.parse.tree;

import minusk.interp.parse.Keyword;
import minusk.interp.parse.SyntaxError;
import minusk.interp.parse.Tokenizer;
import minusk.interp.parse.token.*;
import minusk.interp.parse.tree.partial.Expression;
import minusk.interp.parse.tree.partial.FuncBody;
import minusk.interp.parse.tree.partial.SimpleDeclaration;
import minusk.interp.parse.tree.statements.*;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.stream.Collectors;

/**
 * Created by MinusKelvin on 12/5/15.
 */
public class ParseTree {
	
	
	public ParseTree(String code) {
		Tokenizer in = new Tokenizer(code);
		Statement stmt;
		do {
			stmt = parseStmt(in);
			System.out.println(stmt);
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
				if (second.type == Token.TokenType.IDENTIFIER) {
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
						case FUNC:
							in.pushTokens(first);
							stmt.declaration = parseDeclaration(in);
							break base;
						case LAMBDA:
							break;
						default:
							throw new SyntaxError("Unexpected token: '" + first + "'" + first.generateLineChar());
					}
				}
			case INTEGER:case OPEN_PARENTHESIS:case SUB:case BITWISE_NOT:
			case LOGICAL_NOT:case OPEN_BRACKET:case INCREMENT:case DECREMENT:
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
			case OPEN_BRACKET:
				return parseArray(in);
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
				Identifier identifier = new Identifier();
				identifier.name = ((IdentifierToken) expectAtom).value;
				atom.identifier = identifier;
				break;
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
	
	private Expression parseArray(Tokenizer in) {
		// Open Bracket should have already been parsed
		Expression ary = new Expression();
		Token next = in.next();
		if (next.type == Token.TokenType.CLOSE_BRACKET) {
			ary.array = new ArrayList<>();
			return ary;
		}
		in.pushTokens(next);
		Expression t = parseExpr(in,OperatorLevel.lowest);
		next = in.next();
		switch (next.type) {
			case COMMA:
				ary.array = new ArrayList<>();
				ary.array.add(t);
				while (true) {
					ary.array.add(parseExpr(in,OperatorLevel.lowest));
					next = in.next();
					if (next.type == Token.TokenType.CLOSE_BRACKET)
						return ary;
					if (next.type != Token.TokenType.COMMA)
						throw new SyntaxError("Expected ] or , got: '"+next+"'"+next.generateLineChar());
				}
			case SEMICOLON:
				ary.arrayInit = t;
				ary.arrayInitCount = parseExpr(in,OperatorLevel.lowest);
				Token expectBracket = in.next();
				if (expectBracket.type != Token.TokenType.CLOSE_BRACKET)
					throw new SyntaxError("Expected ] got: '"+expectBracket+"'"+expectBracket.generateLineChar());
				return ary;
			case CLOSE_BRACKET:
				ary.array = new ArrayList<>();
				ary.array.add(t);
				return ary;
			default:
				throw new SyntaxError("Expected ] , or ; got: '"+next+"'"+next.generateLineChar());
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
		decl.name = new Identifier();
		decl.name.name = ((IdentifierToken) next).value;
		
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
		Token first = in.next();
		if (first.type == Token.TokenType.KEYWORD) {
			switch (((KeywordToken) first).value) {
				case FUNC:
					break;
				case STRUCT:
					break;
				default:
					throw new SyntaxError("Expected func struct or identifier got: '"+first+"'"+first.generateLineChar());
			}
		} else if (first.type == Token.TokenType.IDENTIFIER) {
			typeDef.typeName = new Identifier();
			typeDef.typeName.name = ((IdentifierToken) first).value;
		} else
			throw new SyntaxError("Expected func struct or identifier got: '"+first+"'"+first.generateLineChar());
		return typeDef;
	}
	
	private ArrayList<TypeDef> parseParamTypeList(Tokenizer in) {
		ArrayList<TypeDef> types = new ArrayList<>();
		Token next = in.next();
		if (next.type == Token.TokenType.CLOSE_PARENTHESIS)
			return types;
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
		dec.name = new Identifier();
		dec.name.name = ((IdentifierToken) ident).value;
		return dec;
	}
}
