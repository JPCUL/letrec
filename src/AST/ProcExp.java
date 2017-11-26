package AST;

import java.util.List;

import interpreter.ExpType;
import interpreter.TypeVisitor;

public class ProcExp extends Expression{
	public String var;
	public Expression body;
	
	
	public ProcExp(String var, Expression body) {
		this.var = var;
		this.body = body;
		
	}
	
	public String toString() {
		StringBuilder procstring = new StringBuilder();
			procstring.append("ProcExp{(");
			procstring.append(var);
			procstring.append(")");
			
			procstring.append(": ");
			procstring.append(body.toString());
			procstring.append("} ");
			return procstring.toString();
	}
	
	public ExpType visit(TypeVisitor visitor) {
		return visitor.visit(this);
	}
}

