package AST;
import environment.*;
import expval.*;
import interpreter.ExpType;
import interpreter.TypeVisitor;

public class VarExp extends Expression{
	
	public String var;
	
	public VarExp(String var) {
		this.var = var;
	}
	
	public String toString() {
		return "VarExp(" + var + ")";
	}
	
/*	public ExpVal returnVal(Environment env) {
		return env.findVar(var);
	}*/

	@Override
	public ExpType visit(TypeVisitor visitor) {
		return visitor.visit(this);
	}

}
