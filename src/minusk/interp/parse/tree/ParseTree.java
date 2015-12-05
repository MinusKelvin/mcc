package minusk.interp.parse.tree;

import minusk.interp.parse.SyntaxError;
import minusk.interp.parse.Tokenizer;
import minusk.interp.parse.token.IdentifierToken;
import minusk.interp.parse.token.IntToken;
import minusk.interp.parse.token.Token;

import java.util.EnumSet;

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
		switch (first.type) {
			case INTEGER:case IDENTIFIER:case OPEN_PARENTHESIS:case SUB:case BITWISE_NOT:case LOGICAL_NOT:
				in.pushTokens(first);
				stmt.expression = parseExpr(in,OperatorLevel.lowest);
				break;
			default:
				throw new SyntaxError("Unexpected token: '" + first + "'" + first.generateLineChar());
		}
		Token last = in.next();
		if (last.type == Token.TokenType.SEMICOLON)
			return stmt;
		throw new SyntaxError("Expected semicolon, got: '" + last + "'" + last.generateLineChar());
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
	
	private Expression parseExpr2(Tokenizer in) {
		Expression expr = new Expression();
		Token operand = in.next();
		in.pushTokens(operand);
		expr.operator = parseOp2(in);
		if (expr.operator == null) {
			in.pushTokens(operand);
			return parseAtom(in);
		}
		return expr;
	}
	
	private Expression parseExpr1(Tokenizer in) {
		Expression expr = new Expression();
		expr.lhs = parseExpr2(in);
		Token operand = in.next();
		in.pushTokens(operand);
		expr.operator = parseOp1(in);
		if (expr.operator == null) {
			in.pushTokens(operand);
			return expr.lhs;
		}
		expr.rhs = parseExpr2(in);
		while (true) {
			Token next = in.next();
			in.pushTokens(next);
			if (parseOp1(in) == null) {
				in.pushTokens(next);
				return expr;
			}
			Expression ex = new Expression();
			ex.lhs = expr;
			expr = ex;
			expr.operator = next;
			expr.rhs = parseExpr2(in);
		}
	}
	
	private Expression parseExpr0(Tokenizer in) {
		Expression expr = new Expression();
		expr.lhs = parseExpr1(in);
		Token operand = in.next();
		in.pushTokens(operand);
		expr.operator = parseOp0(in);
		if (expr.operator == null) {
			in.pushTokens(operand);
			return expr.lhs;
		}
		expr.rhs = parseExpr1(in);
		while (true) {
			Token next = in.next();
			in.pushTokens(next);
			if (parseOp0(in) == null) {
				in.pushTokens(next);
				return expr;
			}
			Expression ex = new Expression();
			ex.lhs = expr;
			expr = ex;
			expr.operator = next;
			expr.rhs = parseExpr1(in);
		}
	}
	
	private static class OperatorLevel {
		static final OperatorLevel lowest;
		static {
			OperatorLevel l = new OperatorLevel(null, EnumSet.of(
					Token.TokenType.SUB, Token.TokenType.BITWISE_NOT, Token.TokenType.LOGICAL_NOT
			), true, true, false);
			l = new OperatorLevel(l, EnumSet.of(
					Token.TokenType.MUL, Token.TokenType.DIV, Token.TokenType.MOD
			), true, false, false);
			l = new OperatorLevel(l, EnumSet.of(
					Token.TokenType.ADD, Token.TokenType.SUB
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
			return parseAtom(in);
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
	
	private Token parseOp0(Tokenizer in) {
		Token token = in.next();
		switch (token.type) {
			case ADD:case SUB:
				return token;
			default:
				return null;
		}
	}
	
	private Token parseOp1(Tokenizer in) {
		Token token = in.next();
		switch (token.type) {
			case MUL:case DIV:case MOD:
				return token;
			default:
				return null;
		}
	}
	
	private Token parseOp2(Tokenizer in) {
		Token token = in.next();
		switch (token.type) {
			case SUB:
				return token;
			default:
				return null;
		}
	}
	
	private Expression parseParen(Tokenizer in) {
		// Open Parenthesis should have already been parsed
		Expression expr = parseExpr0(in);
		Token expectParen = in.next();
		if (expectParen.type != Token.TokenType.CLOSE_PARENTHESIS)
			throw new SyntaxError("Expected ) got: '" + expectParen + "'" + expectParen.generateLineChar());
		return expr;
	}
}
