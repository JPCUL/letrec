package AST;

import java.util.List;

import interpreter.ExpType;
import interpreter.TypeVisitor;

public class LetExp extends Expression{
	public String var;
	public Expression letexp;
	public Expression body;
	
	public LetExp(String inVar, Expression inExp, Expression inBody) {
		var = inVar;
		letexp= inExp;
		body = inBody;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("LetExp{" + var + "\n");
		sb.append(letexp.toString()+"\n");
		sb.append(body.toString());
		sb.append("}");
		
		return sb.toString();
	}	
	public ExpType visit(TypeVisitor visitor) {
		return visitor.visit(this);
	}
}
