package parser;
import java.util.LinkedList;
import java.util.List;

import scanner.*;
import AST.*;
import interpreter.Interpreter;
import interpreter.ValueOf;
import scanner.let_lang_scanner.Token;
import scanner.let_lang_scanner.TokenType;

public class letrec_parser {
	protected LinkedList<Token> parse_stream;
	protected LinkedList<String> parseexps;
	protected LinkedList<Expression> exps = new LinkedList<>();
	
	public letrec_parser(LinkedList<Token> tokenStream) throws Exception {
		parse_stream = tokenStream;
		parseexps = new LinkedList<String>();
		//LinkedList<Expression> exps = new LinkedList<Expression>();
		//parseExp();
		
		Expression finalExp = parseExp();
		Interpreter inte = new Interpreter();
		ValueOf prog = new ValueOf(finalExp);
		
		//prog.visit(inte);

		System.out.println("AST\n" + finalExp);
/*		for(Expression e : exps) {
			System.out.println(e.toString());
		}*/
		
		//System.out.println("\nRESULT:::");
		System.out.println(prog.visit(inte));
	}
	
	public Expression parseExp() throws Exception{
		TokenType type = parse_stream.peek().type;
		String psdata = parse_stream.peek().data;
		//System.out.print(psdata + " ");
		switch (type) {
		case INTEGER:
			//System.out.println("INTPARSE");
			return parseConstExp();
		/*case IF:
			return parseIfExp();*/
		case LET:
			//System.out.println("LETPARSE");
			return parseLetExp(TokenType.LET);
		case PLUS:
			//System.out.println("PLUSPARSE");
			return parseAddExp(TokenType.PLUS);
		case MINUS:
			//System.out.println("MINUSPARSE");
			return parseDiffExp(TokenType.MINUS);
		case IDENTIFIER:
			//System.out.println("VARPARSE");
			return parseVarExp();
		case PROC:
			return parseProcExp(TokenType.PROC);
		case LPAREN:
			return parseCallProc();
		default:
			throw new Exception("\nIncorrect Parse, check input on: "
					+ psdata);
		}	
	}

	public void consume(TokenType check) throws Exception{
		if(parse_stream.peek().type == check) {
			parse_stream.pollFirst();
		}else {
			throw new Exception("Parsing Type Error\n" 
					+ "Expected: " 
					+ check 
					+ "\n Received: "
					+ parse_stream.peek().type);
		}
	}

	
	private Expression parseConstExp() throws Exception{
		Integer number = Integer.parseInt(parse_stream.peek().data);
		consume(TokenType.INTEGER);
		parseexps.add(new ConstExp(number).toString());
		return new ConstExp(number);
	}
	
/*	private Expression parseLetExp(TokenType LETtype) throws Exception{
		List<String> vars = new LinkedList<>();
		List<Expression> exps = new LinkedList<>();
		
		consume(LETtype);
		
		while(parse_stream.peek().type == TokenType.IDENTIFIER) {
			vars.add(parse_stream.peek().data);
			consume(TokenType.IDENTIFIER);
			consume(TokenType.ASSIGN);
			exps.add(parseExp());
		}
		consume(TokenType.IN);
		Expression letbody = parseExp();
		
		parseexps.add(new LetExp(vars, exps, letbody).toString());
		return new LetExp(vars, exps,letbody);
	}*/
	
	private Expression parseLetExp(TokenType LETtype) throws Exception{
		String var = new String();
		Expression letexp = null;
		
		consume(LETtype);
		
		while(parse_stream.peek().type == TokenType.IDENTIFIER) {
			var = parse_stream.peek().data;
			consume(TokenType.IDENTIFIER);
			consume(TokenType.ASSIGN);
			letexp = parseExp();
		}
		consume(TokenType.IN);
		Expression letbody = parseExp();
		
		exps.add(new LetExp(var, letexp, letbody));
		return new LetExp(var, letexp,letbody);
	}
	
	private Expression parseAddExp(TokenType PLUStype) throws Exception {
		consume(PLUStype);
		consume(TokenType.LPAREN);
		Expression left = parseExp();
		consume(TokenType.COMMA);
		Expression right = parseExp();
		consume(TokenType.RPAREN);
		
		exps.add(new AddExp(left, right));
		return new AddExp(left, right);
	}
	
	private Expression parseDiffExp(TokenType MINUStype) throws Exception {
		consume(MINUStype);
		consume(TokenType.LPAREN);
		Expression left = parseExp();
		consume(TokenType.COMMA);
		Expression right = parseExp();
		consume(TokenType.RPAREN);
		
		exps.add(new DiffExp(left, right));
		return new DiffExp(left, right);
	}
	
	private Expression parseProcExp(TokenType PROCtype) throws Exception{
		
		consume(PROCtype);
		consume(TokenType.LPAREN);
		String arg = parse_stream.pollFirst().data;
		consume(TokenType.RPAREN);
		Expression body = parseExp();
		exps.add(new ProcExp(arg, body));
		return new ProcExp(arg, body);
	}
	
	private Expression parseVarExp() throws Exception{
		String var = parse_stream.peek().data;
		consume(TokenType.IDENTIFIER);
		exps.add(new VarExp(var));
		return new VarExp(var);
	}
	
	private Expression parseCallProc() throws Exception {
		
		consume(TokenType.LPAREN);
		Expression procvar = parseExp();
		Expression exp = parseExp();
		consume(TokenType.RPAREN);
		exps.add(new ProcVarExp(procvar, exp));
		return new ProcVarExp(procvar, exp);
	}
}
