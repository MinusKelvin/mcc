package minusk.interp.parse;

import minusk.interp.parse.token.*;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * Created by MinusKelvin on 12/3/15.
 */
public class Tokenizer {
	private final ArrayDeque<Character> characters;
	private final ArrayList<Token> pushedTokens = new ArrayList<>();
	private int line = 1;
	private int ch = 1;
	
	public Tokenizer(String toTokenize) {
		characters = new ArrayDeque<>(toTokenize.length());
		for (char c : toTokenize.toCharArray())
			characters.addLast(c);
	}
	
	public void pushTokens(Token token) {
		pushedTokens.add(token);
	}
	
	public Token next() {
		if (pushedTokens.size() != 0)
			return pushedTokens.remove(pushedTokens.size()-1);
		
		StringBuilder builder = new StringBuilder();
		State state = State.NONE;
		int lowch = ch;
		char last = 0, lastlast = 0;
		boolean decimal = false, escape = false, expectTick = false;
		while (true) {
			char c;
			{
				Character t = characters.pollFirst();
				if (t == null)
					if (state == State.NONE)
						return null;
					else
						t = '\n';
				c = t;
			}
			ch++;
			switch (state) {
				case NONE:
					if (c == '\n') {
						line++;
						ch = 1;
						lowch = ch;
					} else if (Character.isWhitespace(c)) {
						lowch = ch;
						continue;
					} else if (Character.isAlphabetic(c) || c == '_') {
						builder.append(c);
						state = State.IDENTIFIER;
					} else if (Character.isDigit(c)) {
						builder.append(c);
						state = State.NUMBER;
					} else if (c == '(')
						return new Token(Token.TokenType.OPEN_PARENTHESIS, line, lowch, ch);
					else if (c == ')')
						return new Token(Token.TokenType.CLOSE_PARENTHESIS, line, lowch, ch);
					else if (c == '[')
						return new Token(Token.TokenType.OPEN_BRACKET, line, lowch, ch);
					else if (c == ']')
						return new Token(Token.TokenType.CLOSE_BRACKET, line, lowch, ch);
					else if (c == '{')
						return new Token(Token.TokenType.OPEN_BRACE, line, lowch, ch);
					else if (c == '}')
						return new Token(Token.TokenType.CLOSE_BRACE, line, lowch, ch);
					else if (c == ';')
						return new Token(Token.TokenType.SEMICOLON, line, lowch, ch);
					else if (c == ',')
						return new Token(Token.TokenType.COMMA, line, lowch, ch);
					else if (c == '?')
						return new Token(Token.TokenType.QUESTION, line, lowch, ch);
					else if (c == '~')
						return new Token(Token.TokenType.BITWISE_NOT, line, lowch, ch);
					else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '<' || c == '>' ||
							c == '=' || c == '%' || c == '|' || c == '!' || c == '&' || c == '^' || c == '.') {
						last = c;
						state = State.CHECK_NEXT;
					} else if (c == '"') {
						state = State.STRING;
					} else if (c == '\'') {
						state = State.CHAR;
					} else {
						throw new SyntaxError("Unexpected character: '" + c + "' at line " + line + " character " + (ch-1) + "-" + ch + ".");
					}
					break;
				case IDENTIFIER:
					if (Character.isAlphabetic(c) || Character.isDigit(c) || c == '_')
						builder.append(c);
					else {
						characters.addFirst(c);
						ch--;
						String name = builder.toString();
						switch (name) {
							case "lambda":
								return new KeywordToken(Keyword.LAMBDA, line, lowch, ch);
							case "return":
								return new KeywordToken(Keyword.RETURN, line, lowch, ch);
							case "static":
								return new KeywordToken(Keyword.STATIC, line, lowch, ch);
							case "struct":
								return new KeywordToken(Keyword.STRUCT, line, lowch, ch);
							case "const":
								return new KeywordToken(Keyword.CONST, line, lowch, ch);
							case "while":
								return new KeywordToken(Keyword.WHILE, line, lowch, ch);
							case "func":
								return new KeywordToken(Keyword.FUNC, line, lowch, ch);
							case "else":
								return new KeywordToken(Keyword.ELSE, line, lowch, ch);
							case "for":
								return new KeywordToken(Keyword.FOR, line, lowch, ch);
							case "if":
								return new KeywordToken(Keyword.IF, line, lowch, ch);
							case "do":
								return new KeywordToken(Keyword.DO, line, lowch, ch);
							default:
								return new IdentifierToken(name, line, lowch, ch);
						}
					}
					break;
				case NUMBER:
					if (c == '.') {
						if (decimal)
							throw new SyntaxError("Multiple decimal points: line " + line + " character " + lowch + "-" + ch + ".");
						decimal = true;
						builder.append(c);
					} else if (Character.isDigit(c)) {
						builder.append(c);
					} else {
						characters.addFirst(c);
						ch--;
						if (decimal) {
							try {
								return new DecimalToken(Double.parseDouble(builder.toString()), line, lowch, ch);
							} catch (NumberFormatException e) {
								throw new SyntaxError("Could not parse number: '" + builder.toString() + "' line " + line + " character " + lowch + "-" + ch + ".");
							}
						} else {
							try {
								return new IntToken(Long.parseLong(builder.toString()), line, lowch, ch);
							} catch (NumberFormatException e) {
								throw new SyntaxError("Could not parse number: '" + builder.toString() + "' line " + line + " character " + lowch + "-" + ch + ".");
							}
						}
					}
					break;
				case CHECK_NEXT:
					switch (last) {
						case '+':
							switch (c) {
								case '+':
									return new Token(Token.TokenType.INCREMENT, line, lowch, ch);
								case '=':
									return new Token(Token.TokenType.ADD_ASSIGN, line, lowch, ch);
							}
							characters.addFirst(c);
							ch--;
							return new Token(Token.TokenType.ADD, line, lowch, ch);
						case '-':
							switch (c) {
								case '-':
									return new Token(Token.TokenType.DECREMENT, line, lowch, ch);
								case '=':
									return new Token(Token.TokenType.SUB_ASSIGN, line, lowch, ch);
							}
							characters.addFirst(c);
							ch--;
							return new Token(Token.TokenType.SUB, line, lowch, ch);
						case '*':
							switch (c) {
								case '=':
									return new Token(Token.TokenType.MUL_ASSIGN, line, lowch, ch);
							}
							characters.addFirst(c);
							ch--;
							return new Token(Token.TokenType.MUL, line, lowch, ch);
						case '/':
							switch (c) {
								case '=':
									return new Token(Token.TokenType.DIV_ASSIGN, line, lowch, ch);
								case '/':
									state = State.LINE_COMMENT;
									break;
								case '*':
									state = State.BLOCK_COMMENT;
									break;
								default:
									characters.addFirst(c);
									ch--;
									return new Token(Token.TokenType.DIV, line, lowch, ch);
							}
							break;
						case '=':
							switch (c) {
								case '=':
									return new Token(Token.TokenType.EQUALS, line, lowch, ch);
							}
							characters.addFirst(c);
							ch--;
							return new Token(Token.TokenType.ASSIGN, line, lowch, ch);
						case '<':
							switch (c) {
								case '=':
									if (lastlast == '<')
										return new Token(Token.TokenType.LEFT_SHIFT_ASSIGN, line, lowch, ch);
									else
										return new Token(Token.TokenType.LEQUALS, line, lowch, ch);
								case '<':
									if (lastlast == '<') {
										characters.addFirst(c);
										ch--;
										return new Token(Token.TokenType.LEFT_SHIFT, line, lowch, ch);
									} else
										lastlast = c;
									break;
								default:
									characters.addFirst(c);
									ch--;
									if (lastlast == '<')
										return new Token(Token.TokenType.LEFT_SHIFT, line, lowch, ch);
									else
										return new Token(Token.TokenType.LESS, line, lowch, ch);
							}
							break;
						case '>':
							switch (c) {
								case '=':
									if (lastlast == '>')
										return new Token(Token.TokenType.RIGHT_SHIFT_ASSIGN, line, lowch, ch);
									else
										return new Token(Token.TokenType.GEQUALS, line, lowch, ch);
								case '>':
									if (lastlast == '>') {
										characters.addFirst(c);
										ch--;
										return new Token(Token.TokenType.RIGHT_SHIFT, line, lowch, ch);
									} else
										lastlast = c;
									break;
								default:
									characters.addFirst(c);
									ch--;
									if (lastlast == '>')
										return new Token(Token.TokenType.RIGHT_SHIFT, line, lowch, ch);
									else
										return new Token(Token.TokenType.GREATER, line, lowch, ch);
							}
							break;
						case '%':
							if (c == '=')
									return new Token(Token.TokenType.MOD_ASSIGN, line, lowch, ch);
							characters.addFirst(c);
							ch--;
							return new Token(Token.TokenType.MOD, line, lowch, ch);
						case '.':
							if (Character.isDigit(c)) {
								builder.append(last);
								builder.append(c);
								decimal = true;
								state = State.NUMBER;
								break;
							}
							characters.addFirst(c);
							ch--;
							return new Token(Token.TokenType.DOT, line, lowch, ch);
						case '&':
							switch (c) {
								case '=':
									if (lastlast == '&')
										return new Token(Token.TokenType.LOGICAL_AND_ASSIGN, line, lowch, ch);
									else
										return new Token(Token.TokenType.BITWISE_AND_ASSIGN, line, lowch, ch);
								case '&':
									if (lastlast == '&') {
										characters.addFirst(c);
										ch--;
										return new Token(Token.TokenType.LOGICAL_AND, line, lowch, ch);
									} else
										lastlast = c;
									break;
								default:
									characters.addFirst(c);
									ch--;
									if (lastlast == '&')
										return new Token(Token.TokenType.LOGICAL_AND, line, lowch, ch);
									else
										return new Token(Token.TokenType.BITWISE_AND, line, lowch, ch);
							}
							break;
						case '|':
							switch (c) {
								case '=':
									if (lastlast == '|')
										return new Token(Token.TokenType.LOGICAL_OR_ASSIGN, line, lowch, ch);
									else
										return new Token(Token.TokenType.BITWISE_OR_ASSIGN, line, lowch, ch);
								case '|':
									if (lastlast == '|') {
										characters.addFirst(c);
										ch--;
										return new Token(Token.TokenType.LOGICAL_OR, line, lowch, ch);
									} else
										lastlast = c;
									break;
								default:
									characters.addFirst(c);
									ch--;
									if (lastlast == '|')
										return new Token(Token.TokenType.LOGICAL_OR, line, lowch, ch);
									else
										return new Token(Token.TokenType.BITWISE_OR, line, lowch, ch);
							}
							break;
						case '^':
							switch (c) {
								case '=':
									if (lastlast == '^')
										return new Token(Token.TokenType.LOGICAL_XOR_ASSIGN, line, lowch, ch);
									else
										return new Token(Token.TokenType.BITWISE_XOR_ASSIGN, line, lowch, ch);
								case '^':
									if (lastlast == '^') {
										characters.addFirst(c);
										ch--;
										return new Token(Token.TokenType.LOGICAL_XOR, line, lowch, ch);
									} else
										lastlast = c;
									break;
								default:
									characters.addFirst(c);
									ch--;
									if (lastlast == '^')
										return new Token(Token.TokenType.LOGICAL_XOR, line, lowch, ch);
									else
										return new Token(Token.TokenType.BITWISE_XOR, line, lowch, ch);
							}
							break;
						case '!':
							if (c == '=')
								return new Token(Token.TokenType.NEQUALS, line, lowch, ch);
							characters.addFirst(c);
							ch--;
							return new Token(Token.TokenType.LOGICAL_NOT, line, lowch, ch);
					}
					break;
				case STRING:
					if (c == '\n')
						throw new SyntaxError("Unclosed string literal: line " + line + " character " + lowch + ".");
					if (escape) {
						builder.append(escapeSequence(c));
						escape = false;
					} else if (c == '"')
						return new StringToken(builder.toString(), line, lowch, ch);
					else if (c == '\\') {
						escape = true;
					} else
						builder.append(c);
					break;
				case CHAR:
					if (expectTick) {
						if (c == '\'')
							return new IntToken(last, line, lowch, ch);
						throw new SyntaxError("Expected \"'\", got: '" + c + "' line " + line + "character " + lowch + "-" + ch + ".");
					} else if (escape) {
						last = escapeSequence(c);
						expectTick = true;
					} else if (c == '\'')
						throw new SyntaxError("Unexpected character: \"'\" line " + line + " character " + lowch + "-" + ch + ".");
					else if (c == '\n')
						throw new SyntaxError("Unclosed character literal: line " + line + " character " + lowch + "-" + ch + ".");
					else if (c == '\\')
						escape = true;
					else {
						last = c;
						expectTick = true;
					}
					break;
				case LINE_COMMENT:
					if (c == '\n') {
						characters.addFirst(c);
						ch--;
						state = State.NONE;
					}
					break;
				case BLOCK_COMMENT:
					if (last == '*' && c == '/') {
						state = State.NONE;
						last = 0;
						break;
					}
					last = c;
					break;
			}
		}
	}
	
	private char escapeSequence(char c) {
		switch (c) {
			case '"':case '\\':case '\'':
				return c;
			case 'n':
				return '\n';
			case 't':
				return '\t';
		}
		throw new SyntaxError("Unknown Escape sequence: \\" + c + " line " + line + " character " + (ch-2) + "-" + ch + ".");
	}
	
	private enum State {
		NONE,
		IDENTIFIER,
		NUMBER,
		STRING,
		CHAR,
		LINE_COMMENT,
		BLOCK_COMMENT,
		CHECK_NEXT,
	}
}
