package AST;

import interpreter.ExpType;
import interpreter.TypeVisitor;

public class ConstExp extends Expression{
	public Integer number;
	
	public ConstExp(Integer number){
		this.number = number;
	}
	
	public String toString() {
		return "ConstExp(" + number + ")";
	}
	
	@Override
	public ExpType visit(TypeVisitor visitor) {
		return visitor.visit(this);
	}

}
