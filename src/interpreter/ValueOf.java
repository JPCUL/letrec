package interpreter;

import AST.Expression;

public class ValueOf {
	public Expression exp;
	
	public ValueOf(Expression exp) {
		this.exp = exp;
	}
	
	public String toString() {
		return "Value: " + exp.toString();
	}
	
	public ExpType visit(TypeVisitor visitor) {
		return visitor.visit(this);
	}
}
