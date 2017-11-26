package AST;

import interpreter.ExpType;
import interpreter.TypeVisitor;

public abstract class Expression {
	public abstract ExpType visit(TypeVisitor visitor);
}
